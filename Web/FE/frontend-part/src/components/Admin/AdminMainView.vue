<template>
  <div id="UpperContainer">
    <sample-graph></sample-graph>
    <span>샘플 2</span>
  </div>
  <div id="LowerContainer">
    <sample-blue-print></sample-blue-print>
  </div>
</template>

<script>
import SampleGraph from "../graph/SampleGraph.vue";
import SampleBluePrint from "../BluePrint/SampleBluePrint.vue";


export default {
  name: "AdminMainView",
  components: { 
    SampleBluePrint, 
    SampleGraph 
    },
}
</script>

<style>
#adminMainContainer{
    position: absolute;
    width: 76%;
    height: 900px;
    left: 25%;
    top: 109px;
    border: 9px solid #000000;
}
</style>