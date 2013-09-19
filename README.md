JavaFX dashboard featuring Thermoduino
===

Executive summary
---

This project will, given some requirements, provide you with a nice dashboard featuring:

- Your nice HD TV thanks to HDMI port on your Raspberry Pi
- Date and time from Timeapi.com for your timezone and prefered display, because dates and times are not Raspberry Pi's main feature. (sure, you can solder some RTC board, but see below...)
- Weather forecast from Wunderground.com for your location, because that is what Misses Jones asked for. (woman acceptance factor is a HUGE factor for something lying in front of the sofa below that nice HD TV...)
- Room temperature, because you can do it, and needed a justification for that 35$+ bill of materials from SparkFun electronics. (sure, you could have bought a thermometer, but who sells 55' OLED screen thermometers?)
- Your servers statuses, because that is what makes you a billionaire and should stay center front in the living room

![Screenshot](/docs/capture.png "Screenshot (from computer test, not actual run on TV)")

Requirements
---

This project is not for everybody. It requires, to be fully functionnal, some hardware and software parts.

Hardware:

- A Raspberry Pi, any model with networking enabled and UART should do
- An Arduino board with thermal sensor plugged and programmed so that every second, the last 10 sensor measurments are sent over serial port on one line, semicolon separated (details later on that topic)

Software:

- Ideally a Raspbian, configured so that video memory is at least 128MB
- JavaFX, as featured on [steveonjava](http://javafx.steveonjava.com/javafx-on-raspberry-pi-3-easy-steps/)
- buffer, a nice utility (on Raspbian, 'sudo apt-get install buffer')
- Freeing the serial port from console, as featured first on [eLinux](http://elinux.org/RPi_Serial_Connection#Connection_to_a_microcontroller_or_other_peripheral) and now even easier thanks to [lurch rpi-serial-console](https://github.com/lurch/rpi-serial-console)
- Some Java libraries (sorry, I'm a Maven, I don't know how to share code from Netbeans with easy way to get the libs...), namely commons-io-2.4 gson-2.2.4 and guava-14.0.1

Configuration:

- You have to get a [Wunderground API Key](http://www.wunderground.com/weather/api/) and set it up in SampleController.WundergroundApiKey
- Find your location on Wunderground (sometimes tricky... Try Douai, France if you can't find it... : "zmw:00000.61.07017") in SampleController.WundergroundApiLocation
- Guess a date/time format that suits your needs on [timeapi.org](http://www.timeapi.org). Mine is "?%25d%2F%25m%2F%25Y%20%25Hh%25M" in SampleController.TimeApiFormat
- Same with a timezone. Beware, they are either not applying DST, or offset by one hour. Mine is "CET" in SampleController.TimeApiTimezone
- Some health check configuration. Easy: I left the system with 3 empty services. Tell what you expect them to say, and where, and it should go green...

You should be all set, at least for the basics. Room temperature is trickier...

First startup
---

First of all, you have to startup the Pi AFTER plugging the HDMI cable. The Arduino will power on automagically with the Pi, and they start talking effortlessly.

In Netbeans, build the whole thing (big hammer button) and wait a few moments
Then, `tar -xzf dist.tgz dist`
Then, `scp dist.tgz yourlogin@yourPi:/home/yourlogin/somewhere`
Then ssh into your Pi, and `untar /home/yourlogin/somewhere/dist.tgz`
And finally, `/opt/jdk1.8.0/bin/java -Djavafx.platform=eglfb -cp /home/yourlogin/somewhere/dist/SampleJavaFX.jar:/home/yourlogin/somewhere/dist/lib/commons-io-2.4.jar:/home/yourlogin/somewhere/dist/lib/gson-2.2.4.jar:/home/yourlogin/somewhere/dist/lib/guava-14.0.1.jar samplejavafx.SampleController`

Ouch. If your TV is on, your Pi hooked via HDMI, and you know to dance for the rain (sorry, there are so many moving parts here I can't guarantee anything...), you should see a nice dashboard, with date/time, the current and forecast meteo at your place, three nice green status indicators and, maybe, a constantly updating current room temperature...

Room temperature setup
---

If you get all of it, but the room temperature, here is the full manual...

Go, buy an Arduino Pro Mini 3.3V
Solder some headers on it.
Hook it like that:

|Arduino|Pi  |
|-------|----|
|VCC|3.3 VDC Power|
|GND|GND|
|TxD|pin 15|
|RxD|pin 16|

(sample [here](http://pi4j.com/images/serial-example-large.png) (thanks to them))

Then, solder a TMP36 sensor to:

|TMP36|Arduino|
|-----|-------|
|VCC|Digital 12|
|Tmp|Analog A0|
|GND|GND|

Then, some code... ** TO DO **

That's it, you get the temperature! Accuracy is theorically .33°C, but with heavy smoothing and averages and oversampling, it should be well over that limit (Java code handles all the gory details for you...)