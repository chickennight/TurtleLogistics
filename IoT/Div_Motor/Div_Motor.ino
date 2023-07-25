#include "TLClient.h"

#define THINGNAME "Div_Motor"
// SUBSCRIBE TOPIC
#define TOPIC_INIT "/sup/init"
#define TOPIC_POWER "/web/power"
#define TOPIC_CHANGE "/mod/div/motor/speed"
#define TOPIC_CONNECT "/connect"

// -1: power off, 1: power on
int powered=-1;
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
  div_motor.publish(TOPIC_CONNECT,"/Div/Motor Connected");
  div_motor.subscribe(TOPIC_INIT);
  div_motor.subscribe(TOPIC_POWER);
  div_motor.subscribe(TOPIC_CHANGE);
}

void loop() 
{
  div_motor.mqttLoop();
}

void changePower(){

  if(powered==1){
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, speed); 
    Serial.print("Motor Speed : ");
    Serial.println(speed);

  }
  else{
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, 0); 
    Serial.println("Motor Speed : 0");
  }
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length)
{

  if(strcmp(topic,TOPIC_INIT)==0 && powered!=1){
    powered=1;
    changePower();
    Serial.println("MOTOR INIT!");
  }
  else{
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, payload, length);
    // char res[100];
    // serializeJson(doc,res);
    if(strcmp(topic,TOPIC_POWER)==0){
    
      if(doc["type"].as<int>()!=powered)
        {
          Serial.println("Power Change!");
          powered*=-1;
          changePower();
        }
    }
    else if(strcmp(topic,TOPIC_CHANGE)==0 && powered==1){
      if(doc["speed"].as<int>()!=speed)
      {
        speed = doc["speed"].as<int>();
        changePower();
      }
    
    }
  }
}