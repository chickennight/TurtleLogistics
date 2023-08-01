import orderAPI from "../api/order";

const state = {
  orderList: [],
  packageList: [],
  analysisDayData: [],
  analysisRegionData: [],
};

const actions = {
  async fetchOrderList({ commit }) {
    try {
      const data = await orderAPI.orderNow();
      commit("SET_ORDER_LIST", data);
    } catch (error) {
      console.error(error);
    }
  },

  async fetchPackageList({ commit }) {
    try {
      const data = await orderAPI.packageList();
      commit("SET_PACKAGE_LIST", data);
    } catch (error) {
      console.error(error);
    }
  },

  async fetchDataAnalysisDay({ commit }, { startDay, endDay }) {
    try {
      const data = await orderAPI.dataAnalysisDay(startDay, endDay);
      commit("SET_ANALYSIS_DAY_DATA", data);
    } catch (error) {
      console.error(error);
    }
  },

  async fetchDataAnalysisRegion({ commit }, { year, month }) {
    try {
      const data = await orderAPI.dataAnalysisRegion(year, month);
      commit("SET_ANALYSIS_REGION_DATA", data);
    } catch (error) {
      console.error(error);
    }
  },

  async createOrder({ commit }, newOrderDto) {
    try {
      const data = await orderAPI.newOrder(newOrderDto);
      commit("ADD_ORDER", data);
    } catch (error) {
      console.error(error);
    }
  },

  async updateOrder({ commit }, orderUpdateDto) {
    try {
      const data = await orderAPI.orderUpdate(orderUpdateDto);
      commit("UPDATE_ORDER", data);
    } catch (error) {
      console.error(error);
    }
  },
};

const mutations = {
  SET_ORDER_LIST(state, data) {
    state.orderList = data;
  },
  SET_PACKAGE_LIST(state, data) {
    state.packageList = data;
  },
  SET_ANALYSIS_DAY_DATA(state, data) {
    state.analysisDayData = data;
  },
  SET_ANALYSIS_REGION_DATA(state, data) {
    state.analysisRegionData = data;
  },
  ADD_ORDER(state, data) {
    state.orderList.push(data);
  },
  UPDATE_ORDER(state, data) {
    const index = state.orderList.findIndex((order) => order.id === data.id);
    if (index !== -1) {
      state.orderList.splice(index, 1, data);
    }
  },
};

export default {
  state,
  actions,
  mutations,
};
