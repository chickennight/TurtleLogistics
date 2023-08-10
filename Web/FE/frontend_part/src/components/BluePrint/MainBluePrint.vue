<template>
  <div class="SampleContainer">
    <h1>공정현황</h1>
  </div>
  <div class="LogTableContainer">
    <v-table density="compact" theme="dark">
      <thead>
        <tr>
          <th class="text-left">로그번호</th>
          <th class="text-left">로그 발생 날짜</th>
          <th class="text-left">메세지</th>
          <th class="text-left">기계번호</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in machineStatus[`로그`]" :key="item.log_num">
          <td>{{ item.log_num }}</td>
          <td>{{ item.error_date }}</td>
          <td>{{ item.error_message }}</td>
          <td>{{ item.machine_id }}</td>
          <td><button @click="machineFixed(item.machine_id)">고장 해결</button></td>
        </tr>
      </tbody>
    </v-table>
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
        <tr
          v-for="(item, index) in machineStatus[`상태`]"
          :key="index"
          :class="{ 'red-text': item.broken === true }"
        >
          <td>{{ item.machine_id }}</td>
          <td>{{ item.machine_detail }}</td>
          <td>{{ item.broken }}</td>
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
    machineFixed(machine_id) {
      this.$store.dispatch("machine/machineFixed", machine_id);
    },
  },
  computed: {
    ...mapState("machine", ["machineStatus"]),
  },
};
</script>

<style scoped>
.SampleContainer {
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
  margin: 20px;
  padding: 20px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.LogTableContainer {
  margin: 20px;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.red-text td {
  color: red;
}
</style>
