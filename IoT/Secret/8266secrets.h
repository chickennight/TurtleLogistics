#include <ESP8266WiFi.h>
#include <time.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <WiFiClientSecure.h>
#include <Servo.h>

#define SECRET

const char WIFI_SSID[] = "AndroidHotspot2463";               //change this
const char WIFI_PASSWORD[] = "12341234";           //change this
const char AWS_IOT_ENDPOINT[] = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com";       //change this
const char POST_URL[] = "AndroidHotspot2463";               //change this
const char GET_URL[] = "AndroidHotspot2463";               //change this

void syncTimeWithNTP();
int verify();
void moveservo(Servo* divider, int ANGLELIMIT);
void connectAWS(const char* THINGNAME, const char* AWS_CERT_CA, const char* DEVICE_CERT, const char* PRIKEY, const char* AWS_IOT_ENDPOINT, PubSubClient* mqttClient,WiFiClientSecure* wificlient);
void setupWifi(const char* WIFI_SSID,const char* WIFI_PASSWORD);
// Amazon Root CA 1
static const char AWS_CERT_CA[] PROGMEM = R"EOF(
-----BEGIN CERTIFICATE-----
MIIDQTCCAimgAwIBAgITBmyfz5m/jAo54vB4ikPmljZbyjANBgkqhkiG9w0BAQsF
ADA5MQswCQYDVQQGEwJVUzEPMA0GA1UEChMGQW1hem9uMRkwFwYDVQQDExBBbWF6
b24gUm9vdCBDQSAxMB4XDTE1MDUyNjAwMDAwMFoXDTM4MDExNzAwMDAwMFowOTEL
MAkGA1UEBhMCVVMxDzANBgNVBAoTBkFtYXpvbjEZMBcGA1UEAxMQQW1hem9uIFJv
b3QgQ0EgMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALJ4gHHKeNXj
ca9HgFB0fW7Y14h29Jlo91ghYPl0hAEvrAIthtOgQ3pOsqTQNroBvo3bSMgHFzZM
9O6II8c+6zf1tRn4SWiw3te5djgdYZ6k/oI2peVKVuRF4fn9tBb6dNqcmzU5L/qw
IFAGbHrQgLKm+a/sRxmPUDgH3KKHOVj4utWp+UhnMJbulHheb4mjUcAwhmahRWa6
VOujw5H5SNz/0egwLX0tdHA114gk957EWW67c4cX8jJGKLhD+rcdqsq08p8kDi1L
93FcXmn/6pUCyziKrlA4b9v7LWIbxcceVOF34GfID5yHI9Y/QCB/IIDEgEw+OyQm
jgSubJrIqg0CAwEAAaNCMEAwDwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMC
AYYwHQYDVR0OBBYEFIQYzIU07LwMlJQuCFmcx7IQTgoIMA0GCSqGSIb3DQEBCwUA
A4IBAQCY8jdaQZChGsV2USggNiMOruYou6r4lK5IpDB/G/wkjUu0yKGX9rbxenDI
U5PMCCjjmCXPI6T53iHTfIUJrU6adTrCC2qJeHZERxhlbI1Bjjt/msv0tadQ1wUs
N+gDS63pYaACbvXy8MWy7Vu33PqUXHeeE6V/Uq2V8viTO96LXFvKWlJbYK8U90vv
o/ufQJVtMVT8QtPHRh8jrdkPSHCa2XV4cdFyQzR1bldZwgJcJmApzyMZFo6IQ6XU
5MsI+yMRQ+hDKXJioaldXgjUkK642M4UwtBV8ob2xJNDd2ZhwLnoQdeXeGADbkpy
rqXRfboQnoZsG4q5WTP468SQvvG5
-----END CERTIFICATE-----
)EOF";

