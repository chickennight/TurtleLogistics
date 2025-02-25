# S09P11A204

팀장만P래~

# V1.3.2

<전체를 뒤엎을 변화>,<기능 수정, 기능 추가>,<버그, 내부 적 코드 보완>

## 기간
> 2023/07/04 ~ 2023/08/18

## 서비스 소개

> Turtle Logistics

- 물류 자동화 시스템

## 발표자료
추후 업데이트 예정

## 기능구현
- IoT
  - 수신한 주문 데이터에 따른 상품을 컨베이어 벨트로 이동
  - 상품 갯수에 따른 피스톤 동작 횟수 입력으로 상품 이동
  - 카메라 모듈을 통해 QR을 인식하여 주문 데이터와 맞는 상품인지 확인
  - 주문 데이터의 주소에 따른 가름막 작동으로 분류
  - 분류가 주소에 알맞게 되었는지 가름막의 센서로 확인 후 정보 전송
- Web
  - 관리자 / 사용자 회원가입, 로그인, 로그아웃
  - 기간별(1주, 1개월, 3개월, 6개월, 1년) 주문 건수 그래프
  - 지역별(광역시, 도) 주문 상황 조회 지도
  - 현재 주문 접수, 분류, 배송 상황에 따른 집계 그래프
  - 분류 시스템의 오류 현황 로그 조회
  - 오류 발생 지점을 파악하는 단면도 조회
  - 공정 전체 과정을 관찰하는 CCTV 시스템
  - 공정 작동
- DB
  - DB 최적화 인덱싱 작업을 통해 약 500만개의 데이터에서 빠르게 물류 데이터를 조회
  - 집계 그래프를 위한 주문 접수, 분류, 배송 상황 정보
<br>

## 팀원 소개 및 담당 역할

- OOO (팀장) : Frontend 메인, Backend 보조, 발표
- chickennight (팀원) : Backend 메인, Frontend 보조
- OOO (팀원) : Backend 메인, Frontend 보조
- OOO (팀원) : IoT 개발
- OOO (팀원) : 기구 개발
- OOO (팀원) : IoT 개발

<br>

## 기술 스택

- IoT : PyQT, AWS IoT Core, MQTT, OpenCV, C++, Python
- Frontend : Vue.js, JS
- Backend : Java, Spring Boot, Spring Security, MySQL, JPA
- Infra : Jenkins, AWS EC2, NginX, Docker, Certbot

<br>

## DETAIL

### 0. Conventions

- **git commit message**

  > type: Subject 형태로 작성하기

  type

  - [feat]: A new feature
  - [fix]: A bug fix
  - [docs]: Changes to documentation
  - [style]: Formatting, missing semi colons, etc; no code change
  - [refactor]: Refactoring production code
  - [test]: Adding tests, refactoring test; no production code change
  - [chore]: Updating build tasks, package manager configs, etc; no production code change

  Subject

  - 50자를 넘기지 말 것
  - 대문자로 시작하며 마침표는 적지 않을 것
  - 명령조로 말할것. 예를들면 changed, changes 가 아닌 change
  - 메인 커밋 메세지에 []를 통해서 표시해주기

- **GitLab branch 전략**

  - master : 제품으로 출시될 수 있는 브렌치. 최선의 상태 유지
  - study : 프로젝트 기간동안 공부한 내용을 각자 정리하는 브렌치
  - dev : 개발과정에서 fe/be/iot의 코드를 모아 빌드하여 테스트 하는 브렌치
  - feature/(fe, be, iot)/기능명 : 해당 기능 구현을 진행할 때 사용하는 브렌치
  - hotfix : master의 문제를 해결하기 위한 브렌치

- **Jira**

  - 이슈생성
  - 월요일에 주 단위 스프린트 이슈들을 생성
  - 일주일에 40시간 이상 이슈 생성 및 처리
  - 이슈 완료
  - 하나의 티켓은 되도록 하나의 커밋으로 처리

