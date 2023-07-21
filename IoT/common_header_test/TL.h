#pragma once
#include <PubSubClient.h>
#include <WiFiClientSecure.h>
#include <ArduinoJson.h>
#include <Client.h>
#include <time.h>
#include "secrets.h"
unsigned long lastMillis = 0;
unsigned long previousMillis = 0;
const long interval = 5000;

time_t now;
time_t nowish = 1510592825;
int8_t TIME_ZONE = -5;

void connect_NTP(void)
{
    Serial.print("Setting time using SNTP");
    configTime(TIME_ZONE * 3600, 0 * 3600, "pool.ntp.org", "time.nist.gov");
    now = time(nullptr);
    while (now < nowish)
    {
        delay(500);
        Serial.print(".");
        now = time(nullptr);
    }
    Serial.println("done!");
    struct tm timeinfo;
    gmtime_r(&now, &timeinfo);
    Serial.print("Current time: ");
    Serial.print(asctime(&timeinfo));
}

void connect_WiFi() {
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(1000);
        Serial.println("Connecting...");
    }
    Serial.println("Connected!");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
}

void connect_AWS(PubSubClient& mqttClient, WiFiClientSecure& wifiClient, const BearSSL::X509List* CA, const BearSSL::X509List* CERT, const BearSSL::PrivateKey* PRIKEY)
{
    connect_NTP();

    // Set AWS configuration
    wifiClient.setTrustAnchors(CA);
    wifiClient.setClientRSACert(CERT, PRIKEY);

    mqttClient.setServer(AWS_IOT_ENDPOINT, 8883);
    
    Serial.println("Connecting to AWS IOT");

    while (!mqttClient.connected())
    {
        Serial.print(".");
        delay(1000);
        mqttClient.connect(THINGNAME);
    }

    if (!mqttClient.connected()) {
        Serial.println("AWS IoT Timeout!");
        return;
    }


    Serial.println("AWS IoT Connected!");
}
