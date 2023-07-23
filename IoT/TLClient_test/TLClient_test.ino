#include "TLClient.h"

WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);
TLClient order_scheduler("Arduino_D1");

const char* WIFI_SSID = "seogau";
const char* WIFI_PASSWORD = "1234567890";
//const char AWS_IOT_ENDPOINT[] = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com";

unsigned long lastMillis = 0;
unsigned long previousMillis = 0;

#define SUB_TOPIC_01 "/sup/add"
#define PUB_TOPIC_01 "/sch/test"

void messageReceived(char *topic, byte *payload, unsigned int length){
  StaticJsonDocument<200> doc;
  Serial.print("Received [");
  Serial.print(topic);
  Serial.print("]: ");
  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
      // Deserialize the JSON document'
  }

  // Deserialize the JSON document
  DeserializationError error = deserializeJson(doc, payload);

  // Test if parsing succeeds.
  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    return;
  }
}

void messageServing(int orderno,int flag){
  StaticJsonDocument<200> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[200];
  serializeJson(temp,buf,sizeof(buf));
  Serial.println(buf); 
  order_scheduler.publish("/sch/test",buf);
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  order_scheduler.setMqttClient(mqttClient);
  order_scheduler.setWifiClient(wifiClient);
  order_scheduler.setCallback(messageReceived);

  order_scheduler.connect_WiFi(WIFI_SSID, WIFI_PASSWORD);
  order_scheduler.connect_AWS(AWS_CERT_CA, AWS_CERT_CRT, AWS_CERT_PRIVATE, AWS_IOT_ENDPOINT);
  order_scheduler.subscribe("/sup/add");
  messageServing(1, 1);
}

void loop() {
  // put your main code here, to run repeatedly:
  order_scheduler.mqttLoop();


}
