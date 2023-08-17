# -*- coding: utf-8 -*-

################################################################################
## Form generated from reading UI file 'ui.ui'
##
## Created by: Qt User Interface Compiler version 6.5.2
##
## WARNING! All changes made in this file will be lost when recompiling UI file!
################################################################################

from PySide2.QtCore import (QCoreApplication, QDate, QDateTime, QLocale,
    QMetaObject, QObject, QPoint, QRect,
    QSize, QTime, QUrl, Qt)
from PySide2.QtGui import (QBrush, QColor, QConicalGradient, QCursor,
    QFont, QFontDatabase, QGradient, QIcon,
    QImage, QKeySequence, QLinearGradient, QPainter,
    QPalette, QPixmap, QRadialGradient, QTransform)
from PySide2.QtWidgets import (QApplication, QLCDNumber, QLabel, QMainWindow,
    QMenuBar, QSizePolicy, QStatusBar, QTextEdit,
    QWidget)

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        if not MainWindow.objectName():
            MainWindow.setObjectName(u"MainWindow")
        MainWindow.resize(1121, 780)
        MainWindow.setStyleSheet(u"background-color:rgb(55,55,55)")
        self.centralwidget = QWidget(MainWindow)
        self.centralwidget.setObjectName(u"centralwidget")
        self.ShowImg = QLabel(self.centralwidget)
        self.ShowImg.setObjectName(u"ShowImg")
        self.ShowImg.setGeometry(QRect(420, 210, 640, 480))
        self.SucCnt = QLCDNumber(self.centralwidget)
        self.SucCnt.setObjectName(u"SucCnt")
        self.SucCnt.setGeometry(QRect(90, 320, 111, 71))
        font = QFont()
        font.setPointSize(50)
        self.SucCnt.setFont(font)
        self.SucCnt.setStyleSheet(u"color:white;")
        self.SucCnt.setDigitCount(3)
        self.label = QLabel(self.centralwidget)
        self.label.setObjectName(u"label")
        self.label.setGeometry(QRect(410, 80, 291, 81))
        font1 = QFont()
        font1.setPointSize(25)
        self.label.setFont(font1)
        self.label.setStyleSheet(u"color:white;\n"
"border : 2px solid rgb(230, 72, 120);\n"
"border-radius:5px")
        self.label.setAlignment(Qt.AlignCenter)
        self.label.setMargin(5)
        self.label_2 = QLabel(self.centralwidget)
        self.label_2.setObjectName(u"label_2")
        self.label_2.setGeometry(QRect(80, 240, 131, 51))
        self.label_2.setFont(font1)
        self.label_2.setStyleSheet(u"color:white;\n"
"border : 2px solid rgb(230, 72, 120);\n"
"border-radius:5px")
        self.label_2.setAlignment(Qt.AlignCenter)
        self.label_2.setMargin(5)
        self.label_3 = QLabel(self.centralwidget)
        self.label_3.setObjectName(u"label_3")
        self.label_3.setGeometry(QRect(90, 420, 251, 41))
        self.label_3.setFont(font1)
        self.label_3.setStyleSheet(u"color:white;\n"
"border : 2px solid rgb(230, 72, 120);\n"
"border-radius:5px")
        self.label_3.setAlignment(Qt.AlignCenter)
        self.label_3.setMargin(5)
        self.label_4 = QLabel(self.centralwidget)
        self.label_4.setObjectName(u"label_4")
        self.label_4.setGeometry(QRect(90, 550, 251, 51))
        self.label_4.setFont(font1)
        self.label_4.setStyleSheet(u"color:white;\n"
"border : 2px solid rgb(230, 72, 120);\n"
"border-radius:5px")
        self.label_4.setAlignment(Qt.AlignCenter)
        self.label_4.setMargin(5)
        self.Logo = QLabel(self.centralwidget)
        self.Logo.setObjectName(u"Logo")
        self.Logo.setGeometry(QRect(820, 70, 250, 119))
        self.FailCnt = QLCDNumber(self.centralwidget)
        self.FailCnt.setObjectName(u"FailCnt")
        self.FailCnt.setGeometry(QRect(230, 320, 111, 71))
        self.FailCnt.setFont(font)
        self.FailCnt.setStyleSheet(u"color:white;")
        self.FailCnt.setDigitCount(3)
        self.label_5 = QLabel(self.centralwidget)
        self.label_5.setObjectName(u"label_5")
        self.label_5.setGeometry(QRect(220, 240, 131, 51))
        self.label_5.setFont(font1)
        self.label_5.setStyleSheet(u"color:white;\n"
"border : 2px solid rgb(230, 72, 120);\n"
"border-radius:5px")
        self.label_5.setAlignment(Qt.AlignCenter)
        self.label_5.setMargin(5)
        self.OrderNum = QTextEdit(self.centralwidget)
        self.OrderNum.setObjectName(u"OrderNum")
        self.OrderNum.setGeometry(QRect(90, 480, 251, 51))
        font2 = QFont()
        font2.setPointSize(10)
        self.OrderNum.setFont(font2)
        self.OrderNum.setStyleSheet(u"color:white;\n"
"border:0px;\n"
"margin-top:3px;")
        self.Addr = QTextEdit(self.centralwidget)
        self.Addr.setObjectName(u"Addr")
        self.Addr.setGeometry(QRect(90, 620, 251, 51))
        self.Addr.setFont(font2)
        self.Addr.setStyleSheet(u"color:white;\n"
"border:0px;\n"
"margin-top:3px;")
        self.label_6 = QLabel(self.centralwidget)
        self.label_6.setObjectName(u"label_6")
        self.label_6.setGeometry(QRect(80, 80, 131, 51))
        self.label_6.setFont(font1)
        self.label_6.setStyleSheet(u"color:white;\n"
"border : 2px solid rgb(230, 72, 120);\n"
"border-radius:5px")
        self.label_6.setAlignment(Qt.AlignCenter)
        self.label_6.setMargin(5)
        self.label_7 = QLabel(self.centralwidget)
        self.label_7.setObjectName(u"label_7")
        self.label_7.setGeometry(QRect(80, 140, 131, 51))
        self.label_7.setFont(font1)0
        self.label_7.setStyleSheet(u"color:white;\n"
"border : 2px solid rgb(230, 72, 120);\n"
"border-radius:5px")
        self.label_7.setAlignment(Qt.AlignCenter)
        self.label_7.setMargin(5)
        self.Temp = QTextEdit(self.centralwidget)
        self.Temp.setObjectName(u"Temp")
        self.Temp.setGeometry(QRect(230, 80, 121, 41))
        self.Humid = QTextEdit(self.centralwidget)
        self.Humid.setObjectName(u"Humid")
        self.Humid.setGeometry(QRect(230, 150, 121, 41))
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QMenuBar(MainWindow)
        self.menubar.setObjectName(u"menubar")
        self.menubar.setGeometry(QRect(0, 0, 1121, 22))
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QStatusBar(MainWindow)
        self.statusbar.setObjectName(u"statusbar")
        MainWindow.setStatusBar(self.statusbar)

        self.retranslateUi(MainWindow)

        QMetaObject.connectSlotsByName(MainWindow)
    # setupUi

    def retranslateUi(self, MainWindow):
        MainWindow.setWindowTitle(QCoreApplication.translate("MainWindow", u"MainWindow", None))
        self.ShowImg.setText("")
        self.label.setText(QCoreApplication.translate("MainWindow", u"Live Streaming", None))
        self.label_2.setText(QCoreApplication.translate("MainWindow", u"Success", None))
        self.label_3.setText(QCoreApplication.translate("MainWindow", u"Order Number", None))
        self.label_4.setText(QCoreApplication.translate("MainWindow", u"Address", None))
        self.Logo.setText("")
        self.label_5.setText(QCoreApplication.translate("MainWindow", u"Fail", None))
        self.OrderNum.setHtml(QCoreApplication.translate("MainWindow", u"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><meta charset=\"utf-8\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"hr { height: 1px; border-width: 0; }\n"
"li.unchecked::marker { content: \"\\2610\"; }\n"
"li.checked::marker { content: \"\\2612\"; }\n"
"</style></head><body style=\" font-family:'\ub9d1\uc740 \uace0\ub515'; font-size:10pt; font-weight:400; font-style:normal;\">\n"
"<p align=\"center\" style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><br /></p></body></html>", None))
        self.Addr.setHtml(QCoreApplication.translate("MainWindow", u"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><meta charset=\"utf-8\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"hr { height: 1px; border-width: 0; }\n"
"li.unchecked::marker { content: \"\\2610\"; }\n"
"li.checked::marker { content: \"\\2612\"; }\n"
"</style></head><body style=\" font-family:'\ub9d1\uc740 \uace0\ub515'; font-size:10pt; font-weight:400; font-style:normal;\">\n"
"<p align=\"center\" style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><br /></p></body></html>", None))
        self.label_6.setText(QCoreApplication.translate("MainWindow", u"Temp", None))
        self.label_7.setText(QCoreApplication.translate("MainWindow", u"Humid", None))
    # retranslateUi

