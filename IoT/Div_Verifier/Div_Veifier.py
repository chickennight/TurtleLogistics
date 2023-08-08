from PySide2.QtWidgets import *
from PySide2.QtCore import *
from PySide2.QtGui import *
from ui import Ui_MainWindow
from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
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

cam = cv2.VideoCapture(0)
cam.set(3, 640)
cam.set(4, 480)
detector = cv2.QRCodeDetector()

Address_Info = {}
Address_Lists = ["1", "2", "3"]
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
        cv2.destroyAllwindows()
        print("Camera Release Success")
    except Exception as e:
        print(f"Error :{e}")


class MyThread(QThread):
    imgSignal = Signal(QPixmap)
    envSignal = Signal(temp,humid)
    orderSignal = Signal(ordernum)
    addrSignal = Signal(address)
    resSignal = Signal()


    def __init__(self):
        super().__init__()
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

    def run(self):
        global Order_Lists, Address_Info
        while True:
            try:
                ret, self.img = cam.read()
                if ret:
                    self.mySignal.emit(printImage(self.img))
                    try:
                        o_num, _, _ = detector.detectAndDecode(self.img)
                        o_num = int(o_num)
                        self.orderSignal.emit(o_num)
                        print(f"Order Number : {o_num}")
                        if (self.o_num in Order_Lists):
                            if (Address_Info[str(o_num)] not in Address_Lists):
                                self.MSG(f"Order Num({o_num}) Address not in Address")
                                self.addrSignal.emit("Not Exist")
                                print("self.o_num's address not exist")
                            else:
                                print("self.o_num's address is exist")
                                addr = Address_Info[str(o_num)]
                                TOPIC = TOPIC_DIV_SERVO + addr + "/info"
                                self.myMQTTClient.publish(TOPIC, f"\"order_num\":\"{o_num}\"", 0)
                                self.MSG(f"Get Address({addr}) Success")
                                self.addrSignal.emit(addr)

                                Order_Lists.remove(int(o_num))
                                del Address_Info[str(o_num)]

                    except Exception as e:
                        print(f"Error:{e}")
                        self.MSG(f"Error : {e}")
            except KeyboardInterrupt:
                sys.exit()
            time.sleep(0.01)

    def add_Order(self, self2, params, packet):
        global power
        if (power != 1):
            power = 1
        data = json.loads(packet.payload)

        order_num = int(data["order_num"])
        if (order_num not in Order_Lists):
            Order_Lists.append(order_num)
            Address_Info[str(order_num)] = str(data["address"])
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
        self.th.envSignal.emit(temperature,humidity)

    def get_res(self, self2, params, packet):
        global fail,suc
        data = json.loads(packet.payload)
        result = int(data["type"])
        if(result==0): suc=suc+1
        else: fail=fail+1
        self.th.resSignal.emit()

    def MSG(self, str):
        self.myMQTTClient.publish(TOPIC_WEB_LOG, f"{{\"dev\":\"Div_Veirifier\",\"content\":\"{str}\"}}", 0)


class MyApp(QMainWindow, Ui_MainWindow):
    def __init__(self):
        super().__init__()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.setWindowFlags(PySide2.QtCore.Qt.FramelessWindowHint)
        self.main()
        self.showNum()

    def main(self):
        self.th = MyThread()
        self.th.imgSignal.connect(self.setImage)
        self.th.envSignal.connect(self.setEnv)
        self.th.resSignal.connect(self.setNum)
        self.th.orderSignal.connect(self.setOrderNum)
        self.th.addrSignal.connect(self.setAddress)
        self.th.start()
        self.logo = cv2.imread("./logo.jpg")
        self.ui.Logo.setPixmap(printImage(self.logo))

    def showNum(self):
        self.ui.SucCnt.display(suc)
        self.ui.FailCnt.display(fail)

    def setImage(self, img):
        self.ui.ShowImg.setPixmap(img)

    def setEnv(self,temp,humid):
        self.ui.Temp.setText(str(temp)+"ºC")
        self.ui.Humid.setText(str(humid)+"%")

    def setOrderNum(self,ordernum):
        self.ui.OrderNum.setText(ordernum)

    def setAddress(self,address):
        self.ui.Addr.setText(address)

    def closeEvent(self, event):
        self.th.terminate()
        self.th.wait(3000)
        self.close()


app = QApplication()
win = MyApp()
win.showMaximized()
app.exec_()

