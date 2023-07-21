// Supervisor 
// 1. Rest(Get) -> Polling Every 1 min For get Order List , OrderNo, OrderList, OrderAddress
// 2. Rest(Post) -> When Order Finish or Divide Finish, Polling
// 3. MQTT -> Pub : /sup/add(OrderNo,OrderList), /sup/init(On,Off), /sup/div/info(OrderNo,OrderAddress)
// 4. MQTT -> Sub : /ord/res(OrderNo,Result), /div/res(OrderNo,Result), /web/power(on,off)

#include <secrets.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <time.h>
#include <WiFiClientSecure.h>
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

// POST URL
#define POST_URL "1"

int order_motor=0,div_motor=0;
unsigned long previousMillis = 0;
const long interval = 60000; // 1min

WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);

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

void postRes(byte *payload){
  HTTPClient http;
  http.begin(POST_URL);
  http.addHeader("Content-Type","application/json");
  Serial.println((String)payload[0]);


  StaticJsonDocument<200> data;
  data["type"]=(String)payload[0];
  data["result"]=(String)payload[1];
  String postData;
  serializeJson(data,postData);

  int httpResponseCodePost = http.POST(postData);

  if(httpResponseCodePost>0){
    String response = http.getString();
    Serial.print("HTTP POST Response:");
    Serial.println(response);
  }
  http.end();
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
  mqttClient.subscribe(TOPIC_ORDERRES);
  mqttClient.subscribe(TOPIC_DIVRES);
  mqttClient.subscribe(TOPIC_POWER);
 
  Serial.println("AWS IoT Connected!");
}
// When Sup Sub
void messageReceived(char *topic, byte *payload, unsigned int length)
{

  if(strcmp(topic,TOPIC_ORDERRES)==0){ // orderno, res
      Serial.println(topic);
      postRes(payload);
  }
  else if(strcmp(topic,TOPIC_DIVRES)==0){ // orderno, res
      Serial.println(topic);
      postRes(payload);
  }
  else if(strcmp(topic,TOPIC_POWER)==0){ // type : 0(on),1(off)
      Serial.println(topic);
      StaticJsonDocument<200> data;
      deserializeJson(data, payload);
      StaticJsonDocument<100> temp;
      temp["type"] = data["type"];
      char jsonBuffer[512];
      serializeJson(temp, jsonBuffer);
      Serial.println(jsonBuffer);

      mqttClient.publish("/d", jsonBuffer);
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
    getOrder();
    // 3초마다 실행할 작업
  }
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

      DynamicJsonDocument jsonDoc(1024);
      DeserializationError error = deserializeJson(jsonDoc, response);

      if (error) {
              Serial.print("Error parsing JSON: ");
              Serial.println(error.c_str());
      } 
      else {
        if (jsonDoc.is<JsonArray>()){
            JsonArray ordersArray = jsonDoc.as<JsonArray>();
            if(!ordersArray.isNull() && ordersArray.size() > 0){
                for(JsonObject order:ordersArray){
                  // MQTT pulish data 1,2,3 to Sch
                  StaticJsonDocument<200> Sch;
                  Sch["ProductA"] = order["productA"];
                  Sch["ProductB"] = order["productB"];
                  Sch["ProductC"] = order["productC"];
                  char buf1[512];
                  serializeJson(Sch,buf1);
                  mqttClient.publish(TOPIC_SCH, buf1);
                  StaticJsonDocument<200> Verifier;
                  Verifier["orderno"] = order["order_num"];
                  Verifier["ProductA"] = order["productA"];
                  Verifier["ProductB"] = order["productB"];
                  Verifier["ProductC"] = order["productC"];
                  char buf2[512];
                  serializeJson(Verifier,buf2);
                  mqttClient.publish(TOPIC_TOVERI,buf2);
                  if(!order_motor){
                    StaticJsonDocument<10> motor;
                    motor["type"]="1";
                    char buf[12];
                    serializeJson(motor,buf); 
                    mqttClient.publish(TOPIC_INIT,buf);                                     
                  } 
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



