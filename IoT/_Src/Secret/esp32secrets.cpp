#include "32secrets.h"


void connectAWS(const char* THINGNAME,const char* AWS_CERT_CA,const char* DEVICE_CERT, const char* PRIKEY, const char* AWS_IOT_ENDPOINT, PubSubClient* mqttClient,WiFiClientSecure* wifiClient)
{
    delay(1000);
    wifiClient->setCACert(AWS_CERT_CA);
    wifiClient->setCertificate(DEVICE_CERT);
    wifiClient->setPrivateKey(PRIKEY);

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
    Serial.println("Connect to My Thing Success");
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
    configTime(9 * 3600, 0, "pool.ntp.org", "time.nist.gov"); // GMT+9 (�ѱ� ǥ�ؽ�)�� ����, ���ϴ� �ð���� ���� ����
    time_t now = time(nullptr);
    while (now < 1610880000) // ���� �ð� (Unix timestamp) �������� ��� (2021�� 1�� 17�� 00:00:00)
    {
        delay(500);
        now = time(nullptr);
    }

    now = time(nullptr);
    struct tm timeinfo;
    gmtime_r(&now, &timeinfo);
    return;

}