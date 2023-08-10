<template>
  <div class="LogisticContainer">
    <div class="LogisticsHeader">
      <h1>물류현황</h1>
      <h1>{{ this.currentTime }}</h1>
    </div>
    <div class="ProductStatusContainer">
      <v-table density="compact" theme="dark">
        <thead>
          <tr>
            <th class="text-left" rowspan="2">상품번호</th>
            <th class="text-left" rowspan="2">상품명</th>
            <th class="text-left" rowspan="2">상품재고</th>
            <th class="text-center" colspan="4">주문량</th>
            <th class="text-left" rowspan="2">이상 발생</th>
          </tr>
          <tr>
            <th class="text-left">연평균</th>
            <th class="text-left">월평균</th>
            <th class="text-left">주평균</th>
            <th class="text-left">금일</th>
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
            <td>{{ item.year }}</td>
            <td>{{ item.month }}</td>
            <td>{{ item.week }}</td>
            <td>{{ item.today }}</td>
            <td>{{ item.error_message }}</td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "MainLogistics",
  data() {
    return {
      myTimer: null,
      currentTime: "",
    };
  },
  computed: {
    ...mapState("admin", ["logisticAnalysis"]),
  },
  mounted() {
    this.get_logistic_analysis();
    this.myTimer = setInterval(this.get_logistic_analysis, 60000);
    this.updateParentHeight();
  },
  beforeUnmount() {
    clearInterval(this.myTimer);
    window.removeEventListener("resize", this.updateParentHeight);
  },
  methods: {
    get_logistic_analysis() {
      let today = new Date();

      let month = today.getMonth() + 1; // 월
      let date = today.getDate(); // 날짜

      let hours = today.getHours(); // 시
      let minutes = today.getMinutes(); // 분
      let seconds = today.getSeconds(); // 초

      if (hours < 10) {
        hours = "0" + hours;
      }

      if (minutes < 10) {
        minutes = "0" + minutes;
      }

      if (seconds < 10) {
        seconds = "0" + seconds;
      }

      this.currentTime = `${month}/${date} ${hours}:${minutes}:${seconds}`;

      this.$store.dispatch("admin/getLogisticAnalysis");
    },
    updateParentHeight() {
      const container = this.$el.offsetHeight; // 자식 컴포넌트의 내용 높이
      // App.vue로 이벤트를 발생시켜 자식 컴포넌트의 내용 높이를 전달
      this.$emit("childContentHeightChanged", container);
    },
  },
};
</script>

<style scoped>
.ProductStatusContainer {
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
  margin: 20px;
}
.red-text td {
  color: red;
}
.LogisticsHeader {
  margin: 20px;
  padding: 20px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
</style>
