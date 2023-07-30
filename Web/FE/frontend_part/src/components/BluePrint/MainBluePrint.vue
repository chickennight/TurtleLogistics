<template>
  <div class="SampleContainer">
    공정현황!!!!(단면도너낌)
      <v-btn
      @click="getMachineStatus"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        공정현황 버튼
      </v-btn>
  </div>
  <div class="LogTableContainer">
    <v-table density="compact" theme="dark">
      <thead>
        <tr>
          <th class="text-left">
            기계번호
          </th>
          <th class="text-left">
            기계
          </th>
          <th class="text-left">
            고장유무
          </th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(item, index) in machineStatus"
          :key="index"
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
import {mapState} from "vuex";

export default {
    name: "MainBluePrint",
    methods: {
      getMachineStatus(){
        this.$store.dispatch("getMachineStatus");
      }
    },
    computed:{
      ...mapState(["machineStatus"]),
    }
}
</script>

<style>
.SampleContainer{
  margin: 20px;
  border: 1px solid white;
}
</style>