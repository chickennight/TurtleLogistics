#include "TLClient.h"
#define THINGNAME "Div_Servo2"
#define SUB_TOPIC_01 "/web/power"
#define SUB_TOPIC_02 "/div/info"
#define SUB_TOPIC_03 "/mod/div/ser2/angle"
#define SUB_TOPIC_04 "/mod/div/ser2/servo_interval"
#define SUB_TOPIC_05 "/mod/div/ser2/ir_interval"
#define PUB_TOPIC_00 "/connect"
#define PUB_TOPIC_01 "/div/res"
TLClient Div_Servo(THINGNAME);

void Publish_callback(int orderno, int flag);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);
void Device_function();


//for divider
#include <Servo.h>
#define SENSORPIN 5
#define SERVOPIN 3
Servo divider;
int div_ser_power = -1;
int DEVICE_CONFIG_SERVO_INTERVAL = 6000;
int DEVICE_CONFIG_IR_INTERVAL = 2000;
int DEVICE_CONFIG_ANGLE = 90;

void moveservo(Servo* divider, int ANGLELIMIT);
void pubres(int orderno,int flag);
int verify();

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  Div_Servo.connect_AWS();
  Div_Servo.setCallback(Subscribe_callback);
  Div_Servo.subscribe(SUB_TOPIC_01);
  Div_Servo.subscribe(SUB_TOPIC_02);
  Div_Servo.subscribe(SUB_TOPIC_03);
  Div_Servo.subscribe(SUB_TOPIC_04);
  Div_Servo.subscribe(SUB_TOPIC_05);
  Div_Servo.publish(PUB_TOPIC_00, "/Div_Servo2 Connected");

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
  //order_scheduler.publish(PUB_TOPIC_01,buf);
  //order_scheduler.publish("/sch/test",buf);
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length){
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  if(strcmp(SUB_TOPIC_01, topic)==0){           // /web/power
    div_ser_power = (int)doc["type"];
    Serial.print("POWER : ");
    Serial.println(div_ser_power);
  }
  else if(strcmp(SUB_TOPIC_02, topic)==0){      //  /div/info
    int orderno = doc["order_num"].as<int>();
    delay(1000);
    if(div_ser_power == 1)
      pubres(orderno,verify());
  }
  else if(strcmp(SUB_TOPIC_03, topic)==0){      //  /mod/div/ser1/angle
    DEVICE_CONFIG_ANGLE = (int)doc["angle"];
    Serial.println(DEVICE_CONFIG_ANGLE);
  }
  else if(strcmp(SUB_TOPIC_04, topic)==0){      //  /mod/div/ser1/servo_interval
    DEVICE_CONFIG_SERVO_INTERVAL = (int)doc["servo_interval"];
    Serial.println(DEVICE_CONFIG_SERVO_INTERVAL);
  }
  else if(strcmp(SUB_TOPIC_05, topic)==0){      //  /mod/div/ser1/ir_interval
    DEVICE_CONFIG_IR_INTERVAL = (int)doc["ir_interval"];
    Serial.println(DEVICE_CONFIG_IR_INTERVAL);
  }
}

void moveservo(Servo* divider,int ANGLELIMIT) {
    delay(DEVICE_CONFIG_SERVO_INTERVAL);
    Serial.println("Motor Move!");
    for (int angle = 0; angle <= ANGLELIMIT; angle++) {
        divider->write(angle);
        delay(10);
    }

    for (int angle = ANGLELIMIT; angle >= 0; angle--) {
        divider->write(angle);
        delay(10);
    }
}

void pubres(int orderno,int flag){
  StaticJsonDocument<100> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[100];
  serializeJson(temp,buf);

  if(!Div_Servo.publish(PUB_TOPIC_01,buf)){
    Serial.println("Publish Fail");
  }
  else{
    Serial.println("Publish Success");
  }
}

int verify(){
  // Mover ServoMotor 
  moveservo(&divider, DEVICE_CONFIG_ANGLE);
  int flag=0,val=0;
  unsigned long prev = millis();
  // Check IR Sensor
  Serial.println("IR Sensor Checking...");
  prev = millis();

  while(1){
    val=digitalRead(SENSORPIN);
    if(val==LOW){
      flag=1;
      break;
    }
    if(millis() - prev >=DEVICE_CONFIG_IR_INTERVAL) break;
  }

  return flag;
}


