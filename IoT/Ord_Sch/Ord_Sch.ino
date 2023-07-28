#include "TLClient.h"

#define SUB_WEB_POW "/mod/web/power"
#define SUB_SCH_INFO "/sup/ord/sch/info"
#define SUB_MOD_ANG "/mod/ord/sch/angle"
#define SUB_MOD_ITV "/mod/ord/sch/interval"

#define SUB_PS1_SET "/mod/ord/piston1/angle"
#define SUB_PS2_SET "/mod/ord/piston2/angle"
#define SUB_PS3_SET "/mod/ord/piston3/angle"

#define PUB_LOG "/log"
#define THINGNAME "Ord_Sch"
TLClient order_scheduler(THINGNAME);

void Publish_callback(int orderno, int flag);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);
void Device_function();
//for scheduler
////////////////////////////////////////////////////////////////////////////////////////
#include <cppQueue.h>                                                                 //
#include <Servo.h>                                                                    //
#define	IMPLEMENTATION	FIFO                                                          //
#define PISTON_SERVO1 0                                                              //
#define PISTON_SERVO2 13                                                              //
#define PISTON_SERVO3 14                                                              //
Servo piston1;                                                                        //
Servo piston2;                                                                        //
Servo piston3;                                                                        //
Servo piston[3] = {piston1, piston2, piston3};
int angle = 30;
int interval = 2000;
int power = -1;                                                               //
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
  int cur_angle = piston[piston_num].read();
  for(int i = cur_angle; i<cur_angle+angle; i++){
    piston[piston_num].write(i);
    delay(10);
  }
  cur_angle += angle;
  
  piston[piston_num].write(cur_angle);
  Serial.println(cur_angle);
  if (cur_angle >= 2*angle){
    cur_angle = 0;
    piston[piston_num].write(cur_angle);
  }        
  Serial.print("piston ");                                                                
  Serial.println(piston_num);                                                                
}                                                                                     //
////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  piston[0].attach(0);
  piston[1].attach(13);
  piston[2].attach(14);

  order_scheduler.connect_AWS();
  order_scheduler.setCallback(Subscribe_callback);
  order_scheduler.subscribe(SUB_WEB_POW);
  order_scheduler.subscribe(SUB_SCH_INFO);
  order_scheduler.subscribe(SUB_MOD_ANG);
  order_scheduler.subscribe(SUB_MOD_ITV);

  order_scheduler.subscribe(SUB_PS1_SET);
  order_scheduler.subscribe(SUB_PS2_SET);
  order_scheduler.subscribe(SUB_PS3_SET);
  order_scheduler.publish(PUB_LOG, "{\"dev\":\"Scheduler\",\"content\":\"AWS Connect Success\"}");
}

void loop() {
  order_scheduler.mqttLoop();
  if(power==1)
    Device_function();
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length){
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  if(strcmp(SUB_WEB_POW, topic)==0){           //
    power = (int)doc["power"];
    Serial.print("POWER : ");
    Serial.println(power);
    order_scheduler.publish(PUB_LOG, "{\"dev\":\"Scheduler\",\"content\":\"power\"}");
  }
  else if(strcmp(SUB_SCH_INFO, topic)==0){      // 
    order_list order_list;
    order_list.product1 = (int)doc["productA"];
    order_list.product2 = (int)doc["productB"];
    order_list.product3 = (int)doc["productC"];
    add_order(order_list);
    order_scheduler.publish(PUB_LOG, "{\"dev\":\"Scheduler\",\"content\":\"Order added\"}");
  }
  else if(strcmp(SUB_MOD_ANG, topic)==0){    //   /mod/ord/sch/angle
    angle = (int)doc["angle"];
    order_scheduler.publish(PUB_LOG, "{\"dev\":\"Scheduler\",\"content\":\"Angle changed\"}");
    Serial.println(angle);
  }  
  else if(strcmp(SUB_MOD_ITV, topic)==0){    //  /mod/ord/sch/interval
    interval = (int)doc["interval"];
    order_scheduler.publish(PUB_LOG, "{\"dev\":\"Scheduler\",\"content\":\"Interval changed\"}");
    Serial.println(interval);
  }
  
  else if(strcmp(SUB_PS1_SET, topic)==0){    //  /mod/ord/sch/interval
    int input_angle = (int)doc["angle"];
    piston[0].write(input_angle);
  }
  else if(strcmp(SUB_PS2_SET, topic)==0){    //  /mod/ord/sch/interval
    int input_angle = (int)doc["angle"];
    piston[1].write(input_angle);
  }
  else if(strcmp(SUB_PS3_SET, topic)==0){    //  /mod/ord/sch/interval
    int input_angle = (int)doc["angle"];
    piston[2].write(input_angle);
  }
}

void Device_function(){
  while(!order_q.isEmpty()){
    order_list now_order;
    order_q.pop(&now_order);
    
    //power가 꺼져있으면 while문 안에 멈춰있어야 한다. (power가 켜졌을 때 완료되지 않은 기존 주문이 삭제되면 안되기 때문)
    while(true){
      if(power==1) break;
    }

    //각 주문에 대해 리스트에 있는 상품에 맞춰 피스톤을 움직인다.
    while(true){
      int delay_time = 0;
      if(now_order.product1 != 0){
        now_order.product1 -= 1;
        delay_time += interval;
        push_piston(0);
      }
      
      if(now_order.product2 != 0){
        now_order.product2 -= 1;
        delay_time += interval;
        push_piston(1);
      }
      
      if(now_order.product3 != 0){
        now_order.product3 -= 1;
        delay_time += interval;
        push_piston(2);
      }

      Serial.println(delay_time);
      delay(delay_time);
      if(now_order.product1 == 0 && now_order.product2 == 0 && now_order.product3 == 0) break;
    }
  }
}
