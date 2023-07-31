<template>
  <div class="OrderNowContainer">주문 현황</div>
  <div>
    <Line :data="chartData" :options="chartOptions" style="color: white" :key="renderCount" />
  </div>
  <div class="OrderNowTableContainer">
    <v-table density="compact" theme="dark">
      <thead>
        <tr>
          <th class="text-left">주문번호</th>
          <th class="text-left">현황</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in orderNowList" :key="item.order_num">
          <td>{{ item.order_num }}</td>
          <td>{{ item.status }}</td>
        </tr>
      </tbody>
    </v-table>
  </div>
</template>

<script>
import { mapState } from "vuex";

import { Line } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
} from "chart.js";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement
);

export default {
  name: "MainGraph",
  components: {
    Line,
  },
  data() {
    return {
      renderCount: 0,
      myTimer: null,
      chartData: {
        labels: ["주문 접수", "포장 과정", "분류 과정", "분류 완료", "배송 과정"],
        datasets: [
          {
            label: "Data One",
            borderColor: "white",
            backgroundColor: "white",
            data: [],
          },
        ],
      },
      chartOptions: {
        animation: false,
      },
    };
  },
  created() {
    this.get_order_nows();
  },
  mounted() {
    this.myTimer = setInterval(this.get_order_nows, 5000);
  },
  watch: {
    // orderNowcalculate 데이터의 변화를 감시
    orderNowcalculate: {
      handler(newVal) {
        // 데이터가 변경될 때 실행되는 로직
        this.chartData.datasets[0].data = newVal;
        this.renderCount += 1;
      },
      // deep 옵션을 true로 설정하여 중첩 객체의 변경도 감시 (선택적)
      deep: true,
    },
  },
  beforeUnmount() {
    clearInterval(this.myTimer);
  },
  computed: {
    ...mapState(["orderNowList"]),
    ...mapState(["orderNowcalculate"]),
  },
  methods: {
    get_order_nows() {
      this.$store.dispatch("getOrderNows");
    },
    chart_update() {
      this.chartData.datasets[0].data = this.orderNowcalculate;
      this.renderCount += 1;
    },
  },
};
</script>

<style>
.OrderNowContainer {
  margin: 20px;
  border: 1px solid white;
}
.OrderNowTableContainer {
  margin: 20px;
}
</style>
