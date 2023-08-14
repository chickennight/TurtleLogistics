<template>
  <div class="SampleContainer">
    <h1>공정 현황</h1>
  </div>
  <div class="LogTableContainer">
    <v-table density="compact" theme="dark" class="main_table">
      <thead>
        <tr>
          <th style="text-align: center">로그번호</th>
          <th style="text-align: center">로그 발생 날짜</th>
          <th style="text-align: center">메세지</th>
          <th style="text-align: center">기계번호</th>
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
    <v-table density="compact" theme="dark" class="main_table">
      <thead>
        <tr>
          <th style="text-align: center">기계번호</th>
          <th style="text-align: center">기계</th>
          <th style="text-align: center">고장유무</th>
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
          <td>
            <span v-if="item.broken" :style="{ color: 'rgb(250, 100, 130)' }">기기 고장</span>
            <span v-else>정상 작동</span>
          </td>
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
  methods: {
    getMachineStatus() {
      this.$store.dispatch("machine/getMachineStatus");
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
  color: rgb(250, 100, 130);
}

.main_table th,
.main_table td {
  text-align: center;
  vertical-align: middle;
}
</style>
