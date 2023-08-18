from PySide2.QtWidgets import *
from PySide2.QtCore import *
from PySide2.QtGui import *
from ui import Ui_MainWindow
from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import RPi.GPIO as GPIO
import atexit
import threading
import PySide2
import json
import time
import cv2
import sys

TOPIC_DIV_CAM = "/div/cam"
TOPIC_DIV_SERVO = "/div/servo"
TOPIC_DIV_INFO = "/sup/div/veri/info"
TOPIC_DIV_RES = "/div/res"

TOPIC_WEB_LOG = "/log"
TOPIC_WEB_POWER = "/mod/web/power"

TOPIC_MOD_ITV = "/mod/div/veri/interval"

TOPIC_ENV_INFO = "/sup/div/veri/env/info"

CA = "/home/pi/TL/CA1.pem"
PRIKEY = "/home/pi/TL/PRIVATE.key"
CERT = "/home/pi/TL/CERT.crt"

END_POINT = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com"

global interval
global power
global suc
global fail
global Address_Info
global Address_Lists
global Order_Lists

interval = 1.5
power = 1
suc = 0
fail = 0
RedPIN = 21
GreenPIN=26
GPIO.setmode(GPIO.BCM)
GPIO.setup(RedPIN,GPIO.OUT)
GPIO.setup(GreenPIN,GPIO.OUT)
GPIO.output(GreenPIN,GPIO.HIGH)

cam = cv2.VideoCapture(0)
cam.set(3, 640)
cam.set(4, 480)
detector = cv2.QRCodeDetector()

Address_Info = {}
Address_List = ["1", "2", "3"]
Order_Checked={}
Order_Lists = []


def printImage(imgBGR):
    imgRGB = cv2.cvtColor(imgBGR, cv2.COLOR_BGR2RGB)
    h, w, byte = imgRGB.shape
    img = QImage(imgRGB, w, h, byte * w, QImage.Format_RGB888)
    pix_img = QPixmap(img)
    return pix_img


def release_camera():
    try:
        cam.release()
        print("Camera Release Success")
    except Exception as e:
        print(f"Error :{e}")


