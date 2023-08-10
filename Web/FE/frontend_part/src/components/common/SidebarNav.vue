<template>
  <div id="sideBar">
    <div id="SideLogo">
      <div id="LogoImage">
        <img class="SubTurtle" :src="subSrc" @click="EasterEgg" />
      </div>
      <div id="MainTL">
        <router-link to="/">
          TL <br />
          Turtle Logistics
        </router-link>
      </div>
    </div>
    <div class="SideMenuBar">
      <router-link :to="{ name: 'AdminMainView' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">메인화면</v-btn>
      </router-link>
      <router-link :to="{ name: 'OrderByDate' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">기간주문내역</v-btn>
      </router-link>
      <router-link :to="{ name: 'OrderByRegion' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">지역주문내역</v-btn>
      </router-link>
      <router-link :to="{ name: 'MainGraph' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">주문현황</v-btn>
      </router-link>
      <router-link :to="{ name: 'MainBluePrint' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">공정현황</v-btn>
      </router-link>
      <router-link :to="{ name: 'MainLogistics' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">물류현황</v-btn>
      </router-link>
      <router-link :to="{ name: 'MainMachine' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">기기제어</v-btn>
      </router-link>
      <router-link :to="{ name: 'MainCctv' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">CCTV</v-btn>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "SidebarNav",
  data: () => ({
    subSrc: "/SubTurtle.png",
  }),
  methods: {
    EasterEgg() {
      if (this.subSrc == "/SubTurtle.png") {
        this.subSrc = "/SubTurtle_Easter.png";
      } else {
        this.subSrc = "/SubTurtle.png";
      }
    },
  },
};
</script>

<style scoped>
#sideBar {
  width: 300px;
  /* box-shadow: 1px 1px 3px 3px white; */
  /* border: 1px solid white; */
}
#SideLogo {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
}
.SubTurtle {
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}
#MainTL {
  background: linear-gradient(to Left Bottom, #e64878 30%, #f08556 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
#MainTL * {
  font-family: "GmarketSansTTFBold";
  font-size: 20px;
}
.SideMenuBar {
  margin-top: 30px;
  display: flex;
  flex-direction: column;
}
.SideMenu {
  padding: 5px;
  height: 50px;
}
.v-btn {
  background-color: rgb(55, 55, 55);
  border-style: none;
}
</style>