static const char DIV_SER1_CERT[] PROGMEM = R"KEY(
-----BEGIN CERTIFICATE-----
MIIDWjCCAkKgAwIBAgIVAPbFXSqq4i74IVV52xfuwuj7E3B1MA0GCSqGSIb3DQEB
CwUAME0xSzBJBgNVBAsMQkFtYXpvbiBXZWIgU2VydmljZXMgTz1BbWF6b24uY29t
IEluYy4gTD1TZWF0dGxlIFNUPVdhc2hpbmd0b24gQz1VUzAeFw0yMzA3MjEwMTQ0
NTNaFw00OTEyMzEyMzU5NTlaMB4xHDAaBgNVBAMME0FXUyBJb1QgQ2VydGlmaWNh
dGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDZ/p1JDj1Ro5ZLPkkR
BgQ5Yu8kTXi68EO8H0DE0FEOUEtxQN7yL+neERTIoN3kNgD0nNEXlhasdf0ugVor
FZFQoilH4eO59xRsaWMYPFOCt240Udd/w7UfVnit45solnNRi4xKWPtpF7H0EyZg
QPFcsfj3SP9RwN4MP9sKRxBf2vxpSPUAZMBRPth2zQpbsP3ZVafCyGpTj9BaSWNG
r7r+jo5BGg8Ue66myF7LStpXctx5Ha0lypZMlC45e3o/8nkNf2Pqcoq6EGpeHbUE
htzL+2lceCeup1ywzco9OaYATZKkCttLzl5E2+hiMUjKR0atdEFMI3hYMmXxdqUy
+LsBAgMBAAGjYDBeMB8GA1UdIwQYMBaAFP91j3TATrn7r5aB4Jda5lOKeroqMB0G
A1UdDgQWBBTTNWH5QgWlVsw8oR/SIcuyMBs/UzAMBgNVHRMBAf8EAjAAMA4GA1Ud
DwEB/wQEAwIHgDANBgkqhkiG9w0BAQsFAAOCAQEAOjHJV7h1zsofzOEs04wFiXCK
H9Y4ZOTm+4x9YA7cNUqhH8C50wwm/SjK+YGvGnI2u3s86l+KZEcRjEk9syty+huV
KzNhaRmh3Xkjfj+l9GlYkyKkgAj8rJOMdS4cBuWZVvXNgBqnD6qNNBDZDfFMD3U6
mjr1Ji47/XpA3GN/m4phiJgNhSLayS80SxqLl77TJ2azcQjpcvsuEvEvdAk7nPBX
xvl7URIeHlh+OGrkaOm1cXhnU7RUiw8Bd/E1xEWfULY9kgHxcExxcNRdcV81NGGA
HwyDHxZaiPxcwH4o0ZlU9/UkwLPHucN17S7wvUfleM4LN+i8tp5kI4XYIJXWZA==
-----END CERTIFICATE-----
 
 
)KEY";

