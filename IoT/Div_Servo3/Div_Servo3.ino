#include "8266secrets.h"

#define THINGNAME "Div_Servo3"
// PUBLISH TOPIC
#define TOPIC_DIV_RES "/div/res"
// SUBSCRIBE TOPIC
#define TOPIC_DIV_VERI "/div/info"
// IRSENSOR PIN NUM
#define SENSORPIN 4
// SERVO MOTOR PIN NUM
#define SERVOPIN 3
// IR SENSOR TIME LIMIT
#define TIMELIMIT 2000
// Servo Motor Angle LIMIT
#define ANGLELIMIT 90

WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);
Servo divider;

void pubres(int orderno, int flag) {
    StaticJsonDocument<100> temp;
    temp["order_num"] = orderno;
    temp["result"] = flag;
    char buf[100];
    serializeJson(temp, buf);

    if (!mqttClient.publish(TOPIC_DIV_RES, buf)) {
        Serial.println("Publish Fail");
    }
    else {
        Serial.println("Publish Success");
    }
}

void messageReceived(char *topic, byte *payload, unsigned int length)
{
  Serial.println("Message Received!");
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  int orderno = doc["order_num"].as<int>();
  delay(1000);
  moveservo(&divider,90);
  pubres(orderno,verify());
}

void setup() 
{
  Serial.begin(115200);

  pinMode(SENSORPIN,INPUT);
  divider.attach(SERVOPIN);
  divider.write(0);

  setupWifi(WIFI_SSID,WIFI_PASSWORD);
  syncTimeWithNTP();
  connectAWS("Div_Servo3",AWS_CERT_CA,DIV_SER3_CERT,DIV_SER3_PRIKEY,AWS_IOT_ENDPOINT,&mqttClient,&wifiClient);
  
  mqttClient.subscribe(TOPIC_DIV_VERI);
  mqttClient.setCallback(messageReceived);
}

void loop() 
{
  wdt_disable();
  if(WiFi.status() != WL_CONNECTED){setupWifi(WIFI_SSID,WIFI_PASSWORD);}
  while (!mqttClient.connect(THINGNAME))
  {
    Serial.print(".");
    delay(1000);
  }
  mqttClient.loop();
}