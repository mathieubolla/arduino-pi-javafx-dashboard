package samplejavafx.services.thermoduino;

import com.google.common.collect.AbstractIterator;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public enum SerialPortIterable implements Iterable<String> {
    INSTANCE(new SerialPortIterator());
    private SerialPortIterator iterator;

    private SerialPortIterable(SerialPortIterator iterator) {
        this.iterator = iterator;
    }

    @Override
    public synchronized Iterator<String> iterator() {
        if (iterator == null) {
            throw new IllegalStateException("Serial port can only iterate once");
        }
        Iterator<String> value = iterator;
        iterator = null;
        return value;
    }

    private static final class SerialPortIterator extends AbstractIterator<String> implements AutoCloseable {
        private final Thread dataMover;
        private final AtomicBoolean running;
        private final LinkedBlockingQueue<String> serialData;

        private SerialPortIterator() {
            running = new AtomicBoolean(true);
            serialData = new LinkedBlockingQueue<>(1000);
            dataMover = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Process pb = new ProcessBuilder().command("/bin/sh", "-c", "(stty raw; cat | buffer -s 82 -b 2) < /dev/ttyAMA0").start();
                        Scanner scanner = new Scanner(new InputStreamReader(new BufferedInputStream(pb.getInputStream())));

                        while (running.get() && scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (serialData.remainingCapacity() > 0) {
                                serialData.offer(System.currentTimeMillis() + ";" + line);
                            }
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        @Override
        protected String computeNext() {
            try {
                if (running.get() && !dataMover.isAlive()) {
                    dataMover.start();
                } else if (!running.get()) {
                    return endOfData();
                }

                return serialData.take();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void close() throws Exception {
            running.set(false);
        }
    }
}