static const char DIV_SER1_PRIKEY[] PROGMEM = R"KEY(
-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEA2f6dSQ49UaOWSz5JEQYEOWLvJE14uvBDvB9AxNBRDlBLcUDe
8i/p3hEUyKDd5DYA9JzRF5YWrHX9LoFaKxWRUKIpR+HjufcUbGljGDxTgrduNFHX
f8O1H1Z4reObKJZzUYuMSlj7aRex9BMmYEDxXLH490j/UcDeDD/bCkcQX9r8aUj1
AGTAUT7Yds0KW7D92VWnwshqU4/QWkljRq+6/o6OQRoPFHuupshey0raV3LceR2t
JcqWTJQuOXt6P/J5DX9j6nKKuhBqXh21BIbcy/tpXHgnrqdcsM3KPTmmAE2SpArb
S85eRNvoYjFIykdGrXRBTCN4WDJl8XalMvi7AQIDAQABAoIBAQDBRFo/5c75bTYX
OX5gCFyAYDUykzjBS7hRtcefdi0WqptoKiI+x7no9m3dvblb1YEdXVaHObqaZfT0
YRwG7JUrzskYgK18NxE1WG3Rk9JaJrQe0JxlGqaexPQy/oKWBmTw2+6iD6e9sZNE
mCHC3nDeRK881cv+uCZk58wTnZNn1sRSWh0Qz0GJwI5yJam48a1dYlhYVhnvzwmy
IHqKoXnK6Ck6us1vouPkFjytiMEzJkXna3vsaCLthJtVksVBMCb0q6y2zMD37p+T
9rZ7NeDzOR5i56jnovXpiYpbM+7+fWT9fCQYn3FPryylbMoz5pebNjWyMP5bZkOh
wDLSksZBAoGBAPDMUSBOTlHAwaOMaJoUmru1O3RYyEzrhs7uiFk39DEYUqEdwR7c
H9uYvo+L/bvhpt2OxfE3O6dTXXuytePiVXQmz0D1k/Kw3gJ/WHZzVuGASembO21g
9ohVJwHPFJGxRRQTYIlmMhxjrXxix6Xtpqy5SX2qTzMFwme5pff+XOQpAoGBAOfB
wYnlV0Fgc00cLebuoYZ38L3qxrDQGG/7yxB3vo4NcEgnFEHkQ9Ki6TlbcvaInxlE
0420dgHoT1zVf0lXORCQg2js9W952Jnd1cuwzrb57VV1GhR+CtzyjK2TtpgqDnwB
eaPknd2lzeqeQlUJQZ2CoERym30Ybz+QGSk66TsZAoGBALgzQ5fX7veA4V/Pp7C+
NEOE9o8g31BgF0ayl6DQOb9YoNTY4wNNVh1w8czzRMz1Z+UyM187ZimwjuFyKsvB
NqdcjMxfT6al/56fdbdescZcka3e7o3UT6KoqOcsVsrAdVxPGljg0VKAOZTeJ2VZ
r1ePgZVcs6sPK9CW5kwXy01JAoGAaeL9wTo2lpkxtP51/nfy9tQf+zWkWC6Lq8Z4
Ler39kEvlte8Cqr3Teq+8kTVDsy3HD11Bt53uEFi2UB2apTebrIISE1Tif46t3pC
b1E18SXZiNz68HfyEKcFHV1K5NT6TzABWp9efWF+5n4uQln7T7LZ9MwEPLCIS5tT
06Gl5CkCgYAEHtoitcCnfx5jyrQ7gzcaeNV+gu16aA7VSEDDZWxEW9MJN8Bl6+7u
jS1CrFVqnxeQHHKKgIOrnxUXVBLGtITn01RXY8yFstx5oiRvBSOlCSbGvVvmRXmu
eYumVuypuETvhc+MJcBbpB88Z8i2AEPqNBHZ9lhAzuhAU2syu9ljUg==
-----END RSA PRIVATE KEY-----
 
)KEY";

static const char DIV_SER2_CERT[] PROGMEM = R"KEY(
-----BEGIN CERTIFICATE-----
MIIDWjCCAkKgAwIBAgIVAMfPKeVqhbL3PKa20RAAHVxGuxd0MA0GCSqGSIb3DQEB
CwUAME0xSzBJBgNVBAsMQkFtYXpvbiBXZWIgU2VydmljZXMgTz1BbWF6b24uY29t
IEluYy4gTD1TZWF0dGxlIFNUPVdhc2hpbmd0b24gQz1VUzAeFw0yMzA3MjEwMTU0
MzZaFw00OTEyMzEyMzU5NTlaMB4xHDAaBgNVBAMME0FXUyBJb1QgQ2VydGlmaWNh
dGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDiAOVZHoCKJvesTCbo
zEA42Fyv4U7pndsE2GRbsUTiQOU8d5pUkHoEJzwFflwbUcXOT6K7NjEK96pjdFl1
aFT7h4nvt4Ygr3u7V9KDFUi2nFWtwAp6F845JUNgOeHDF2L2fz5dOFgEvJe+yjqE
CVc15xCd/kGhyvArhiuwUtWPLik9JVD5d0kJnuoKHHkPQFEdt01p95ERmt02fPa2
mTHwZIkLZT79AmqWGHR7MVFGx92YHtGvsfA4u8bpw8D80PXnglKq5d92o6vn2CJR
q9VkrsVEapV+nIiQOmTvl+4u+0ytc/jMMWB7L+dGqxiwy7VlQTaW+1ViDoe6If3E
thLbAgMBAAGjYDBeMB8GA1UdIwQYMBaAFOlLbR7smy+Wx+w43llPEz6UHUsCMB0G
A1UdDgQWBBTGDHxRRwfweNr/2Yd3gdkIqP0z3TAMBgNVHRMBAf8EAjAAMA4GA1Ud
DwEB/wQEAwIHgDANBgkqhkiG9w0BAQsFAAOCAQEAPxVEhVnxK7iW/VadcrP4itst
Fc3rmERkMiWrbjGEGi/0FTvJphcFVUQEEWgPTqVIHrvXgCkeSntdU196BzmexsOf
Q14b8nniw1Q9KDbAL3m8sQlIPGBBeyafyGty/KmO7Q/IxckTQPHCjpVeODW4pfSx
V/0Z1a7K3PGnScoE226TjgMOEBOesHwgHs9dvbbTMtAv0mtXrrRGdhRmW+7mXpZ/
nOmcLiWSdgNsaBqDmcmJ9WZQaYMQCFzOK1tjN55rJxZai4vBKUZlOO5HFl9A2adK
+6cyrCbxZmutSyNzZpUaHSp9kZjF8BBsyvN+pQrVGPVeLMHatG3eps8Tz5uffg==
-----END CERTIFICATE-----
 
 
)KEY";

