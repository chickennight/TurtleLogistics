// import Vue from "vue";
import { createStore } from "vuex";
import axios from "axios";
import router from "../router";
import createPersistedState from "vuex-persistedstate";

const REST_API = "http://localhost:8080";

// axiosInstance
const axiosInstance = axios.create({
  baseURL: REST_API,
});

const store = createStore({
  state: {
    orderData: [],
    machineLog: [],
    machineStatus: [],
    orderNowList: [],
    orderNowcalculate: [],
    logisticAnalysis: [],

    //Token
    adminToken: null,
    customerToken: null,
  },
  getters: {},
  mutations: {
    ADMIN_LOGIN(state, data) {
      state.adminToken = data.accessToken; //토큰 저장
      localStorage.setItem("adminToken", data.accessToken); // 로컬스토리지에 accessToken 저장
      localStorage.setItem("adminRefreshToken", data.refreshToken); //로컬스토리지에 refreshToken 저장
      console.log(data);
      console.log(data.accessToken);
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
      state.customerToken = data.accessToken; //토큰 저장
      localStorage.setItem("customerToken", data.accessToken); // 로컬스토리지에 accessToken 저장
      localStorage.setItem("customerRefreshToken", data.refreshToken); //로컬스토리지에 refreshToken 저장
      console.log(data);
      router.push("/customer");
    },
    LOGOUT(state) {
      state.adminToken = null;
      state.customerToken = null;
      localStorage.removeItem("adminToken");
      localStorage.removeItem("adminRefreshToken");
      localStorage.removeItem("customerToken");
      localStorage.removeItem("customerRefreshToken");
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
    GET_LOGISTIC_ANALYSIS(state, data) {
      state.logisticAnalysis = data;
    },
  },
  actions: {
    adminLogin({ commit }, admin) {
      const API_URL = `/admin/login`;
      axiosInstance({
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
      const API_URL = `customer/login`;
      axiosInstance({
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
    refreshToken({ commit }, userRole) {
      const API_URL = "/${userRole}/refreshToken";
      axiosInstance({
        url: API_URL,
        method: "post",
        data: {
          refreshToken: localStorage.getItem(`${userRole}RefreshToken`),
        },
      })
        .then((res) => {
          console.log(res.data);
          commit(`${userRole.toUpperCase()}_LOGIN`, res.data);
        })
        .catch((err) => {
          console.log(err);
          commit("LOGOUT");
        });
    },
    getOrderData({ commit }, date) {
      const API_URL = `/order/analysis/day`;
      axiosInstance({
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
      const API_URL = `/order/analysis/day`;
      axiosInstance({
        url: `${API_URL}?start_day=${date.start}&end_day=${date.end}`,
        method: "get",
      })
        .then((res) => {
          commit("GET_ORDER_WEEK_DATE", res.data);
        })
        .catch((err) => {
          console.log(err.data);
        });
    },
    getMachineStatus({ commit }) {
      const API_URL = `/machine`;
      axiosInstance({
        url: API_URL,
        method: "get",
      })
        .then((res) => {
          commit("GET_MACHINE_STATUS", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getMachineLog({ commit }) {
      const API_URL = `/machine/log`;
      axiosInstance({
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
      const API_URL = `/order/now`;
      axiosInstance({
        url: API_URL,
        method: "get",
      })
        .then((res) => {
          let calculate = [0, 0, 0, 0, 0, 0];
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
              case "이상 발생":
                calculate[5] += 1;
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
    getLogisticAnalysis({ commit }) {
      const API_URL = `/admin/logistic`;
      axiosInstance({
        url: API_URL,
        method: "get",
      })
        .then((res) => {
          commit("GET_LOGISTIC_ANALYSIS", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  plugins: [
    createPersistedState({
      whiteList: ["orderWeekData"],
    }),
  ],
});

//axiosInstance 인터셉터 설정

// Request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    const adminToken = localStorage.getItem("adminToken");
    const customerToken = localStorage.getItem("customerToken");
    if (adminToken) {
      config.headers.Authorization = `Bearer ${adminToken}`;
    } else if (customerToken) {
      config.headers.Authorization = `Bearer ${customerToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response.status === 401) {
      const originalRequest = error.config;
      const userRole = originalRequest.url.startsWith("/admin") ? "admin" : "customer";
      store.dispatch("refreshToken", userRole).then(() => {
        return axiosInstance(originalRequest);
      });
    }
    return Promise.reject(error);
  }
);

export default store;
