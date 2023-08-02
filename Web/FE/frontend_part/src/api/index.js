import axios from "axios";
import store from "@/store";

// 토큰이 필요 없는 인스턴스
const publicApi = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL,
  headers: {
    "Content-Type": "application/json; charset=uft-8",
  },
});

//토큰이 필요한 인스턴스
const authorizedApi = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL,
  headers: {
    "Content-Type": "application/json; charset=uft-8",
  },
});

//authorizedApi 인터셉터 설정

// Request interceptor
authorizedApi.interceptors.request.use(
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
authorizedApi.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response.status === 401) {
      const originalRequest = error.config;
      const userRole = originalRequest.url.startsWith("/admin") ? "admin" : "customer";
      store.dispatch("refreshToken", userRole).then(() => {
        return authorizedApi(originalRequest);
      });
    }
    return Promise.reject(error);
  }
);

export { publicApi, authorizedApi };
