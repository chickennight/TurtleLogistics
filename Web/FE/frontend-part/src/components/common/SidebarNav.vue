<template>
  <div id="sideBar">
    <div id="SideLogo">
      <div id="LogoImage">
        <img src="./SubTurtle.png" alt="" width="150px">
      </div>
      <!-- <div id="MainTL">TL</div>
      <div id="SubTL">Turtle Logistics</div> -->
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
  position: absolute;
  width: 25%;
  height: 100vmax;
  left: 1px;
  top: 0px;
  border: 9px solid #000000;
}
#SideLogo{
  width: 100%;
  height: 100%;
  border: 2px solid black;
}
#LogoImage{
  position: absolute;
  left: 1.88%;
  top: 4.63%;
  width : 100%;
  height: 100%;
}
/* #MainTL{
  position: absolute;
width: 87px;
height: 76px;
left: 173px;
top: 30px;

font-family: 'Inter';
font-style: normal;
font-weight: 400;
font-size: 65px;
line-height: 79px;

color: #665D5D;
}
#SubTL{
  position: absolute;
width: 312px;
height: 54px;
left: 173px;
top: 103px;

font-family: 'Inter';
font-style: normal;
font-weight: 400;
font-size: 35px;
line-height: 42px;

background: linear-gradient(180deg, #E64878 0%, #F08556 100%);
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;
background-clip: text;
text-fill-color: transparent;
} */
</style>