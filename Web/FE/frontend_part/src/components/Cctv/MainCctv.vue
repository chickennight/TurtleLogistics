<!-- Webcam.vue -->
<template>
  <div class="CctvContainer">
    <div class="CctvHeader"><h1>CCTV</h1></div>
    &nbsp;
    <div class="SubCctvContainer">
      <div class="CctvUpperContainer">
        <video
          class="VideoContainer"
          ref="videoElement"
          autoplay
          @click="showInModal($refs.videoElement)"
        ></video>
        <video
          class="VideoContainer"
          ref="notebookVideo"
          autoplay
          @click="showInModal($refs.notebookVideo)"
        ></video>
      </div>
      <!-- 모달추가 -->
      <cctv-modal
        :show="isModalVisible"
        :videoStream="selectedVideoStream"
        @close="isModalVisible = false"
      ></cctv-modal>
      <div class="CctvLowerContainer">
        <button @click="takeScreenshot">Take Screenshot</button>
        <canvas ref="canvasElement" style="display: none"></canvas>
        <img v-if="screenshot" :src="screenshot" alt="Screenshot" />
      </div>
      <div class="imageGetContainer">
        <button @click="getImage">getImage</button>
        <img v-if="image" :src="image" alt="Image" />
      </div>
    </div>
  </div>
</template>

<script>
import CctvModal from "../Modals/CCTVModal.vue";
import { mapState } from "vuex";
export default {
  name: "MainCctv",
  components: {
    CctvModal,
  },
  data() {
    return {
      screenshot: null,
      isModalVisible: false, // 모달 보이기/숨기기 상태
      selectedVideoStream: null, // 모달에 표시될 비디오 스트림
    };
  },
  computed: {
    ...mapState("admin", ["image"]),
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

      // Convert the dataURL to an image File
      const fetchImage = async (url) => {
        const response = await fetch(url);
        const blob = await response.blob();
        return new File([blob], "screenshot.png", { type: "image/png" });
      };

      // Set the screenshot in the data property to display it on the page
      this.screenshot = dataURL;

      fetchImage(dataURL).then((imageFile) => {
        this.$store.dispatch("admin/takeScreenshot", { image: imageFile, log_num: 2 });
      });
    },
    async getImage() {
      try {
        await this.$store.dispatch("admin/getImage", 2);
      } catch (error) {
        console.error(error);
      }
    },
    showInModal(videoElement) {
      const originalStream = videoElement.srcObject;
      // srcObject의 값이 null인지 확인 => 화면 안나오는 곳
      if (!originalStream) {
        alert("CCTV 고장");
        return;
      }
      const clonedStream = originalStream.clone();
      this.selectedVideoStream = clonedStream;
      this.isModalVisible = true;
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
