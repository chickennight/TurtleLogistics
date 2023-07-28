#include "TLClient.h"

#define THINGNAME "Div_Motor"

#define TOPIC_POW_CTR "/mod/web/power" 
#define TOPIC_MOD_SPD "/mod/div/motor/speed"
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


void change();
void WriteLog();
void MSG(String str);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);

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
  div_motor.subscribe(TOPIC_MOD_SPD);
  MSG("AWS Connect Success");
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
    if(power==1)
    {
      MSG("power set by web_power=1");
    }
    else
    {
      MSG("power set by web_power=-1");
    }
    
  }
  else if(strcmp(topic,TOPIC_MOD_SPD)==0)
  {
    speed = doc["speed"].as<int>();
    change();
    MSG("Speed Changed");
  }
  
}


void MSG(String str){
  String base="{\"dev\":\"Div_Motor\",\"content\":\"";
  String base2="\"}";
  div_motor.publish(TOPIC_WEB_LOG, (base+str+base2).c_str());
}