class MyThread(QThread):
    imgSignal = Signal(QPixmap)
    envSignal = Signal(int,int)
    orderSignal = Signal(str)
    addrSignal = Signal(str)
    RedSignal = Signal()
    GreenSignal = Signal()
    resSignal = Signal()


    def __init__(self):
        super().__init__()
        self.flag=1
        self.prev_time = time.time()
        self.myMQTTClient = AWSIoTMQTTClient("clientid")
        self.myMQTTClient.configureEndpoint(END_POINT, 8883)
        self.myMQTTClient.configureCredentials(CA, PRIKEY, CERT)

        self.myMQTTClient.configureOfflinePublishQueueing(-1)
        self.myMQTTClient.configureDrainingFrequency(2)
        self.myMQTTClient.configureConnectDisconnectTimeout(10)
        self.myMQTTClient.configureMQTTOperationTimeout(5)

        print("MQTTClient configure Success")
        print("MQTT Connect Try")
        self.myMQTTClient.connect()
        print("MQTT Connect Success")
        self.MSG("AWS Connect Success")
        self.myMQTTClient.subscribe(TOPIC_DIV_INFO, 0, self.add_Order)
        self.myMQTTClient.subscribe(TOPIC_WEB_POWER, 0, self.change_Power)
        self.myMQTTClient.subscribe(TOPIC_ENV_INFO, 0, self.get_env)
        self.myMQTTClient.subscribe(TOPIC_DIV_RES, 0, self.get_res)
        self.GreenSignal.emit()

    def run(self):

        while self.flag:
            try:
                ret, self.img = cam.read()
                if ret:
                    o_num, points, _ = detector.detectAndDecode(self.img)
                    if(o_num!=''):
                        try:
                            self.orderSignal.emit(o_num)
                            points = [points[0].astype(int)]
                            n = len(points[0])
                            for i in range(n):
                                cv2.line(self.img, tuple(points[0][i]), tuple(points[0][(i+1) % n]), (120,72,230), 3)
                            cv2.putText(self.img,str(o_num),(points[0][2][0],points[0][2][1]-10),cv2.FONT_HERSHEY_PLAIN,3,(120,72,230),3)
                            self.imgSignal.emit(printImage(self.img))
                            if (o_num in Order_Lists):
                                if (Address_Info[str(o_num)] not in Address_List):
                                    self.MSG(f"Order Num({o_num}) Address not in Address")
                                    self.addrSignal.emit("Not Exist")
                                    print("self.o_num's address not exist")
                                    self.RedSignal.emit()
                                else:
                                    print("self.o_num's address is exist")
                                    if(Order_Checked[str(o_num)]=='0'):
                                        Order_Checked[str(o_num)]='1'
                                        addr = Address_Info[str(o_num)]
                                        TOPIC = TOPIC_DIV_SERVO + addr + "/info"
                                        self.myMQTTClient.publish(TOPIC, f"{{\"order_num\":\"{o_num}\"}}", 0)
                                        self.MSG(f"Get Address({addr}) Success")
                                        self.addrSignal.emit(addr)
                                        self.GreenSignal.emit()
                            else:
                                self.RedSignal.emit()

                        except Exception as e:
                            print(f"Error:{e}")
                    else:
                        self.imgSignal.emit(printImage(self.img))
                        self.GreenSignal.emit()
            except:
                pass
            time.sleep(0.01)


    def stop(self):
        self.flag=0
        release_camera()
        self.quit()
        self.wait(5000)

    def add_Order(self, self2, params, packet):
        global power
        if (power != 1):
            power = 1
        data = json.loads(packet.payload)

        order_num = str(data["order_num"])
        if (order_num not in Order_Lists):
            Order_Lists.append(order_num)
            Address_Info[str(order_num)] = str(data["address"])
            Order_Checked[str(order_num)] = '0'
            self.MSG(f"Order Num({order_num}) Add Success")
        else:
            self.MSG(f"Order Num({order_num}) Already exist")

    def change_Power(self, self2, params, packet):
        global power
        data = json.loads(packet.payload)
        power = int(data["power"])

    def get_env(self, self2, params, packet):
        data = json.loads(packet.payload)
        temperature = int(data["temp"])
        humidity = int(data["humid"])
        self.envSignal.emit(temperature,humidity)

    def get_res(self, self2, params, packet):
        global fail,suc
        data = json.loads(packet.payload)

        result = int(data["result"])
        o_num=str(data["order_num"])
        print(o_num)
        print(Order_Lists)
        if o_num in Order_Lists:
            if(result==0): suc=suc+1
            else: fail=fail+1
            Order_Lists.remove(o_num)
            del Address_Info[o_num]
            self.resSignal.emit()

    def MSG(self, str):
        self.myMQTTClient.publish(TOPIC_WEB_LOG, f"{{\"dev\":\"Div_Veirifier\",\"content\":\"{str}\"}}", 0)


class MyApp(QMainWindow, Ui_MainWindow):
    def __init__(self):
        super().__init__()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.setWindowFlags(PySide2.QtCore.Qt.FramelessWindowHint)
        self.main()

    def main(self):
        self.th = MyThread()
        self.th.imgSignal.connect(self.setImage)
        self.th.envSignal.connect(self.setEnv)
        self.th.resSignal.connect(self.setNum)
        self.th.orderSignal.connect(self.setOrderNum)
        self.th.addrSignal.connect(self.setAddress)
        self.th.RedSignal.connect(self.setRedLED)
        self.th.GreenSignal.connect(self.setGreenLED)
        self.th.start()
        self.logo = cv2.imread("./logo2.jpg")
        self.ui.Logo.setPixmap(printImage(self.logo))

    def keyReleaseEvent(self,e):
        if e.key() == Qt.Key_Escape:
            self.close()

    def setRedLED(self):
        GPIO.output(GreenPIN,GPIO.LOW)
        GPIO.output(RedPIN,GPIO.HIGH)

    def setGreenLED(self):
        GPIO.output(RedPIN,GPIO.LOW)
        GPIO.output(GreenPIN,GPIO.HIGH)

    def setNum(self):
        self.ui.SucCnt.display(suc)
        self.ui.FailCnt.display(fail)

    def setImage(self, img):
        self.ui.ShowImg.setPixmap(img)

    def setEnv(self,temp,humid):
        print("setEnv")
        self.ui.Temp.setText("Temp: "+str(temp)+"ºC")
        self.ui.Humid.setText("Humid: "+str(humid)+"%")

    def setOrderNum(self,ordernum):
        self.ui.OrderNum.setText(str(ordernum))

    def setAddress(self,address):
        self.ui.Addr.setText(address)

    def closeEvent(self, e):
        self.th.stop()


app = QApplication()
win = MyApp()
win.showMaximized()
app.exec_()
GPIO.cleanup()