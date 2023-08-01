<template>
  <div class="SampleContainer">
    <div id="UpperContainer">
      <sample-graph class="GraphContainer" />
      <div class="SampleSecond">샘플 2</div>
    </div>
    <div id="LowerContainer">
    <div class="LogTableContainer">
      <v-table density="compact" theme="dark" class="MiniGraph">
        <thead>
          <tr>
            <th class="text-left">
              로그번호
            </th>
            <th class="text-left">
              로그 발생 날짜
            </th>
            <th class="text-left">
              메세지
            </th>
            <th class="text-left">
              기계번호
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="item in machineStatus[`로그`]"
            :key="item.log_num"
          >
            <td>{{ item.log_num }}</td>
            <td>{{ item.error_date }}</td>
            <td>{{ item.error_message }}</td>
            <td>{{ item.machine_id }}</td>
          </tr>
        </tbody>
      </v-table>
    </div>
      <sample-blue-print class="BlueprintContainer" />
    </div>
  </div>
</template>

<script>
import SampleGraph from "../graph/SampleGraph.vue";
import SampleBluePrint from "../BluePrint/SampleBluePrint.vue";
import {mapState} from "vuex";

export default {
  name: "AdminMainView",
  components: { 
    SampleBluePrint, 
    SampleGraph 
    },
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

<style scoped>
.SampleContainer{
  display : flex;
  flex-direction: column;
}
#UpperContainer{
  height: 450px;
  margin: 20px;
  display: flex;
}
#LowerContainer{

  height: 450px;
  margin: 20px;
  display: flex;
}
.GraphContainer{
  padding : 20px;
  width: 60%;
  box-shadow: 2px 2px 3px 3px black;
}
.SampleSecond{
  box-shadow: 2px 2px 3px 3px black;
  width: 35%;
  margin-left: 30px;
}
.LogTableContainer{
  padding: 20px;
  box-shadow: 2px 2px 3px 3px black;
  width: 35%;
}
.BlueprintContainer{
  box-shadow: 2px 2px 3px 3px black;
  width: 60%;
  margin-left: 30px;
}
</style>