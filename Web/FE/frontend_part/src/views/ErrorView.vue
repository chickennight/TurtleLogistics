<template>
  <div id="ErrorContainer">
    <div id="MainLogo">
      <img class="MainTurtle" src="./MainTurtle.png" />
      TL <br />
      TURTLE LOGISTICS
    </div>
    &nbsp;
    <div class="ErrorDiv">
      <span class="ErrorMsg">페이지를 찾을 수 없습니다. <br /></span>
      <router-link :to="{ name: 'MainView' }" class="ErrorLink">메인페이지로 가기</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "ErrorView",
};
</script>

<style scoped>
#ErrorContainer {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 1000px;
  border: 1px solid black;
  color: white;
}
#MainLogo {
  width: 400px;
  height: 400px;
  text-align: center;
  font-size: 35px;
  font-family: "GmarketSansTTFBold";
  background: linear-gradient(to Right Bottom, #e64878 30%, #f08556 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.MainTurtle {
  height: 300px;
  width: 300px;
  object-fit: contain;
  margin: auto;
  display: block;
}
#CardDiv {
  display: flex;
  flex-direction: row;
}
.ErrorDiv {
  text-align: center;
}
.ErrorMsg {
  font-size: 30px;
}
.ErrorLink {
  font-size: 20px;
}
</style>