static const char DIV_SER2_PRIKEY[] PROGMEM = R"KEY(
-----BEGIN RSA PRIVATE KEY-----
MIIEowIBAAKCAQEA4gDlWR6Aiib3rEwm6MxAONhcr+FO6Z3bBNhkW7FE4kDlPHea
VJB6BCc8BX5cG1HFzk+iuzYxCveqY3RZdWhU+4eJ77eGIK97u1fSgxVItpxVrcAK
ehfOOSVDYDnhwxdi9n8+XThYBLyXvso6hAlXNecQnf5BocrwK4YrsFLVjy4pPSVQ
+XdJCZ7qChx5D0BRHbdNafeREZrdNnz2tpkx8GSJC2U+/QJqlhh0ezFRRsfdmB7R
r7HwOLvG6cPA/ND154JSquXfdqOr59giUavVZK7FRGqVfpyIkDpk75fuLvtMrXP4
zDFgey/nRqsYsMu1ZUE2lvtVYg6HuiH9xLYS2wIDAQABAoIBAQDdFuNGe4BjphtE
gnrN4oIxGfgSVudLwxEvfEExMgmhDLs0nAxuDahPx9H/zc1tLGHFokIfwrPXdtFI
2dlp8daw+bcGuxKLhyAT9aYKdhfcDLQEdYnKRt+HJ9zxla41m/tECNaJe0Uf3R7V
liAIatrV8GVhKgoqrMymx2qV43LnzPlgxTzG5p19CoD0uxngQ5+lDHCRuD4Y32fH
VXiwRRPlQ6Rcy0A/+zSXs/reTCRipdE+S7oiuVNUT/6cIcS9VFYIZbOzuVR6k75d
9MDZ/rKM/Px5X2xr8CE4OMtJ4Puh4M2V4wgswg2P5WxaKibOojlumr62aoahqKTJ
XN2yxUwBAoGBAP2+uSAMOJqOs2qHydakZzK8CY0yWYyFZB4eRssid+LV6OacJ2bd
fHVP63D8qMw+TBkSEV3ubxH/PgiT8u/RY5Z20VkF2fzIQG4uC9mj5mdSXrWpFphy
87UVCMu3O1Y4FHoyBO2qGthwjxdvebue/TdaW1SNUr2qTKopowiF5bp7AoGBAOQD
D18Rxn2G9n6ufDsxS3qZ+tdqgD8g9J6cymQIamFl56GJ0hOfQQElTZYbzEtY0Gds
D+j+9qASH27PVFwAlf4GFDWIwXyi2t40xTJzYRNNuwkxSkEa4O5ooE2AgaBzPggf
HOQZKq3KqgbZmDg8IOA8RFIHcxDtz0Ga94z7NUshAoGAK7tWLpbMnp6ff2m9uJeH
DbBouQQHePd4ZNR1AQeEv/x78lrRU6IylAldG5EACotmFCAQDC9OxzxFVbch9IES
99ishfpDMpTUveiDtFPSHtaMaiSlN0sAg6IHbQy8VkS+Cr1aKhfaHplvn+tfgJ2L
KKITAYSF0RhrtBwO9CYXjMcCgYBN0j2M1SRJ86Zt1l/QVCLO1X8nxtG+uiDDggAR
5E1qsL0+BSwORmyJV5IDiblQfh+jLN7BR0QlngncaCcCv7Bx4uFC5Eoln+/Tp28m
qPqY/A66NHiDgAkFVfBgg1u6x5qEaxoRQAWxC1q8u8eYae1Om0M2PBSp0oDKg3m6
RIMMAQKBgDsEp9KrqxTUiW4i+bJC0e0yGMfBfDhfjcDlOzKwoie2TbJgggHaMeFM
RM2upMj6T0NeXt8dhV6d75wO7X+QxP/zffh0s2zt1VeiucKvmmeBglVsg9DRof0D
CpFkc6uY8B1LBkDO+SbIyp6uCQF1y3WH2J12Nv+jtMqeYtpZxiQT
-----END RSA PRIVATE KEY-----
 
)KEY";

