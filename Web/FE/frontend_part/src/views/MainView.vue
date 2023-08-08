<template>
  <div id="MainContainer">
    <div id="MainLogo">
      <img class="MainTurtle" :src="mainSrc" @click="EasterEgg" />
      TL <br />
      TURTLE LOGISTICS
    </div>
    &nbsp;
    <div id="CardDiv">
      <v-card class="mx-auto" max-width="344" variant="outlined">
        <v-card-item>
          <div>
            <div class="mb-1">주문 페이지</div>
            <div>터틀 로지스틱스 구매자 페이지</div>
          </div>
        </v-card-item>
        <v-card-actions>
          <v-btn variant="outlined" href="/customerLogin"> Button </v-btn>
        </v-card-actions>
      </v-card>
      &nbsp;
      <v-card class="mx-auto" max-width="344" variant="outlined">
        <v-card-item>
          <div>
            <div class="mb-1">관리자 페이지</div>
            <div>터틀 로지스틱스 관리자 페이지</div>
          </div>
        </v-card-item>

        <v-card-actions>
          <v-btn variant="outlined" href="/adminLogin"> Button </v-btn>
        </v-card-actions>
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
</style>
