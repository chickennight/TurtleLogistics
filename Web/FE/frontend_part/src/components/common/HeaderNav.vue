<template>
  <div id="header">
    <div class="AdminProfile">
      관리자님 환영합니다 &nbsp;&nbsp;&nbsp;
      마이페이지  &nbsp; |  &nbsp;
      로그아웃
    </div>
  </div>
</template>

<script>
export default {
    name : "HeaderNav",
}
</script>

<style>
#header{
  width: 100%;
  height: 100px;
  display: flex;
  justify-content: flex-end;
  box-shadow: 6px 1px 3px 3px black;
  
}
.AdminProfile{
  width: 350px;
  margin: auto 0;
}
</style>