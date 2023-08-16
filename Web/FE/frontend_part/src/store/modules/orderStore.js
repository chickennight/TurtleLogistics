import orderAPI from "@/api/order";

const orderStore = {
  namespaced: true,
  state: {
    orderData: [],
    orderWeekData: [],
    orderNowList: [],
    orderNowcalculate: [],
    orderRegion: [],
    cachedOrderData: {
      "3month": {
        data: null,
        timestamp: null,
      },
      "6month": {
        data: null,
        timestamp: null,
      },
      "1year": {
        data: null,
        timestamp: null,
      },
    },
  },
  getters: {
    getCachedOrderData: (state) => (period) => {
      let cacheDuration;

      switch (period) {
        case "3month":
        case "6month":
          cacheDuration = 7 * 24 * 60 * 60 * 1000; // 1주일
          break;
        case "1year":
          cacheDuration = 30 * 24 * 60 * 60 * 1000; // 1달
          break;
        default:
          return null;
      }

      const now = Date.now();
      const cachedData = state.cachedOrderData[period];
      if (cachedData.data && now - cachedData.timestamp <= cacheDuration) {
        return cachedData.data;
      }
      return null;
    },
  },
  mutations: {
    SET_ORDER_DATA(state, { period, data }) {
      state.cachedOrderData[period].data = data;
      state.cachedOrderData[period].timestamp = Date.now();
    },
    GET_ORDER_DATE(state, data) {
      state.orderData = data;
    },
    GET_ORDER_WEEK_DATE(state, data) {
      state.orderWeekData = data;
    },
    GET_ORDER_NOWS(state, data) {
      state.orderNowList = data;
    },
    GET_CALCULATE(state, data) {
      state.orderNowcalculate = data;
    },
    GET_DATA_ANALYSIS_REGION(state, data) {
      state.orderRegion = data;
    },
    DO_ORDER() {
      alert("주문이 완료되었습니다.");
    },
    RESET_ORDER_REGION(state) {
      state.orderRegion = [];
    },
  },
  actions: {
    //캐싱
    async fetchAndCacheOrderData({ commit, getters }, date) {
      try {
        let startDay = date.start;
        let endDay = date.end;
        const response = await orderAPI.dataAnalysisDay(startDay, endDay);

        let period;
        if (date.period === "3month") period = "3month";
        else if (date.period === "6month") period = "6month";
        else if (date.period === "year") period = "1year";
        else return;

        // 캐싱 된 데이터를 가져와서 현재 데이터와 비교
        const cachedData = getters.getCachedOrderData(period);
        if (!cachedData) {
          commit("SET_ORDER_DATA", { period, data: response.data });
        }
      } catch (error) {
        console.log(error);
      }
    },
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
    // 지역별 주문 분석
    async getDataAnalysisRegion({ commit }, regioncode) {
      try {
        const response = await orderAPI.dataAnalysisRegion(regioncode);
        commit("GET_DATA_ANALYSIS_REGION", response.data);
      } catch (error) {
        console.log(error);
      }
    },
    async doOrder({ commit }, order) {
      try {
        await orderAPI.newOrder(order);
        commit("DO_ORDER");
      } catch (error) {
        alert("오류가 발생했습니다. 다시 시도해주세요.");
        console.log(error);
      }
    },
    //데이터 초기화
    resetOrderRegion({ commit }) {
      commit("RESET_ORDER_REGION");
    },
  },
};

export default orderStore;
