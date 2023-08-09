import adminAPI from "@/api/admin";
import router from "@/router";

const adminStore = {
  namespaced: true,
  state: {
    logisticAnalysis: [],
    image: null,
  },
  getters: {},
  mutations: {
    ADMIN_REGIST() {
      alert("관리자 등록이 완료되었습니다. 로그인해주세요.");
      router.push("/adminLogin");
    },
    ADMIN_LOGIN(state, data) {
      state;
      localStorage.setItem("adminToken", data.accessToken);
      localStorage.setItem("adminRefreshToken", data.refreshToken);
    },
    LOGOUT() {
      localStorage.removeItem("adminToken");
      localStorage.removeItem("adminRefreshToken");
    },
    GET_LOGISTIC_ANALYSIS(state, data) {
      state.logisticAnalysis = data;
    },
    GET_IMAGE(state, data) {
      state.image = data;
    },
  },
  actions: {
    //관리자 등록
    async adminRegist({ commit }, admin) {
      try {
        const response = await adminAPI.registerAdmin(admin);
        commit("ADMIN_REGIST", response.data);
        router.push("/adminLogin");
      } catch (error) {
        console.log(error);
      }
    },
    //관리자 로그인
    async adminLogin({ commit }, admin) {
      try {
        const response = await adminAPI.login(admin);
        if (response.status == 200) {
          commit("ADMIN_LOGIN", response.data);
          router.push("/admin");
        } else {
          alert("아이디 또는 비밀번호를 확인해주세요");
        }
      } catch (error) {
        console.log(error);
      }
    },
    //로그아웃
    Logout({ commit }) {
      commit("LOGOUT");
      router.push("/");
    },
    //물류 분석 자료
    async getLogisticAnalysis({ commit }) {
      try {
        const response = await adminAPI.logisticAnalysis();
        commit("GET_LOGISTIC_ANALYSIS", response.data);
      } catch (error) {
        console.log(error);
      }
    },
    async SendSMS(_, machineDetail) {
      try {
        await adminAPI.sendMessage(machineDetail);
      } catch (error) {
        console.log(error);
      }
    },
    takeScreenshot(_, { image, log_num }) {
      try {
        adminAPI.uploadImage(image, log_num);
      } catch (error) {
        console.log(error);
      }
    },
    async getImage({ commit }, log_num) {
      try {
        const response = await adminAPI.downloadImage(log_num);
        const base64 = btoa(
          new Uint8Array(response.data).reduce((data, byte) => data + String.fromCharCode(byte), "")
        );
        const imageDataUrl = "data:" + response.headers["content-type"] + ";base64," + base64;

        commit("GET_IMAGE", imageDataUrl);
      } catch (error) {
        console.log(error);
      }
    },
  },
};

export default adminStore;
