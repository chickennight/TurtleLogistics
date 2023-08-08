<!-- Webcam.vue -->
<template>
  <div class="CctvContainer">
    <div class="CctvHeader"><h1>CCTV</h1></div>
    &nbsp;
    <div class="SubCctvContainer">
      <div class="CctvUpperContainer">
        <video class="VideoContainer" ref="videoElement" autoplay></video>
        <video class="VideoContainer" ref="notebookVideo" autoplay></video>
      </div>
      <div class="CctvLowerContainer">
        <button @click="takeScreenshot">Take Screenshot</button>
        <canvas ref="canvasElement" style="display: none"></canvas>
        <img v-if="screenshot" :src="screenshot" alt="Screenshot" />
      </div>
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
        const devices = await navigator.mediaDevices.enumerateDevices();
        const notebookCamera = devices.find(
          (device) => device.kind === "videoinput" && device.label.includes("Web Camera")
        );

        console.log(devices);

        if (notebookCamera) {
          const notebookStream = await navigator.mediaDevices.getUserMedia({
            video: { deviceId: notebookCamera.deviceId },
          });
          this.$refs.notebookVideo.srcObject = notebookStream; // 노트북 카메라 비디오 요소
        } else {
          console.error("Notebook camera not found.");
        }

        const webcamStream = await navigator.mediaDevices.getUserMedia({ video: true });
        this.$refs.videoElement.srcObject = webcamStream; // 웹캠 비디오 요소
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

<style scoped>
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
  width: 450px;
}
.SubCctvContainer {
  box-shadow: 2px 2px 3px 3px black;
  display: flex;
  flex-direction: column;
}
</style>
