// Div_Servo3 
// Pub : Supervisor(/div/res) {orderno, result}
// Sub : Div_Veri(/div/info) {orderno}

#include <secrets.h>
// #include <Servo.h>

#define THINGNAME "Div_Servo3"
// PUBLISH TOPIC
#define TOPIC_DIV_RES "/div/res"
// SUBSCRIBE TOPIC
#define TOPIC_DIV_VERI "/div/info"
// IRSENSOR PIN NUM
#define BUZZERPIN 7;
#define SENSORPIN 9;
// SERVO MOTOR PIN NUM
#define SERVOPIN 12;
// IR SENSOR TIME LIMIT
#define LIMIT 3000;

int val;
int myturn=0;

unsigned long prev = millis();
WiFiClientSecure wifiClient;
PubSubClient mqttClient(wifiClient);
HTTPClient http;
// Servo divider;

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
  mqttClient.subscribe(TOPIC_DIV_VERI);
 
  Serial.println("AWS IoT Connected!");
}

// void moveservo(){
//   for(angle = 0; angle<=90; angle++){
//     divider.write(angle);
//     delay(10); 
//   }

//   for(angle = 90; angle>=0; angle--){
//     divider.write(angle);
//     delay(10); 
//   }
// }

void pubres(int orderno,int flag){
  // global var ordernum + result falg
 
  Serial.print("ordernum:");
  Serial.print(orderno);
  Serial.print(" result");
  Serial.print(flag);
  Serial.println();

   StaticJsonDocument<200> temp;
  temp["order_num"] = orderno;
  temp["result"] = flag;
  char buf[200];
  
  serializeJson(temp,buf,sizeof(buf));
  Serial.println(buf); 
  mqttClient.publish(TOPIC_DIV_RES,buf);
}

void messageReceived(char *topic, byte *payload, unsigned int length)
{
  StaticJsonDocument<200> doc;
  DeserializationError error = deserializeJson(doc, payload, length);
  char res[100];
  serializeJson(doc,res);

  int orderno = doc["order_num"].as<int>();
  delay(1000);
  pubres(orderno,1);
  pubres(orderno,0);
}

void setup() 
{
  Serial.begin(115200);
  setupWifi();
  connectAWS();
  // pinMode(BUZZERPIN,OUTPUT);
  // pinMode(SENSORPIN,INPUT);
  // divider.attach(SERVOPIN);
  // divider.write(0);

}

void loop() 
{
  mqttClient.loop();

  // if(myturn==1){
  //   moveservo()
  //   int flag=0;
  //   // n초 안에 초음파 감지 됐는지 확인
  //   prev = millis();
  //   while(1){
  //     vall=digitalRead(SENSORPIN);
  //     if(val==LOW){
  //       flag=1;
  //       break;
  //     }
  //     unsigned long current = millis();
  //     if(current - prev >=LIMIT) break;
  //   }
  //   pubres(flag);
  //   myturn=0;
  // }
}