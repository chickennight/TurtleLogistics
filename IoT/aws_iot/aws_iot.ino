//for mqtt
#include <PubSubClient.h>
#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <time.h>
#include <ArduinoJson.h>
#include "secrets.h"

#define SUB_TOPIC_01 "/sup/add"
#define PUB_TOPIC_01 "/sch/test"

//for scheduler
#include <cppQueue.h>
#include <Servo.h>

#define	IMPLEMENTATION	FIFO
#define PISTON_SERVO1 12
#define PISTON_SERVO2 13
#define PISTON_SERVO3 14
#define MOVE_DEGREE 30

Servo piston1;
Servo piston2;
Servo piston3;
Servo piston[3] = {piston1, piston2, piston3};

typedef struct order_list {
	int product1;
  int product2;
  int product3;
} order;

cppQueue order_q(sizeof(order), 100, IMPLEMENTATION);

void add_order(order_list order){
  order_q.push(&order);
}

void push_piston(int piston_num){

  if(piston[piston_num].read() > 0){
    Serial.println(piston[piston_num].read());
    piston[piston_num].write(piston[piston_num].read() - MOVE_DEGREE);
    Serial.print(piston_num+1);
    Serial.println(" piston moved");
  }
  else{
    piston[piston_num].write(180);
  }
}

WiFiClientSecure wifiClient;
PubSubClient client(wifiClient);


unsigned long lastMillis = 0;
unsigned long previousMillis = 0;
const long interval = 5000;

time_t now;
time_t nowish = 1510592825;

void NTPConnect(void)
{
  Serial.print("Setting time using SNTP");
  configTime(TIME_ZONE * 3600, 0 * 3600, "pool.ntp.org", "time.nist.gov");
  now = time(nullptr);
  while (now < nowish)
  {
    delay(500);
    Serial.print(".");
    now = time(nullptr);
  }
  Serial.println("done!");
  struct tm timeinfo;
  gmtime_r(&now, &timeinfo);
  Serial.print("Current time: ");
  Serial.print(asctime(&timeinfo));
}


BearSSL::X509List cert(AWS_CERT_CA);
BearSSL::X509List client_crt(AWS_CERT_CRT);
BearSSL::PrivateKey key(AWS_CERT_PRIVATE);

void connectWiFi(){
  delay(3000);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(1000);
    Serial.println("Connecting...");
  }
  Serial.println("Connected!");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void messageReceived(char *topic, byte *payload, unsigned int length)
{
  StaticJsonDocument<200> doc;

  Serial.print("Received [");
  Serial.print(topic);
  Serial.print("]: ");
  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
      // Deserialize the JSON document'
  }

  // Deserialize the JSON document
  DeserializationError error = deserializeJson(doc, payload);

  // Test if parsing succeeds.
  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    return;
  }

  int productAnum = doc["productA"];
  int productBnum = doc["productB"];
  int productCnum = doc["productC"];
  Serial.print(productAnum);
  Serial.print(productBnum);
  Serial.print(productCnum);

  order_list order_list;
  order_list.product1 = productAnum;
  order_list.product2 = productBnum;
  order_list.product3 = productCnum;

  add_order(order_list);

  Serial.println();
  client.publish(PUB_TOPIC_01, "hello");
}
 
void connectAWS()
{
  connectWiFi();
  NTPConnect();
  // Set AWS configuration
  wifiClient.setTrustAnchors(&cert);  
  wifiClient.setClientRSACert(&client_crt, &key);
  
  client.setServer(AWS_IOT_ENDPOINT,8883);
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
  client.subscribe(SUB_TOPIC_01);
  //client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC2);
  //client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC3);
  Serial.println("AWS IoT Connected!");
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  connectAWS();

  piston1.attach(PISTON_SERVO1);
  piston2.attach(PISTON_SERVO2);
  piston3.attach(PISTON_SERVO3);

}


void loop() {
  now = time(nullptr);
  delay(1000);
  
  if (!client.connected())
  {
    connectAWS();
  }
  else
  {
    client.loop();
    if (millis() - lastMillis > 5000)
    {
      lastMillis = millis();
      //client.publish(PUB_TOPIC_01, "hello");
      
    }
  }
  
  while(!order_q.isEmpty()){
    order_list now_order;
    order_q.pop(&now_order);
    
    //각 주문에 대해 리스트에 있는 상품에 맞춰 피스톤을 움직인다.
    while(true){
      int delay_time = 0;
      if(now_order.product1 != 0){
        now_order.product1 -= 1;
        delay_time += 2000;
        push_piston(0);
      }
      
      if(now_order.product2 != 0){
        now_order.product2 -= 1;
        delay_time += 2000;
        push_piston(1);
      }
      
      if(now_order.product3 != 0){
        now_order.product3 -= 1;
        delay_time += 2000;
        push_piston(2);
      }

      Serial.println(delay_time);
      delay(delay_time);
      if(now_order.product1 == 0 && now_order.product2 == 0 && now_order.product3 == 0) break;

    }
  }

}