<template>
  <div id="header">
    <!-- 관리자 헤더 -->
    <div class="AdminProfile" v-if="isAdmin">
      관리자님 환영합니다 &nbsp; | &nbsp;
      <span @click="adminLogout">로그아웃</span>
    </div>
    <!-- 사용자 헤더 -->
    <div class="UserProfile" v-else>
      사용자님 환영합니다 &nbsp; | &nbsp;
      <span @click="customerLogout">로그아웃</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "HeaderNav",
  data() {
    return {
      isAdmin: this.checkAdmin(),
    };
  },
  methods: {
    adminLogout() {
      alert("로그아웃되었습니다.");
      this.$store.dispatch("admin/Logout");
    },
    customerLogout() {
      alert("로그아웃되었습니다.");
      this.$store.dispatch("customer/Logout");
    },
    checkAdmin() {
      const adminToken = localStorage.getItem("adminToken");
      return adminToken ? true : false;
    },
  },
  created() {
    this.isAdmin = this.checkAdmin(); //컴포넌트가 생성될 때 토큰을 확인
  },
};
</script>

<style scoped>
#header {
  width: 100%;
  height: 100px;
  display: flex;
  justify-content: flex-end;
  /* box-shadow: 6px 1px 3px 3px black; */
}
.AdminProfile,
.UserProfile {
  width: 350px;
  margin: auto 0;
}
</style>
