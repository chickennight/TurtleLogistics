<template>
  <div id="header">
    <div class="AdminProfile">
      %%%님 환영합니다 &nbsp;&nbsp;&nbsp;
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
  box-shadow: 5px 5px 3px 3px black;
  
}
.AdminProfile{
  width: 350px;
  border: 1px solid white;
  margin: auto 0;
}
</style>