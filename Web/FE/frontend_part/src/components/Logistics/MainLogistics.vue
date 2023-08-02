<template>
  <div class="LogisticContainer">물류현황</div>
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
    ...mapState(["logisticAnalysis"]),
  },
  mounted() {
    this.get_logistic_analysis();
    this.myTimer = setInterval(this.get_logistic_analysis, 60000);
  },
  beforeUnmount() {
    clearInterval(this.myTimer);
  },
  methods: {
    get_logistic_analysis() {
      this.$store.dispatch("admin/getLogisticAnalysis");
    },
  },
};
</script>

<style scoped>
.LogisticContainer {
  margin: 20px;
  border: 1px solid white;
}
.ProductStatusContainer {
  margin: 20px;
}
.red-text td {
  color: red;
}
</style>
