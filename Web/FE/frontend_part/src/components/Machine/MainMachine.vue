<template>
  <div class="MachineMainContainer">
    <div class="MachineContainer">
      <h1>기기 제어</h1>
      <span class="MachineSpan">
        &nbsp;&nbsp;&nbsp;
        <v-btn
          @click="getMachineOff"
          background-color="rgb(53, 53, 53)"
          variant="outlined"
          class="offButton"
        >
          전원 종료
        </v-btn>
        <v-btn
          @click="getMachineOn"
          background-color="rgb(53, 53, 53)"
          variant="outlined"
          class="onButton"
        >
          전원 시작
        </v-btn>
      </span>
    </div>
    <div class="MachineImgContainer">
      <img class="machineImg" :src="errorImg" alt="image" />
    </div>
    <div class="LogTableContainer">
      <v-table density="compact" theme="dark" class="main_table">
        <thead>
          <tr>
            <th style="text-align: center">번호</th>
            <th style="text-align: center">날짜</th>
            <th style="text-align: center">로그명</th>
            <th style="text-align: center">기계</th>
            <th style="text-align: center"></th>
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
    <div class="log-details">
      <table>
        <tr>
          <th>로그번호</th>
          <th>날짜</th>
          <th>로그명</th>
          <th>기계</th>
        </tr>
        <tr>
          <td>{{ selectedLog.log_num }}</td>
          <td>{{ selectedLog.error_date }}</td>
          <td>{{ selectedLog.error_message }}</td>
          <td>{{ selectedLog.machine_id }}</td>
        </tr>
      </table>
    </div>
    <div class="log-image">
      <img :src="image" alt="Selected Machine Image" />
    </div>
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
    changeImg(machine_id) {
      this.$store.state.errorImg = `/Error_BluePrint/BluePrint_${machine_id}.png`;
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
    this.getMachineLog();
  },
  beforeUnmount() {
    clearInterval(this.myTimer);
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
  align-items: center;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.LogTableContainer {
  margin: 0px 20px 0px 20px;
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
  align-items: center;
}
.MachineImgContainer {
  background-color: rgb(55, 55, 55);
  text-align: center;
  margin: 0px 20px 20px 20px;
  box-shadow: 0px 0px 6px -1px black;
  border-radius: 10px;
}
.machineImg {
  width: 60%;
  height: auto;
}
.main_table th,
.main_table td {
  overflow-y: auto;
  scrollbar-width: 0px;
  text-align: center;
  vertical-align: middle;
}
.offButton {
  margin-right: 5%;
}

/* 모달 css */

.log-details table {
  width: 100%;
  max-width: 100%;
  border-collapse: collapse;
  border-radius: 8px;
}

.log-details th,
.log-details td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

.log-image img {
  max-width: 100%;
  width: auto;
  display: block;
  margin: 20px auto;
}
</style>
