#include <Servo.h>

int power = 4;
Servo dir;

void setup() {
  pinMode(power, OUTPUT);
  digitalWrite(power, HIGH);
  dir.attach(6, 400, 2600);
  dir.write(90);
  Serial.begin(9600);
}

void loop() {
  int temp = Serial.read() - '0';
  switch (temp) {
    case 0:
      startMoving();
      break;
    case 1:
      stopMoving();
      break;
    case 2:
      turnLeft();
      break;
    case 3:
      turnRight();
      break;
  }
  delay(10);
}
void turnLeft(){
  int d = dir.read();
  if (d <= 175) {
    dir.write(d + 5);
  }
}

void turnRight(){
  int d = dir.read();
  if (d >= 5) {
    dir.write(d - 5);
  }
}

void startMoving(){
  digitalWrite(power, LOW);
}

void stopMoving(){
  digitalWrite(power, HIGH);
}
