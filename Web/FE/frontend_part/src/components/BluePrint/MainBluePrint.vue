<template>
  <div class="SampleContainer">
    <h1>공정현황</h1>
    <span>
      <v-btn @click="getMachineStatus" background-color="rgb(53, 53, 53)" variant="outlined">
        공정현황 버튼
      </v-btn>
    </span>
  </div>
  <div class="LogTableContainer">
    <v-table density="compact" theme="dark">
      <thead>
        <tr>
          <th class="text-left">기계번호</th>
          <th class="text-left">기계</th>
          <th class="text-left">고장유무</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in machineStatus[`상태`]" :key="index">
          <td>{{ item.machine_id }}</td>
          <td>{{ item.machine_detail }}</td>
          <td>{{ item.broken }}</td>
        </tr>
      </tbody>
    </v-table>
  </div>
  <div class="LogTableContainer">
    <v-table density="compact" theme="dark">
      <thead>
        <tr>
          <th class="text-left">로그번호</th>
          <th class="text-left">로그 발생 날짜</th>
          <th class="text-left">메세지</th>
          <th class="text-left">기계번호</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in machineStatus[`로그`]" :key="item.log_num">
          <td>{{ item.log_num }}</td>
          <td>{{ item.error_date }}</td>
          <td>{{ item.error_message }}</td>
          <td>{{ item.machine_id }}</td>
        </tr>
      </tbody>
    </v-table>
  </div>
  <div class="LogTableContainer">
    <v-table density="compact" theme="dark">
      <thead>
        <tr>
          <th class="text-left">로그번호</th>
          <th class="text-left">로그 발생 날짜</th>
          <th class="text-left">메세지</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in machineStatus[`인식 오류 로그`]" :key="item.log_num">
          <td>{{ item.log_num }}</td>
          <td>{{ item.error_date }}</td>
          <td>{{ item.error_message }}</td>
        </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MainBluePrint",
  data: () => ({}),
  mounted() {
    // 컴포넌트가 마운트될 때 실행되는 로직
    this.updateParentHeight();
  },
  beforeUnmount() {
    // 컴포넌트가 언마운트(제거)되기 전 실행되는 로직
    window.removeEventListener("resize", this.updateParentHeight);
  },
  methods: {
    getMachineStatus() {
      this.$store.dispatch("machine/getMachineStatus");
    },
    updateParentHeight() {
      const container = this.$el.offsetHeight; // 자식 컴포넌트의 내용 높이
      // App.vue로 이벤트를 발생시켜 자식 컴포넌트의 내용 높이를 전달
      this.$emit("childContentHeightChanged", container);
    },
  },
  computed: {
    ...mapState("machine", ["machineStatus"]),
  },
};
</script>

<style scoped>
.SampleContainer {
  margin: 20px;
  padding: 20px;
  box-shadow: 2px 2px 3px 3px black;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.LogTableContainer {
  margin: 20px;
}
</style>
