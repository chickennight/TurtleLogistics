#include "8266secrets.h"

void moveservo(Servo* divider,int ANGLELIMIT) {
    Serial.println("Motor Move!");
    for (int angle = 0; angle <= ANGLELIMIT; angle++) {
        divider->write(angle);
        delay(10);
    }

    for (int angle = ANGLELIMIT; angle >= 0; angle--) {
        divider->write(angle);
        delay(10);
    }
}

int verify() {
    int flag = 0, val = 0;
    unsigned long prev = millis();
    // Check IR Sensor
    Serial.println("IR Sensor Checking...");
    while (millis() - prev <= 2000) {
        ESP.wdtDisable();
        val = digitalRead(3);
        if (val == LOW) {
            Serial.println("Find!!");
            flag = 1;
            break;
        }
    }
    return flag;
}

void connectAWS(const char* THINGNAME,const char* AWS_CERT_CA,const char* DEVICE_CERT, const char* PRIKEY, const char* AWS_IOT_ENDPOINT, PubSubClient* mqttClient,WiFiClientSecure* wifiClient)
{
    BearSSL::X509List cert(AWS_CERT_CA);
    BearSSL::X509List client_crt(DEVICE_CERT);
    BearSSL::PrivateKey key(PRIKEY);
    
    delay(1000);

    wifiClient->setTrustAnchors(&cert);
    wifiClient->setClientRSACert(&client_crt, &key);

    mqttClient->setServer(AWS_IOT_ENDPOINT, 8883);

    Serial.println("Connecting to AWS IOT");

    while (!mqttClient->connect(THINGNAME))
    {
        Serial.print(".");
        delay(1000);
    }
    Serial.println("My IoT Thing Connected!");
    if (!mqttClient->connected()) {
        Serial.println("AWS IoT Timeout!");
        return;
    }
    // Subscribe to a topic
}

void setupWifi(const char* WIFI_SSID, const char* WIFI_PASSWORD)
{
    WiFi.mode(WIFI_STA);
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.println(String("Attempting to connect to SSID: ") + String(WIFI_SSID));

    while (WiFi.status() != WL_CONNECTED)
    {
        delay(1000);
        Serial.println("Connecting...");
    }
    Serial.println("WiFi Connect Success!");
    return;
}

void syncTimeWithNTP()
{
    configTime(9 * 3600, 0, "pool.ntp.org", "time.nist.gov"); // GMT+9 (한국 표준시)로 설정, 원하는 시간대로 변경 가능
    time_t now = time(nullptr);
    while (now < 1610880000) // 기준 시간 (Unix timestamp) 이전까지 대기 (2021년 1월 17일 00:00:00)
    {
        delay(500);
        now = time(nullptr);
    }

    now = time(nullptr);
    struct tm timeinfo;
    gmtime_r(&now, &timeinfo);
    return;

}