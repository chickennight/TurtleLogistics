#include "TLClient.h"

TLClient order_scheduler("Ord_Sch");

#define SUB_TOPIC_01 "/sup/add"
#define PUB_TOPIC_01 "/sch/test"

void Publish_callback(int orderno, int flag);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  order_scheduler.setCallback(Subscribe_callback);
  order_scheduler.connect_AWS();
  order_scheduler.subscribe(SUB_TOPIC_01);
  order_scheduler.publish(PUB_TOPIC_01, "hello");
}

void loop() {
  order_scheduler.mqttLoop();
}


void Publish_callback(int orderno,int flag){
  StaticJsonDocument<200> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[200];
  serializeJson(temp,buf,sizeof(buf));
  Serial.println(buf); 
  order_scheduler.publish(PUB_TOPIC_01,buf);
  order_scheduler.publish("/sch/test",buf);
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length){
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
  Publish_callback(1,1);
}
