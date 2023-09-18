#include <WiFiManager.h>
#include <DHTesp.h>
#include <LiquidCrystal_I2C.h>
LiquidCrystal_I2C lcd(0x27, 16, 2);  //SCL A5 SDA A4
#include "FirebaseESP8266.h"
#define FIREBASE_HOST "https://da4-trangquanchien-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "YPArN0pIUZeJ3QVaz0nzweMox6Fngwc5bj2KL41l"
#define WIFI_SSID "Chien"
#define WIFI_PASSWORD "12345678"
FirebaseData firebaseData;
#include <Wire.h>
#include <SPI.h>
#include <RTClib.h>
#include <NTPtimeESP.h>
RTC_DS1307 rtc;
NTPtime NTPch("ch.pool.ntp.org");  // Server NTP
strDateTime dateTime;
DHTesp dht;
const int dht_pin = D7;  // khai báo chân  cho cảm biến D7 DHT11
int binhnonglanh = D5;
int dieuhoa = D6;
int gio;
int phut;
int gioon;
int phuton;
int giooff;
int phutoff;
float old_h = -1, old_t = -1;  // tạo 2 biến lưu giá trị cảm biến dht 11
int old_gio = -1;
int old_phut = -1;
void setup() {
  Serial.begin(115200);
  pinMode(binhnonglanh, OUTPUT);
  pinMode(dieuhoa, OUTPUT);
  dht.setup(dht_pin, DHTesp::DHT11);
  lcd.init();
  lcd.backlight();
  // Kết nối wifi.
  WiFiManager wm;
  bool res;
  res = wm.autoConnect("TranChien");
  if (!res) {
  } else {
    Serial.println("connected...yeey :)");
  }
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Connected !!!");
  delay(500);
  lcd.clear();
  // Kết nối và khởi động mô-đun RTC DS1307
  Wire.begin(4, 5);  // Chân 4,5 I2C của ESP8266
  rtc.begin();
  if (!rtc.isrunning()) {
    Serial.println("RTC is NOT running!");
    rtc.adjust(DateTime(F(__DATE__), F(__TIME__)));
  } else {
    Serial.println("RTC is running.");
  }
}
void loop() {
  // Lấy thời gian từ máy chủ NTP
  dateTime = NTPch.getNTPtime(7.0, 0);
  if (dateTime.valid) {
    // Cập nhật thời gian vào mô-đun RTC DS1307
    rtc.adjust(DateTime(dateTime.year, dateTime.month, dateTime.day, dateTime.hour, dateTime.minute, dateTime.second));
  }
  // Đọc thời gian từ mô-đun RTC DS1307
  DateTime now = rtc.now();
  gio = now.hour();
  phut = now.minute();
  // Gửi thời gian lên Firebase
  Firebase.setInt(firebaseData, "/thoigian/gio", gio);
  Firebase.setInt(firebaseData, "/thoigian/phut", phut);

  // hiển thị giờ lên lcd
  if (phut != old_phut) {
    lcd.setCursor(10, 0);
    lcd.print("       ");
    lcd.setCursor(10, 0);
    lcd.print(gio);
    lcd.print(":");
    lcd.print(phut);
    old_phut = phut;
  }
  // Đọc giờ bật/tắt từ Firebase
  Firebase.getInt(firebaseData, "/thoigian/hon");
  gioon = firebaseData.intData();
  Firebase.getInt(firebaseData, "/thoigian/mon");
  phuton = firebaseData.intData();
  Firebase.getInt(firebaseData, "/thoigian/hoff");
  giooff = firebaseData.intData();
  Firebase.getInt(firebaseData, "/thoigian/moff");
  phutoff = firebaseData.intData();
  // Kiểm tra và điều khiển trạng thái của thiết bị từ Firebase
  if (gio == gioon && phut == phuton) {
    Firebase.setInt(firebaseData, "/dieukhien/binhnonglanh", 1);
  } else if (gio == giooff && phut == phutoff) {
    Firebase.setInt(firebaseData, "/dieukhien/binhnonglanh", 0);
  }
  // Đọc trạng thái điều khiển từ Firebase và điều khiển tương ứng
  Firebase.getInt(firebaseData, "/dieukhien/binhnonglanh");
  digitalWrite(binhnonglanh, firebaseData.intData() == 1 ? HIGH : LOW);
  Firebase.getInt(firebaseData, "/dieukhien/dieuhoa");
  digitalWrite(dieuhoa, firebaseData.intData() == 1 ? HIGH : LOW);
  // đọc cảm biến dht11
  float new_h = dht.getHumidity();
  float new_t = dht.getTemperature();
  if (new_t != old_t) {
    Firebase.setFloat(firebaseData, "/giamsat/nhietdo", new_t);
    Firebase.setFloat(firebaseData, "/giamsat/doam", new_h);
    old_t = new_t;
    old_h = new_h;
  }
  lcd.setCursor(0, 0);
  lcd.print("T:");
  lcd.print(old_t);
  lcd.print("C");
  lcd.setCursor(0, 1);
  lcd.print("H:");
  lcd.print(old_h);
  lcd.print("%");
}