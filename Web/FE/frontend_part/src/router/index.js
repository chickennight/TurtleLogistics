// 연결할 컴포넌트 import
import { createWebHistory, createRouter } from "vue-router";
import MainView from "../views/MainView.vue";
import AdminView from "../views/AdminView.vue";
import CustomerView from "../views/CustomerView.vue";
import AdminMainView from "../components/Admin/AdminMainView.vue";
import MainBluePrint from "../components/BluePrint/MainBluePrint.vue";
import MainGraph from "../components/graph/MainGraph.vue";
// import LoginView from "../views/LoginView.vue";
import AdminLogin from "../components/Login/AdminLogin.vue";
import CustomerLogin from "../components/Login/CustomerLogin.vue";
import MainLogistics from "../components/Logistics/MainLogistics.vue";
import MainMachine from "../components/Machine/MainMachine.vue";
import OrderByDate from "../components/Order/OrderByDate.vue";
import OrderByRegion from "../components/Order/OrderByRegion.vue";
import AdminRegist from "../components/Regist/AdminRegist.vue";
import CustomerRegist from "../components/Regist/CustomerRegist.vue";
import CustomerOrder from "../components/Order/CustomerOrder.vue";
import ErrorView from "../views/ErrorView.vue";
import MainCctv from "../components/Cctv/MainCctv.vue";

// 라우터 설계
const routes = [
  {
    path: "/",
    name: "MainView",
    component: MainView,
  },
  {
    path: "/admin",
    name: "AdminView",
    component: AdminView,
    meta: { auth: true, role: "admin" },
    children: [
      {
        path: "",
        name: "AdminMainView",
        component: AdminMainView,
      },
      {
        path: "date",
        name: "OrderByDate",
        component: OrderByDate,
      },
      {
        path: "logistics",
        name: "MainLogistics",
        component: MainLogistics,
      },
      {
        path: "blueprint",
        name: "MainBluePrint",
        component: MainBluePrint,
      },
      {
        path: "machine",
        name: "MainMachine",
        component: MainMachine,
      },
      {
        path: "graph",
        name: "MainGraph",
        component: MainGraph,
      },
      {
        path: "region",
        name: "OrderByRegion",
        component: OrderByRegion,
      },
      {
        path: "mainCctv",
        name: "MainCctv",
        component: MainCctv,
      }
    ],
  },
  {
    path: "/adminLogin",
    name: "AdminLogin",
    component: AdminLogin,
  },
  {
    path: "/customerLogin",
    name: "CustomerLogin",
    component: CustomerLogin,
  },
  {
    path: "/customer",
    name: "CustomerView",
    component: CustomerView,
    children: [
      {
        path: "",
        name: "CustomerOrder",
        component: CustomerOrder,
        meta: { auth: true, role: "customer" },
      },
      {
        path: "/regist",
        name: "CustomerRegist",
        component: CustomerRegist,
      },
    ],
  },
  {
    path: "/adminRegist",
    name: "AdminRegist",
    component: AdminRegist,
  },
  {
    path: "/errorView",
    name: "ErrorView",
    component: ErrorView,
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/errorView",
  },
];

// 라우터 생성
// 루트를 내부에 생성해도 되지만 코드가 복잡해지기 때문에 외부에 배열 형태로 생성한다
const router = createRouter({
  history: createWebHistory(),
  routes,
});

//주소 직접 접근시 토큰검사
router.beforeEach((to, from, next) => {
  const adminToken = localStorage.getItem("adminToken");
  const customerToken = localStorage.getItem("customerToken");

  let requiresAuth = false;
  let requiredRole = null;

  to.matched.forEach((routeRecord) => {
    if (routeRecord.meta.auth) {
      requiresAuth = routeRecord.meta.auth;
      requiredRole = routeRecord.meta.role;
    }
  });

  if (requiresAuth) {
    if (requiredRole == "admin" && !adminToken) {
      alert("관리자 권한이 필요합니다");
      next("/");
      return;
    }
    if (requiredRole == "customer" && !customerToken) {
      alert("로그인이 필요합니다");
      next("/");
      return;
    }
  }
  next();
});

// 라우터 추출
// 추출한 라우터는 main.js에서 import하여 사용
export default router;
