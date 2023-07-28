#include "TLClient.h"

#define THINGNAME "Div_Motor"

#define TOPIC_POW_CTR "/mod/web/power" 
#define TOPIC_MOT_SPD "/mod/div/motor/speed"
#define TOPIC_WEB_LOG "/log"

int power=-1;
int speed = 200;

int motor1Pin1 = 27; 
int motor1Pin2 = 26; 
int enable1Pin = 14; 

const int freq = 30000;
const int pwmChannel = 0;
const int resolution = 8;

TLClient div_motor(THINGNAME);

void Subscribe_callback(char *topic, byte *payload, unsigned int length);
void change();
void WriteLog();

void setup() 
{
  Serial.begin(115200);
  // sets the pins as outputs:
  pinMode(motor1Pin1, OUTPUT);
  pinMode(motor1Pin2, OUTPUT);
  pinMode(enable1Pin, OUTPUT);
  
  // configure LED PWM functionalitites
  ledcSetup(pwmChannel, freq, resolution);
    
  // attach the channel to the GPIO to be controlled
  ledcAttachPin(enable1Pin, pwmChannel);

  div_motor.setCallback(Subscribe_callback);
  div_motor.connect_AWS();
  div_motor.subscribe(TOPIC_POW_CTR);
  div_motor.subscribe(TOPIC_MOT_POW);
  div_motor.subscribe(TOPIC_MOT_SPD);

  div_motor.publish(TOPIC_WEB_LOG,"{\"dev\":\"Div_Motor\",\"content\":\"AWS Connect Success\"}");
}

void loop() 
{
  div_motor.mqttLoop();
}

void change()
{

  if(power==1)
  {
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, speed);
  }
  else
  {
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, 0);
  }
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length)
{
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);

  if(strcmp(topic,TOPIC_POW_CTR)==0)
  {  
    power = doc["power"].as<int>();
    change();
    WriteLog("power");
  }
  else if(strcmp(topic,TOPIC_MOT_SPD)==0)
  {
    speed = doc["speed"].as<int>();
    change();
    WriteLog("speed");
  }
  
}

void WriteLog(const char* str){
  if(strcmp(str,"power")==0)
  {
    if(power==1)
        {
          div_motor.publish(TOPIC_WEB_LOG,"{\"dev\":\"Div_Motor\",\"content\":\"User Turn on Motor!\"}");
        }
        else
        {
          div_motor.publish(TOPIC_WEB_LOG,"{\"dev\":\"Div_Motor\",\"content\":\"User Turn off Motor!\"}");
        }
  }
  else if(strcmp(str,"speed")==0)
  {
    div_motor.publish(TOPIC_WEB_LOG,"{\"dev\":\"Div_Motor\",\"content\":\"Change Speed!\"}");
  }

}