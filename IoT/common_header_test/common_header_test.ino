#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include "TL.h"
#include "secrets.h"


//for scheduler
#include <cppQueue.h>
#include <Servo.h>
#define THINGNAME "Ord_Sch "
#define	IMPLEMENTATION	FIFO
#define PISTON_SERVO1 12
#define PISTON_SERVO2 13
#define PISTON_SERVO3 14
#define MOVE_DEGREE 30

// topic list
#define AWS_IOT_SUBSCRIBE_TOPIC1 "Ord/Sch"

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
PubSubClient mqttClient(wifiClient);

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
}
 

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  connect_WiFi();
  connect_AWS(mqttClient, wifiClient, AWS_CERT_CA, ORD_SCHE_CERT, ORD_SCHE_PRIKEY);
  
  mqttClient.setCallback(messageReceived);

  //subsribe topics
  mqttClient.subscribe(AWS_IOT_SUBSCRIBE_TOPIC1);
  
  
  piston1.attach(PISTON_SERVO1);
  piston2.attach(PISTON_SERVO2);
  piston3.attach(PISTON_SERVO3);
}

void loop() {
  // put your main code here, to run repeatedly:
now = time(nullptr);
 
  if (!mqttClient.connected())
  {
    connect_AWS(mqttClient, wifiClient, AWS_CERT_CA, ORD_SCHE_CERT, ORD_SCHE_PRIKEY);
  }
  else
  {
    mqttClient.loop();
    if (millis() - lastMillis > 5000)
    {
      lastMillis = millis();
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
