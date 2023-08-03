#include "TLClient.h"

#define THINGNAME "Ord_Motor"
#define TOPIC_POW_CTR "/mod/web/power" 
#define TOPIC_MOT_POW "/mod/ord/motor/power"
#define TOPIC_MOT_SPD "/mod/ord/motor/speed"
#define TOPIC_WEB_LOG "/log"

int power=-1;
int speed = 210;

int motor1Pin1 = 27; 
int motor1Pin2 = 26; 
int enable1Pin = 14; 

const int freq = 30000;
const int pwmChannel = 0;
const int resolution = 8;

TLClient div_motor(THINGNAME);
void MSG(String str);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);

void setup() 
{
  Serial.begin(115200);
  pinMode(motor1Pin1, OUTPUT);
  pinMode(motor1Pin2, OUTPUT);
  pinMode(enable1Pin, OUTPUT);
  
  ledcSetup(pwmChannel, freq, resolution);
    
  ledcAttachPin(enable1Pin, pwmChannel);

  div_motor.setCallback(Subscribe_callback);
  div_motor.connect_AWS();
  MSG("AWS Connect Success");
  div_motor.subscribe(TOPIC_POW_CTR);
  div_motor.subscribe(TOPIC_MOT_POW);
  div_motor.subscribe(TOPIC_MOT_SPD);
}

void loop() 
{
  div_motor.mqttLoop();
  
  digitalWrite(motor1Pin1, HIGH);
  digitalWrite(motor1Pin2, LOW);

  if(power==1) ledcWrite(pwmChannel, speed); 
  else if(power==-1) ledcWrite(pwmChannel, 0); 

}

void Subscribe_callback(char *topic, byte *payload, unsigned int length)
{
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);

  if(strcmp(topic,TOPIC_POW_CTR)==0)
  {
    power = doc["power"].as<int>();
    if(power == 1) MSG("power set by web_power = 1");
    else if (power == -1) MSG("power set by web_power = -1");
  }

  else if(strcmp(topic,TOPIC_MOT_POW)==0)
  {
    power = doc["power"].as<int>();
    if(power == 1) MSG("power set by device_power = 1");
    else if (power == -1) MSG("power set by device_power = -1");
  }
  
  else if(strcmp(topic,TOPIC_MOT_SPD)==0)
  {
    speed = doc["speed"].as<int>();
    MSG("Speed Changed");
  }
  
}

void MSG(String str){
  String base="{\"dev\":\"Ord_Motor\",\"content\":\"";
  String base2="\"}";
  div_motor.publish(TOPIC_WEB_LOG, (base+str+base2).c_str());
}


