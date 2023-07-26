// Div_Servo1(ESP12E)
// Pub : Supervisor(/div/res) {orderno, result}
// Sub : Div_Veri(/div/info) {orderno}

#include "8266secrets.h"

#define THINGNAME "Div_Servo2"
// PUBLISH TOPIC
#define TOPIC_DIV_RES "/div/res"
// SUBSCRIBE TOPIC
#define TOPIC_DIV_VERI "/div/info"
// IRSENSOR PIN NUM
#define SENSORPIN 4
// SERVO MOTOR PIN NUM
#define SERVOPIN 3
// IR SENSOR TIME LIMIT
#define TIMELIMIT 2000
// Servo Motor Angle LIMIT
#define ANGLELIMIT 90

WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);
Servo divider;

BearSSL::X509List cert(AWS_CERT_CA);
BearSSL::X509List client_crt(DIV_SER1_CERT);
BearSSL::PrivateKey key(DIV_SER1_PRIKEY);


void pubres(int orderno,int flag){
  StaticJsonDocument<100> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[100];
  serializeJson(temp,buf);

  if(!mqttClient.publish(TOPIC_DIV_RES,buf)){
    Serial.println("Publish Fail");
  }
  else{
    Serial.println("Publish Success");
  }
}

int verify(){
  // Mover ServoMotor 
  moveservo(&divider,90);
  int flag=0,val=0;
  unsigned long prev = millis();
  // Check IR Sensor
  Serial.println("IR Sensor Checking...");
  while(millis() - prev <=TIMELIMIT){
    ESP.wdtDisable();
    val=digitalRead(SENSORPIN);
    if(val==LOW){
      Serial.println("Find!!");
      flag=1;
      break;
    }
  }
  return flag;
}

void messageReceived(char *topic, byte *payload, unsigned int length)
{
  Serial.println("Message Received!");
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  int orderno = doc["order_num"].as<int>();
  delay(1000);
  pubres(orderno,verify());
}

void connectAWS()
{
  delay(1000);
 
  wifiClient.setTrustAnchors(&cert);
  wifiClient.setClientRSACert(&client_crt, &key);

  mqttClient.setServer(AWS_IOT_ENDPOINT, 8883);
  mqttClient.setCallback(messageReceived);
 
  Serial.println("Connecting to AWS IOT");
 
  while (!mqttClient.connect(THINGNAME))
  {
    Serial.print(".");
    delay(1000);
  }
 
  if (!mqttClient.connected()) {
    Serial.println("AWS IoT Timeout!");
    return;
  }
  // Subscribe to a topic
  mqttClient.subscribe(TOPIC_DIV_VERI);
 
  Serial.println("AWS IoT Connected!");
}

void setupWifi(){
    WiFi.mode(WIFI_STA);
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.println(String("Attempting to connect to SSID: ") + String(WIFI_SSID));

    while (WiFi.status() != WL_CONNECTED)
    {
        delay(1000);
        Serial.println("Connecting...");
    }
    Serial.println("WiFi Connect Success!");
}

void syncTimeWithNTP()
{
    configTime(9 * 3600, 0, "pool.ntp.org", "time.nist.gov"); // GMT+9 (한국 표준시)로 설정, 원하는 시간대로 변경 가능
    time_t now = time(nullptr);
    while (now < 1610880000) // 기준 시간 (Unix timestamp) 이전까지 대기 (2021년 1월 17일 00:00:00)
    {
        delay(500);
        now = time(nullptr);
    }

    now = time(nullptr);
    struct tm timeinfo;
    gmtime_r(&now, &timeinfo);

}

void setup() 
{
  Serial.begin(115200);

  setupWifi();
  syncTimeWithNTP();
  connectAWS();

  pinMode(SENSORPIN,INPUT);

  divider.attach(SERVOPIN);
  divider.write(0);
}

void loop() 
{
  if(WiFi.status() != WL_CONNECTED){
    setupWifi();
  }
  if (!mqttClient.connected()) {
    Serial.println("AWS IoT Timeout!");
  }
  while (!mqttClient.connect(THINGNAME))
  {
    Serial.print(".");
    delay(1000);
  }
  mqttClient.loop();
}