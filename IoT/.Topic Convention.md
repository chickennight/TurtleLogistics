# Topic convention
```
(수정) / 역할 / 기기명 / 값 
수정 : mod
역할 : ord,div,web
기기명 : sch, veri, cam, servo
값 : power, interval, speed, distance,info,res
```
```
A가 B에게 보낸다, A가 나(B)에게 보낸다
=>/A역할/B역할/B기기명/value
=>/sup/div/veri/info
```
supervisor
/sup/ord/sch/info = add
/sup/ord/veri/info
/sup/div/veri/info
/order/res

|Json key|
|:-:|
|power|
|result|
|order_num|
|interval|
|speed|
|distance|
|address|
|productA|
|productB|
|productC|
|servo_interval|
|ir_interval|

※ 예외 : /sup/init

[Supervisor]

/mod/ord/motor/power

/mod/div/motor/power

/mod/web/power

/sup/ord/sch/info 

/sup/ord/veri/info

/sup/div/veri/info


/ord/res

/div/res

/log



[Div_Verifier]

/log

/div/ser1/info

/div/cam

/sup/div/veri

/mod/div/motor/power



/mod/web/power

/mod/div/veri/interval

/mod/div/veri/distance


[Servo]
/mod/web/power

/div/servo1/info => 주문번호 받음

/mod/div/ser1/angle 

/mod/div/ser1/servo_interval

/mod/div/ser1/ir_interval

/log

/div/res


[Motor]


/mod/web/power -> 전체 제어용

/mod/ord/motor/power -> 사진 찍을때 , 주문 왔을 때 

/mod/div/motor/speed -> 디버깅용 속도 조절

/log

