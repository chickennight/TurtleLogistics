#include "TLClient.h"
#define THINGNAME "Div_Servo3"

#define TOPIC_WEB_LOG "/log"
#define TOPIC_DIV_RES "/div/res"
#define TOPIC_DIV_INFO "/div/servo3/info"
#define TOPIC_MOD_SER_ANG "/mod/div/servo3/angle"
#define TOPIC_MOD_SER_INT "/mod/div/servo3/servo_interval"
#define TOPIC_MOD_IR_INT "/mod/div/servo3/ir_interval"

TLClient Div_Servo(THINGNAME);

void Publish_callback(int orderno, int flag);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);
void Device_function();


//for divider
#include <Servo.h>
#define SENSORPIN 5
#define SERVOPIN 3
Servo divider;

int servo_interval = 1000;
int ir_interval = 10;
int angle = 90;

void moveservo(Servo* divider, int ANGLELIMIT);
void pubres(int orderno,int flag);
int verify();

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  Div_Servo.connect_AWS();
  Div_Servo.setCallback(Subscribe_callback);
  Div_Servo.subscribe(TOPIC_DIV_INFO); 
  Div_Servo.subscribe(TOPIC_MOD_SER_ANG);
  Div_Servo.subscribe(TOPIC_MOD_SER_INT);
  Div_Servo.subscribe(TOPIC_MOD_IR_INT);
  Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"AWS Connect Success\"}");

  pinMode(SENSORPIN,INPUT);
  divider.attach(SERVOPIN);
  divider.write(0);
}

void loop() {
  // put your main code here, to run repeatedly:
  Div_Servo.mqttLoop();
}

void Publish_callback(int orderno,int flag){
  StaticJsonDocument<200> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[200];
  serializeJson(temp,buf,sizeof(buf));
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length){
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  if(strcmp(TOPIC_DIV_INFO, topic)==0){
    const char* orderno = doc["order_num"];
    delay(servo_interval);
    pubres(orderno,verify());
  }
  else if(strcmp(TOPIC_MOD_SER_ANG, topic)==0){      
    angle = (int)doc["angle"];
    Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"Angle Changed\"}");
    
  }
  else if(strcmp(TOPIC_MOD_SER_INT, topic)==0){
    servo_interval = (int)doc["servo_interval"];
    Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"Servo Interval Changed\"}");
  }
  else if(strcmp(TOPIC_MOD_IR_INT, topic)==0){
    ir_interval = (int)doc["ir_interval"];
    Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"IR Interval Changed\"}");
  }
}

void moveservo(Servo* divider,int ANGLELIMIT) {
    delay(servo_interval);
    Serial.println("Motor Move!");
    for (int i = 0; i <= ANGLELIMIT; i++) {
        divider->write(i);
        delay(ir_interval);
    }

    for (int i = ANGLELIMIT; i >= 0; i--) {
        divider->write(i);
        delay(ir_interval);
    }
}

void pubres(const char* orderno,int flag){
  StaticJsonDocument<100> temp;
  temp["order_num"] = orderno;
  temp["type"]="1";
  temp["result"] = String(flag);
  char buf[100];
  serializeJson(temp,buf);
  Div_Servo.publish(TOPIC_DIV_RES,buf);
  if(flag==1){
    Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"Divide Success\"}");
  }
  else{
    Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"Divide Error\"}");
  }
  if(!Div_Servo.publish(TOPIC_DIV_RES,buf)){
    Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"Publish Result Success\"}");
  }
  else{
    Div_Servo.publish(TOPIC_WEB_LOG, "{\"dev\":\"Div_Servo3\",\"content\":\"Publish Result Fail\"}");
  }
}

int verify(){
  moveservo(&divider, angle);
  int flag=1,val=0;
  unsigned long prev = millis();
  
  prev = millis();

  while(1){
    val=digitalRead(SENSORPIN);
    if(val==LOW){
      flag=0;
      break;
    }
    if(millis() - prev >=ir_interval) break;
  }

  return flag;
}


