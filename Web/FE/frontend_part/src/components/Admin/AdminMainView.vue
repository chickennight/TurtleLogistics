<template>
  <div class="SampleContainer">
    <div id="UpperContainer">
      <sample-graph class="GraphContainer" />
      <div class="ProductStatusContainer">
        <v-table density="compact" theme="dark">
          <thead>
            <tr>
              <th class="text-left" rowspan="2">번호</th>
              <th class="text-left" rowspan="2">상품명</th>
              <th class="text-left" rowspan="2">재고</th>
              <th class="text-left" rowspan="2">이상</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="item in logisticAnalysis"
              :key="item.product_num"
              :class="{ 'red-text': item.error_message !== `` }"
            >
              <td>{{ item.product_num }}</td>
              <td>{{ item.name }}</td>
              <td>{{ item.stock }}</td>
              <td>{{ item.error_message }}</td>
            </tr>
          </tbody>
        </v-table>
      </div>
    </div>
    <div id="LowerContainer">
      <div class="LogTableContainer">
        <v-table density="compact" theme="dark">
          <thead>
            <tr>
              <th class="text-left">로그</th>
              <th class="text-left">날짜</th>
              <th class="text-left">메세지</th>
              <th class="text-left">기계</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in machineStatus['로그']" :key="item.log_num">
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
import { mapState } from "vuex";

export default {
  name: "AdminMainView",
  components: {
    SampleBluePrint,
    SampleGraph,
  },
  created() {
    this.getMachineStatus();
    this.getlogisticAnalysis();
  },
  methods: {
    getMachineStatus() {
      this.$store.dispatch("machine/getMachineStatus");
    },
    getlogisticAnalysis() {
      this.$store.dispatch("admin/getLogisticAnalysis");
    },
  },
  computed: {
    ...mapState("machine", ["machineStatus"]),
    ...mapState("admin", ["logisticAnalysis"]),
  },
};
</script>

<style scoped>
.SampleContainer {
  display: flex;
  flex-direction: column;
}
#UpperContainer {
  height: 450px;
  margin: 20px;
  display: flex;
}
#LowerContainer {
  height: 450px;
  margin: 20px;
  display: flex;
}
.GraphContainer {
  padding: 20px;
  width: 60%;
  box-shadow: 2px 2px 3px 3px black;
}
.ProductStatusContainer {
  padding: 20px;
  box-shadow: 2px 2px 3px 3px black;
  width: 35%;
  margin-left: 30px;
}
.LogTableContainer {
  padding: 10px;
  box-shadow: 2px 2px 3px 3px black;
  width: 40%;
}
.BlueprintContainer {
  box-shadow: 2px 2px 3px 3px black;
  width: 55%;
  margin-left: 30px;
}
</style>
