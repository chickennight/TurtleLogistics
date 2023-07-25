// Supervisor 
// 1. Rest(Get) -> Polling Every 1 min For get Order List , OrderNo, OrderList, OrderAddress
// 2. Rest(Post) -> When Order Finish or Divide Finish, Polling
// 3. MQTT -> Pub : /sup/add(OrderNo,OrderList), /sup/init(On,Off), /sup/div/info(OrderNo,OrderAddress)
// 4. MQTT -> Sub : /ord/res(OrderNo,Result), /div/res(OrderNo,Result), /web/power(on,off)
#include "TLClient.h"
#include <HTTPClient.h>

#define THINGNAME "Supervisor"
// PUBLISH TOPIC
#define TOPIC_INIT "/sup/init"
#define TOPIC_ORD_SCH "/sup/add"
#define TOPIC_ORD_VERI "/sup/ord/veri"
#define TOPIC_DIV_VERI "/sup/div/veri"

// SUBSCRIBE TOPIC
#define TOPIC_ORDER_RES "/order/res"
#define TOPIC_DIV_RES "/div/res"
#define TOPIC_POWER "/web/power"

int order_motor=-1,div_motor=-1;
unsigned long previousMillis = 0;
const long interval = 60000; // 1min

HTTPClient http;
TLClient supervisor(THINGNAME);

void GETorder();
void POSTres(const char* jsonData);
void Subscribe_callback(char *topic, byte *payload, unsigned int length);

void setup() 
{
  Serial.begin(115200);
  supervisor.setCallback(Subscribe_callback);
  supervisor.connect_AWS();
  supervisor.subscribe(TOPIC_ORDER_RES);
  supervisor.subscribe(TOPIC_DIV_RES);
  supervisor.subscribe(TOPIC_POWER);
 
}

void loop() 
{
  supervisor.mqttLoop();
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    GETorder();
  }
}

void GETorder(){
  if (WiFi.status() == WL_CONNECTED) 
  {
    Serial.println("Request Get For Get ORDER LISTS");
    http.begin("http://localhost:8080/order/start");
    int httpCode = http.GET();
    Serial.print("HttpCODE:");
    Serial.println(httpCode);

    if (httpCode == 200) 
    {
      String response = http.getString();

      DynamicJsonDocument jsonDoc(1024);
      DeserializationError error = deserializeJson(jsonDoc, response);

      if (error) 
      {
              Serial.print("Error parsing JSON: ");
              Serial.println(error.c_str());
      } 
      else 
      {
        if (jsonDoc.is<JsonArray>())
        {
            JsonArray ordersArray = jsonDoc.as<JsonArray>();
            if(!ordersArray.isNull() && ordersArray.size() > 0)
              {
                if(order_motor==-1)
                  {
                    order_motor=1;
                    StaticJsonDocument<10> Motor;
                    Motor["type"]="1";
                    char buf3[12];
                    serializeJson(Motor,buf3); 
                    supervisor.publish(TOPIC_INIT,buf3);                                     
                  } 
                for(JsonObject order:ordersArray)
                {
                  StaticJsonDocument<200> Data;
                  Data["orderno"] = order["order_num"];
                  Data["ProductA"] = order["productA"];
                  Data["ProductB"] = order["productB"];
                  Data["ProductC"] = order["productC"];

                  char buf1[200];
                  serializeJson(Data,buf1);

                  supervisor.publish(TOPIC_ORD_SCH, buf1);
                  supervisor.publish(TOPIC_ORD_VERI, buf1);

                  StaticJsonDocument<100> Data2;
                  Data2["orderno"] = order["order_num"];
                  Data2["address"] = order["adderess"];
                  
                  char buf2[100];
                  serializeJson(Data2,buf2);
                  supervisor.publish(TOPIC_DIV_VERI,buf2);
                }
              }
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
  http.begin("http://localhost:8080/result");
  http.addHeader("Content-Type","application/json");

  int httpResponseCodePost = http.POST(jsonData);

  if(httpResponseCodePost>0){
    String response = http.getString();
    Serial.print("HTTP POST Response:");
    Serial.println(response);
  }
  http.end();
}

void Subscribe_callback(char *topic, byte *payload, unsigned int length)
{

  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);
  

  if(strcmp(topic,TOPIC_ORDER_RES)==0){ // orderno, res
      Serial.println("Get Result From Order Conveyor");
      Serial.println(res);
      POSTres(res);
  }
  else if(strcmp(topic,TOPIC_DIV_RES)==0){ // orderno, res
      Serial.println("Get Result From Divide Conveyor");
      Serial.println(res);
      POSTres(res);
  }
  else if(strcmp(topic,TOPIC_POWER)==0){ // type : 0(on),1(off)
      Serial.println("Motor Power Control From User!");
      int val = doc["type"].as<int>();

      if((val!=order_motor) && (val!=div_motor)){
        if(val==1 or val==-1){
          order_motor*=-1;
          div_motor*=-1;
          StaticJsonDocument<20> temp;
          char buffer[20];
          temp["type"] = val;
          serializeJson(temp,buffer);
          supervisor.publish("/div/motor", buffer);
          supervisor.publish("/ord/motor", buffer);

          Serial.print("Now Order Motor Power : ");
          Serial.println(order_motor);
          Serial.print("Now Divider Motor Power : ");
          Serial.println(div_motor);
        }

      }
  }
}