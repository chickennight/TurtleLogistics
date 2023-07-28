<template>
  <div class="MachineContainer">
    기기제어
      <v-btn
      @click="getMachineLog"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        로그 가져오는 버튼
      </v-btn>
  </div>
</template>

<script>
export default {
    name: "MainMachine",
    methods:{
      getMachineLog(){
        this.$store.dispatch("getMachineLog");
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