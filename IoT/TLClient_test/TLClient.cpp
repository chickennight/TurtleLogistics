#include "Arduino.h"
#include "TLClient.h"

TLClient::TLClient(const char* THINGNAME)
{
  this->_THINGNAME = THINGNAME;
  this->_mqttClient = NULL;
  this->_wifiClient = NULL;
  this->_lastMillis = 0;
  this->_previousMillis = 0;
  //this->_interval;
  
  this->_CA = NULL;
  this->_CERT = NULL;
  this->_PRIVATEKEY = NULL;
  this->_now = NULL;
  this->_nowish = 1510592825;

}

void TLClient::connect_WiFi(const char* SSID, const char* PASSWORD){
  ESP8266WiFiClass WiFi;
  //WiFiClass WiFi;
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

void TLClient::connect_AWS(const char* CA, const char* CERT, const char* PRIVATEKEY, const char* ENDPOINT){

  Serial.print("Setting time using SNTP");
  int8_t TIME_ZONE = 9;
  configTime(TIME_ZONE * 3600, 0 * 3600, "pool.ntp.org", "time.nist.gov");
  this->_now = time(nullptr);
  while (this->_now < this->_nowish)
  {
    delay(500);
    Serial.print(".");
    this->_now = time(nullptr);
  }
  Serial.println("done!");
  struct tm timeinfo;
  gmtime_r(&this->_now, &timeinfo);
  Serial.print("Current time: ");
  Serial.print(asctime(&timeinfo));

  BearSSL::X509List cert(CA);
  BearSSL::X509List client_crt(CERT);
  BearSSL::PrivateKey key(PRIVATEKEY);

  delay(1000);
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
