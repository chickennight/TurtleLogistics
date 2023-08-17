from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import paho.mqtt.client as mqtt
import cv2
import time
import json

cap = cv2.VideoCapture(0)
detector = cv2.QRCodeDetector()

global interval
global power

PRODUCT_LIST = [1,2,3]
Order_Lists=[]
interval = 1.5
deleted_flag = 0
power=1

print(f"power : {power}")
print(f"interval : {interval}")

PUB_LOG = "/log"
PUB_RES = "/ord/res"
PUB_ORD_MOTOR = "/mod/ord/motor/power"


def delete_product(product_num):
  if Order_Lists:
      product_num = int(product_num)-1
      if (Order_Lists[0][1][product_num]==0): #현재 주문 목록에 없는 상품이 찍히면 에러
           print("no product in current order...")
		   
      else: #현재 주문 목록에 있는 상품이라면 하나 빼줌
          Order_Lists[0][1][product_num] -= 1
          if (sum(Order_Lists[0][1])==0):
              Order_Lists.pop(0)
  print(Order_Lists)

Order_Lists.append([1001, [100,100,100]])

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
