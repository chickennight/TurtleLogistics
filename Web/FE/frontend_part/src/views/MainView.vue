<template>
  <div id="MainContainer">
    <div id="MainLogo">
      <img class="MainTurtle" :src="mainSrc" @click="EasterEgg" />
      TL <br />
      TURTLE LOGISTICS
    </div>
    <div id="CardDiv">
      <v-card class="customCard" variant="outlined" style="margin-right: 5%" href="/customerLogin">
        <v-card-actions> 사용자 페이지 </v-card-actions>
      </v-card>
      <v-card class="customCard" variant="outlined" href="/adminLogin">
        <v-card-actions> 관리자 페이지 </v-card-actions>
      </v-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "MainView",
  data: () => ({
    mainSrc: "./MainTurtle.png",
  }),
  methods: {
    EasterEgg() {
      if (this.mainSrc == "./MainTurtle.png") {
        this.mainSrc = "./Turtle_Easter.png";
      } else {
        this.mainSrc = "./MainTurtle.png";
      }
    },
  },
};
</script>

<style scoped>
#MainContainer {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  /* height: 1000px; */
  height: 100vh;
  border: 1px solid black;
  color: white;
  /* 
  background-image: url("@/assets/conveyor.svg"), url("@/assets/belt.svg");
  background-position: top right, bottom right;
  background-size: 80%, 80%;
  background-repeat: no-repeat; */
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
  margin-bottom: 30px; /* 로고와 카드 사이의 간격 조절 */
}
.MainTurtle {
  height: 300px;
  width: 300px;
  object-fit: contain;
  margin: auto;
  display: block;
  filter: drop-shadow(5px 5px 10px #555); /* 로고에 그림자 효과 추가 */
}
#CardDiv {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.v-card-actions {
  display: flex;
  justify-content: center;
  padding: 0px 8px 10px 8px;
}
.customCard {
  width: 200px;
  height: 130px;
  background-color: rgb(55, 55, 55);
  border-style: none;
  color: rgb(250, 100, 130);
  display: flex;
  justify-content: center;
  align-content: center;
  font-size: 22px;
  font-weight: bold;
  padding-top: 13px;
}
.v-card-item {
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  padding-bottom: -40px;
  padding: 10px 8px 0px 8px;
}
.customCard:hover {
  border: 2px solid rgb(250, 100, 130);
  transform: translateY(-5px);
}
</style>
