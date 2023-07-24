#include "32secrets.h"

#define THINGNAME "Ord_Motor"
// SUBSCRIBE TOPIC
#define TOPIC_INIT "/sup/init"
#define TOPIC_POWER "/web/power"

// -1: power off, 1: power on
int powered=-1;

// Motor A
int motor1Pin1 = 27; 
int motor1Pin2 = 26; 
int enable1Pin = 14; 

// Setting PWM properties
const int freq = 30000;
const int pwmChannel = 0;
const int resolution = 8;

WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);

void changePower(){

  if(powered==1){
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, 200); 
    Serial.println("Motor Speed : 200");
  }
  else{
    digitalWrite(motor1Pin1, HIGH);
    digitalWrite(motor1Pin2, LOW);
    ledcWrite(pwmChannel, 0); 
    Serial.println("Motor Speed : 0");
  }
}

void messageReceived(char *topic, byte *payload, unsigned int length)
{

  if(strcmp(topic,TOPIC_POWER)==0 && powered!=1){
    powered=1;
    changePower();
    Serial.println("MOTOR INIT!");
  }
  else{
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, payload, length);
    char res[100];
    serializeJson(doc,res);
    if(doc["power"].as<int>()!=powered)
      {
        Serial.println("Power Change!");
        powered*=-1;
        changePower();
      }
  }
}

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

  setupWifi(WIFI_SSID,WIFI_PASSWORD);
  connectAWS(THINGNAME,AWS_CERT_CA,ORD_MOTO_CERT,ORD_MOTO_PRIKEY,AWS_IOT_ENDPOINT,&mqttClient,&wifiClient);

  mqttClient.setCallback(messageReceived);
  mqttClient.subscribe(TOPIC_INIT);
  mqttClient.subscribe(TOPIC_POWER);
}

void loop() 
{
  
  mqttClient.loop();
}
