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

  color: white;
  /* color: black; */
}

#app {
  background-color: rgb(53, 53, 53);
  /* background-color: rgb(255, 255, 255); */
}
</style>
