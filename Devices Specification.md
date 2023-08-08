## Supervisor

- ##### 기능
	-  웹 서버로부터 받은 데이터를 시스템 내 디바이스들에게 전달
	-  특정 디바이스들로부터 받은 작동 결과를 웹에 전달
	-  웹 서버 통신(RSET API)
		- to Web :
		- from Web :
	-  시스템 내 디바이스 통신(MQTT)
		- to Order_Scheduler : 주문 번호 및 주문 목록
		- to Order_Verifier : 주문 번호 및 주문 목록
		- to Divde_Verifer : 주문 번호 및 배송지 정보
		- from Order_Verifer : 상품 선택 성공 여부
		- from Divide_Divider : 분류 성공 여부


- ##### H/W 스펙
	- esp32 : 1ea
	- 1.5V 배터리 4구  : 1ea


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Supervisor", "content":"message"}|
|Pub|to Order_Scheduler|sup/ord/sch/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|
|Pub|to Order_Verifier|sup/ord/veri/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|
|Pub|to Divide_Verifier|sup/div/veri/info|{"order_num":"1001", "address":"1"}|
|Sub|from Web|web/mod/power|{"power":"1"}|
|Sub|from Order_Verifier|/ord/res|{"order_num":"1001", "result":"1"}|
|Sub|from Divide_Divider|/div/res|{"order_num":"1001", "result":"1"}|


## Order_Schdeuler

- ##### 기능
	- Supervisor에게 받은 주문 목록에 해당하는 상품 보관함의 피스톤을 작동
	- 시스템 내 디바이스 통신(MQTT)
		- from Supervisor : 주문 번호 및 주문 목록


- ##### H/W 스펙
	- 아두이노 D1 R1
	- 서보모터 mg996r : 3ea
	- 1.5V 배터리 4구 : 3ea


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Ord_Scheduler", "content":"message"}|
|Sub|from Web|/web/mod/power|{"power":"1"}|
|Sub|from Supervisor|/sup/ord/sch/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|

## Order_Verifier

- ##### 기능
	-  상품의 QR을 인식하여 현재 주문 목록에 포함되는지 확인
		- 주문 목록에 포함되지 않은 상품이 인식된다면 DC모터에 정지 신호를 게시
		- 주문 목록 검증 결과를 Supervisor에 게시


- ##### H/W 스펙
	- Rasberry Pi4
	- Rasberry Pi Camera Rev 1.3


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|Pub|to Web|/log|{"dev":"Ord_Verifier", "content":"message"}|
|Pub|to Order_Motor|/mod/ord/motor/power|{"power":"-1"}|
|Pub|to Supervisor|/ord/res|{"order_num":"1001", "result":"1"}|


## Order_Motor

- ##### 기능
	-  컨베이어 벨트를 작동시키는 DC 모터
		- 주문 검증기에서 오류 신호를 게시하면 작동을 중지

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


- ##### H/W 스펙


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|


## Divide_Motor

- ##### 기능


- ##### H/W 스펙


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|


## Divide_Divider[3]

- ##### 기능


- ##### H/W 스펙


- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|


