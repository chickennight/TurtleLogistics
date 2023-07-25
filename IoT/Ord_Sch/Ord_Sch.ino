#include "TLClient.h"
#define THINGNAME "Ord_Sch"
TLClient order_scheduler(THINGNAME);
#define SUB_TOPIC_01 "/web/power"
#define SUB_TOPIC_02 "/sup/add"
#define SUB_TOPIC_03 "/mod/ord/sch/angle"
#define SUB_TOPIC_04 "/mod/ord/sch/interval"
#define PUB_TOPIC_00 "/connect"

void Publish_callback(int orderno, int flag);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);
void Device_function();
//for scheduler
////////////////////////////////////////////////////////////////////////////////////////
#include <cppQueue.h>                                                                 //
#include <Servo.h>                                                                    //
#define	IMPLEMENTATION	FIFO                                                          //
#define PISTON_SERVO1 12                                                              //
#define PISTON_SERVO2 13                                                              //
#define PISTON_SERVO3 14                                                              //
Servo piston1;                                                                        //
Servo piston2;                                                                        //
Servo piston3;                                                                        //
Servo piston[3] = {piston1, piston2, piston3};
int DEVICE_CONFIG_ANGLE = 30;
int DEVICE_CONFIG_INTERVAL = 2000;
int ord_sch_power = -1;                                                               //
typedef struct order_list {                                                           //
	int product1;
  int product2;
  int product3;
} order;                                                                              //
cppQueue order_q(sizeof(order), 100, IMPLEMENTATION);                                 //
void add_order(order_list order){                                                     //
  order_q.push(&order);
}                                                                                     //
                                                                                      //
void push_piston(int piston_num){                                                     //

  if(piston[piston_num].read() > 0){
    Serial.println(piston[piston_num].read());
    piston[piston_num].write(piston[piston_num].read() - DEVICE_CONFIG_ANGLE);
    Serial.print(piston_num+1);                                                       
    Serial.println(" piston moved");
  }                                                                                        
  else{                                                                               
    piston[piston_num].write(180);                                                    
  }                                                                                   
}                                                                                     //
////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  order_scheduler.connect_AWS();
  order_scheduler.setCallback(Subscribe_callback);
  order_scheduler.subscribe(SUB_TOPIC_01);
  order_scheduler.subscribe(SUB_TOPIC_02);
  order_scheduler.subscribe(SUB_TOPIC_03);
  order_scheduler.subscribe(SUB_TOPIC_04);
  order_scheduler.publish(PUB_TOPIC_00, "/Ord_Sch Connected");
}

void loop() {
  order_scheduler.mqttLoop();
  if(ord_sch_power==1)
    Device_function();
}

void Publish_callback(int orderno,int flag){
  StaticJsonDocument<200> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[200];
  serializeJson(temp,buf,sizeof(buf));
  Serial.println(buf); 
  //order_scheduler.publish(PUB_TOPIC_01,buf);
  //order_scheduler.publish("/sch/test",buf);
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length){
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  if(strcmp(SUB_TOPIC_01, topic)==0){           // /web/power
    ord_sch_power = doc["type"];
    Serial.print("POWER : ");
    Serial.println(ord_sch_power);
  }
  else if(strcmp(SUB_TOPIC_02, topic)==0){      //  /sup/add
    if(ord_sch_power == -1) ord_sch_power = 1;
    order_list order_list;
    order_list.product1 = (int)doc["productA"];
    order_list.product2 = (int)doc["productB"];
    order_list.product3 = (int)doc["productC"];
    add_order(order_list);
  }
  else if(strcmp(SUB_TOPIC_03, topic)==0){    //   /mod/ord/sch/angle
    DEVICE_CONFIG_ANGLE = (int)doc["angle"];
    Serial.println(DEVICE_CONFIG_ANGLE);
  }  
  else if(strcmp(SUB_TOPIC_04, topic)==0){    //  /mod/ord/sch/interval
    DEVICE_CONFIG_INTERVAL = (int)doc["interval"];
    Serial.println(DEVICE_CONFIG_INTERVAL);
  }
}

void Device_function(){
  while(!order_q.isEmpty()){
    order_list now_order;
    order_q.pop(&now_order);
    
    //각 주문에 대해 리스트에 있는 상품에 맞춰 피스톤을 움직인다.
    while(true){
      int delay_time = 0;
      if(now_order.product1 != 0){
        now_order.product1 -= 1;
        delay_time += DEVICE_CONFIG_INTERVAL;
        push_piston(0);
      }
      
      if(now_order.product2 != 0){
        now_order.product2 -= 1;
        delay_time += DEVICE_CONFIG_INTERVAL;
        push_piston(1);
      }
      
      if(now_order.product3 != 0){
        now_order.product3 -= 1;
        delay_time += DEVICE_CONFIG_INTERVAL;
        push_piston(2);
      }

      Serial.println(delay_time);
      delay(delay_time);
      if(now_order.product1 == 0 && now_order.product2 == 0 && now_order.product3 == 0) break;
    }
  }
}
