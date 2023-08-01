<template>
  <h2>관리자 로그인이거나 회원 로그인이거나</h2>
  <admin-login></admin-login>
  <customer-login></customer-login>
</template>


<script>
// 들어오는 변수에 따라 보여지는 템플릿을 다르게 해야한다
// AdminLogin.vue 거나 CustomerLogin.vue 거나
// 각각의 뷰에서 또 각각의 회원가입으로 이동할 수 있다
import AdminLogin from "@/components/Login/AdminLogin.vue";
import CustomerLogin from "@/components/Login/CustomerLogin.vue";
  
export default {
  name: "LoginView",
  components: { 
    AdminLogin, 
    CustomerLogin 
    },
  
}
</script>

<style scoped>

</style>