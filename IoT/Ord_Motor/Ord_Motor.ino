#include "TLClient.h"

#define THINGNAME "Ord_Motor"
// SUBSCRIBE TOPIC
#define TOPIC_POWER "/mod/web/power" // Sup ctrl
#define TOPIC_INIT "/mod/ord/motor/power"
#define TOPIC_CHANGE "/mod/ord/motor/speed"
#define TOPIC_LOG "/log"

// -1: power off, 1: power on
int power=-1;
int speed = 200;

// Motor A
int motor1Pin1 = 27; 
int motor1Pin2 = 26; 
int enable1Pin = 14; 

// Setting PWM properties
const int freq = 30000;
const int pwmChannel = 0;
const int resolution = 8;

TLClient div_motor(THINGNAME);

void changePower();
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
  div_motor.publish(TOPIC_LOG,"{\"dev\":\"Ord_Motor\",\"content\":\"AWS Connect Success\"}");
  div_motor.subscribe(TOPIC_INIT);
  div_motor.subscribe(TOPIC_POWER);
  div_motor.subscribe(TOPIC_CHANGE);
}

void loop() 
{
  div_motor.mqttLoop();
}

void changePower(){

  if(power==1){
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, speed); 
    div_motor.publish(TOPIC_LOG,"{\"dev\":\"Ord_Motor\",\"content\":\"Motor Speed Change\"}");
    Serial.print("Motor Speed : ");
    Serial.println(speed);

  }
  else{
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, 0); 
    div_motor.publish(TOPIC_LOG,"{\"dev\":\"Ord_Motor\",\"content\":\"Turn Off Motor\"}");
    Serial.println("Motor Speed : 0");
  }
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length)
{

  if(strcmp(topic,TOPIC_INIT)==0){
    power*=-1;
    changePower();
    div_motor.publish(TOPIC_LOG,"{\"dev\":\"Ord_Motor\",\"content\":\"Motor INIT!\"}");
    Serial.println("MOTOR INIT!");
  }
  else{
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, payload, length);
    if(strcmp(topic,TOPIC_POWER)==0){
    
      if(doc["power"].as<int>()!=power)
        {
          Serial.println("Power Change!");
          power*=-1;
          changePower();
        }
    }
    else if(strcmp(topic,TOPIC_CHANGE)==0 && power==1){
      if(doc["speed"].as<int>()!=speed)
      {
        speed = doc["speed"].as<int>();
        changePower();
      }
    
    }
  }
  
}