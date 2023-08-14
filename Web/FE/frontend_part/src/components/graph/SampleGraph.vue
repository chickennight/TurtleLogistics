<template>
  <div class="SampleGraphContainer">
    <Line :data="chartData" :key="renderCount" :options="chartOptions" />
  </div>
</template>

<script>
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
import { mapState } from "vuex";

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
  name: "SampleGraph",
  components: {
    Line,
  },
  data: () => ({
    chartData: {
      labels: [],
      datasets: [
        {
          label: "주문건수",
          backgroundColor: "rgb(250, 100, 130)",
          borderColor: "rgb(250, 100, 130)",
          color: "red",
          data: [],
        },
      ],
    },
    chartOptions: {
      plugins: {
        legend: {
          display: true,
          labels: {
            color: "white",
          },
        },
      },
      scales: {
        x: {
          ticks: {
            color: "white", // x축 레이블의 글자색을 지정합니다.
          },
        },
        y: {
          beginAtZero: true,
          ticks: {
            color: "white", // y축 레이블의 글자색을 지정합니다.
          },
        },
      },
    },
    renderCount: 0,
  }),
  async mounted() {
    const offset = new Date().getTimezoneOffset() * 60000;
    const today = new Date(Date.now() - offset);
    const end_day = today.toISOString();
    const week = new Date(Date.now() - 6 * 24 * 60 * 60 * 1000 - offset);
    const start_day = week.toISOString();

    const date = {
      end: end_day,
      start: start_day,
    };

    await this.$store.dispatch("order/getOrderWeekData", date);

    var idx = 0;
    this.chartData.labels = [];
    this.chartData.datasets.data = [];

    for (let key in this.orderWeekData) {
      this.chartData.labels[idx] = key.substr(4);
      this.chartData.datasets[0].data[idx] = this.orderWeekData[key];
      idx++;
    }

    this.renderCount += 1;
  },
  computed: {
    ...mapState("order", ["orderWeekData"]),
  },
};
</script>

<style scoped>
.SampleGraphContainer {
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: center;
}
</style>
