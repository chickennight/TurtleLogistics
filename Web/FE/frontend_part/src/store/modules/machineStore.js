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
    //기기 전원 종료 명령
    async machineOff({ commit }) {
      try {
        const payload = {
          topic: "/mod/web/power",
          message: `{"power":"-1"}`,
        };
        const response = await machineAPI.machineControl(payload);
        if (response.status == 200) {
          commit;
        } else {
          alert("통신이상");
        }
      } catch (error) {
        console.log(error);
      }
    },
    async machineOn({ commit }) {
      try {
        const payload = {
          topic: "/mod/web/power",
          message: `{"power":"1"}`,
        };
        const response = await machineAPI.machineControl(payload);
        if (response.status == 200) {
          commit;
        } else {
          alert("통신이상");
        }
      } catch (error) {
        console.log(error);
      }
    },
    async machineFixed({ commit }, machine_id) {
      try {
        const response = await machineAPI.updateMachine(machine_id, false);
        if (response.status == 200) {
          commit;
        } else {
          alert("통신이상");
        }
      } catch (error) {
        console.log(error);
      }
    },
  },
};

export default machineStore;
