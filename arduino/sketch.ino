const int power = 12;
const int monitor = 13;
const int analog = A0;
const int serialRate = 9600;
const int samples = 10;

int lastValues[samples];

void setup() {
  // Beware: This is for Arduino Pro Mini 8MHz
  // most boards do not need this division
  Serial.begin(serialRate / 2);
  pinMode(power, OUTPUT);
  pinMode(monitor, OUTPUT);
}

void loop() {
  // Power up the thermo sensor
  digitalWrite(power, HIGH);
  // Power up monitor led
  digitalWrite(monitor, HIGH);
  
  // Wait for consistent ADC reading and enough to see led light
  delay(250);
  
  for (int i = 0; i < samples; i++) {
    // Read sensor value
    lastValues[i] = analogRead(analog);
    delay(50);
  }
  
  // Shutdown sensor
  digitalWrite(power, LOW);
  digitalWrite(monitor, LOW);
  
  // Print values to serial
  // Formula is 10mV per degree celsius for this sensor
  // Arduino reference is 3.3 on this board
  // ADC range is 0 to 1023 (10 bits)
  // Shift at zero is 500mV or .5V
  for (int i = 0; i < samples - 1; i++) {
    Serial.print(lastValues[i]);
    Serial.print(";");
  }
  Serial.print(lastValues[samples - 1]);
  Serial.println();
  // Flush serial to get more consistent decoding at the other end of serial link
  Serial.flush();
  
  // Wait for next loop
  delay(250);
}
