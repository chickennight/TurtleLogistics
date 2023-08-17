<template>
  <div class="CctvContainer">
    <video class="VideoContainer" ref="videoElement" autoplay></video>
  </div>
</template>

<script>
export default {
  name: "SampleCCTV",
  data() {
    return {
      screenshot: null,
    };
  },
  mounted() {
    this.initWebcam();
  },
  methods: {
    async initWebcam() {
      try {
        const webcamStream = await navigator.mediaDevices.getUserMedia({ video: true });
        this.$refs.videoElement.srcObject = webcamStream; // 웹캠 비디오 요소
      } catch (error) {
        console.error("Error accessing webcam:", error);
      }
    },
  },
};
</script>

<style scoped>
.CctvContainer {
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.VideoContainer {
  width: 100%;
  height: 100%;
}
</style>
