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
from PySide2.QtWidgets import (QApplication, QLCDNumber, QLabel, QListWidget,
    QListWidgetItem, QMainWindow, QMenuBar, QSizePolicy,
    QStatusBar, QWidget)

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        if not MainWindow.objectName():
            MainWindow.setObjectName(u"MainWindow")
        MainWindow.resize(1366, 780)
        MainWindow.setStyleSheet(u"background-color:rgb(53,53,53)")
        self.centralwidget = QWidget(MainWindow)
        self.centralwidget.setObjectName(u"centralwidget")
        self.ShowImg = QLabel(self.centralwidget)
        self.ShowImg.setObjectName(u"ShowImg")
        self.ShowImg.setGeometry(QRect(420, 210, 640, 480))
        self.TotalCnt = QLCDNumber(self.centralwidget)
        self.TotalCnt.setObjectName(u"TotalCnt")
        self.TotalCnt.setGeometry(QRect(90, 180, 241, 71))
        self.label = QLabel(self.centralwidget)
        self.label.setObjectName(u"label")
        self.label.setGeometry(QRect(420, 70, 291, 81))
        font = QFont()
        font.setPointSize(25)
        self.label.setFont(font)
        self.label.setStyleSheet(u"color:white\n"
"")
        self.label.setAlignment(Qt.AlignCenter)
        self.label.setMargin(5)
        self.label_2 = QLabel(self.centralwidget)
        self.label_2.setObjectName(u"label_2")
        self.label_2.setGeometry(QRect(80, 90, 251, 51))
        self.label_2.setFont(font)
        self.label_2.setStyleSheet(u"color:white")
        self.label_2.setAlignment(Qt.AlignCenter)
        self.label_2.setMargin(5)
        self.label_3 = QLabel(self.centralwidget)
        self.label_3.setObjectName(u"label_3")
        self.label_3.setGeometry(QRect(90, 290, 251, 41))
        self.label_3.setFont(font)
        self.label_3.setStyleSheet(u"color:white")
        self.label_3.setAlignment(Qt.AlignCenter)
        self.label_3.setMargin(5)
        self.label_4 = QLabel(self.centralwidget)
        self.label_4.setObjectName(u"label_4")
        self.label_4.setGeometry(QRect(90, 470, 251, 51))
        self.label_4.setFont(font)
        self.label_4.setStyleSheet(u"color:white")
        self.label_4.setAlignment(Qt.AlignCenter)
        self.label_4.setMargin(5)
        self.OrderNum = QLCDNumber(self.centralwidget)
        self.OrderNum.setObjectName(u"OrderNum")
        self.OrderNum.setGeometry(QRect(90, 370, 241, 71))
        self.listWidget = QListWidget(self.centralwidget)
        self.listWidget.setObjectName(u"listWidget")
        self.listWidget.setGeometry(QRect(90, 540, 251, 141))
        self.Logo = QLabel(self.centralwidget)
        self.Logo.setObjectName(u"Logo")
        self.Logo.setGeometry(QRect(820, 70, 250, 119))
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QMenuBar(MainWindow)
        self.menubar.setObjectName(u"menubar")
        self.menubar.setGeometry(QRect(0, 0, 1366, 22))
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
        self.label_2.setText(QCoreApplication.translate("MainWindow", u"Success Order", None))
        self.label_3.setText(QCoreApplication.translate("MainWindow", u"Order Number", None))
        self.label_4.setText(QCoreApplication.translate("MainWindow", u"Product Lists", None))
        self.Logo.setText(QCoreApplication.translate("MainWindow", u"TextLabel", None))
    # retranslateUi

