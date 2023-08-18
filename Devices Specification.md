## Supervisor

- ##### 기능
	-  웹 서버로부터 받은 데이터를 시스템 내 디바이스들에게 전달
	-  특정 디바이스들로부터 받은 작동 결과를 웹에 전달
	-  웹 서버 통신(RSET API)
		- to Web : 주문번호, 주문/분류 처리 결과
		- from Web : 주문번호, 주문목록, 배송지 정보
	-  시스템 내 디바이스 통신(MQTT)
		- to Order_Scheduler : 주문 번호 및 주문 목록
		- to Order_Verifier : 주문 번호 및 주문 목록
		- to Divde_Verifer : 주문 번호 및 배송지 정보, 온습도 정보
		- from Order_Verifer : 상품 선택 성공 여부
		- from Divide_Divider : 분류 성공 여부


- ##### H/W 스펙
	- esp32 : 1ea
	- 1.5V 배터리 4구  : 1ea


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Supervisor", "content":"message"}|
|Pub|to Order_Scheduler|/sup/ord/sch/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|
|Pub|to Order_Verifier|/sup/ord/veri/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|
|Pub|to Divide_Verifier|/sup/div/veri/info|{"order_num":"1001", "address":"1"}|
|Pub|to Divide_Verifier|/sup/div/veri/env/info|{"temp":"23", "humid":"55"}|
|Sub|from Web|/mod/web/power|{"power":"1"}|
|Sub|from Order_Verifier|/ord/res|{"order_num":"1001", "result":"1"}|
|Sub|from Divide_Divider|/div/res|{"order_num":"1001", "result":"1"}|


## Order_Schdeuler

- ##### 기능
	- Supervisor에게 받은 주문 목록에 해당하는 상품 보관함의 피스톤을 작동
	- 시스템 내 디바이스 통신(MQTT)
		- from Web : 전원, 피스톤 관련 각도 설정, 피스톤 작동 간격
		- from Supervisor : 주문 번호 및 주문 목록


- ##### H/W 스펙
	- 아두이노 D1 R1
	- 서보모터 mg996r : 3ea
	- 1.5V 배터리 4구 : 3ea


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Ord_Scheduler", "content":"message"}|
|Sub|from Web|/mod/web/power|{"power":"1"}|
|Sub|from Web|/mod/ord/sch/start_angle|{"angle":"70"}|
|Sub|from Web|/mod/ord/sch/end_angle|{"angle":"180"}|
|Sub|from Web|/mod/ord/sch/interval|{"interval":"2000"}|
|Sub|from Web|/mod/ord/piston1/angle|{"angle":"70"}|
|Sub|from Web|/mod/ord/piston2/angle|{"angle":"70"}|
|Sub|from Web|/mod/ord/piston3/angle|{"angle":"70"}|
|Sub|from Supervisor|/sup/ord/sch/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|


## Order_Verifier

- ##### 기능
	-  상품의 QR을 인식하여 현재 주문 목록에 포함되는지 확인
		- 주문 목록에 포함되지 않은 상품이 인식된다면 DC모터에 정지 신호를 게시
		- 주문 목록 검증 결과를 Supervisor에 게시
	-  시스템 내 디바이스 통신(MQTT)
		- to Supervisor : 주문 번호 및 주문 목록
		- to Order_Motor : QR 인식 오류 시 모터 전원 제어
		- from Web : 전원, QR 인식 간격
		- from Supervisor : 주문 번호 및 주문 목록


- ##### H/W 스펙
	- Rasberry Pi4
	- Rasberry Pi Camera Rev 1.3


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Ord_Verifier", "content":"message"}|
|Pub|to Order_Motor|/mod/ord/motor/power|{"power":"-1"}|
|Pub|to Supervisor|/ord/res|{"order_num":"1001", "type":"0", "result":"1"}|
|Sub|from Web|/mod/web/power|{"power":"1"}|
|Sub|from Web|/mod/ord/veri/interval|{"interval":"0.3"}|
|Sub|from Supervisor|/sup/ord/veri/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|



## Order_Motor

- ##### 기능
	-  컨베이어 벨트를 작동시키는 DC 모터
		- 주문 검증기에서 오류 신호를 게시하면 작동을 중지
	  - 시스템 내 디바이스 통신(MQTT)
		- from Web : 전원, 모터 속도
		- from Order_Verifier : QR 인식 오류 시 모터 전원 제어


- ##### H/W 스펙
	-  esp32 : 1ea
	-  1.5V 배터리 4구 : 1ea
	-  모터 드라이버 L298N : 1ea
	-  DC모터 25GA370 : 1ea


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Ord_Motor", "content":"message"}|
|Sub|from Web|/mod/ord/motor/speed|{"speed":"180"}|
|Sub|from Web|/web/mod/power|{"power":"1"}|
|Sub|from Order_Verifier|/mod/ord/motor/power|{"power":"-1"}|


##  Divide_Verifier

- ##### 기능
	-  QR을 인식하여 배송지 정보를 알아내고 이에 해당하는 분류기에 신호를 게시
	- 시스템 내 디바이스 통신(MQTT)
		- to Supervisor : 분류 성공 여부
		- to Divde_Divider : 주문번호 
		- from Web : 전원
		- from Supervisor : 주문번호 및 배송지 정보, 온습도 정보


- ##### H/W 스펙
	- Rasberry Pi4
	- Rasberry Pi Camera Rev 1.3


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Div_Verifier", "content":"message"}|
|Pub|to Supervisor|/div/res|{"order_num":"1001", "result":"1"}|
|Pub|to Divide_Divider|/div/servo[3]/info|{"order_num":"1001"}|
|Sub|from Web|/web/mod/power|{"power":"1"}|
|Sub|from Supervisor|/sup/div/veri/info|{"order_num":"1001", "address":"1"}|
|Sub|from Supervisor|/sup/div/veri/env/info|{"temp":"23", "humid":"55"}|


## Divide_Motor

- ##### 기능
	- 분류 컨베이어 벨트 작동 모터

- ##### H/W 스펙
	- esp32

- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Div_Motor", "content":"message"}|
|Pub|from Web|/mod/web/power|{"power":"-1"}|
|Pub|from Web|/lmod/div/motor/speed|{"speed":"190"}|



## Divide_Divider[3]

- ##### 기능
	- 포장된 상자를 지정된 장소로 분류
	- Divide_Verifier에게 작동 지시를 받음
	- Interval 만큼 대기 후 분류 막대기 작동(0º->65º->0º)
	- 분류 막대기 작동 후 적외선 센서를 통해 2차 검증
		- type (0:Order, 1:Divider), result (0:Error, 1:Success)

- ##### H/W 스펙
	- esp12E(NodeMCU V3)
	- esp12F(NodeMCU V2)
	- esp8266(NodeMCU devkit V0.9)

- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Div_Servo[3]", "content":"message"}|
|Pub|to Supervisor|/div/res|{"order_num":"2308000001", "type":"1",result:"0"}|
|Sub|from Div_Verifier|/mod/div/servo[3]/info|{"order_num":"230800001"}|
|Sub|from Web|/mod/div/servo[3]/angle|{"angle":"65"}|
|Sub|from Web|/mod/div/servo[3]/servo_interval|{"servo_interval":"1000"}|
|Sub|from Web|/mod/div/servo[3]/wait_interval|{"wait_interval":"1500"}|
|Sub|from Web|/mod/div/servo[3]/ir_interval|{"ir_interval":"1000"}|



