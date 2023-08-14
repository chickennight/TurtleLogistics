<template>
  <div class="LogisticContainer">
    <div class="LogisticsHeader">
      <h1>물류 현황</h1>
      <div class="refresh-button">
        <font-awesome-icon
          @click="get_logistic_analysis"
          :icon="['fas', 'arrows-rotate']"
          class="refresh-icon"
          v-if="!isLoading"
        />
        <font-awesome-icon
          :icon="['fas', 'arrows-rotate']"
          style="color: rgb(250, 100, 130)"
          :class="{ 'refresh-icon': true, 'refresh-icon-spinning': isLoading }"
          v-if="isLoading"
        />
      </div>
    </div>
    <div class="ProductStatusContainer">
      <v-table density="compact" theme="dark" class="main_table">
        <thead>
          <tr>
            <th style="text-align: center" rowspan="2" @click="sortTable('product_num', $event)">
              상품번호
              <font-awesome-icon
                :icon="['fas', 'sort']"
                style="color: #666a70"
                class="icon-padding-left"
              />
            </th>
            <th style="text-align: center" rowspan="2">상품명</th>
            <th style="text-align: center" rowspan="2" @click="sortTable('stock', $event)">
              상품재고
              <font-awesome-icon
                :icon="['fas', 'sort']"
                style="color: #666a70"
                class="icon-padding-left"
              />
            </th>
            <th style="text-align: center" colspan="4">주문량</th>
            <th style="text-align: center" rowspan="2" @click="sortTable('error_message', $event)">
              이상 발생
              <font-awesome-icon
                :icon="['fas', 'sort']"
                style="color: #666a70"
                class="icon-padding-left"
              />
            </th>
          </tr>
          <tr>
            <th style="text-align: center" @click="sortTable('year', $event)">
              연평균
              <font-awesome-icon
                :icon="['fas', 'sort']"
                style="color: #666a70"
                class="icon-padding-left"
              />
            </th>
            <th style="text-align: center" @click="sortTable('month', $event)">
              월평균
              <font-awesome-icon
                :icon="['fas', 'sort']"
                style="color: #666a70"
                class="icon-padding-left"
              />
            </th>
            <th style="text-align: center" @click="sortTable('week', $event)">
              주평균
              <font-awesome-icon
                :icon="['fas', 'sort']"
                style="color: #666a70"
                class="icon-padding-left"
              />
            </th>
            <th style="text-align: center" @click="sortTable('today', $event)">
              금일
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
      sortBy: null, // 현재 정렬 기준
      sortOrder: true, // 정렬 순서 (true: 오름차순, false: 내림차순)
      isLoading: false,
    };
  },
  computed: {
    ...mapState("admin", ["logisticAnalysis"]),
  },
  methods: {
    async get_logistic_analysis() {
      this.isLoading = true;
      await this.$store.dispatch("admin/getLogisticAnalysis");
      this.isLoading = false;
    },
    //테이블 정렬 기능
    sortTable(column) {
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
        } else if (this.sortBy === "year") {
          result = a.year - b.year;
        } else if (this.sortBy === "month") {
          result = a.month - b.month;
        } else if (this.sortBy === "week") {
          result = a.week - b.week;
        } else if (this.sortBy === "today") {
          result = a.today - b.today;
        }

        return this.sortOrder ? result : -result;
      });
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
  color: rgb(250, 100, 130);
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
.main_table th,
.main_table td {
  text-align: center;
  vertical-align: middle;
}

.icon-padding-left {
  margin-left: 5px;
  cursor: pointer;
}
.refresh-icon-spinning * {
  color: #fa6482;
}
.refresh-icon {
  font-size: 230%;
}
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
.refresh-icon-spinning {
  animation: spin 2s linear infinite;
}
.refresh-button {
  margin-right: 30px;
}
</style>
