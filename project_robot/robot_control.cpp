#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <M5Stack.h>
#include "AudioFileSourceSD.h"
#include "AudioFileSourceID3.h"
#include "AudioGeneratorMP3.h"
#include "AudioOutputI2S.h"

//タッチセンサのピン番号の指定
#define TOUCH_SENSOR 5

// 最小パルス幅(~4096の範囲)
#define SERVOMIN 110

// 最大パルス幅(~4096の範囲)
#define SERVOMAX 480

//　音声ファイル系の定義
AudioGeneratorMP3 *mp3;
AudioFileSourceSD *file;
AudioOutputI2S *out;
AudioFileSourceID3 *id3;

//　サーボドライバの通信経路の指定
// PCA9685のI2Cアドレスを指定
Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x40);

//　move関数で扱う角度を示す変数
int angle;

// テキストカラー
int baseColor = 0xFFFF;
int runningColor = 0xFC40;

//　サーボモータのピン番号の指定
int servoElbowRight_PIN = 0;
int servoLateralShoulderRight_PIN = 1;
int servoLongitudinalShoulderRight_PIN = 2;
int servoElbowLeft_PIN = 3;
int servoLateralShoulderLeft_PIN = 4;
int servoLongitudinalShoulderLeft_PIN = 5;
int servoNeck_PIN = 6;

//　サーボモータの初期位置
int initialPositionElbowRight = 75;
int initialPositionLateralShoulderRight = 165;
int initialPositionLongitudinalShoulderRight = 90;
int initialPositionElbowLeft = 105;
int initialPositionLateralShoulderLeft = 15;
int initialPositionLongitudinalShoulderLeft = 90;
int initialPositionNeck = 80;

//　サーボモータの位置
int positionElbowRight;
int positionLateralShoulderRight;
int positionLongitudinalShoulderRight;
int positionElbowLeft;
int positionLateralShoulderLeft;
int positionLongitudinalShoulderLeft;
int positionNeck;

void setRunningTextColor(const char* runningFunctionName){
  
  if (runningFunctionName == "dummbellCurl"){
    M5.Lcd.fillScreen(BLACK);
    M5.Lcd.setTextColor(baseColor);
    M5.Lcd.setTextSize(2);
    M5.Lcd.setCursor(130, 200);
    M5.Lcd.print("Funny");
    M5.Lcd.setCursor(230, 200);
    M5.Lcd.print("Strike");
    M5.Lcd.setCursor(230, 10);
    M5.Lcd.print("Dislike");
    M5.Lcd.setTextColor(runningColor);
    M5.Lcd.setCursor(16, 200);
    M5.Lcd.print("Dumbbell");
  }
  if (runningFunctionName == "funnyMove"){
    M5.Lcd.fillScreen(BLACK);
    M5.Lcd.setTextColor(baseColor);
    M5.Lcd.setTextSize(2);
    M5.Lcd.setCursor(16, 200);
    M5.Lcd.print("Dumbbell");
    M5.Lcd.setCursor(230, 200);
    M5.Lcd.print("Strike");
    M5.Lcd.setCursor(230, 10);
    M5.Lcd.print("Dislike");
    M5.Lcd.setTextColor(runningColor);
    M5.Lcd.setCursor(130, 200);
    M5.Lcd.print("Funny");
  }
  if (runningFunctionName == "strikePose"){
    M5.Lcd.fillScreen(BLACK);
    M5.Lcd.setTextColor(baseColor);
    M5.Lcd.setTextSize(2);
    M5.Lcd.setCursor(16, 200);
    M5.Lcd.print("Dumbbell");
    M5.Lcd.setCursor(130, 200);
    M5.Lcd.print("Funny");
    M5.Lcd.setCursor(230, 10);
    M5.Lcd.print("Dislike");
    M5.Lcd.setTextColor(runningColor);
    M5.Lcd.setCursor(230, 200);
    M5.Lcd.print("Strike");
  }
  if (runningFunctionName == "expressDislike"){
    M5.Lcd.fillScreen(BLACK);
    M5.Lcd.setTextColor(baseColor);
    M5.Lcd.setTextSize(2);
    M5.Lcd.setCursor(16, 200);
    M5.Lcd.print("Dumbbell");
    M5.Lcd.setCursor(130, 200);
    M5.Lcd.print("Funny");
    M5.Lcd.setCursor(230, 200);
    M5.Lcd.print("Strike");
    M5.Lcd.setTextColor(runningColor);
    M5.Lcd.setCursor(230, 10);
    M5.Lcd.print("Dislike");
  }
  M5.Lcd.setTextColor(0xfed6);
}

void resetRunningTextColor(){
  M5.Lcd.fillScreen(BLACK);
  M5.Lcd.setTextColor(0xfed6);
  M5.Lcd.setTextSize(2);
  M5.Lcd.setCursor(16, 200);
  M5.Lcd.print("Dumbbell");
  M5.Lcd.setCursor(130, 200);
  M5.Lcd.print("Funny");
  M5.Lcd.setCursor(230, 200);
  M5.Lcd.print("Strike");  
  M5.Lcd.setCursor(230, 10);
  M5.Lcd.print("Dislike");
}

