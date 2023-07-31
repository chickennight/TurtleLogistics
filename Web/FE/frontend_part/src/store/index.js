// import Vue from "vue";
import { createStore } from "vuex";
import axios from "axios";
import router from "../router";
import createPersistedState  from "vuex-persistedstate";

const REST_API = "http://localhost:8080";

const store = createStore({
  state: {
    orderData: [],
    machineLog: [],
    machineStatus: [],
    orderNowList: [],
    orderNowcalculate: [],
  },
  getters: {},
  mutations: {
    ADMIN_LOGIN(state, data) {
      state;
      console.log(data);
      router.push("/admin");
    },
    ADMIN_REGIST(state, data) {
      state;
      console.log(data);
      alert("관리자 등록이 완료되었습니다. 로그인해주세요.");
      router.push("/adminLogin");
    },
    CUSTOMER_REGIST(state, data) {
      state;
      console.log(data);
      alert("회원 가입이 완료되었습니다. 로그인해주세요.");
      router.push("/customerLogin");
    },
    CUSTOMER_LOGIN(state, data) {
      state;
      console.log(data);
      router.push("/customer");
    },
    GET_ORDER_DATE(state, date) {
      state.orderData = date;
    },
    GET_ORDER_WEEK_DATE(state, date) {
      state.orderWeekData = date;
    },
    GET_MACHINE_LOG(state, data) {
      state.machineLog = data;
    },
    GET_MACHINE_STATUS(state, data) {
      state.machineStatus = data;
    },
    GET_ORDER_NOWS(state, data) {
      state.orderNowList = data;
    },
    GET_CALCULATE(state, data) {
      state.orderNowcalculate = data;
    },
  },
  actions: {
    adminLogin({ commit }, admin) {
      const API_URL = `${REST_API}/admin/login`;
      axios({
        url: API_URL,
        method: "post",
        data: admin,
      })
        .then((res) => {
          console.log(res.data);
          commit("ADMIN_LOGIN", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    adminRegist({ commit }, admin) {
      const API_URL = `${REST_API}/admin/register`;
      axios({
        url: API_URL,
        method: "post",
        data: admin,
      })
        .then((res) => {
          console.log(res.data);
          commit("ADMIN_REGIST", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    customerRegist({ commit }, customer) {
      const API_URL = `${REST_API}/customer/register`;
      axios({
        url: API_URL,
        method: "post",
        data: customer,
      })
        .then((res) => {
          console.log(res.data);
          commit("CUSTOMER_REGIST", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    customerLogin({ commit }, customer) {
      const API_URL = `${REST_API}/customer/login`;
      axios({
        url: API_URL,
        method: "post",
        data: customer,
      })
        .then((res) => {
          console.log(res.data);
          commit("CUSTOMER_LOGIN", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getOrderData({ commit }, date) {
      const API_URL = `${REST_API}/order/analysis/day`;
      axios({
        url: `${API_URL}?start_day=${date.start}&end_day=${date.end}`,
        method: "get",
      })
        .then((res) => {
          commit("GET_ORDER_DATE", res.data);
        })
        .catch((err) => {
          console.log(err.data);
        });
    },
    getOrderWeekData({ commit }, date) {
      const API_URL = `${REST_API}/order/analysis/day`;
      axios({
        url: `${API_URL}?start_day=${date.start}&end_day=${date.end}`,
        method: "get",
      })
        .then((res) => {
        commit("GET_ORDER_WEEK_DATE", res.data);
        })
        .catch((err) => {
        console.log(err.data);    
        })
    },
    getMachineStatus({ commit }) {
      const API_URL = `${REST_API}/machine`;
      axios({
        url: API_URL,
        method: "get",
      })
        .then((res) => {
          commit("GET_MACHINE_STATUS", res.data["상태"]);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getMachineLog({ commit }) {
      const API_URL = `${REST_API}/machine/log`;
      axios({
        url: API_URL,
        method: "get",
      })
        .then((res) => {
          commit("GET_MACHINE_LOG", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getOrderNows({ commit }) {
      const API_URL = `${REST_API}/order/now`;
      axios({
        url: API_URL,
        method: "get",
      })
        .then((res) => {
          let calculate = [0, 0, 0, 0, 0];
          for (let item of res.data) {
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
            }
          }
          commit("GET_CALCULATE", calculate);
          commit("GET_ORDER_NOWS", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  plugins: [createPersistedState({
    whiteList: ["orderWeekData"],
  })]
})

export default store;
