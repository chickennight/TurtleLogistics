<!-- Webcam.vue -->
<template>
  <div>
    <video ref="videoElement" autoplay></video>
    <button @click="takeScreenshot">Take Screenshot</button>
    <canvas ref="canvasElement" style="display: none"></canvas>
    <img v-if="screenshot" :src="screenshot" alt="Screenshot" />
  </div>
</template>

<script>
export default {
  name: "CamTest",
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
/* Add some CSS styling for the video element if needed */
</style>
