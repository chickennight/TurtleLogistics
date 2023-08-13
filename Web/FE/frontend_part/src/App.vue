<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "App",

  data: () => ({}),
};
</script>

<style>
@font-face {
  font-family: "GmarketSansTTFMedium";
  src: url("../public/fonts/GmarketSansTTFMedium.ttf");
}
@font-face {
  font-family: "GmarketSansTTFBold";
  src: url("../public/fonts/GmarketSansTTFBold.ttf");
}
@font-face {
  font-family: "GmarketSansTTFLight";
  src: url("../public/fonts/GmarketSansTTFLight.ttf");
}
*:not(.v-overlay-container *) {
  font-family: "GmarketSansTTFMedium";
  text-decoration: none;

  color: rgb(210, 210, 210);
  /* color: black; */
}

#app {
  height: 100%;
  width: 100%;
  background-color: rgb(39, 40, 41);
  /* background-color: rgb(255, 255, 255); */
}

body,
html {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  background-color: rgb(39, 40, 41);
}
</style>
