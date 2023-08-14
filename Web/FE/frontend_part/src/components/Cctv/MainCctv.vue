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
  display: flex;
  height: (100vh - 40px);
  flex-direction: column;
}
.CctvHeader {
  padding: 20px;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
}
.VideoContainer {
  width: 450px;
  height: 450px;
}
.SubCctvContainer {
  display: flex;
  flex-direction: column;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
  flex-grow: 1;
}
.CctvUpperContainer {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>
