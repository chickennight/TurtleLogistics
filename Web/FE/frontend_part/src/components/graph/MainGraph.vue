<template>
  <div class="OrderNowContainer">
    <div class="OrderNowHeader"><h1>주문 현황</h1></div>
    <div class="OrderNowGraph">
      <Line :data="updatedChartData" :options="chartOptions" style="color: white" />
    </div>
    &nbsp;
    <div class="search-container">
      <label for="statusSearch" style="margin-right: 0.5%">현황별 검색 : </label>
      <select v-model="searchStatus" @change="filterOrders" style="margin-right: 5px">
        <option value="">모든 현황</option>
        <option value="주문 접수">주문 접수</option>
        <option value="포장 과정">포장 과정</option>
        <option value="분류 과정">분류 과정</option>
        <option value="분류 완료">분류 완료</option>
        <option value="배송 과정">배송 과정</option>
        <option value="이상 발생">이상 발생</option>
      </select>
      ▼
    </div>
    <div class="OrderNowTableContainer">
      <v-table density="compact" theme="dark" class="main_table">
        <thead>
          <tr>
            <th style="text-align: center">주문번호</th>
            <th style="text-align: center">현황</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in paginatedOrderNowList" :key="item.order_num">
            <td>{{ item.order_num }}</td>
            <td>{{ item.status }}</td>
          </tr>
        </tbody>
      </v-table>
    </div>
    <div class="pagination">
      <button @click="currentPage = Math.max(1, currentPage - 1)" style="margin-right: 1%">
        이전
      </button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="currentPage = Math.min(totalPages, currentPage + 1)" style="margin-left: 1%">
        다음
      </button>
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
  name: "MainGraph",
  components: {
    Line,
  },
  data() {
    return {
      renderCount: 0,
      myTimer: null,
      chartData: {
        labels: ["주문 접수", "포장 과정", "분류 과정", "분류 완료", "배송 과정", "이상 발생"],
        datasets: [
          {
            label: "주문건수",
            borderColor: "rgb(250, 100, 130)",
            backgroundColor: "rgb(250, 100, 130)",
            color: "red",
            data: [],
          },
        ],
      },
      chartOptions: {
        animation: false,
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
      //페이지네이션과 검색기능
      currentPage: 1,
      itemsPerPage: 10,
      searchStatus: "",
      lastApiResponse: null,
      filteredOrderNowList: [],
    };
  },
  created() {
    this.get_order_nows();
    this.get_order_nows().then(() => {
      this.filterOrders(); // 초기 필터링
    });
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
    ...mapState("order", ["orderNowList"]),
    ...mapState("order", ["orderNowcalculate"]),
    paginatedOrderNowList() {
      if (!this.filteredOrderNowList) return [];
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredOrderNowList.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredOrderNowList.length / this.itemsPerPage);
    },
    updatedChartData() {
      return {
        ...this.chartData,
        datasets: [
          {
            ...this.chartData.datasets[0],
            data: this.orderNowcalculate,
          },
        ],
      };
    },
  },
  methods: {
    async get_order_nows() {
      try {
        const response = await this.$store.dispatch("order/getOrderNows");

        if (JSON.stringify(response) !== JSON.stringify(this.lastApiResponse)) {
          this.lastApiResponse = response;
        }
      } catch (error) {
        console.error("주문 정보 가져오기 실패:", error);
      }
    },
    filterOrders() {
      if (!this.searchStatus) {
        this.filteredOrderNowList = this.orderNowList;
      } else {
        this.filteredOrderNowList = this.orderNowList.filter((order) =>
          order.status.includes(this.searchStatus)
        );
      }
      this.currentPage = 1; // 페이지를 처음으로 재설정
    },
  },
};
</script>

<style scoped>
.OrderNowContainer {
  margin: 20px;
}
.OrderNowGraph {
  padding: 20px;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
  margin-top: 20px;
}
.OrderNowHeader {
  padding: 20px;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}

.main_table th,
.main_table td {
  overflow-y: auto;
  scrollbar-width: 0px;
  text-align: center;
  vertical-align: middle;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 0;
}

.search-container {
  display: flex;
  justify-content: flex-end;
  padding-bottom: 17px;
  margin-right: 2%;
  font-size: 20px;
}
.OrderNowTableContainer {
  border-radius: 10px;
}
select:hover {
  background-color: rgb(55, 55, 55);
}
option {
  background-color: rgb(55, 55, 55);
}
</style>
