#include <ESP8266WiFi.h>
#include <time.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <WiFiClientSecure.h>
#include "secrets.h"

#define AWS_IOT_PUBLISH_TOPIC1   "esp8266/pub"
#define AWS_IOT_SUBSCRIBE_TOPIC1 "Div/Dev/Servo/1"
#define AWS_IOT_SUBSCRIBE_TOPIC2 "Div/Dev/Servo/2"
#define AWS_IOT_SUBSCRIBE_TOPIC3 "Div/Dev/Servo/3"
#define AWS_IOT_SUBSCRIBE_TOPIC4 "Div/Dev/Servo/4"

WiFiClientSecure net;
PubSubClient client(net);

BearSSL::X509List cert(cacert);
BearSSL::X509List client_crt(client_cert);
BearSSL::PrivateKey key(privkey);

void connectAWS()
{
  delay(3000);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
 
  Serial.println(String("Attempting to connect to SSID: ") + String(WIFI_SSID));
 
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(1000);
  }
 
  //  NTPConnect();
 
  net.setTrustAnchors(&cert);
  net.setClientRSACert(&client_crt, &key);
 
  client.setServer(MQTT_HOST, 8883);
  client.setCallback(messageReceived);
 
 
  Serial.println("Connecting to AWS IOT");
 
  while (!client.connect(THINGNAME))
  {
    Serial.print(".");
    delay(1000);
  }
 
  if (!client.connected()) {
    Serial.println("AWS IoT Timeout!");
    return;
  }
  // Subscribe to a topic
  client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC1);
  client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC2);
  client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC3);
  client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC4);
 
  Serial.println("AWS IoT Connected!");
}
void messageReceived(char *topic, byte *payload, unsigned int length)
{
  if(strcmp(topic,AWS_IOT_SUBSCRIBE_TOPIC1)==0){
      Serial.print("Received [");
      Serial.print(topic);
      Serial.print("]: ");
      Serial.print("분류기 1 메시지 도착");
      for (int i = 0; i < length; i++)
      {
          Serial.print((char)payload[i]);
      }
      Serial.println();
  }
  else if(strcmp(topic,AWS_IOT_SUBSCRIBE_TOPIC2)==0){
      Serial.print("Received [");
      Serial.print(topic);
      Serial.print("]: ");
      Serial.print("분류기 2 메시지 도착");
      for (int i = 0; i < length; i++)
      {
          Serial.print((char)payload[i]);
      }
      Serial.println();
  }
  else if(strcmp(topic,AWS_IOT_SUBSCRIBE_TOPIC3)==0){
      Serial.print("Received [");
      Serial.print(topic);
      Serial.print("]: ");
      Serial.print("분류기 3 메시지 도착");
      for (int i = 0; i < length; i++)
      {
          Serial.print((char)payload[i]);
      }
      Serial.println();
  }
  else if(strcmp(topic,AWS_IOT_SUBSCRIBE_TOPIC4)==0){
      Serial.print("Received [");
      Serial.print(topic);
      Serial.print("]: ");
      Serial.print("분류기 4 메시지 도착");
      for (int i = 0; i < length; i++)
      {
          Serial.print((char)payload[i]);
      }
      Serial.println();
  }
  
}
void setup()
{
  Serial.begin(115200);
  syncTimeWithNTP();
  connectAWS();
}

void loop()
{
  client.loop();
  // 이후 동기화된 시간을 이용하여 작업을 수행할 수 있습니다.
  // 예를 들어, 시간을 확인하거나 특정 작업을 특정 시간에 수행할 수 있습니다.
}

 
void syncTimeWithNTP()
{
  configTime(9 * 3600, 0, "pool.ntp.org"); // GMT+9 (한국 표준시)로 설정, 원하는 시간대로 변경 가능
  Serial.print("Waiting for time sync...");
  while (time(nullptr) < 1610880000) // 기준 시간 (Unix timestamp) 이전까지 대기 (2021년 1월 17일 00:00:00)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  time_t now = time(nullptr);
  struct tm timeinfo;
  gmtime_r(&now, &timeinfo);

  Serial.print("Current time: ");
  Serial.println(asctime(&timeinfo));
}
