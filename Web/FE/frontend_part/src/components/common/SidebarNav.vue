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
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'house-chimney']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            메인화면
          </div>
        </v-btn>
      </router-link>
      <router-link :to="{ name: 'OrderByDate' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'calendar-check']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            기간주문내역
          </div>
        </v-btn>
      </router-link>
      <router-link :to="{ name: 'OrderByRegion' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'map-location-dot']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            지역주문내역
          </div></v-btn
        >
      </router-link>
      <router-link :to="{ name: 'MainGraph' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'clipboard']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            주문현황
          </div></v-btn
        >
      </router-link>
      <router-link :to="{ name: 'MainBluePrint' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'industry']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            공정현황
          </div></v-btn
        >
      </router-link>
      <router-link :to="{ name: 'MainLogistics' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'list-check']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            물류현황
          </div></v-btn
        >
      </router-link>
      <router-link :to="{ name: 'MainMachine' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'hand-pointer']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            기기제어
          </div></v-btn
        >
      </router-link>
      <router-link :to="{ name: 'MainCctv' }" class="SideMenu">
        <v-btn elevation="8" block variant="outlined">
          <div class="btn-inner-content">
            <font-awesome-icon
              :icon="['fas', 'record-vinyl']"
              style="color: #d2d2d2"
              class="icon-padding-right"
            />
            CCTV
          </div></v-btn
        >
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
  height: 100%;
  /* box-shadow: 1px 1px 3px 3px white; */
  /* border: 1px solid white; */
}
#SideLogo {
  margin-left: 13px;
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
  margin-left: 13px;
  display: flex;
  flex-direction: column;
  position: -webkit-sticky;
  position: sticky;
  align-self: flex-end;
  top: 30%;
}
.SideMenu {
  padding: 5px;
  height: 65px;
}
.v-btn {
  background-color: rgb(55, 55, 55);
  border-style: none;
  height: 100%; /* 버튼의 높이를 부모 요소의 높이와 동일하게 설정 */
}
.icon-padding-right {
  padding-right: 7px;
}
.btn-inner-content {
  margin-top: 3%;
  display: flex;
  width: 100%;
}
</style>
