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
              v-for="item in sortedLogisticAnalysis()"
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
  mounted() {
    // 컴포넌트가 마운트될 때 실행되는 로직
    this.updateParentHeight();
    window.addEventListener("resize", this.updateParentHeight);
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
    getlogisticAnalysis() {
      this.$store.dispatch("admin/getLogisticAnalysis");
    },
    sortedLogisticAnalysis() {
      // Sort the array in such a way that items with 'error_message' are on top
      return this.logisticAnalysis.sort((a, b) => {
        if (a.error_message !== "" && b.error_message === "") {
          return -1; // 'a' has an error_message and should come before 'b'
        } else if (a.error_message === "" && b.error_message !== "") {
          return 1; // 'b' has an error_message and should come before 'a'
        } else {
          return 0; // Maintain the current order if both have an error_message or none
        }
      });
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
  overflow-y: auto;
}
.LogTableContainer {
  padding: 10px;
  box-shadow: 2px 2px 3px 3px black;
  width: 40%;
  overflow-y: auto;
}
.BlueprintContainer {
  box-shadow: 2px 2px 3px 3px black;
  width: 55%;
  margin-left: 30px;
}
.red-text td {
  color: red;
}
</style>