static const char DIV_SER3_CERT[] PROGMEM = R"KEY(
-----BEGIN CERTIFICATE-----
MIIDWjCCAkKgAwIBAgIVAIiePXiHHW2LwzCEg1Ubg7kfLF3ZMA0GCSqGSIb3DQEB
CwUAME0xSzBJBgNVBAsMQkFtYXpvbiBXZWIgU2VydmljZXMgTz1BbWF6b24uY29t
IEluYy4gTD1TZWF0dGxlIFNUPVdhc2hpbmd0b24gQz1VUzAeFw0yMzA3MjEwMTU1
MjZaFw00OTEyMzEyMzU5NTlaMB4xHDAaBgNVBAMME0FXUyBJb1QgQ2VydGlmaWNh
dGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDH2LSo18BpwAHkTL7D
Eg6iOaYA+1uHPsdH2dMRjLowzWPduXe65Q2DLR2HPWNM3syKsEIsFcnQRy8fc9a0
5HXsCfX4Rhs3b8SLXqvKxA02S8XQh1nv++JD9HoHt0Vmb1rR138HhF9d8nQ3C8a8
LTfO28WyIbgA/ONp5eJ8y77mC+ATMrgI6HplRW97Jnz/MRuLawtqiKnmv6zbdREt
Twl6D9rrQfjaMLORPXYcGrDY/19lg5A25Ua2kSX8aBhcBLwuV8dE1/3rVoNL+tID
dFfN+qsx5Dl2RZbT0tj8ebTxmaUoVP7kmv/7PVtn4rHpxs4hpsoA/RLrqU80eBuY
J8UxAgMBAAGjYDBeMB8GA1UdIwQYMBaAFJ1ibdW7q0LjnGgUkc2CbABIN3poMB0G
A1UdDgQWBBRxpYkavRyDSZyci2PrxJBHNiQnZTAMBgNVHRMBAf8EAjAAMA4GA1Ud
DwEB/wQEAwIHgDANBgkqhkiG9w0BAQsFAAOCAQEAOrc17rtnzOaWPc2k8GYm0o3C
+pQuocipgMxEQwv9rLfjUTZ2QSzdGa92ciSBrqL8hIUrfetY3x2JMghvAyWx1DPd
loBjLplehFHoAVlZfO/Twz7o0uCVkEASHtaXDm82jpkq54nPSDQ1P/ethSdcRlfa
u/74pmZjrL3LEcdJvBSlgv5F+fIhS6gBAvDTYdw3NB9QA6dy0JP88MKScIE2HqZS
MerFfHBpSHAJl4kE0bKHgYjdMwxIqA3yUqQjssgX5+ekHOm2SSPRg+gMVGGI0ImN
IPAdwY6ZqOkF+pvfxViOGEO1sIlnK3Oue8VxU0fbMBO0FCi/0slHoKtjuo2C0g==
-----END CERTIFICATE-----
 
 
)KEY";

