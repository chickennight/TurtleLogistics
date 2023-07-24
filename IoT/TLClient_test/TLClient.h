#ifndef TLClient_h
#define TLClient_h

#include "Arduino.h"
#include "secrets.h"
#include <PubSubClient.h>
#include <WiFiClientSecure.h>
#include <ArduinoJson.h>
#include <Client.h>
#include <time.h>

// check your ESP Type :: ESP8266 -> ESP8266WiFi.h / ESP32 -> WiFi.h
#include <ESP8266WiFi.h>
//#include <WiFi.h>

class TLClient
{
  public:
    //TLClient();
    TLClient(const char* THINGNAME);  
    
    ~TLClient() {
      delete this->_mqttClient;
      delete this->_wifiClient;
    }
    
    void connect_WiFi(const char* SSID, const char* PASSWORD);
    void connect_AWS(const char* CA, const char* CERT, const char* PRIVATEKEY, const char* ENDPOINT);
    void connect_AWS();

    bool mqttConnected();
    boolean mqttLoop();
    boolean subscribe(const char* topic);
    boolean publish(const char* topic, const char* payload);

    //String convert_name_to_value(String name);
    
    //TLClient& setMCU(const char*);
    TLClient& setMqttClient(PubSubClient& mqttClient);
    TLClient& setWifiClient(WiFiClientSecure& wifiClient);
    TLClient& setConfigAWS(const char* CA, const char* CERT, const char* PRIVATEKEY);
    TLClient& setCallback(MQTT_CALLBACK_SIGNATURE);
  
  private:
    WiFiClientSecure* _wifiClient;
    PubSubClient* _mqttClient;

    unsigned long _lastMillis;
    unsigned long _previousMillis;
    
    const char* _THINGNAME;
    const char* _MCU_TYPE;
    const char* _CA;
    const char* _CERT;
    const char* _PRIVATEKEY;
    const char* _WIFI_SSID;
    const char* _WIFI_PASSWORD;
    const char* _AWS_IOT_ENDPOINT;

    String _DEVICE_NAME_LIST[9];
    String _DEVICE_VALUE_LIST[9];

    time_t _now;
    time_t _nowish;

};

#endif