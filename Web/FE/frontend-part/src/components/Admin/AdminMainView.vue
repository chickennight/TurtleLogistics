<template>
  <div class="SampleContainer">
    <div id="UpperContainer">
      위쪽컨테이너
      <sample-graph></sample-graph>
      <span>샘플 2</span>
    </div>
    <div id="LowerContainer">
      아래컨테이너
      <sample-blue-print></sample-blue-print>
    </div>
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

<style scoped>
.SampleContainer{
  display : flex;
  flex-direction: column;
}
#UpperContainer{
  border : 1px solid black;
  height: 450px;
  margin: 20px;
}
#LowerContainer{
  border : 1px solid black;
  height: 450px;
  margin: 20px
}
</style>