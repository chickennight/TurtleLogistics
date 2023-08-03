#include "TLClient.h"
#include <Servo.h>

#define THINGNAME "Div_Servo3"

#define TOPIC_WEB_LOG "/log"
#define TOPIC_DIV_RES "/div/res"
#define TOPIC_DIV_INFO "/div/servo3/info"
#define TOPIC_MOD_SER_ANG "/mod/div/servo3/angle"
#define TOPIC_MOD_SER_INT "/mod/div/servo3/servo_interval"
#define TOPIC_MOD_WA_INT "/mod/div/servo3/wait_interval"
#define TOPIC_MOD_IR_INT "/mod/div/servo3/ir_interval"


TLClient Div_Servo(THINGNAME);

void Publish_callback(int orderno, int flag);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);
void Device_function();

#define SENSORPIN 5
#define SERVOPIN 3
Servo divider;

int servo_interval = 4000;
int wait_interval = 4000;
int ir_interval = 2000;
int angle = 65;


void moveservo();
void pubres(int orderno,int flag);
int verify();
void MSG(String str);

void setup() {
  Serial.begin(115200);
  Div_Servo.connect_AWS();
  Div_Servo.setCallback(Subscribe_callback);
  Div_Servo.subscribe(TOPIC_DIV_INFO); 
  Div_Servo.subscribe(TOPIC_MOD_SER_ANG);
  Div_Servo.subscribe(TOPIC_MOD_SER_INT);
  Div_Servo.subscribe(TOPIC_MOD_IR_INT);
  Div_Servo.subscribe(TOPIC_MOD_WA_INT);
  MSG("AWS Connect Success");

  pinMode(SENSORPIN,INPUT);
  divider.attach(SERVOPIN);
  divider.write(0);
}

void loop() {
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
    MSG("Angle Changed");
  }
  else if(strcmp(TOPIC_MOD_SER_INT, topic)==0){
    servo_interval = (int)doc["servo_interval"];
    MSG("Servo Interval Changed");
  }
  else if(strcmp(TOPIC_MOD_IR_INT, topic)==0){
    ir_interval = (int)doc["ir_interval"];
    MSG("Servo Interval Changed");
  }
  else if(strcmp(TOPIC_MOD_WA_INT,topic)==0)
  {
    wait_interval = (int)doc["wait_interval"];
    MSG("Servo Wait Interval Changed");
  }
}

void pubres(const char* orderno,int flag){
  StaticJsonDocument<100> temp;
  temp["order_num"] = orderno;
  temp["type"]="1";
  temp["result"] = String(flag);
  char buf[100];
  serializeJson(temp,buf);

  if(flag==1){
    MSG("Divide Success");
  }
  else{
    MSG("Divide Error");
  }

  if(Div_Servo.publish(TOPIC_DIV_RES,buf)){
    MSG("Publish Result Success");
  }
  else{
    MSG("Publish Result Fail");
  }
}

int verify(){
  moveservo();
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

void moveservo() {
    divider.write(angle);
    delay(servo_interval);
    divider.write(0);
}
void MSG(String str){
  String base="{\"dev\":\"Div_Servo3\",\"content\":\"";
  String base2="\"}";
  Div_Servo.publish(TOPIC_WEB_LOG, (base+str+base2).c_str());
}

