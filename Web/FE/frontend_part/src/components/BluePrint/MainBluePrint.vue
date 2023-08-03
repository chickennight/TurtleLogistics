<template>
  <div class="SampleContainer">
    공정현황!!!!(단면도너낌)
    <v-btn @click="getMachineStatus" background-color="rgb(53, 53, 53)" variant="outlined">
      공정현황 버튼
    </v-btn>
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
  methods: {
    getMachineStatus() {
      this.$store.dispatch("machine/getMachineStatus");
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
  border: 1px solid white;
}
</style>