- **Style Guide**

  FE

  - var대신 const, let 사용하기
  - 가능한 Arrow Function 사용하지 않기
  - 변수명 : file_scope 상수는 UPPER_SNAKE_CASE, 그 외엔 camelCase. Boolean변수는 앞에 is 붙이기.
  - 클래스(생성자)명 : 파스칼 케이스를 이용한다. PascalCase
  - 클래스(HTML)명 : 케밥 케이스를 이용한다. kebal-case

  BE

  - 변수명, 인스턴스명: camelCase
  - 상수 변수명(static final): 대문자, 띄어쓰기는 \_ 사용
  - 클래스명, 생성자명: PascalCase
  - 함수명: camelCase(동사 + 명사로 구성)
  - Mysql table, column name: snake_case

  IoT

  - snake_case
  - 

### 1. Planning & Design

- 아이디어 정리
![Subject.png](./image/Subject.png)
- 와이어 프레임
![image.png](./image/wire.png)
- ERDCloud

![ERD_DIAGRAM.png](./image/ERD_DIAGRAM.png)
- API 명세서
![API_Image](./image/teamP_API.png)
- 아키텍처
  <img src="ProfileImage/IoTArci.png">
  <img src="ProfileImage/WebArci.png">

<br>

### 2. Development & Test

- H/W Development

  - 피스톤 <br>
    ### vh.1
    ![piston_vh1](./image/HW/piston/vh1.png) ![piston_vh11.png](./image/HW/piston/vh1-1.png)
    ### vh.2
    ![piston_vh2](./image/HW/piston/vh22.jpg)
    ### vh.3
    ![piston_vh3](./image/HW/piston/vh3.png) ![piston_vh31](./image/HW/piston/vh3-1.png)
  - 컨베이어 벨트 <br>
    ### iv.1
    <img src="./image/HW/belt/ih1.jpg" width="500"> <img src="./image/HW/belt/ih11.png" width="500">
    ### iv.2
    <img src="./image/HW/belt/ih2.jpg" width="500"> <br>
    ### iv.3
    <img src="./image/HW/belt/ih3.jpg" width="500">
  - 전체 공정 <br>
    <img src="./image/HW/hw_main.jpg" width="500">

- Jira를 통한 일정관리
  - 매 주 월요일에 주 단위 이슈를 생성하고 금요일까지 40의 Story Point를 완료함으로써 일정을 관리했습니다

  ![3week_jira](./image/3week_jira.png)
  3주차 번다운 차트

  ![4week_jira](./image/4week_jira.png)
  4주차 번다운 차트

  ![5week_jira](./image/5week_jira.png)
  5주차 번다운 차트

  ![6week_jira](./image/6week_jira.PNG)
  5주차 번다운 차트

- Confluence를 통한 문서 작업 및 기록

### 3. Operation

- Web

| 웹 페이지 | 주요 화면 |
| ---- | ---- |
| 웹 페이지 메인화면| 관리자 페이지 메인화면 |
| <img width="400" src="image/web/web_main.PNG"> | <img width="400" src="image/web/admin_main.png"> |
| 주문을 넣기 위한 사용자 페이지, 관리를 위한 관리자 페이지로 이동할 수 있습니다 | 주요 기능을 간소화하여 메인화면에서 한 눈에 보기 쉽습니다 |
| 기간별 주문 데이터 조회 | 지역별 주문 데이터 조회 |
| <img width="400" src="image/web/admin_date.png"> | <img width="400" src="image/web/admin_region.png"> |
| 일주일, 한 달, 세 달, 6개월, 1년의 주문 건수 데이터를 조회할 수 있습니다 | 도, 광역시별로 배송 현황을 조회할 수 있습니다 |
| 물류 분석 데이터 조회 | 공정 현황 조회 |
| <img width="400" src="image/web/admin_logistics.png"> | <img width="400" src="image/web/admin_machine.png"> |
| 물류 재고와 소비량을 분석하여 나타냅니다 | 주요 공정의 현황을 나타냅니다 |
| 기기 제어 | cctv |
| <img width="400" src="image/web/admin_control.png"> | <img width="400" src="image/web/admin_cctv.png"> |
| 에러가 발생했을 시 단면도를 통해 확인하고, 비상 상황에서 전체 공정의 전원을 제어할 수 있습니다 | CCTV를 통해 메인 공정을 실시간으로 확인할 수 있습니다 |

- HW

| HW | 주요 작동 영상 |
| ---- | ---- |
| 주문 피스톤 작동 영상 | 상품 분류막 작동 영상 |
| 영상 추후 업로드 예정 | 영상 추후 업로드 예정 |

### 4. ETC
