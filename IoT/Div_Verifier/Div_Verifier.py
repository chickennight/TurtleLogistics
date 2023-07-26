from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import paho.mqtt.client as mqtt
import RPi.GPIO as GPIO
import time
import cv2
import json

## PUBLISH TOPIC
PUB_DIV_SERVO = "/div/servo/"
PUB_LOG = "/log"
PUB_DIV_MOTOR = "/mod/div/motor/power"

SUB_DIV_INFO = "/sup/div/veri"
SUB_DIV_CAM = "/div/cam"
SUB_MOD_ITV = "/mod/div/veri/time"
SUB_MOD_DIST = "/mod/div/veri/distance"
SUB_WEB_POWER ="/web/power"

CA = "/home/ssafy09204/TL/CA1.pem"
PRIKEY = "/home/ssafy09204/TL/PRIVATE.key"
CERT = "/home/ssafy09204/TL/CERT.crt"
END_POINT = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com"

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

TRIG=23
ECHO=24

GPIO.setup(TRIG,GPIO.OUT)
GPIO.setup(ECHO,GPIO.IN)

GPIO.output(TRIG,False)
print("init ultra sensor")
time.sleep(2)

## Ul Sensor detect or not
global detect
global distance
global interval
global QR_value
global power
global already


detect = 0
already=1
power=1
distance = 5
interval = 2
QR_value = -1
QR_finish = 0
Order_Lists=[]
Address_Info={}
Address_list=["1","2","3"]

myMQTTClient = AWSIoTMQTTClient("clientid")

myMQTTClient.configureEndpoint(END_POINT,8883)
myMQTTClient.configureCredentials(CA,PRIKEY,CERT)

myMQTTClient.configureOfflinePublishQueueing(-1)
myMQTTClient.configureDrainingFrequency(2)
myMQTTClient.configureConnectDisconnectTimeout(10)
myMQTTClient.configureMQTTOperationTimeout(5)

print("MQTTClient configure Success")

def add_Order(self,params,packet):
    global power
    if(power!=1):power=1
    data = json.loads(packet.payload)
    order_num = int(data["order_num"])
    Order_Lists.append(order_num)
    Address_Info[str(order_num)] = str(data["address"])
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Div_Veifier\",\"content\":\"Add Order List Success\",\"order_num\":\"{str(order_num)}\"}}",0)

def read_QR(self,params,packet):
    global QR_value , detect
    time.sleep(interval)
    camera = cv2.VideoCapture(0)
    camera.set(3,640)
    camera.set(4,480)

    myMQTTClient.publish(PUB_DIV_MOTOR,"{\"type\":-1}",0)
    ret, img = camera.read()
    print("Capture!")
    cv2.imwrite('image.jpg',img)
    QR_temp = cv2.QRCodeDetector()
    order_num, box, st_qrcode = QR_temp.detectAndDecode(img)

    myMQTTClient.publish(PUB_DIV_MOTOR,"{\"type\":1}",0)
    if(order_num==None):
        myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Div_Veifier\",\"content\":\"Order Number is empty\"}",0)
        return
    if(int(order_num) in Order_Lists):
        if(Address_Info[str(order_num)] not in Address_list):
            myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Div_Veifier\",\"content\":\"Address not in Address List\",\"order_num\":{order_num}}}",0)
        else:
            myMQTTClient.publish(PUB_DIV_SERVO+Address_Info[order_num],f"{{\"order_num\" : \"{order_num}\"}}",0)
            myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Div_Veifier\",\"content\":\"Get Address Success\"}",0)
            Order_Lists.remove(int(order_num))
            del Address_Info[str(order_num)]

    else:
      myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Div_Veifier\",\"content\":\"Order Number is Weird\",\"order_num\":\"{order_num}\"}}",0)
    detect = 0

def change_Distance(self,params,packet):
    global distance
    data = json.loads(packet.payload)
    distance = int(data["distance"])
    print(f"Change Distance : {distance}")
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Div_Veifier\",\"content\":\"Change Distance\",\"order_num\":\"{distance}\"}}",0)

def change_Interval(self,params,packet):
    global interval
    data = json.loads(packet.payload)
    interval = int(data["interval"])
    print(f"Change Interval : {interval}")
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Div_Veifier\",\"content\":\"Change Interval\",\"order_num\":\"{interval}\"}}",0)

def change_Power(self,params,packet):
    global power
    data = json.loads(packet.payload)
    power = int(data["type"])
    print(f"Change Power : {power}")
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Div_Veifier\",\"content\":\"Change Power\",\"type\":\"{power}\"}}",0)


myMQTTClient.connect()
myMQTTClient.subscribe(SUB_DIV_INFO,0,add_Order)
myMQTTClient.subscribe(SUB_DIV_CAM,0,read_QR)

myMQTTClient.subscribe(SUB_MOD_ITV,0,change_Interval)
myMQTTClient.subscribe(SUB_MOD_DIST,0,change_Distance)
myMQTTClient.subscribe(SUB_WEB_POWER,0,change_Power)

myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Div_Veifier\",\"content\":\"AWS Connect Success\"}",0)

while True:

    if(len(Order_Lists)>0 & power==1):
        ## 초음파 센서 감지 loop
        ## 감지 완료
        if(detect==1 & already==0):
            # 카메라 센서에 찍으라고 PUB
            myMQTTClient.publish(SUB_DIV_CAM,"{\"func\":\"1\"}",0)
            detect=0
            already=1
        else:
          while True:
              GPIO.output(TRIG, True)
              time.sleep(0.00001) # 10uS의 펄스 발생을 위한 딜레이

          while GPIO.input(ECHO)==0:
              start = time.time() # ECHO핀 상승 시간값 저장

          while GPIO.input(ECHO)==1:
              stop = time.time() # ECHO핀 하강 시간값 저장

              check_time = stop-start
              dist = check_time * 34300 / 2
              if(detect==0 & already==1):## already 0으로 바꾸기 위한 감지
                  if(dist > distance):
                      already=0
              if(detect==0 & already==0):## detect 1으로 바꾸기 위한 감지
                  if (dist < distance):
                      detect=1
