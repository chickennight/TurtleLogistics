<template>
  <div class="SampleContainer">
    공정현황!!!!(단면도너낌)
      <v-btn
      @click="getMachineStatus"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        공정현황 버튼
      </v-btn>
  </div>
</template>

<script>
export default {
    name: "MainBluePrint",
    methods: {
      getMachineStatus(){
        this.$store.dispatch("getMachineStatus");
      }
    }
}
</script>

<style>
.SampleContainer{
  margin: 20px;
  border: 1px solid white;
}
</style>