static const char DIV_SER3_PRIKEY[] PROGMEM = R"KEY(
-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEAx9i0qNfAacAB5Ey+wxIOojmmAPtbhz7HR9nTEYy6MM1j3bl3
uuUNgy0dhz1jTN7MirBCLBXJ0EcvH3PWtOR17An1+EYbN2/Ei16rysQNNkvF0IdZ
7/viQ/R6B7dFZm9a0dd/B4RfXfJ0NwvGvC03ztvFsiG4APzjaeXifMu+5gvgEzK4
COh6ZUVveyZ8/zEbi2sLaoip5r+s23URLU8Jeg/a60H42jCzkT12HBqw2P9fZYOQ
NuVGtpEl/GgYXAS8LlfHRNf961aDS/rSA3RXzfqrMeQ5dkWW09LY/Hm08ZmlKFT+
5Jr/+z1bZ+Kx6cbOIabKAP0S66lPNHgbmCfFMQIDAQABAoIBAAfOb0ApLVr0JR7F
4K2+BtRPbCydpPWjfiVQXO5SkPNXEo+Wrxad2+lD2eNugnJ+QX8pY2xVbYy1OyH/
ZGOsiO6vdOw90NJZi1PmXs+M2PdF/nQm1VyKekwtQJcMkalusB2CTXP9t7vzvJZp
31l6arOp4g8TQhzHxBpe4Hn/e3WA75EQQfZg/+2jHZDR3s8XwPu6LxpIq0WlDSCw
tgm3l6OHnBrb5gyV6tPw8y+iZf8dTaY5b/LoG7MCpOJjJGPurqEkJaXBQW/e2DWa
3Sd6+7uFHYTjiHPabxiaOiEe0I0BUMZSilSHdQ8kBUawWZwdVER66sGjSh10umep
J3O4YAECgYEA6pUBInF0GFFGlo7nvgC0f7HxbFs7aiOnDchGkWTwOwEFiYLLyyuH
y5zHo6+3mQUaQPnLLJAT2QB97DoaLzg1cUGl39a7vpfUCNAoZhm/VPmR77ug1svi
FAwqxnS+u+KqLdNafxtmnqAJP5NXICk6KHclMW5woy55m534OHhE/wUCgYEA2hfP
rocojWoXU7qzlUw7AE0oidAf5LsQWBnMOfCbeI+D9Fx2Fm8QKitITyo6h60qPhFp
XzecxJlw1g5NO6HnCVeeBnCeumnhq6ODvuDksJRTB0L1QFOzN0a5ZjIJNK0mFpFy
bxQ50kvlRFv7ihsONXwwEoRYD6oaHYyLLQmAzT0CgYBHt241XYIkvRRbiy0GRajp
MTmh6vJrLKMx73zDjpxzLYjH1ZkAcDp/4VqXjG0Q9yJjovqekR3ARgo5YjoHHaQZ
D/zqRff3+Or2Fs2DCXQZQoN/S1mM0aA4iM+7BI4bWty0Td1o5rOk40Bwk1cbmf8R
x/sy9ZSyDs9W6CCvAvX1EQKBgQDPxgeRmipEL2Jx3ljgT3OWYevQ/3qjCqQTiuBK
ArPbM77euoe177FSu1F0OiLwx0SLRUNB91Ve51/cTlu+CAx3lgVy1TAjIGRihLEq
1E6K+4PuUxgbZKJEcxiGB/1FNgAnWAHRWU+MzclqSsdlV0k+I0YZHgRXSBMBO7gL
/BW9cQKBgQCAtNhv5H/llf8rszCJX2mJbsAEro8SWPp5xwetdDlyHLE+gJ6zriQG
CzMcmTSTyM3iwUWunZJCSznNoOwpawfva+A2W1XdPF33kTN9+F9gAG8h1K8g54M9
aC1Kw+9uuYdyhZ/cr4q+75jl12Nljm8fpnQPMOVgfvhCWygIB86D4Q==
-----END RSA PRIVATE KEY-----
 
)KEY";