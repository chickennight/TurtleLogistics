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
            <th class="text-left"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in machineLog" :key="item.log_num" @click="changeImg(item.machine_id)">
            <td>{{ item.log_num }}</td>
            <td>{{ item.error_date }}</td>
            <td>{{ item.error_message }}</td>
            <td>{{ item.machine_id }}</td>
            <td><button @click.stop="showLogDetails(item)">상세보기</button></td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </div>
  <LogModal :isOpen="isModalOpen" @close="closeModal">
    <template #header>
      <h3>상세 정보</h3>
    </template>

    <div class="log-details">
      <table>
        <tr>
          <th>로그번호</th>
          <td>{{ selectedLog.log_num }}</td>
        </tr>
        <tr>
          <th>날짜</th>
          <td>{{ selectedLog.error_date }}</td>
        </tr>
        <tr>
          <th>로그명</th>
          <td>{{ selectedLog.error_message }}</td>
        </tr>
        <tr>
          <th>기계</th>
          <td>{{ selectedLog.machine_id }}</td>
        </tr>
      </table>
    </div>

    <div class="log-image">
      <img :src="image" alt="Selected Machine Image" />
    </div>

    <template #footer>
      <button @click="closeModal">닫기</button>
    </template>
  </LogModal>
</template>

<script>
import LogModal from "../Modals/LogModal.vue";
import { mapState } from "vuex";

export default {
  name: "MainMachine",
  components: {
    LogModal,
  },
  data: () => ({
    imgURL: "",
    isModalOpen: false,
    selectedLog: null,
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
    changeImg(machine_id) {
      this.$store.state.errorImg = `/Error_BluePrint/BluePrint_${machine_id}.png`;
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
      this.$store.state.errorImg = "/Error_BluePrint/BluePrint_0000.png";

      await this.$store.dispatch("machine/getMachineLog");
    },
    async showLogDetails(log) {
      try {
        await this.$store.dispatch("admin/getImage", log.log_num);
      } catch (error) {
        console.error(error);
      }
      this.selectedLog = log;
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
    },
  },
  computed: {
    ...mapState("machine", ["machineLog"]),
    ...mapState(["currentTime"]),
    ...mapState(["errorImg"]),
    ...mapState("admin", ["image"]),
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
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.LogTableContainer {
  margin: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
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
  margin: 30px;
}
.machineImg {
  width: 1000px;
  height: auto;
}
.v-table {
  overflow-y: auto;
  scrollbar-width: 0px;
}
</style>
