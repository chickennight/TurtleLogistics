<template>
  <div id="header">
    <div class="AdminProfile">
      관리자님 환영합니다 &nbsp;&nbsp;&nbsp; 마이페이지 &nbsp; | &nbsp;
      <span @click="logout">로그아웃</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "HeaderNav",
  methods: {
    logout() {
      alert("로그아웃되었습니다.");
      this.$store.dispatch("admin/Logout");
    },
  },
};
</script>

<style scoped>
#header {
  width: 100%;
  height: 100px;
  display: flex;
  justify-content: flex-end;
  box-shadow: 6px 1px 3px 3px black;
}
.AdminProfile {
  width: 350px;
  margin: auto 0;
}
</style>
