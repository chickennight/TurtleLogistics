<template>
  <div class="OrderDateContainer">
    <div class="ButtonContainer">
      <h1>기간별 조회</h1>
      <span>
        <v-btn @click="getOrderDataWeek" background-color="rgb(53, 53, 53)" variant="outlined">
          일주일
        </v-btn>

        <v-btn @click="getOrderDataMonth" background-color="rgb(53, 53, 53)" variant="outlined">
          1개월
        </v-btn>

        <v-btn @click="getOrderData3Month" background-color="rgb(53, 53, 53)" variant="outlined">
          3개월
        </v-btn>

        <v-btn @click="getOrderData6Month" background-color="rgb(53, 53, 53)" variant="outlined">
          6개월
        </v-btn>

        <v-btn @click="getOrderDataYear" background-color="rgb(53, 53, 53)" variant="outlined">
          1년
        </v-btn>
      </span>
    </div>
    &nbsp;
    <div class="OrderGraphContainer">
      <Line :data="chartData" :key="renderCount" :options="chartOptions" />
    </div>
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
  name: "OrderByDate",
  components: {
    Line,
  },
  data: () => ({
    chartData: {
      labels: [],
      datasets: [
        {
          label: "주문건수",
          backgroundColor: "salmon",
          borderColor: "salmon",
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
    this.updateParentHeight();

    const offset = new Date().getTimezoneOffset() * 60000;
    const today = new Date(Date.now() - offset);
    const end_day = today.toISOString();
    const week = new Date(Date.now() - 6 * 24 * 60 * 60 * 1000 - offset);
    const start_day = week.toISOString();

    const date = {
      end: end_day,
      start: start_day,
    };

    await this.$store.dispatch("order/getOrderData", date);

    var idx = 0;
    this.chartData.labels = [];
    this.chartData.datasets.data = [];

    for (let key in this.orderData) {
      this.chartData.labels[idx] = key.substr(4);
      this.chartData.datasets[0].data[idx] = this.orderData[key];
      idx++;
    }

    this.renderCount += 1;
  },
  beforeUnmount() {
    // 컴포넌트가 언마운트(제거)되기 전 실행되는 로직
    window.removeEventListener("resize", this.updateParentHeight);
  },
  computed: {
    ...mapState("order", ["orderData"]),
  },
  methods: {
    async getOrderDataWeek() {
      const offset = new Date().getTimezoneOffset() * 60000;
      const today = new Date(Date.now() - offset);
      const end_day = today.toISOString();
      const week = new Date(Date.now() - 6 * 24 * 60 * 60 * 1000 - offset);
      const start_day = week.toISOString();

      const date = {
        end: end_day,
        start: start_day,
      };

      await this.$store.dispatch("order/getOrderData", date);

      var idx = 0;
      this.chartData.labels = [];
      this.chartData.datasets.data = [];

      setTimeout(() => {
        for (let key in this.orderData) {
          this.chartData.labels[idx] = key.substr(4);
          this.chartData.datasets[0].data[idx] = this.orderData[key];
          idx++;
        }
        this.renderCount += 1;
      }, 10);
    },
    async getOrderDataMonth() {
      const offset = new Date().getTimezoneOffset() * 60000;
      const today = new Date(Date.now() - offset);
      const end_day = today.toISOString();
      const month = new Date(today.setMonth(today.getMonth() - 1));
      const start_day = month.toISOString();

      const date = {
        end: end_day,
        start: start_day,
      };

      await this.$store.dispatch("order/getOrderData", date);
      this.chartData.labels = [];
      this.chartData.datasets.data = [];
      var idx = 0;

      setTimeout(() => {
        for (let key in this.orderData) {
          console.log(key);
          console.log(this.orderData[key]);
          this.chartData.labels[idx] = key.substr(4);
          this.chartData.datasets[0].data[idx] = this.orderData[key];
          idx++;
        }
        this.renderCount += 1;
      }, 10);
    },
    async getOrderData3Month() {
      const offset = new Date().getTimezoneOffset() * 60000;
      const today = new Date(Date.now() - offset);
      const end_day = today.toISOString();
      const month = new Date(today.setMonth(today.getMonth() - 3));
      const start_day = month.toISOString();

      const date = {
        end: end_day,
        start: start_day,
      };
      console.log(date);
      var idx = 0;

      await this.$store.dispatch("order/getOrderData", date);

      this.chartData.labels = [];
      this.chartData.datasets[0].data = [];

      setTimeout(() => {
        for (let key in this.orderData) {
          if (this.chartData.labels[idx] == undefined) {
            this.chartData.labels[idx] = key.substr(0, 6);
            this.chartData.datasets[0].data[idx] = this.orderData[key];
          } else if (key.substr(0, 6) != this.chartData.labels[idx]) {
            idx++;
            this.chartData.datasets[0].data[idx] = 0;
            this.chartData.labels[idx] = key.substr(0, 6);
            this.chartData.datasets[0].data[idx] += this.orderData[key];
          } else if (key.substr(0, 6) == this.chartData.labels[idx]) {
            this.chartData.datasets[0].data[idx] += this.orderData[key];
          }
        }

        this.renderCount += 1;
      }, 10);
    },
    async getOrderData6Month() {
      const offset = new Date().getTimezoneOffset() * 60000;
      const today = new Date(Date.now() - offset);
      const end_day = today.toISOString();
      const month = new Date(today.setMonth(today.getMonth() - 6));
      const start_day = month.toISOString();

      const date = {
        end: end_day,
        start: start_day,
      };

      await this.$store.dispatch("order/getOrderData", date);
      var idx = 0;
      this.chartData.labels = [];
      this.chartData.datasets[0].data = [];

      setTimeout(() => {
        for (let key in this.orderData) {
          if (this.chartData.labels[idx] == undefined) {
            this.chartData.labels[idx] = key.substr(0, 6);
            this.chartData.datasets[0].data[idx] = this.orderData[key];
          } else if (key.substr(0, 6) != this.chartData.labels[idx]) {
            idx++;
            this.chartData.datasets[0].data[idx] = 0;
            this.chartData.labels[idx] = key.substr(0, 6);
            this.chartData.datasets[0].data[idx] += this.orderData[key];
          } else if (key.substr(0, 6) == this.chartData.labels[idx]) {
            this.chartData.datasets[0].data[idx] += this.orderData[key];
          }
        }

        this.renderCount += 1;
      }, 10);
    },
    async getOrderDataYear() {
      const offset = new Date().getTimezoneOffset() * 60000;
      const today = new Date(Date.now() - offset);
      const end_day = today.toISOString();
      const month = new Date(today.setFullYear(today.getFullYear() - 1));
      const start_day = month.toISOString();

      const date = {
        end: end_day,
        start: start_day,
      };

      await this.$store.dispatch("order/getOrderData", date);

      var idx = 0;
      this.chartData.labels = [];
      this.chartData.datasets.data = [];

      setTimeout(() => {
        for (let key in this.orderData) {
          if (this.chartData.labels[idx] == undefined) {
            this.chartData.labels[idx] = key.substr(0, 6);
            this.chartData.datasets[0].data[idx] = this.orderData[key];
          } else if (key.substr(0, 6) != this.chartData.labels[idx]) {
            idx++;
            this.chartData.datasets[0].data[idx] = 0;
            this.chartData.labels[idx] = key.substr(0, 6);
            this.chartData.datasets[0].data[idx] += this.orderData[key];
          } else if (key.substr(0, 6) == this.chartData.labels[idx]) {
            this.chartData.datasets[0].data[idx] += this.orderData[key];
          }
        }

        this.renderCount += 1;
      }, 10);
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
.OrderDateContainer {
  margin: 20px;
}
.OrderGraphContainer {
  padding: 20px;
  box-shadow: 2px 2px 3px 3px black;
}
.b .graph {
  background-color: white;
  border: 2px solid #222;
  width: 300px;
  height: 300px;
}
.ButtonContainer {
  box-shadow: 2px 2px 3px 3px black;
  padding: 20px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
</style>
