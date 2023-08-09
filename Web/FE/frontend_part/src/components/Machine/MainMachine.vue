<template>
  <div class="MachineMainContainer">
    <div class="MachineContainer">
      <h1>기기제어</h1>
      <span class="MachineSpan">
        <h2>{{ this.currentTime }}</h2>
        &nbsp;&nbsp;&nbsp;
        <v-btn @click="getMachineOff" background-color="rgb(53, 53, 53)" variant="outlined">
          전원 종료
        </v-btn>
        <v-btn @click="getMachineOn" background-color="rgb(53, 53, 53)" variant="outlined">
          전원 시작
        </v-btn>
      </span>
    </div>
    <div class="LogTableContainer">
      <div class="MachineImgContainer"><img class="machineImg" :src="errorImg" /></div>
      <v-table density="compact" theme="dark">
        <thead>
          <tr>
            <th class="text-left">번호</th>
            <th class="text-left">날짜</th>
            <th class="text-left">로그명</th>
            <th class="text-left">기계</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in machineLog" :key="item.log_num" @click="changeImg(item.machine_id)">
            <td>{{ item.log_num }}</td>
            <td>{{ item.error_date }}</td>
            <td>{{ item.error_message }}</td>
            <td>{{ item.machine_id }}</td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MainMachine",
  data: () => ({
    errorImg: "/Error_BluePrint/error_nukki.png",
    myTimer: null,
    currentTime: "",
    previousMachineLog: [],
  }),
  methods: {
    async getMachineLog() {
      let today = new Date();

      let month = today.getMonth() + 1; // 월
      let date = today.getDate(); // 날짜

      let hours = today.getHours(); // 시
      let minutes = today.getMinutes(); // 분
      let seconds = today.getSeconds(); // 초

      if (hours < 10) {
        hours = "0" + hours;
      }

      if (minutes < 10) {
        minutes = "0" + minutes;
      }

      if (seconds < 10) {
        seconds = "0" + seconds;
      }

      this.currentTime = `${month}/${date} ${hours}:${minutes}:${seconds}`;

      this.previousMachineLog = [...this.machineLog];

      await this.$store.dispatch("machine/getMachineLog");
    },
    getMachineOff() {
      this.$store.dispatch("machine/machineOff");
    },
    getMachineOn() {
      this.$store.dispatch("machine/machineOn");
    },
    updateParentHeight() {
      const container = this.$el.offsetHeight; // 자식 컴포넌트의 내용 높이
      // App.vue로 이벤트를 발생시켜 자식 컴포넌트의 내용 높이를 전달
      this.$emit("childContentHeightChanged", container);
    },
    changeImg(machine_id) {
      this.errorImg = `/Error_BluePrint/BluePrint_${machine_id}.PNG`;
      this.updateParentHeight();
    },
    sendMessage(machineDetail) {
      this.$store.dispatch("admin/SendSMS", machineDetail);
    },
  },
  computed: {
    ...mapState("machine", ["machineLog"]),
  },
  mounted() {
    this.updateParentHeight();
    this.getMachineLog();
    this.myTimer = setInterval(async () => {
      await this.getMachineLog(); // 매 분마다 새 데이터를 가져옵니다.

      // previousMachineLog와 machineLog를 비교하여 새로운 로그를 찾습니다.
      const addedLogs = this.machineLog.filter(
        (log) => !this.previousMachineLog.some((prevLog) => prevLog.log_num === log.log_num)
      );

      // addedLogs가 비어있지 않으면, 새로운 로그가 추가되었음을 의미합니다.
      if (addedLogs.length > 0) {
        const plainAddedLogs = addedLogs.map((log) => ({ ...log }));
        console.log("새로운 로그가 추가되었습니다:", plainAddedLogs[0].log_num);
        console.log("새로운 로그가 추가되었습니다:", plainAddedLogs[0].machine_id);
        // 새로운 로그에 대해 원하는 동작을 수행합니다.
        this.changeImg(plainAddedLogs[0].machine_id);
        alert(
          `${plainAddedLogs[0].machine_id} 공정에 이상이 발생했습니다. 확인 후 메뉴얼에 따라 조치해주시기 바랍니다`
        );

        switch (plainAddedLogs[0].machine_id) {
          case 1000:
            this.sendMessage(
              "[주문 컨베이어 벨트] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 1010:
            this.sendMessage(
              "[1차 피스톤] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 1020:
            this.sendMessage(
              "[2차 피스톤] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 1030:
            this.sendMessage(
              "[3차 피스톤] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 2000:
            this.sendMessage(
              "[분류 컨베이어 벨트] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 2100:
            this.sendMessage(
              "[카메라 모듈] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 2010:
            this.sendMessage(
              "[1차 가름막] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 2020:
            this.sendMessage(
              "[2차 가름막] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
          case 2030:
            this.sendMessage(
              "[3차 가름막] 오류 발생 \n 에러 내용 : " + plainAddedLogs[0].error_message
            );
            break;
        }
      }
    }, 60000);
  },
  beforeUnmount() {
    clearInterval(this.myTimer);
    // 컴포넌트가 언마운트(제거)되기 전 실행되는 로직
    window.removeEventListener("resize", this.updateParentHeight);
  },
};
</script>

<style scoped>
.MachineContainer {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin: 20px;
  padding: 20px;
  box-shadow: 2px 2px 3px 3px black;
}
.LogTableContainer {
  margin: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.MachineMainContainer {
  display: flex;
  flex-direction: column;
}
.MachineSpan {
  display: flex;
}
.MachineImgContainer {
  text-align: center;
}
.machineImg {
  width: auto;
  height: auto;
}
</style>
