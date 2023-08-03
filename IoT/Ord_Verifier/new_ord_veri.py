from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import paho.mqtt.client as mqtt
import RPi.GPIO as GPIO
import time
import cv2
import json

CA = "/home/ssafy09204/Order_Verifier/secrets/CA1.pem"
PRIKEY = "/home/ssafy09204/Order_Verifier/secrets/PRIVATE.key"
CERT = "/home/ssafy09204/Order_Verifier/secrets/CERT.crt"
END_POINT = "a3r8259knz52ke-ats.iot.ap-northeast-2.amazonaws.com"

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

TRIG=2
ECHO=3

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
distance = 4
interval = 0.75
QR_value = -1
QR_finish = 0
Order_Lists=[]
Address_Info={}
Address_list=["1","2","3"]

log_data = dict()
log_data["dev"] = "ord_verifier"

PUB_LOG = "/log"
PUB_RES = "/ord/res"
PUB_ORD_MOTOR = "/mod/ord/motor/power"

SUB_WEB_POWER = "/mod/web/power"
SUB_ORD_CAM = "/ord/cam"
SUB_MOD_ITV = "/mod/ord/veri/time"
SUB_MOD_DIS = "/mod/ord/veri/distance"
SUB_SUP_ADD = "/sup/ord/veri/info"

myMQTTClient = AWSIoTMQTTClient("clientid")

myMQTTClient.configureEndpoint(END_POINT,8883)
myMQTTClient.configureCredentials(CA,PRIKEY,CERT)

myMQTTClient.configureOfflinePublishQueueing(-1)
myMQTTClient.configureDrainingFrequency(2)
myMQTTClient.configureConnectDisconnectTimeout(10)
myMQTTClient.configureMQTTOperationTimeout(5)

print("MQTTClient configure Success")

def change_Power(self, params, packet):
    global power
    data = json.loads((packet.payload))
    power = int(data["power"])
    print(f"Change Power : {power}")
    myMQTTClient.publish(PUB_LOG, f"{{\"dev\":\"Ord_Veifier\",\"content\":\"Change Power\",\"power\":\"{power}\"}}",0);

def change_Distance(self,params,packet):
    global distance
    data = json.loads(packet.payload)
    distance = int(data["distance"])
    print(f"Change Distance : {distance}")
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Ord_Veifier\",\"content\":\"Change Distance\",\"order_num\":\"{distance}\"}}",0)

def change_Interval(self,params,packet):
    global interval
    data = json.loads(packet.payload)
    interval = int(data["interval"])
    print(f"Change Interval : {interval}")
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Ord_Veifier\",\"content\":\"Change Interval\",\"order_num\":\"{interval}\"}}",0)

def add_Order(self, params, packet):
    global power
    if(power!=1): power=1
    data = json.loads(packet.payload)
    order_num = int(data["order_num"])
    numA = int(data["productA"])
    numB = int(data["productB"])
    numC = int(data["productC"])
    Order_Lists.append([order_num, [numA, numB, numC]])
    myMQTTClient.publish(PUB_LOG, f"{{\"dev\":\"Ord_Veifier\",\"content\":\"Add Order List Success\",\"order_num\":\"{str(order_num)}\"}}",0)
	#myMQTTClient.publish(PUB_ORD_MOTOR, "{\"power\":\"1\"}",0)
    print("Order added")

def read_QR(self, params, packet):
    global QR_value , detect, Order_Lists
    time.sleep(interval)
    camera = cv2.VideoCapture(0)
    camera.set(3,640)
    camera.set(4,480)
    #myMQTTClient.publish(PUB_ORD_MOTOR,"{\"power\":-1}",0)
    ret, img = camera.read()
    print("Capture!")
    cv2.imwrite('image.jpg',img)
    QR_temp = cv2.QRCodeDetector()
    product_num, box, st_qrcode = QR_temp.detectAndDecode(img)
#    myMQTTClient.publish(PUB_ORD_MOTOR,"{\"power\":1}",0)
    print(product_num)
    if(product_num==None):
        myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Ord_Veifier\",\"content\":\"Order Number is empty\"}",0)
        
    elif Order_Lists and product_num:
            product_num = int(product_num)-1
            if (Order_Lists[0][1][product_num]==0): #현재 주문 목록에 없는 상품이 찍히면 에러
                myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Ord_Verifier\",\"content\":\"Wrong product in current order list\"}",0)
                myMQTTClient.publish(PUB_RES,f"{{\"order_num\":\"{Order_Lists[0][0]}\", \"result\":\"-1\"}}",0)
                myMQTTClient.publish(PUB_ORD_MOTOR,"{\"power\":\"-1\"}",0)
            else: #현재 주문 목록에 있는 상품이라면 하나 빼줌
                Order_Lists[0][1][product_num] -= 1
                myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Ord_Verifier\", \"content\":\"product {product_num+1} counted for order {Order_Lists[0][0]}\"}}",0)
                if (sum(Order_Lists[0][1])==0):
                    Order_Lists.pop(0)
                    myMQTTClient.publish(PUB_RES,"{\"result\":\"1\"}",0)
    else:
        myMQTTClient.publish(PUB_ORD_MOTOR,"{\"power\":\"-1\"}",0)
        myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Ord_Verifier\",\"content\":\"QR or Order error\"}",0)
    print("end of read_QR_function")
    time.sleep(0.5)

myMQTTClient.connect()
myMQTTClient.subscribe(SUB_WEB_POWER,0,change_Power)
myMQTTClient.subscribe(SUB_MOD_ITV,0, change_Interval)
myMQTTClient.subscribe(SUB_MOD_DIS,0, change_Distance)
myMQTTClient.subscribe(SUB_ORD_CAM,0, read_QR)
myMQTTClient.subscribe(SUB_SUP_ADD,0, add_Order)

myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Ord_Veifier\",\"content\":\"AWS Connect Success\"}",0)

while True:
    if(int(power)==1):
        ## 초음파 센서 감지 loop
        ## 감지 완료
        print(f"detect : {detect}")
        print(f"already  {already}")
        if(detect==1 and already==0):
            # 카메라 센서에 찍으라고 PUB
            myMQTTClient.publish(SUB_ORD_CAM,"{\"func\":\"1\"}",0)
            time.sleep(1)
            detect=0
            already=1
            print("detect and already change 0, 1")
        else:
# while True:
#print("while1")
          GPIO.output(TRIG, True)
          time.sleep(0.05) # 10uS의 펄스 발생을 위한 딜레이
          GPIO.output(TRIG, False)
          while GPIO.input(ECHO)==0:
#print("while2")
              start = time.time() # ECHO핀 상승 시간값 저장

          while GPIO.input(ECHO)==1:
#print("while3")
              stop = time.time() # ECHO핀 하강 시간값 저장

          check_time = stop - start
          dist = check_time * 34300 / 2
          print(dist)
          if(detect==0 and already==1):## already 0으로 바꾸기 위한 감지
              if(dist > distance):
                  already=0
          if(detect==0 and already==0):## detect 1으로 바꾸기 위한 감지
              if (dist <= distance):
                  detect=1

