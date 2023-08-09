#include "TLClient.h"
#include <HTTPClient.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>

#define THINGNAME "Supervisor"
// PUBLISH TOPIC
#define TOPIC_ORD_SCH "/sup/ord/sch/info"
#define TOPIC_ORD_VER "/sup/ord/veri/info"
#define TOPIC_DIV_VER "/sup/div/veri/info"
#define TOPIC_ENV_INFO "/sup/div/veri/env/info"
#define TOPIC_WEB_LOG "/log"

// SUBSCRIBE TOPIC
#define TOPIC_ORD_RES "/ord/res"
#define TOPIC_DIV_RES "/div/res"
#define TOPIC_WEB_POW "/mod/web/power"

int power=-1;
unsigned long httpMillis = 0;
unsigned long envMillis = 0;
const long interval = 60000; // 1min


DHT_Unified dht(13,DHT11);
HTTPClient http;
StaticJsonDocument<200> EnvData;
TLClient supervisor(THINGNAME);


void GETorder();
void checkmotor();
void MSG(const char* strcmp);
void POSTres(const char* jsonData);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);

void setup() 
{
  Serial.begin(115200);
  sensor_t sensor;
  dht.begin();
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);
  supervisor.connect_AWS();
  supervisor.setCallback(Subscribe_callback);
  supervisor.subscribe(TOPIC_ORD_RES);
  supervisor.subscribe(TOPIC_DIV_RES);
  supervisor.subscribe(TOPIC_WEB_POW);

  supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"AWS Connect Success\"}");
}

void loop() 
{
  supervisor.mqttLoop();
  unsigned long currentMillis = millis();
  if (currentMillis - httpMillis >= interval && power==1) {
    httpMillis = currentMillis;
    GETorder();
    supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Get Order Lists\"}");
  }
  if (currentMillis - envMillis >= 10000) {
    envMillis = currentMillis;
    sensors_event_t event;
    dht.temperature().getEvent(&event);
    int temp = event.temperature;
    EnvData["temp"] = temp;
    Serial.print("Temperature : ");
    Serial.println( temp);

    dht.humidity().getEvent(&event);
    int humid = event.relative_humidity;
    EnvData["humid"] = humid;
    Serial.print("Humidity : ");
    Serial.println(humid);

    supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Env Pub\"}");
    char buf1[200];
    serializeJson(EnvData,buf1);
    supervisor.publish(TOPIC_ENV_INFO,buf1);
  }
  
}

void GETorder(){
  if (WiFi.status() == WL_CONNECTED) 
  {
    http.begin("https://i9A204.p.ssafy.io/turtle/order/start");
    int httpCode = http.GET();

    if (httpCode == 200) 
    {
      String response = http.getString();
      DynamicJsonDocument jsonDoc(8000);
      DeserializationError error = deserializeJson(jsonDoc, response);
      if (error) 
      {
          supervisor.publish(TOPIC_WEB_LOG,error.c_str());
      } 
      else 
      {
        if (jsonDoc.is<JsonArray>())
        {
            JsonArray ordersArray = jsonDoc.as<JsonArray>();
            for(JsonObject order:ordersArray)
            {
              StaticJsonDocument<200> Data;
              Data["order_num"] = order["order_num"];
              Data["ProductA"] = order["productA"];
              Data["ProductB"] = order["productB"];
              Data["ProductC"] = order["productC"];

              char buf1[200];
              serializeJson(Data,buf1);

              supervisor.publish(TOPIC_ORD_SCH, buf1);
              supervisor.publish(TOPIC_ORD_VER, buf1);
              supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Pub Ord Sch OrderLists\"}");
              supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Pub Ord Veri OrderLists\"}");
              StaticJsonDocument<100> Data2;
              Data2["order_num"] = order["order_num"];
              Data2["address"] = order["address"];
              
              char buf2[100];
              serializeJson(Data2,buf2);
              supervisor.publish(TOPIC_DIV_VER,buf2);
              supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Pub Div Veri OrderLists\"}");
            }
        }
        else
        {
          StaticJsonDocument<200> Data;
          Data["order_num"] = jsonDoc["order_num"];
          Data["ProductA"] = jsonDoc["ProductA"];
          Data["ProductB"] = jsonDoc["ProductB"];
          Data["ProductC"] = jsonDoc["ProductC"];

          char buf1[200];
          serializeJson(Data,buf1);

          supervisor.publish(TOPIC_ORD_SCH, buf1);
          supervisor.publish(TOPIC_ORD_VER, buf1);
          supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Pub Ord Sch OrderLists\"}");
          supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Pub Ord Veri OrderLists\"}");
          StaticJsonDocument<100> Data2;
          Data2["order_num"] = jsonDoc["order_num"];
          Data2["address"] = jsonDoc["adderess"];
          
          char buf2[100];
          serializeJson(Data2,buf2);
          supervisor.publish(TOPIC_DIV_VER,buf2);
          supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Pub Div Veri OrderLists\"}");
        }
      }
    }
    http.end(); 
  }
  else
  {
    Serial.println("Wifi is not connected!");
  }

}

void POSTres(const char* jsonData){
  http.begin("https://i9A204.p.ssafy.io/turtle/order/update");
  http.addHeader("Content-Type","application/json");

  int httpCode = http.PUT(jsonData);
  if(httpCode==200)
  {
    supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Post Success\"}");
  }
  else
  {
    supervisor.publish(TOPIC_WEB_LOG, "{\"dev\":\"Supervisor\",\"content\":\"Post Fail\"}");
  }
  http.end();
  
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length)
{
  StaticJsonDocument<100> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);  
  if(strcmp(topic,TOPIC_ORD_RES)==0 ||strcmp(topic,TOPIC_DIV_RES)==0 )
  { 
      POSTres(res);
  }
  else if(strcmp(topic,TOPIC_WEB_POW)==0)
  { 
    power = doc["power"].as<int>();
  }
  
}
