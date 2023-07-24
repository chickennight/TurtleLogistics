<template>
  <div id="sideBar">
    <div id="SideLogo">
      <div id="LogoImage">
        <img class="SubTurtle" src="./SubTurtle.png">
      </div>
      <div id="MainTL">
        TL <br>
        Turtle Logistics
      </div>
    </div>
    <div class="SideMenuBar">
      <v-btn class="SideMenu" variant="outlined">주문내역</v-btn>
      <v-btn class="SideMenu" variant="outlined">주문현황</v-btn>
      <v-btn class="SideMenu" variant="outlined">공정현황</v-btn>
      <v-btn class="SideMenu" variant="outlined">물류현황</v-btn>
      <v-btn class="SideMenu" variant="outlined">기기제어</v-btn>
      <v-btn class="SideMenu" variant="outlined">6</v-btn>
    </div>
  </div>
</template>

<script>
export default {
    name: "SidebarNav",
}
</script>

<style>
#sideBar{
  width: 300px;
  box-shadow: 5px 5px 3px 3px black;
}
#SideLogo{
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.SubTurtle{
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}
#MainTL{
  margin-left: 45px;
  background: linear-gradient(to Left Bottom, #E64878 30%, #F08556 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.SideMenuBar{
  margin-top: 50px;
  display: flex;
  flex-direction: column;
}
.SideMenu{
  margin: 5px;
  height: 50px;
}
</style>