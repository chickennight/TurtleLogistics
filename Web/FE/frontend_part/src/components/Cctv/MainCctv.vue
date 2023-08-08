<!-- Webcam.vue -->
<template>
  <div class="CctvContainer">
    <div class="CctvHeader"><h1>CCTV</h1></div>
    &nbsp;
    <div class="SubCctvContainer">
      <video class="VideoContainer" ref="videoElement" autoplay></video>
      <button @click="takeScreenshot">Take Screenshot</button>
      <canvas ref="canvasElement" style="display: none"></canvas>
      <img v-if="screenshot" :src="screenshot" alt="Screenshot" />
    </div>
  </div>
</template>

<script>
export default {
  name: "MainCctv",
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
        const stream = await navigator.mediaDevices.getUserMedia({ video: true });
        this.$refs.videoElement.srcObject = stream;
      } catch (error) {
        console.error("Error accessing webcam:", error);
      }
    },
    takeScreenshot() {
      const videoElement = this.$refs.videoElement;
      const canvasElement = this.$refs.canvasElement;

      // Set canvas dimensions to match the video dimensions
      canvasElement.width = videoElement.videoWidth;
      canvasElement.height = videoElement.videoHeight;

      // Draw the video frame onto the canvas
      const ctx = canvasElement.getContext("2d");
      ctx.drawImage(videoElement, 0, 0, videoElement.videoWidth, videoElement.videoHeight);

      // Get the data URL of the canvas content (base64 encoded image)
      const dataURL = canvasElement.toDataURL("image/png");

      // Set the screenshot in the data property to display it on the page
      this.screenshot = dataURL;
      console.log(this.screenshot);
    },
  },
};
</script>

<style>
.CctvContainer {
  margin: 20px;
  padding: 20px;
  display: flex;
  flex-direction: column;
}
.CctvHeader {
  padding: 20px;
  box-shadow: 2px 2px 3px 3px black;
}
.VideoContainer {
}
.SubCctvContainer {
  box-shadow: 2px 2px 3px 3px black;
  display: flex;
  flex-direction: column;
  width: 500px;
}
</style>
