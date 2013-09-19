package samplejavafx.services.thermoduino;

import com.google.common.base.Function;
import com.google.common.collect.*;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static com.google.common.collect.Lists.newArrayList;
import com.google.common.util.concurrent.AtomicDouble;
import java.util.concurrent.atomic.AtomicLong;
import samplejavafx.model.thermoduino.ThermoduinoData;

public class ThermoduinoService {
    private final AtomicDouble lastKnownTemp;
    private final AtomicLong lastKnownDate;

    public ThermoduinoService() {
        this.lastKnownTemp = new AtomicDouble(0.0);
        this.lastKnownDate = new AtomicLong(0l);

        final Iterable<DatedRecord<Double>> filteredAveragedRawInput = FluentIterable.from(SerialPortIterable.INSTANCE)
                .transform(PARSE_LINE) // 1s at a time
                .transform(FILTER_OUTLIERS) // keep the best out of ten
                .transform(AVERAGE); // and average the remainder
        final Iterable<DatedRecord<Double>> oneMinuteSmoothedRawInput = FluentIterable.from(new WindowIterable<DatedRecord<Double>>(filteredAveragedRawInput, 60)).transform(AVERAGE2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (DatedRecord<Double> smoothedRawInput : oneMinuteSmoothedRawInput) {
                    if (!Double.isNaN(smoothedRawInput.getRecord())) {
                        lastKnownTemp.set(sensorAverageVoltsToDegreeCelcius(smoothedRawInput.getRecord()));
                        lastKnownDate.set(smoothedRawInput.getDate());
                    }
                }
            }
        }).start();
    }

    public ThermoduinoData getData() {
        return new ThermoduinoData(lastKnownTemp.get(), lastKnownDate.get());
    }

    private double sensorAverageVoltsToDegreeCelcius(double input) {
        return 3.3 + 100 * input * 3.3 / 1023 - 50;
    }
    private static final Function<String, DatedRecord<Iterable<Double>>> PARSE_LINE = new Function<String, DatedRecord<Iterable<Double>>>() {
        public DatedRecord<Iterable<Double>> apply(String input) {
            String[] dateAndRemainder = input.split(";", 2);
            return DatedRecord.<Iterable<Double>>from(Long.parseLong(dateAndRemainder[0]), FluentIterable.from(newArrayList(dateAndRemainder[1]
                    .split(";")))
                    .transform(new Function<String, Double>() {
                public Double apply(String s) {
                    try {
                        return Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        return Double.NaN;
                    }
                }
            }));
        }
    };
    private static final Function<DatedRecord<Iterable<Double>>, DatedRecord<Iterable<Double>>> FILTER_OUTLIERS = new Function<DatedRecord<Iterable<Double>>, DatedRecord<Iterable<Double>>>() {
        public DatedRecord<Iterable<Double>> apply(DatedRecord<Iterable<Double>> input) {
            Multiset<Double> frequencyCounters = HashMultiset.create();

            for (Double value : input.getRecord()) {
                frequencyCounters.add(value);
            }

            int highestFrequency = 0;
            Double valueAtHighestFrequency = 0.0;
            for (Double value : input.getRecord()) {
                int frequency = frequencyCounters.count(value);
                if (frequency > highestFrequency) {
                    highestFrequency = frequency;
                    valueAtHighestFrequency = value;
                }
            }
            frequencyCounters.remove(valueAtHighestFrequency, highestFrequency);

            int secondHighestFrequency = 0;
            Double valueAtSecondHighestFrequency = 0.0;
            for (Double value : input.getRecord()) {
                int frequency = frequencyCounters.count(value);
                if (frequency > secondHighestFrequency) {
                    secondHighestFrequency = frequency;
                    valueAtSecondHighestFrequency = value;
                }
            }

            List<Double> results = Lists.newArrayListWithCapacity(highestFrequency + secondHighestFrequency);
            for (int i = 0; i < highestFrequency; i++) {
                results.add(valueAtHighestFrequency);
            }

            if (Math.abs(valueAtHighestFrequency - valueAtSecondHighestFrequency) <= 4) {
                for (int i = 0; i < secondHighestFrequency; i++) {
                    results.add(valueAtSecondHighestFrequency);
                }
            }

            return DatedRecord.<Iterable<Double>>from(input.getDate(), results);
        }
    };
    private static final Function<DatedRecord<Iterable<Double>>, DatedRecord<Double>> AVERAGE = new Function<DatedRecord<Iterable<Double>>, DatedRecord<Double>>() {
        public DatedRecord<Double> apply(DatedRecord<Iterable<Double>> doubles) {
            Double sum = 0.0;
            Integer count = 0;
            for (Double value : doubles.getRecord()) {
                if (!Double.isNaN(value)) {
                    sum += value;
                    count++;
                }
            }
            return DatedRecord.from(doubles.getDate(), sum / count);
        }
    };
    private static final Function<Iterable<DatedRecord<Double>>, DatedRecord<Double>> AVERAGE2 = new Function<Iterable<DatedRecord<Double>>, DatedRecord<Double>>() {
        public DatedRecord<Double> apply(Iterable<DatedRecord<Double>> input) {
            Double sum = 0.0;
            Integer count = 0;
            Long lastDate = 0l;
            for (DatedRecord<Double> value : input) {
                sum += value.getRecord();
                count++;
                lastDate = value.getDate();
            }
            return DatedRecord.from(lastDate, sum / count);
        }
    };

    private static class WindowIterable<T> implements Iterable<Iterable<T>> {

        private final Iterable<T> delegate;
        private final int samples;

        public WindowIterable(Iterable<T> delegate, int samples) {
            this.delegate = delegate;
            this.samples = samples;
        }

        public Iterator<Iterable<T>> iterator() {
            return new Window(delegate, samples);
        }
    }

    private static class Window<T> extends AbstractIterator<Iterable<T>> {

        private final Iterator<T> delegate;
        private final ArrayBlockingQueue<T> lastNValues;

        public Window(Iterable<T> delegate, int samples) {
            this.delegate = delegate.iterator();
            this.lastNValues = new ArrayBlockingQueue<T>(samples);
        }

        @Override
        protected Iterable<T> computeNext() {
            if (!delegate.hasNext()) {
                return endOfData();
            }

            lastNValues.offer(delegate.next());

            if (lastNValues.remainingCapacity() > 0) {
                return computeNext();
            }

            lastNValues.remove();

            return newArrayList(lastNValues);
        }
    }

    private static class DatedRecord<T> {

        private final Long date;
        private final T record;

        public DatedRecord(Long date, T record) {
            this.date = date;
            this.record = record;
        }

        public Long getDate() {
            return date;
        }

        public T getRecord() {
            return record;
        }

        public static <T> DatedRecord<T> from(Long date, T record) {
            return new DatedRecord<>(date, record);
        }
    }
}