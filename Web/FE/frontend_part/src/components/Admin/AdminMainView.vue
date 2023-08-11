<template>
  <div class="SampleContainer">
    <div id="LowerContainer">
      <sample-CCTV class="BlueprintContainer" @click="moveCCTV" />
      <div class="LogTableContainer" @click="moveMachine">
        <img class="machineImg" src="/Error_BluePrint/BluePrint_0000.png" />
      </div>
    </div>
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
  </div>
</template>

<script>
import SampleGraph from "../graph/SampleGraph.vue";
import SampleCCTV from "../Cctv/SampleCCTV.vue";
import { mapState } from "vuex";

export default {
  name: "AdminMainView",
  components: {
    SampleGraph,
    SampleCCTV,
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
    moveCCTV() {
      this.$router.push({ name: "MainCctv" });
    },
    moveMachine() {
      this.$router.push({ name: "MainMachine" });
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
  width: 55%;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.ProductStatusContainer {
  padding: 20px;
  width: 40%;
  margin-left: 30px;
  overflow-y: auto;
  scrollbar-width: 0px;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.ProductStatusContainer:hover,
.LogTableContainer:hover,
.BlueprintContainer:hover,
.GraphContainer:hover {
  background-color: rgb(62, 62, 62);
}
.ProductStatusContainer::-webkit-scrollbar {
  display: none;
}
.LogTableContainer {
  padding: 10px;
  box-shadow: 0px 0px 6px -1px black;
  width: 55%;
  overflow-y: auto;
  margin-left: 30px;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.BlueprintContainer {
  box-shadow: 0px 0px 6px -1px black;
  width: 40%;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.red-text td {
  color: red;
}
.machineImg {
  margin-top: 2%;
  width: 100%;
  height: 95%;
}
</style>
