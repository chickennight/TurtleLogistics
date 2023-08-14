<template>
  <div class="clock">
    {{ currentTime }}
  </div>
</template>

<script>
export default {
  name: "HeaderClock",
  data() {
    return {
      currentTime: this.getCurrentTime(),
    };
  },
  methods: {
    getCurrentTime() {
      const now = new Date();
      return now.toLocaleTimeString();
    },
    updateClock() {
      this.currentTime = this.getCurrentTime();
    },
  },
  mounted() {
    // 1초마다 updateClock 메서드를 호출하여 시간을 업데이트
    this.timer = setInterval(this.updateClock, 1000);
  },
  beforeUnmount() {
    // 컴포넌트 파기 전 타이머를 해제
    clearInterval(this.timer);
  },
};
</script>

<style scoped>
.clock {
  font-size: 18px;
  margin-top: 20px;
}
</style>
