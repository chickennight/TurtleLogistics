import machineAPI from "@/api/machine";

const machineStore = {
  namespaced: true,
  state: {
    machineLog: [],
    machineStatus: [],
  },
  getters: {},
  mutations: {
    GET_MACHINE_STATUS(state, data) {
      state.machineStatus = data;
    },
    GET_MACHINE_LOG(state, data) {
      state.machineLog = data;
    },
  },
  actions: {
    //기기 상태 보기
    async getMachineStatus({ commit }) {
      try {
        const response = await machineAPI.getMachineStatus();
        commit("GET_MACHINE_STATUS", response.data);
      } catch (error) {
        console.log(error);
      }
    },
    //기기 로그 보기
    async getMachineLog({ commit }) {
      try {
        const response = await machineAPI.getLogs();
        commit("GET_MACHINE_LOG", response.data);
      } catch (error) {
        console.log(error);
      }
    },
  },
};

export default machineStore;
