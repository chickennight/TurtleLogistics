// Supervisor 
// 1. Rest(Get) -> Polling Every 1 min For get Order List , OrderNo, OrderList, OrderAddress
// 2. Rest(Post) -> When Order Finish or Divide Finish, Polling
// 3. MQTT -> Pub : /sup/add(OrderNo,OrderList), /sup/init(On,Off), /sup/div/info(OrderNo,OrderAddress)
// 4. MQTT -> Sub : /ord/res(OrderNo,Result), /div/res(OrderNo,Result), /web/power(on,off)

#include <secrets.h>

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

WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);
HTTPClient http;

void setupWifi(){
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.println(String("Attempting to connect to SSID: ") + String(WIFI_SSID));

  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(1000);
    Serial.println("Connecting...");
  }
  Serial.println("WiFi Connect Success!");
}

void connectAWS()
{
  delay(1000);
  wifiClient.setCACert(AWS_CERT_CA);
  wifiClient.setCertificate(SUP_CERT);
  wifiClient.setPrivateKey(SUP_PRIKEY);
 
  mqttClient.setServer(AWS_IOT_ENDPOINT, 8883);
  mqttClient.setCallback(messageReceived);
 
  Serial.println("Connecting to AWS IOT");
 
  while (!mqttClient.connect(THINGNAME))
  {
    Serial.print(".");
    delay(1000);
  }
 
  if (!mqttClient.connected()) {
    Serial.println("AWS IoT Timeout!");
    return;
  }
  // Subscribe to a topic
  mqttClient.subscribe(TOPIC_ORDER_RES);
  mqttClient.subscribe(TOPIC_DIV_RES);
  mqttClient.subscribe(TOPIC_POWER);
 
  Serial.println("AWS IoT Connected!");
}


void GETorder(){
  if (WiFi.status() == WL_CONNECTED) 
  {
    http.begin("http://localhost:8080/order/start");
    int httpCode = http.GET();
    Serial.print("HttpCODE:");
    Serial.print(httpCode);
    Serial.print(" From:");
    Serial.print(GET_URL);
    Serial.println();

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
                for(JsonObject order:ordersArray)
                {
                  StaticJsonDocument<200> Data;
                  Data["orderno"] = order["order_num"];
                  Data["ProductA"] = order["productA"];
                  Data["ProductB"] = order["productB"];
                  Data["ProductC"] = order["productC"];

                  char buf1[200];
                  serializeJson(Data,buf1);

                  mqttClient.publish(TOPIC_ORD_SCH, buf1);
                  mqttClient.publish(TOPIC_ORD_VERI, buf1);

                  StaticJsonDocument<100> Data2;
                  Data2["orderno"] = order["order_num"];
                  Data2["address"] = order["adderess"];
                  
                  char buf2[100];
                  serializeJson(Data2,buf2);
                  mqttClient.publish(TOPIC_DIV_VERI,buf2);

                  if(order_motor==-1)
                  {
                    order_motor=1;
                    StaticJsonDocument<10> Motor;
                    Motor["type"]="1";
                    char buf3[12];
                    serializeJson(Motor,buf3); 
                    mqttClient.publish(TOPIC_INIT,buf3);                                     
                  } 
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
  http.begin(POST_URL);
  http.addHeader("Content-Type","application/json");

  int httpResponseCodePost = http.POST(jsonData);

  if(httpResponseCodePost>0){
    String response = http.getString();
    Serial.print("HTTP POST Response:");
    Serial.println(response);
  }
  http.end();
}

void messageReceived(char *topic, byte *payload, unsigned int length)
{

  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);
  

  if(strcmp(topic,TOPIC_ORDER_RES)==0){ // orderno, res
      Serial.println(topic);
      Serial.println(res);
      POSTres(res);
  }
  else if(strcmp(topic,TOPIC_DIV_RES)==0){ // orderno, res
      Serial.println(topic);
      Serial.println(res);
      POSTres(res);
  }
  else if(strcmp(topic,TOPIC_POWER)==0){ // type : 0(on),1(off)
      Serial.println(topic);
      Serial.println(res);
      int val = doc["type"].as<int>();

      if((val!=order_motor) && (val!=div_motor)){
        order_motor*=-1;
        div_motor*=-1;
        StaticJsonDocument<20> temp;
        char buffer[20];
        temp["type"] = val;
        serializeJson(temp,buffer);
        mqttClient.publish("/div/motor", buffer);
        mqttClient.publish("/ord/motor", buffer);
      }
  }
}

void setup() 
{
  Serial.begin(115200);
  setupWifi();
  connectAWS();
}

void loop() 
{
  mqttClient.loop();
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    GETorder();
    // 3초마다 실행할 작업
  }
}