//　初期位置に戻す関数
void resetPosition(int delayTime){
  servo_move(servoElbowRight_PIN, initialPositionElbowRight);
  servo_move(servoLateralShoulderRight_PIN, initialPositionLateralShoulderRight);
  servo_move(servoLongitudinalShoulderRight_PIN, initialPositionLongitudinalShoulderRight);
  servo_move(servoElbowLeft_PIN, initialPositionElbowLeft);
  servo_move(servoLateralShoulderLeft_PIN, initialPositionLateralShoulderLeft);
  servo_move(servoLongitudinalShoulderLeft_PIN, initialPositionLongitudinalShoulderLeft);
  servo_move(servoNeck_PIN, initialPositionNeck);
  delay(delayTime);
}

//　サーボの位置を表す変数の初期化
void initializeAngle(){
  positionElbowRight = initialPositionElbowRight;
  positionLateralShoulderRight = initialPositionLateralShoulderRight;
  positionLongitudinalShoulderRight = initialPositionLongitudinalShoulderRight;
  positionElbowLeft = initialPositionElbowLeft;
  positionLateralShoulderLeft = initialPositionLateralShoulderLeft;
  positionLongitudinalShoulderLeft = initialPositionLongitudinalShoulderLeft;
  positionNeck = initialPositionNeck;
}

//　サーボを動かす
void servo_move(int n, int angle){
  angle = map(angle, 0, 180, SERVOMIN, SERVOMAX);
  pwm.setPWM(n, 0, angle);
}

//　音声ファイルを流す
void playMP3(char *filename){
  file = new AudioFileSourceSD(filename);
  id3 = new AudioFileSourceID3(file);
  out = new AudioOutputI2S(0, 1); // Output to builtInDAC
  out->SetOutputModeMono(true);
  out->SetGain(0.5);
  mp3 = new AudioGeneratorMP3();
  mp3->begin(id3, out);
  while(mp3->isRunning()) {
    if (!mp3->loop()) mp3->stop();
  }
}

// ダンベルカール
void dumbbellCurl(){
  setRunningTextColor("dummbellCurl");
  initializeAngle();
  
  for (int count = 0; count <= 5; count++){
    for (int count = 0; count < 30; count += 1){
      servo_move(servoElbowRight_PIN, positionElbowRight);
      servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
      servo_move(servoElbowLeft_PIN, positionElbowLeft);
      servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
      delay(40);
      positionElbowRight += 3;
      positionLongitudinalShoulderRight += 1;
      positionElbowLeft -= 3;
      positionLongitudinalShoulderLeft -= 1;
    }

    servo_move(servoElbowRight_PIN, 165);
    servo_move(servoLongitudinalShoulderRight_PIN, 120);
    servo_move(servoElbowLeft_PIN, 15);
    servo_move(servoLongitudinalShoulderLeft_PIN, 60);
    playMP3("/dumbbellCurl.mp3");
    delay(1000);

    for (int count = 30; count > 0; count -= 1){
      servo_move(servoElbowRight_PIN, positionElbowRight);
      servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
      servo_move(servoElbowLeft_PIN, positionElbowLeft);
      servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
      delay(40);
      positionElbowRight -= 3;
      positionLongitudinalShoulderRight -= 1;
      positionElbowLeft += 3;
      positionLongitudinalShoulderLeft += 1;
    }

    resetPosition(40);
    delay(40);
  }
  resetRunningTextColor();
}

// 変な動き
void funnyMove(){
  setRunningTextColor("funnyMove");
  initializeAngle();

  servo_move(servoElbowRight_PIN, 165);
  servo_move(servoLateralShoulderRight_PIN, 75);
  servo_move(servoLongitudinalShoulderRight_PIN, 180);
  servo_move(servoElbowLeft_PIN, 15);
  servo_move(servoLateralShoulderLeft_PIN, 105);
  servo_move(servoLongitudinalShoulderLeft_PIN, 180);
  positionElbowRight = 165;
  positionLateralShoulderRight = 75;
  positionLongitudinalShoulderRight = 180;
  positionElbowLeft = 15;
  positionLateralShoulderLeft = 105;
  positionLongitudinalShoulderLeft = 180;
  delay(1000);

  for (int count = 0; count < 3; count += 1){
    for (int count = 0; count < 30; count += 1){
      servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
      servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
      delay(60);
      positionLongitudinalShoulderRight -= 6;
      positionLongitudinalShoulderLeft -= 6;
    }

    for (int count = 30; count > 0; count -= 1){
      servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
      servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
      delay(60);
      positionLongitudinalShoulderRight += 6;
      positionLongitudinalShoulderLeft += 6;
    }
    positionElbowRight = 165;
    positionLateralShoulderRight = 75;
    positionLongitudinalShoulderRight = 180;
    positionElbowLeft = 15;
    positionLateralShoulderLeft = 105;
    positionLongitudinalShoulderLeft = 180;
    delay(1000);
  }
  resetPosition(3000);
  delay(40);
  resetRunningTextColor();
} 

