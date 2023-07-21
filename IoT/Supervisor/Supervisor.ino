// Supervisor 
// 1. Rest(Get) -> Polling Every 1 min For get Order List , OrderNo, OrderList, OrderAddress
// 2. Rest(Post) -> When Order Finish or Divide Finish, Polling
// 3. MQTT -> Pub : /sup/add(OrderNo,OrderList), /sup/init(On,Off), /sup/div/info(OrderNo,OrderAddress)
// 4. MQTT -> Sub : /ord/res(OrderNo,Result), /div/res(OrderNo,Result), /web/power(on,off)

#include <secrets.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <WiFi.h>
#include <time.h>
#include <WiFiClientSecure.h>
#include <secrets.h>
#include <PubSubClient.h>

#define THINGNAME "Supervisor"
// PUBLISH TOPIC
#define TOPIC_SCH "/sup/add"
#define TOPIC_INIT "/sup/init"
#define TOPIC_TOVERI "/sup/div/info"

// SUBSCRIBE TOPIC
#define TOPIC_ORDERRES "/order/res"
#define TOPIC_DIVRES "/div/res"
#define TOPIC_POWER "/web/power"

int order_motor=0,div_motor=0;
WifiClient wifiClient;
PubSubClient mqttClient(wifiClient);

void setup() 
{
  Serial.begin(115200);
  setupWifi();
  connectAWS();
}

void loop() 
{
  getOrder(); 
  delay(60000);
}

void getOrder(){
  if (WiFi.status() == WL_CONNECTED) 
  {
    HTTPClient http; //Object of class HTTPClient
    http.begin("http://jsonplaceholder.typicode.com/users/1");
    int httpCode = http.GET();
    Serial.println(httpCode);
    if (httpCode > 0) 
    {
      Serial.println("JSON PARSING...");

      String response = http.getString();
      // Serial.print("HTTP GET Response: ");
      // Serial.println(response);

      DynamicJsonDocument jsonDoc(1024);
      DeserializationError error = deserializeJson(jsonDoc, response);

      if (error) {
              Serial.print("Error parsing JSON: ");
              Serial.println(error.c_str());
      } 
      else() {
        if (jsonDoc.is<JsonArray>()){
            JsonArray ordersArray = jsonDoc.as<JsonArray>();
            if(!ordersArray.isNull() && ordersArray.size() > 0){
                for(JsonObject order:ordersArray){
                  int data[5];
                  data[0] = order["order_num"];
                  data[1] = order["productA"];
                  data[2] = order["productB"];
                  data[3] = order["prodcutC"];
                  data[4] = order["address"];

                  // MQTT pulish data 1,2,3 to Sch
                  StaticJsonDocument<200> Sch;
                  Sch["order_num"] = data[0];
                  Sch["ProductA"] = data[1];
                  Sch["ProductB"] = data[2];
                  Sch["ProductC"] = data[3];
                  char jsonBuffer[512];
                  serializeJson(Sch,jsonBuffer);
                  mqttclient.publish(TOPIC_TOSCH, jsonBuffer);
                  // MQTT publish data 0,1,2,3 to Verifier

                  // MQTT publish to order motor 1 
                  if(!order_motor){
                    
                  } 
                }
              }
            }
      }
    http.end(); //Close connection
  }
  else{
    Serial.println("Wifi is not connected!");
  }
}

void postRes(){
}

void connectAWS()
{
  delay(1000);
  wifiClient.setTrustAnchors(&AWS_CERT_CA);
  wifiClient.setClientRSACert(&SUP_CERT, &SUP_PRIKEY);
 
  mqttclient.setServer(AWS_IOT_ENDPOINT, 8883);
  mqttclient.setCallback(messageReceived);
 
  Serial.println("Connecting to AWS IOT");
 
  while (!mqttclient.connect(THINGNAME))
  {
    Serial.print(".");
    delay(1000);
  }
 
  if (!mqttclient.connected()) {
    Serial.println("AWS IoT Timeout!");
    return;
  }
  // Subscribe to a topic
  client.subscribe(AWS_IOT_SUBSCRIBE_TOPIC1);
 
  Serial.println("AWS IoT Connected!");
}
// When Sup Sub
void messageReceived(char *topic, byte *payload, unsigned int length)
{

  if(strcmp(topic,TOPIC_ORDERRES)==0){ // orderno, res
      postRes();
  }
  else if(strcmp(topic,TOPIC_DIVRES)==0){ // orderno, res
      postRes();
  }
  else if(strcmp(topic,TOPIC_POWER)==0){ // type : 0(on),1(off)
      StaticJsonDocument<200> data;
      deserializeJson(data, payload);
      PowerMsg(topic,data["type"]);
  } 
}

void PowerMsg(char *topic,char* type ){
  StaticJsonDocument<200> data;
  data["type"] = type;
  char jsonBuffer[512];
  serializeJson(doc, jsonBuffer); // print to client
  mqttclient.publish(topic, jsonBuffer);
}


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
void syncTimeWithNTP()
{
  configTime(9 * 3600, 0, "pool.ntp.org"); // GMT+9 (한국 표준시)로 설정, 원하는 시간대로 변경 가능
  Serial.print("Waiting for time sync...");
  while (time(nullptr) < 1610880000) // 기준 시간 (Unix timestamp) 이전까지 대기 (2021년 1월 17일 00:00:00)
  {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  time_t now = time(nullptr);
  struct tm timeinfo;
  gmtime_r(&now, &timeinfo);

  Serial.print("Current time: ");
  Serial.println(asctime(&timeinfo));
}