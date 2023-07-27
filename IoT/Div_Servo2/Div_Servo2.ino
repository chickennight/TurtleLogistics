#include "TLClient.h"
#define THINGNAME "Div_Servo2"

#define PUB_TOPIC_00 "/log"
#define PUB_TOPIC_01 "/div/res"

#define SUB_TOPIC_02 "/div/servo2/info"
#define SUB_TOPIC_03 "/mod/div/servo2/angle"
#define SUB_TOPIC_04 "/mod/div/servo2/servo_interval"
#define SUB_TOPIC_05 "/mod/div/servo2/ir_interval"

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
int ir_interval = 2000;
int angle = 90;

void moveservo(Servo* divider, int ANGLELIMIT);
void pubres(int orderno,int flag);
int verify();

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  Div_Servo.connect_AWS();
  Div_Servo.setCallback(Subscribe_callback);
  Div_Servo.subscribe(SUB_TOPIC_02);
  Div_Servo.subscribe(SUB_TOPIC_03);
  Div_Servo.subscribe(SUB_TOPIC_04);
  Div_Servo.subscribe(SUB_TOPIC_05);
  Div_Servo.publish(PUB_TOPIC_00, "{\"dev\":\"Div_Servo2\",\"content\":\"AWS Connect Success\"}");

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
  Serial.println(buf); 
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length){
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  if(strcmp(SUB_TOPIC_02, topic)==0){      //  /div/servo1/info
    const char* orderno = doc["order_num"];
    delay(servo_interval);
    pubres(orderno,verify());
  }
  else if(strcmp(SUB_TOPIC_03, topic)==0){      //  /mod/div/ser1/angle
    angle = (int)doc["angle"];
    Div_Servo.publish(PUB_TOPIC_00, "{\"dev\":\"Div_Servo2\",\"content\":\"Angle Changed\"}");
    
  }
  else if(strcmp(SUB_TOPIC_04, topic)==0){      //  /mod/div/ser1/servo_interval
    servo_interval = (int)doc["servo_interval"];
    Div_Servo.publish(PUB_TOPIC_00, "{\"dev\":\"Div_Servo2\",\"content\":\"Servo Interval Changed\"}");
  }
  else if(strcmp(SUB_TOPIC_05, topic)==0){      //  /mod/div/ser1/ir_interval
    ir_interval = (int)doc["ir_interval"];
    Div_Servo.publish(PUB_TOPIC_00, "{\"dev\":\"Div_Servo2\",\"content\":\"IR Interval Changed\"}");
  }
}

void moveservo(Servo* divider,int ANGLELIMIT) {
    delay(servo_interval);
    Serial.println("Motor Move!");
    for (int i = 0; i <= ANGLELIMIT; i++) {
        divider->write(i);
        delay(10);
    }

    for (int i = ANGLELIMIT; i >= 0; i--) {
        divider->write(i);
        delay(10);
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
    Div_Servo.publish(PUB_TOPIC_00, "{\"dev\":\"Div_Servo2\",\"content\":\"Divide Success\"}");
  }
  else{
    Div_Servo.publish(PUB_TOPIC_00, "{\"dev\":\"Div_Servo2\",\"content\":\"Divide Error\"}");
  }
  if(!Div_Servo.publish(PUB_TOPIC_01,buf)){
    Serial.println("Publish Fail");
  }
  else{
    Serial.println("Publish Success");
  }
}

int verify(){
  // Mover ServoMotor 
  moveservo(&divider, angle);
  int flag=1,val=0;
  unsigned long prev = millis();
  // Check IR Sensor
  Serial.println("IR Sensor Checking...");
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


