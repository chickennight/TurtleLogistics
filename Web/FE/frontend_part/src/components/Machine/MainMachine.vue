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
      <div class="MachineImgContainer">
        <img class="machineImg" :src="errorImg" alt="image" />
      </div>
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
    imgURL: "",
  }),
  methods: {
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
    changeImg() {
      this.imgURL = this.$store.state.errorImg;
      this.updateParentHeight();
    },
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

      let currentTime = `${month}/${date} ${hours}:${minutes}:${seconds}`;

      this.$store.state.currentTime = currentTime;
      this.$store.state.errorImg = "/Error_BluePrint/error_nukki.png";

      await this.$store.dispatch("machine/getMachineLog");
    },
  },
  computed: {
    ...mapState("machine", ["machineLog"]),
    ...mapState(["currentTime"]),
    ...mapState(["errorImg"]),
  },
  mounted() {
    this.updateParentHeight();
    this.getMachineLog();
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
