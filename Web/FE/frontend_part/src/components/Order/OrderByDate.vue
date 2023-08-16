<template>
  <div class="OrderDateContainer">
    <div class="ButtonContainer">
      <h1>기간별 조회</h1>
      <span>
        <v-btn
          @click="setPeriod('week')"
          :class="{ selected: selectedPeriod === 'week' }"
          variant="outlined"
          :disabled="isLoading"
        >
          일주일
        </v-btn>
        <v-btn
          @click="setPeriod('month')"
          :class="{ selected: selectedPeriod === 'month' }"
          variant="outlined"
          :disabled="isLoading"
        >
          1개월
        </v-btn>
        <v-btn
          @click="setPeriod('3month')"
          :class="{ selected: selectedPeriod === '3month' }"
          variant="outlined"
          :disabled="isLoading"
        >
          3개월
        </v-btn>
        <v-btn
          @click="setPeriod('6month')"
          :class="{ selected: selectedPeriod === '6month' }"
          variant="outlined"
          :disabled="isLoading"
        >
          6개월
        </v-btn>
        <v-btn
          @click="setPeriod('year')"
          :class="{ selected: selectedPeriod === 'year' }"
          variant="outlined"
          :disabled="isLoading"
        >
          1년
        </v-btn>
      </span>
    </div>
    <div class="OrderGraphContainer">
      <Line :data="chartData" :key="renderCount" :options="chartOptions" />
      <div v-if="isLoading" class="loading-overlay">
        <v-progress-circular
          :size="50"
          color="rgba(250, 100, 130, 1)"
          indeterminate
        ></v-progress-circular>
      </div>
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
    selectedPeriod: sessionStorage.getItem("selectedPeriod") || "week",
    isLoading: false,
    renderCount: 0,
  }),
  async mounted() {
    if (this.selectedPeriod) {
      switch (this.selectedPeriod) {
        case "week":
          await this.getOrderDataWeek();
          break;
        case "month":
          await this.getOrderDataMonth();
          break;
        case "3month":
          await this.getOrderData3Month();
          break;
        case "6month":
          await this.getOrderData6Month();
          break;
        case "year":
          await this.getOrderDataYear();
          break;
      }
    } else {
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
      this.chartData.datasets[0].data = [];
      for (let key in this.orderData) {
        this.chartData.labels[idx] = key.substr(4);
        this.chartData.datasets[0].data[idx] = this.orderData[key];
        idx++;
      }
      this.renderCount += 1;
    }
  },
  computed: {
    ...mapState("order", ["orderData"]),
  },
  methods: {
    setPeriod(period) {
      if (this.selectedPeriod === period) {
        return;
      }
      this.selectedPeriod = period;
      sessionStorage.setItem("selectedPeriod", period);

      switch (period) {
        case "week":
          this.getOrderDataWeek();
          break;
        case "month":
          this.getOrderDataMonth();
          break;
        case "3month":
          this.getOrderData3Month();
          break;
        case "6month":
          this.getOrderData6Month();
          break;
        case "year":
          this.getOrderDataYear();
          break;
      }
    },
    async getOrderDataWeek() {
      this.isLoading = true;
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
      this.isLoading = false;

      var idx = 0;
      this.chartData.labels = [];
      this.chartData.datasets[0].data = [];

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
      this.isLoading = true;
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
      this.isLoading = false;
      this.chartData.labels = [];
      this.chartData.datasets[0].data = [];
      var idx = 0;

      setTimeout(() => {
        for (let key in this.orderData) {
          this.chartData.labels[idx] = key.substr(4);
          this.chartData.datasets[0].data[idx] = this.orderData[key];
          idx++;
        }
        this.renderCount += 1;
      }, 10);
    },
    async getOrderData3Month() {
      this.isLoading = true;
      const offset = new Date().getTimezoneOffset() * 60000;
      const today = new Date(Date.now() - offset);
      const end_day = today.toISOString();
      const month = new Date(today.setMonth(today.getMonth() - 3));
      const start_day = month.toISOString();

      const date = {
        end: end_day,
        start: start_day,
      };
      var idx = 0;

      await this.$store.dispatch("order/getOrderData", date);

      this.isLoading = false;
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
      this.isLoading = true;
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
      this.isLoading = false;
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
      this.isLoading = true;
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
      this.isLoading = false;

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
  },
};
</script>

<style scoped>
.OrderDateContainer {
  margin: 20px;
}
.OrderGraphContainer {
  margin-top: 20px;
  padding: 20px;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
  position: relative;
}
.b .graph {
  background-color: white;
  border: 2px solid #222;
  width: 300px;
  height: 300px;
}
.ButtonContainer {
  padding: 20px;
  display: flex;
  align-items: center;
  flex-direction: row;
  justify-content: space-between;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}

.ButtonContainer span {
  display: flex;
  gap: 10px;
  align-items: center;
}

.v-btn.selected :deep(*) {
  color: rgb(250, 100, 130, 0.9);
}
.v-btn.selected {
  color: rgb(250, 100, 130, 0.9);
  /* background-color: rgb(250, 100, 130); */
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}
.loading-overlay :deep(.v-progress-circular .v-progress-circular__overlay) {
  stroke: rgb(250, 100, 130);
}
</style>
