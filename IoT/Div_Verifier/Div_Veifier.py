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

TOPIC_WEB_LOG = "/log"
TOPIC_WEB_POWER = "/mod/web/power"

TOPIC_MOD_ITV = "/mod/div/veri/interval"

CA = "/home/pi/TL/CA1.pem"
PRIKEY = "/home/pi/TL/PRIVATE.key"
CERT = "/home/pi/TL/CERT.crt"

END_POINT = "a1s6tkbm4cenud-ats.iot.ap-northeast-2.amazonaws.com"

global interval
global power
global tot
global Address_Info
global Address_Lists
global Order_Lists

interval = 1.5
power = 1
tot=0

cam = cv2.VideoCapture(0)
cam.set(3,640)
cam.set(4,480)
detector = cv2.QRCodeDetector()

Address_Info={}
Address_Lists=["1","2","3"]
Order_Lists=[]


def printImage(imgBGR):
    imgRGB = cv2.cvtColor(imgBGR,cv2.COLOR_BGR2RGB)
    h,w,byte = imgRGB.shape
    img = QImage(imgRGB,w,h,byte*w, QImage.Format_RGB888)
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
    mySignal = Signal(QPixmap)

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
        self.myMQTTClient.subscribe(TOPIC_DIV_INFO,0,self.add_Order)
        self.myMQTTClient.subscribe(TOPIC_WEB_POWER,0,self.change_Power)

    def run(self):
        global Order_Lists,Address_Info,tot
        while True:
            try:
                ret,self.img = cam.read()
                if ret:
                    self.mySignal.emit(printImage(self.img))
                    try:
                        self.o_num,_,_ = detector.detectAndDecode(self.img)
                        print(self.o_num)
                        self.o_num =int(self.o_num)
                        print(Order_Lists)
                        if(self.o_num in Order_Lists):
                            print(Order_Lists)
                            print("self.o_num is in OrderLIST")
    
                            if(Address_Info[str(self.o_num)] not in Address_Lists):
                                self.MSG(f"Order Num({self.o_num}) Address not in Address")
                                print("self.o_num's address not exist")
                            else:
                                print("self.o_num's address is exist")
                                addr = Address_Info[str(self.o_num)]
                                print(type(addr))
                                print(f"Addr = {addr}")
                                TOPIC=TOPIC_DIV_SERVO+addr+"/info"
                                print(TOPIC)
                                self.myMQTTClient.publish(TOPIC,"{\"msg\":\"go\"}",0)
                                self.MSG(f"Get Address({addr}) Success")
                                print("MSG Success")
                                tot=tot+1
                                print("tot plus")
                                Order_Lists.remove(int(self.o_num))
                                print("Order List remove")
                                del Address_Info[str(self.o_num)]
                                print(Order_Lists)
                                print(Address_Info)
                    except Exception as e:
                        print(f"Error:{e}")
                    
                else:
                    print("Capture Fail")
            except KeyboardInterrupt:
                sys.exit()
            time.sleep(0.01)

    def add_Order(self,self2,params,packet):
        global power
        if (power != 1):
            power = 1
        data = json.loads(packet.payload)

        order_num = int(data["order_num"])
        if(order_num not in Order_Lists):
            Order_Lists.append(order_num)
            Address_Info[str(order_num)] = str(data["address"])
            self.MSG(f"Order Num({order_num}) Add Success")
        else:
            self.MSG(f"Order Num({order_num}) Already exist")
        print(Address_Info)
        print(Order_Lists)


    def change_Power(self,self2,params,packet):
        global power
        data = json.loads(packet.payload)
        power = int(data["power"])

    def MSG(self,str):
        self.myMQTTClient.publish(TOPIC_WEB_LOG, f"{{\"dev\":\"Div_Veirifier\",\"content\":\"{str}\"}}",0)


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
        self.th.mySignal.connect(self.setImage)
        self.logo = cv2.imread("./logo.jpg")
        self.ui.Logo.setPixmap(printImage(self.logo))
        self.th.start()
 
    def showNum(self):
        if(len(Order_Lists)>0):
            self.ui.OrderNum.display(Order_Lists[0])
        else:
            self.ui.OrderNum.display(0)
        self.ui.TotalCnt.display(tot)

        self.showth = threading.Timer(1,self.showNum)
        self.showth.start()

    def setImage(self,img):
        self.ui.ShowImg.setPixmap(img)
        
    def closeEvent(self,event):
        self.th.terminate()
        self.th.wait(3000)
        self.close()


app=QApplication()
win=MyApp()
win.showMaximized()
app.exec_()

