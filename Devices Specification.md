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
|Pub|to Order_Scheduler|sup/ord/sch/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|
|Pub|to Order_Verifier|sup/ord/veri/info|{"order_num":"1001","productA":"1","productB":"2","productC":"0"}|
|Pub|to Divide_Verifier|sup/div/veri/info||
|Sub|from Order_Verifier|||
|Sub|from Divide_Verifier|||
|Sub|from Web|web/mod/power|{"power":"1"}|


## Order_Schdeuler
- ##### 기능
	- Supervisor에게 받은 주문 목록에 해당하는 상품 보관함의 피스톤을 작동
	- 시스템 내 디바이스 통신(MQTT)
		- from Supervisor : 주문 번호 및 주문 목록
- ##### H/W 스펙
	- 아두이노 D1 R1
	- 서보모터 mg996r : 3ea
	- 1.5V 배터리 4구 : 3ea
## 주문 - 검증기
- ##### 기능
	- 
- ##### H/W 스펙
	- 
##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|||||


## 주문 - 모터
- ##### 기능

- ##### H/W 스펙
	-  esp32 : 1ea
	-  1.5V 배터리 4구 : 1ea
	-  모터 드라이버 L298N : 1ea
	-  DC모터 25GA370 : 1ea

- ##### Topic description
|Pub/Sub|Direction|Topic|Detail|
|----------|----------|----------|----------|
|||||
