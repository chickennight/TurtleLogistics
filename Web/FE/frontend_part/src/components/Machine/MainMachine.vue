<template>
  <div class="MachineContainer">
    기기제어
    <v-btn @click="getMachineLog" background-color="rgb(53, 53, 53)" variant="outlined">
      새로고침
    </v-btn>
  </div>
  <div class="LogTableContainer">
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
        <tr v-for="item in machineLog" :key="item.log_num">
          <td>{{ item.log_num }}</td>
          <td>{{ item.error_date }}</td>
          <td>{{ item.error_message }}</td>
          <td>{{ item.error_message }}</td>
        </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MainMachine",
  methods: {
    getMachineLog() {
      this.$store.dispatch("machine/getMachineLog");
      console.log(this.machineLog);
    },
  },
  computed: {
    ...mapState(["machineLog"]),
  },
};
</script>

<style>
.MachineContainer {
  margin: 20px;
  border: 1px solid white;
}
.LogTableContainer {
  margin: 20px;
}
</style>
