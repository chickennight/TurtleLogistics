import customerAPI from "@/api/customer";
import router from "@/router";

const customerStore = {
  namespaced: true,
  state: {
    customer_id: "",
  },
  getters: {},
  mutations: {
    CUSTOMER_REGIST() {
      alert("회원가입이 완료되었습니다. 로그인해주세요.");
    },
    CUSTOMER_LOGIN(state, data) {
      state.customer_id = data.customer_id;
      localStorage.setItem("customerToken", data.accessToken); // 로컬스토리지에 accessToken 저장
      localStorage.setItem("customerRefreshToken", data.refreshToken); //로컬스토리지에 refreshToken 저장
      localStorage.removeItem("adminToken");
      localStorage.removeItem("adminRefreshToken");
    },
    LOGOUT() {
      localStorage.removeItem("adminToken");
      localStorage.removeItem("adminRefreshToken");
      localStorage.removeItem("customerToken");
      localStorage.removeItem("customerRefreshToken");
    },
  },
  actions: {
    //회원가입
    async customerRegist({ commit }, customer) {
      try {
        const response = await customerAPI.registerCustomer(customer);
        commit("CUSTOMER_REGIST", response.data);
        router.push("/customerLogin");
      } catch (error) {
        console.log(error);
      }
    },
    //사용자 로그인
    async customerLogin({ commit }, customer) {
      try {
        const response = await customerAPI.login(customer);
        if (response.status == 200) {
          commit("CUSTOMER_LOGIN", response.data);
          router.push("/customer");
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
  },
};

export default customerStore;
