import orderAPI from "@/api/order";

const orderStore = {
  namespaced: true,
  state: {
    orderData: [],
    orderNowList: [],
    orderNowcalculate: [],
  },
  getters: {},
  mutations: {
    GET_ORDER_DATE(state, date) {
      state.orderData = date;
    },
    GET_ORDER_WEEK_DATE(state, date) {
      state.orderWeekData = date;
    },
    GET_ORDER_NOWS(state, data) {
      state.orderNowList = data;
    },
    GET_CALCULATE(state, data) {
      state.orderNowcalculate = data;
    },
  },
  actions: {
    //일자별 주문 분석
    async getOrderData({ commit }, date) {
      try {
        let startDay = date.start;
        let endDay = date.end;
        const response = await orderAPI.dataAnalysisDay(startDay, endDay);
        commit("GET_ORDER_DATE", response.data);
      } catch (error) {
        console.log(error);
      }
    },
    //주간별 주문 분석
    async getOrderWeekData({ commit }, date) {
      try {
        let startDay = date.start;
        let endDay = date.end;
        const response = await orderAPI.dataAnalysisDay(startDay, endDay);
        commit("GET_ORDER_WEEK_DATE", response.data);
      } catch (error) {
        console.log(error);
      }
    },
    //현재 진행중인 주문 목록
    async getOrderNows({ commit }) {
      try {
        const response = await orderAPI.orderNow();
        let calculate = [0, 0, 0, 0, 0, 0];
        for (let item of response.data) {
          switch (item.status) {
            case "주문 접수":
              calculate[0] += 1;
              break;
            case "포장 과정":
              calculate[1] += 1;
              break;
            case "분류 과정":
              calculate[2] += 1;
              break;
            case "분류 완료":
              calculate[3] += 1;
              break;
            case "배송 과정":
              calculate[4] += 1;
              break;
            case "이상 발생":
              calculate[5] += 1;
              break;
          }
        }
        commit("GET_CALCULATE", calculate);
        commit("GET_ORDER_NOWS", response.data);
      } catch (error) {
        console.log(error);
      }
    },
  },
};

export default orderStore;
