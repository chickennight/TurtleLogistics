// import Vue from "vue";
import createPersistedState from "vuex-persistedstate";
import { createStore } from "vuex";
import adminStore from "./modules/adminStore";
import customerStore from "./modules/customerStore";
import machineStore from "./modules/machineStore";
import orderStore from "./modules/orderStore";

const store = createStore({
  modules: {
    admin: adminStore,
    customer: customerStore,
    machine: machineStore,
    order: orderStore,
  },
  devtools: true,
  plugins: [
    createPersistedState({
      whiteList: ["orderWeekData"],
    }),
  ],
});
// 다른 코드 고쳐야할것들....
// this.$store.dispatch("admin/login", this.admin);
export default store;
