{orderno, producA:n, productB:n, producC:n , address}

// 매개 변수 바꾸기

speed : 속도
angle : 각도
interval : 딜레이 시간



Div_Servo3 : 함수 헤더파일에 모두

에픽 : MQTT 통신
	중복 함수, 라이브러리 통합 헤더파일 만들기

에픽 : MQTT 모듈 테스트

	보드 & 모듈 연동 실험
	(SUP / SCH / DIV / VERI / MOTOR)

	SUP :      Get -> PUB 되는지
        		SUB -> POST 되는지
		=> VERI 아직 주문 있다면 어떡할지 ? 
		=> MQTT 완

	SCH : 

	VERI :      SUB 되는지 ( order_no, order_list)
        	 	 초음파 센서 감지(pub) -> 카메라 센서 수신(sub) -> 찍기-> QR -> 확인
         		 PUB 되는지 (/order/res, order_no,result)

	DIV : (완)  SUB 되는지 (/div/info)
       		 PUB 되는지 (/div/res)
       		모터 작동/IR 센서 작동 잘 되는지

	Motor :   SUB 되는지(/web/power)
           		SUB 되는지(/sup/init)
           		sub 키(result) 값에 따라 모터가 회전 시작/멈춤 되는지

에픽 : MQTT 모듈 디버깅

	기기 & 배터리 연결(납땜? 임시 브레드보드?)
	구현 완료된 HW 이용 디버깅

에픽 : 중간 PPT 자료 만들기




HW 초안 ~ 7/26

MUC - 모듈 연결, 업로드 ~ 7/26
모듈 기기  테스트 7/27 ~
중간 발표 PPT 제작  ~ 7/27
디버깅 완료 ~ 7/28
납땜 완료? ~ 8/1
