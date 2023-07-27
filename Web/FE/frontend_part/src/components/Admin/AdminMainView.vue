<template>
  <div class="SampleContainer">
    <div id="UpperContainer">
      <sample-graph class="GraphContainer" />
      <div class="SampleSecond">샘플 2</div>
    </div>
    <div id="LowerContainer">
      <div class="SampleThird"></div>
      <sample-blue-print class="BluepringContainer" />
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
  display: flex;
}
#LowerContainer{
  border : 1px solid black;
  height: 450px;
  margin: 20px;
  display: flex;
}
.GraphContainer{
  width: 60%;
  border: 1px solid white;
}
.SampleSecond{
  border : 1px solid white;
  width: 35%;
  margin-left: 30px;
}
.SampleThird{
  border : 1px solid white;
  width: 35%;
}
.BluepringContainer{
  width: 60%;
  border: 1px solid white;
  margin-left: 30px;
}
</style>