<template>
  <div class="LogisticContainer">
    <div class="LogisticsHeader"><h1>물류현황</h1></div>
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
.LogisticContainer {
}
.ProductStatusContainer {
  margin: 20px;
}
.red-text td {
  color: red;
}
.LogisticsHeader {
  margin: 20px;
  box-shadow: 2px 2px 3px 3px black;
  padding: 20px;
}
</style>
