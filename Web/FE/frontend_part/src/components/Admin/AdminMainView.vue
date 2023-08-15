<template>
  <div class="SampleContainer">
    <div id="LowerContainer">
      <sample-CCTV class="BlueprintContainer" @click="moveCCTV" />
      <div class="LogTableContainer" @click="moveMachine">
        <img class="machineImg" :src="this.errorImg" />
      </div>
    </div>
    <div id="UpperContainer">
      <sample-graph class="GraphContainer" @click="moveGraph" />
      <div class="ProductStatusContainer" @click="moveProduct">
        <v-table density="compact" theme="dark" class="main_table">
          <thead>
            <tr>
              <th rowspan="2" style="text-align: center" @click="sortTable('product_num', $event)">
                번호
                <font-awesome-icon
                  :icon="['fas', 'sort']"
                  style="color: #666a70"
                  class="icon-padding-left"
                />
              </th>
              <th rowspan="2" style="text-align: center">상품명</th>

              <th rowspan="2" style="text-align: center" @click="sortTable('stock', $event)">
                재고
                <font-awesome-icon
                  :icon="['fas', 'sort']"
                  style="color: #666a70"
                  class="icon-padding-left"
                />
              </th>
              <th
                rowspan="2"
                style="text-align: center"
                @click="sortTable('error_message', $event)"
              >
                이상
                <font-awesome-icon
                  :icon="['fas', 'sort']"
                  style="color: #666a70"
                  class="icon-padding-left"
                />
              </th>
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

  data() {
    return {
      sortBy: null, // 현재 정렬 기준
      sortOrder: true, // 정렬 순서 (true: 오름차순, false: 내림차순)
    };
  },
  methods: {
    getMachineStatus() {
      this.$store.dispatch("machine/getMachineStatus");
    },

    getlogisticAnalysis() {
      this.$store.dispatch("admin/getLogisticAnalysis");
    },
    moveCCTV() {
      this.$router.push({ name: "MainCctv" });
    },
    moveMachine() {
      this.$router.push({ name: "MainMachine" });
    },
    moveGraph() {
      this.$router.push({ name: "OrderByDate" });
    },
    moveProduct() {
      this.$router.push({ name: "MainLogistics" });
    },
    //테이블 정렬 기능
    sortTable(column) {
      event.stopPropagation(); // 이벤트 전파 중지
      // 선택된 칼럼이 현재 정렬 기준과 동일한 경우, 정렬 순서만 변경
      if (this.sortBy === column) {
        this.sortOrder = !this.sortOrder;
      } else {
        // 다른 칼럼을 선택한 경우, 해당 칼럼으로 정렬 기준을 변경하고 오름차순으로 초기화
        this.sortBy = column;
        this.sortOrder = true;
      }
    },
    sortedLogisticAnalysis() {
      return this.logisticAnalysis.sort((a, b) => {
        let result = 0;

        if (this.sortBy === "stock") {
          result = a.stock - b.stock;
        } else if (this.sortBy === "error_message") {
          result = a.error_message.localeCompare(b.error_message);
        } else if (this.sortBy === "product_num") {
          result = a.product_num - b.product_num;
        }

        return this.sortOrder ? result : -result;
      });
    },
  },
  computed: {
    ...mapState("machine", ["machineStatus"]),
    ...mapState("admin", ["logisticAnalysis"]),
    ...mapState(["errorImg"]),
  },
};
</script>

<style scoped>
.SampleContainer {
  display: flex;
  flex-direction: column;
  height: 100%; /* 컨테이너를 부모 높이에 맞추기 위해 추가 */
}
#UpperContainer {
  height: 450px;
  margin: 0px -25px 20px 20px;
  padding-bottom: 3%;
  display: flex;
}
#LowerContainer {
  height: 450px;
  margin: 20px -25px 20px 20px;
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
  margin-left: 20px;
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
  transition: all 1s;
  background-color: rgb(62, 62, 62);
  transform: translateY(-5px);
}
.ProductStatusContainer::-webkit-scrollbar {
  display: none;
}
.LogTableContainer {
  padding: 20px;
  box-shadow: 0px 0px 6px -1px black;
  width: 55%;
  overflow-y: auto;
  margin-left: 20px;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
  overflow: hidden; /* 스크롤 숨기기 */
}
.BlueprintContainer {
  box-shadow: 0px 0px 6px -1px black;
  width: 40%;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.red-text td {
  color: rgb(250, 100, 130);
}
.machineImg {
  margin-top: 2%;
  width: 100%;
  height: 95%;
  border-radius: 8px;
  object-fit: contain; /*이미지 비율 유지하면서 컨테이너 안에 맞춤*/
}

.main_table th,
.main_table td {
  text-align: center;
  vertical-align: middle;
}

.icon-padding-left {
  margin-left: 5px;
  cursor: pointer;
}
</style>
