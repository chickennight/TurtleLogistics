import adminAPI from "@/api/admin";
import router from "@/router";

const adminStore = {
  namespaced: true,
  state: {
    adminToken: null,
    logisticAnalysis: [],
  },
  getters: {},
  mutations: {
    ADMIN_REGIST() {
      alert("관리자 등록이 완료되었습니다. 로그인해주세요.");
      router.push("/adminLogin");
    },
    ADMIN_LOGIN(state, data) {
      state.adminToken = data.accessToken; //토큰 저장
      localStorage.setItem("adminToken", data.accessToken); // 로컬스토리지에 accessToken 저장
      localStorage.setItem("adminRefreshToken", data.refreshToken); //로컬스토리지에 refreshToken 저장
    },
    LOGOUT(state) {
      state.adminToken = null;
      localStorage.removeItem("adminToken");
      localStorage.removeItem("adminRefreshToken");
    },
    GET_LOGISTIC_ANALYSIS(state, data) {
      state.logisticAnalysis = data;
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
    //물류 분석 자료
    async getLogisticAnalysis({ commit }) {
      try {
        const response = await adminAPI.logisticAnalysis();
        commit("GGET_LOGISTIC_ANALYSIS", response.data);
      } catch (error) {
        console.log(error);
      }
    },
  },
};

export default adminStore;
