#include "TLClient.h"
WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);

//const char* WIFI_SSID = "seogau";
//const char* WIFI_PASSWORD = "1234567890";

TLClient order_scheduler("Ord_Sch");
//TLClient order_scheduler(wifiClient, mqttClient, "Arduino_D1", AWS_CERT_CRT, AWS_CERT_PRIVATE);

unsigned long lastMillis = 0;
unsigned long previousMillis = 0;

#define SUB_TOPIC_01 "/sup/add"
#define PUB_TOPIC_01 "/sch/test"

void messageServing(int orderno,int flag){
  StaticJsonDocument<200> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[200];
  serializeJson(temp,buf,sizeof(buf));
  Serial.println(buf); 
  order_scheduler.publish(PUB_TOPIC_01,buf);
  mqttClient.publish("/sch/test",buf);
}

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
  messageServing(1, 1);
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  order_scheduler.setCallback(messageReceived);
  order_scheduler.connect_AWS();
  order_scheduler.subscribe(SUB_TOPIC_01);
  order_scheduler.publish(PUB_TOPIC_01, "hello");
}

void loop() {
  order_scheduler.mqttLoop();
  delay(100);
}
