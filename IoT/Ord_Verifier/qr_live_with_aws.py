from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import paho.mqtt.client as mqtt
import cv2
import time
import json

CA = "/home/ssafy09204/Order_Verifier/secrets/CA1.pem"
PRIKEY = "/home/ssafy09204/Order_Verifier/secrets/PRIVATE.key"
CERT = "/home/ssafy09204/Order_Verifier/secrets/CERT.crt"

#CA = "/home/pi/TL/CA1.pem"
#PRIKEY = "/home/pi/TL/PRIVATE.key"
#CERT = "/home/pi/TL/CERT.crt"

END_POINT = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com"

cap = cv2.VideoCapture(0)
detector = cv2.QRCodeDetector()

global interval
global power

PRODUCT_LIST = [1,2,3]
Order_Lists=[]
interval = 0.3
deleted_flag = 0
power=-1

print(f"power : {power}")
print(f"interval : {interval}")

PUB_LOG = "/log"
PUB_RES = "/ord/res"
PUB_ORD_MOTOR = "/mod/ord/motor/power"

SUB_WEB_POWER = "/mod/web/power"
SUB_MOD_ITV = "/mod/ord/veri/interval"
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

def change_Interval(self,params,packet):
    global interval
    data = json.loads(packet.payload)
    interval = float(data["interval"])
    print(f"Change Interval : {interval}")
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Ord_Veifier\",\"content\":\"Change Interval\",\"order_num\":\"{interval}\"}}",0)

def add_Order(self, params, packet):
    global power
    print("add")
#if(power!=1): power=1
    data = json.loads(packet.payload)
    order_num = int(data["order_num"])
    numA = int(data["productA"])
    numB = int(data["productB"])
    numC = int(data["productC"])
    Order_Lists.append([order_num, [numA, numB, numC]])
    myMQTTClient.publish(PUB_LOG, f"{{\"dev\":\"Ord_Veifier\",\"content\":\"Add Order List Success\",\"order_num\":\"{str(order_num)}\"}}",0)
	  #myMQTTClient.publish(PUB_ORD_MOTOR, "{\"power\":\"1\"}",0)
    print("Order added")

def delete_product(product_num):
  if Order_Lists:
      product_num = int(product_num)-1
      if (Order_Lists[0][1][product_num]==0): #현재 주문 목록에 없는 상품이 찍히면 에러
          myMQTTClient.publish(PUB_LOG,"{\"dev\":\"Ord_Verifier\",\"content\":\"Wrong product in current order list\"}",0)
          myMQTTClient.publish(PUB_RES,f"{{\"order_num\":\"{Order_Lists[0][0]}\", \"product_num\":\"{product_num}\", \"result\":\"-1\"}}",0)
          myMQTTClient.publish(PUB_ORD_MOTOR,"{\"power\":\"-1\"}",0)
          print("wrong product!")
      else: #현재 주문 목록에 있는 상품이라면 하나 빼줌
          Order_Lists[0][1][product_num] -= 1
          myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Ord_Verifier\", \"content\":\"product {product_num+1} counted for order {Order_Lists[0][0]}\"}}",0)
          if (sum(Order_Lists[0][1])==0):
              myMQTTClient.publish(PUB_RES,f"{{\"order_num\":\"{Order_Lists[0][0]}\",\"type\":\"0\" , \"result\":\"1\"}}",0)
              Order_Lists.pop(0)

      print(Order_Lists)
  else:
    print("Wrong product!")
    myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Ord_Verifier\", \"content\":\"wrong product in no order\"}}",0)
    myMQTTClient.publish(PUB_ORD_MOTOR,"{\"power\":\"-1\"}",0)


myMQTTClient.connect()
myMQTTClient.subscribe(SUB_WEB_POWER,0,change_Power)
myMQTTClient.subscribe(SUB_SUP_ADD,0, add_Order)
myMQTTClient.subscribe(SUB_MOD_ITV,0, change_Interval)
myMQTTClient.publish(PUB_LOG,f"{{\"dev\":\"Ord_Verifier\", \"content\":\"AWS connect Scueess\"}}",0)
print("AWS connect Success")

unrecognized_time = time.time()
time.sleep(interval)
recognized_time = time.time()

print("The system is ready.")

while True:
  _, img = cap.read()
  data, bbox, _ = detector.detectAndDecode(img)
  cur_product = data
  try : cur_product = int(cur_product)
  except: pass
  print(cur_product)

  if cur_product in PRODUCT_LIST:
    recognized_time = time.time()

    if deleted_flag == 0 :
      print("New prodcut has been recognized.")
      delete_product(cur_product)
      deleted_flag = 1

  else:
    if deleted_flag == 0 : continue
    unrecognized_time = time.time()
    itv = unrecognized_time - recognized_time
    if itv > interval:
      deleted_flag = 0
