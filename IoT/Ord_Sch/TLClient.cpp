#include "Arduino.h"
#include "TLClient.h"

#define DEVICE_NUMS 9

String DEVICE_FULLNAME_LIST[9] = { "Supervisor", "Ord_Verifier","Ord_Sch","Ord_Motor","Div_Verifier","Div_Motor","Div_Servo1","Div_Servo2","Div_Servo3" };
const char* DEVICE_NAME_LIST[9] = {SUP_CERT, ORD_VERI_CERT, ORD_SCHE_CERT, ORD_MOTO_CERT, DIV_VERI_CERT, DIV_MOTO_CERT, DIV_SER1_CERT, DIV_SER2_CERT, DIV_SER3_CERT};
const char* DEVICE_KEY_LIST[9] = {SUP_PRIKEY, ORD_VERI_PRIKEY, ORD_SCHE_PRIKEY, ORD_MOTO_PRIKEY, DIV_VERI_PRIKEY, DIV_MOTO_PRIKEY, DIV_SER1_PRIKEY, DIV_SER2_PRIKEY, DIV_SER3_PRIKEY};
const char* SSID = WIFI_SSID;
const char* PASSWORD = WIFI_PASSWORD;
const char* ENDPOINT = AWS_IOT_ENDPOINT;

const char* convert_fullname_to_name(const char* name)
{
  for(int i =0; i<9; i++){
    if(strcmp(DEVICE_FULLNAME_LIST[i].c_str(),name)==0){
       return DEVICE_NAME_LIST[i];
    }
  }
  return "NULLDEVICE";
}

const char* convert_fullname_to_key(const char* name)
{
  for(int i =0; i<9; i++){
    if(strcmp(DEVICE_FULLNAME_LIST[i].c_str(),name)==0){
       return DEVICE_KEY_LIST[i];
    }
  }
  return "NULLDEVICE";
}

TLClient::TLClient(const char* THINGNAME)
{
  this->_wifiClient = new WiFiClientSecure();
  this->_mqttClient = new PubSubClient(*this->_wifiClient);
  this->_THINGNAME = THINGNAME;
  this->_lastMillis = 0;
  this->_previousMillis = 0;
  
  this->_CA = AWS_CERT_CA;
  this->_CERT = NULL;
  this->_PRIVATEKEY = NULL;

  this->_now = NULL;
  this->_nowish = 1510592825;

  this->_WIFI_SSID = SSID;
  this->_WIFI_PASSWORD = PASSWORD;
  this->_AWS_IOT_ENDPOINT = ENDPOINT;
  
}

void TLClient::connect_WiFi(){
  ESP8266WiFiClass WiFi;
  //WiFiClass WiFi;
  this->_WIFI_SSID = SSID;
  this->_WIFI_PASSWORD = PASSWORD;
  WiFi.begin(SSID, PASSWORD);
  while (WiFi.status() != WL_CONNECTED)
  {
      delay(1000);
      Serial.println("Connecting...");
  }
  Serial.println("Connected!");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void TLClient::connect_AWS(){
  
  this->connect_WiFi();
  
  Serial.print("Setting time using SNTP");
  int8_t TIME_ZONE = 9;
  configTime(TIME_ZONE * 3600, 0 * 3600, "pool.ntp.org", "time.nist.gov");
  this->_now = time(nullptr);
  while (this->_now < this->_nowish)
  {
    delay(500);
    Serial.print(".");
    Serial.println(this->_now);
    this->_now = time(nullptr);
  }
  Serial.println("done!");
  struct tm timeinfo;
  gmtime_r(&this->_now, &timeinfo);
  Serial.print("Current time: ");
  Serial.print(asctime(&timeinfo));

  this->_CERT = convert_fullname_to_name(this->_THINGNAME);
  this->_PRIVATEKEY = convert_fullname_to_key(this->_THINGNAME);

  BearSSL::X509List cert(this->_CA);
  BearSSL::X509List client_crt(this->_CERT);
  BearSSL::PrivateKey key(this->_PRIVATEKEY);

  delay(100);
  this->_wifiClient->setTrustAnchors(&cert);
  this->_wifiClient->setClientRSACert(&client_crt, &key);
  this->_mqttClient->setServer(ENDPOINT, 8883);

  Serial.println("Connecting to AWS IoT");

  this->_mqttClient->connect(_THINGNAME);
  while(!(this->_mqttClient->connected()))
  {
    Serial.print(".");
    delay(1000);
    this->_mqttClient->connect(_THINGNAME);
  }

  if (!(this->_mqttClient->connected())) {
    Serial.println("AWS IoT Timeout!");
    return;
  }
  Serial.println("AWS IoT Connected!");
}

bool TLClient::mqttConnected(){
  return this->_mqttClient->connected();
}

boolean TLClient::mqttLoop(){
  return this->_mqttClient->loop();
}

boolean TLClient::subscribe(const char* topic){
  return this->_mqttClient->subscribe(topic);
}

boolean TLClient::publish(const char* topic, const char* payload){
  return this->_mqttClient->publish(topic, payload);
}

TLClient& TLClient::setMqttClient(PubSubClient& mqttClient){
  this->_mqttClient = &mqttClient;
  return *this;
}

TLClient& TLClient::setWifiClient(WiFiClientSecure& wifiClient){
  this->_wifiClient = &wifiClient;
  return *this;
}

TLClient& TLClient::setConfigAWS(const char* CA, const char* CERT, const char* PRIVATEKEY){
  this->_CA = CA;
  this->_CERT = CERT;
  this->_PRIVATEKEY = PRIVATEKEY;
  return *this;
}

TLClient& TLClient::setCallback(MQTT_CALLBACK_SIGNATURE){
  this->_mqttClient = &(this->_mqttClient->setCallback(callback));
  return *this;
};