// ダブルバイセップス
void strikePose(){
  setRunningTextColor("strikePose");
  initializeAngle();

  for (int count = 0; count < 30; count += 1){
    servo_move(servoElbowRight_PIN, positionElbowRight);
    servo_move(servoLateralShoulderRight_PIN, positionLateralShoulderRight);
    servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
    servo_move(servoElbowLeft_PIN, positionElbowLeft);
    servo_move(servoLateralShoulderLeft_PIN, positionLateralShoulderLeft);
    servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
    delay(40);
    positionElbowRight += 3;
    positionLateralShoulderRight -= 3;
    positionLongitudinalShoulderRight += 3;
    positionElbowLeft -= 3;
    positionLateralShoulderLeft += 3;
    positionLongitudinalShoulderLeft -= 3;
  }
  servo_move(servoElbowRight_PIN, 165);
  servo_move(servoLateralShoulderRight_PIN, 75);
  servo_move(servoLongitudinalShoulderRight_PIN, 180);
  servo_move(servoElbowLeft_PIN, 15);
  servo_move(servoLateralShoulderLeft_PIN, 105);
  servo_move(servoLongitudinalShoulderLeft_PIN, 0);
  playMP3("/strikePose.mp3");
  delay(1000);

  for (int count = 30; count > 0;count -= 1){
    servo_move(servoElbowRight_PIN, positionElbowRight);
    servo_move(servoLateralShoulderRight_PIN, positionLateralShoulderRight);
    servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
    servo_move(servoElbowLeft_PIN, positionElbowLeft);
    servo_move(servoLateralShoulderLeft_PIN, positionLateralShoulderLeft);
    servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
    delay(40);
    positionElbowRight -= 3;
    positionLateralShoulderRight += 3;
    positionLongitudinalShoulderRight -= 3;
    positionElbowLeft += 3;
    positionLateralShoulderLeft -= 3;
    positionLongitudinalShoulderLeft += 3;
  }

  resetPosition(3000);
  delay(40);
  resetRunningTextColor();
}

// 嫌な動き
void expressDislike(){
  setRunningTextColor("expressDislike");
  initializeAngle();
  
  for (int count = 0; count < 20; count += 1){
    servo_move(servoElbowRight_PIN, positionElbowRight);
    servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
    servo_move(servoNeck_PIN, positionNeck);
    delay(20);
    positionElbowRight += 1;
    positionLongitudinalShoulderRight += 1;
    positionNeck += 2;
  }
    playMP3("/expressDislike.mp3");
  
  for (int count = 0; count <= 3; count++){

    for (int count = 20; count > 0; count -= 1){
      servo_move(servoElbowRight_PIN, positionElbowRight);
      servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
      servo_move(servoElbowLeft_PIN, positionElbowLeft);
      servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
      servo_move(servoNeck_PIN, positionNeck);
      delay(20);
      positionElbowRight -= 1;
      positionLongitudinalShoulderRight -= 1;
      positionElbowLeft -= 1;
      positionLongitudinalShoulderLeft -= 1;
      positionNeck -= 4;
    }

    for (int count = 20; count > 0; count -= 1){
      servo_move(servoElbowRight_PIN, positionElbowRight);
      servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
      servo_move(servoElbowLeft_PIN, positionElbowLeft);
      servo_move(servoLongitudinalShoulderLeft_PIN, positionLongitudinalShoulderLeft);
      servo_move(servoNeck_PIN, positionNeck);
      delay(20);
      positionElbowRight += 1;
      positionLongitudinalShoulderRight += 1;
      positionElbowLeft += 1;
      positionLongitudinalShoulderLeft += 1;
      positionNeck += 4;
    }
  }

  for (int count = 0; count < 20; count += 1){
    servo_move(servoElbowRight_PIN, positionElbowRight);
    servo_move(servoLongitudinalShoulderRight_PIN, positionLongitudinalShoulderRight);
    servo_move(servoNeck_PIN, positionNeck);
    delay(20);
    positionElbowRight -= 1;
    positionLongitudinalShoulderRight -= 1;
    positionNeck -= 2;
  }
  resetPosition(40);
  resetRunningTextColor();
}

void setup() {
  M5.begin();
  pwm.begin();
  pwm.setPWMFreq(50);
  pinMode(TOUCH_SENSOR, INPUT);
  resetRunningTextColor();
  initializeAngle();
  resetPosition(1000);
  delay(1000);
}

void loop() {
  M5.update();

  if (M5.BtnA.wasReleased()){
    dumbbellCurl();
  }

  if (M5.BtnB.wasReleased()){
    funnyMove();
  }

  if (M5.BtnC.wasReleased()){
    strikePose();
  }

  if (digitalRead(TOUCH_SENSOR) == HIGH) {
    expressDislike();
  }
}