// import Vue from "vue";
import { createStore } from "vuex";
import axios from "axios";
import router from "../router";

const REST_API = "http://localhost:8080";

const store = createStore({
    state: {},
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
            alert("관리자 등록이 완료되었습니다. 로그인해주세요.")
            router.push("/adminLogin");
        },
        CUSTOMER_REGIST(state, data) {
            state;
            console.log(data);
            alert("회원 가입이 완료되었습니다. 로그인해주세요.");
        },
        CUSTOMER_LOGIN(state, data) {
            state;
            console.log(data);
            router.push("/customer");
        }
    },
    actions: {
        adminLogin({commit}, admin) {
            const API_URL = `${REST_API}/admin/login`;
            axios({
                url: API_URL,
                method: "post",
                data: admin
            })
            .then((res) => {
                console.log(res.data);
                commit("ADMIN_LOGIN", res.data);
            })
            .catch((err) => {
                console.log(err);    
            })
        },
        adminRegist({commit}, admin) {
            const API_URL = `${REST_API}/admin/register`;
            axios({
                url: API_URL,
                method: "post",
                data: admin
            })
            .then((res) => {
                console.log(res.data);
                commit("ADMIN_REGIST", res.data);
            })
            .catch((err) => {
                console.log(err);    
            })
        },
        customerRegist({commit}, customer) {
            const API_URL = `${REST_API}/customer/register`;
            axios({
                url: API_URL,
                method: "post",
                data: customer
            })
            .then((res) => {
                console.log(res.data);
                commit("CUSTOMER_REGIST", res.data);
            })
            .catch((err) => {
                console.log(err);    
            })
        },
        customerLogin({commit}, customer) {
            const API_URL = `${REST_API}/customer/login`;
            axios({
                url: API_URL,
                method: "post",
                data: customer
            })
            .then((res) => {
                console.log(res.data);
                commit("CUSTOMER_LOGIN", res.data);
            })
            .catch((err) => {
                console.log(err);    
            })
        },
    }
})

export default store