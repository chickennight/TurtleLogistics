<template>
  <div class="CctvContainer">
    <div class="CctvHeader"><h1>CCTV</h1></div>
    &nbsp;
    <div class="SubCctvContainer">
      <div class="CctvDiv">
        <h2>주문 파트</h2>
        <video
          class="VideoContainer"
          ref="videoElement"
          autoplay
          @click="showInModal($refs.videoElement)"
        ></video>
        <p v-if="!isVideoConnected" class="error-message">CCTV 연결 안됨</p>
      </div>
      <div class="CctvDiv">
        <h2>분류 파트</h2>
        <video
          class="VideoContainer"
          ref="notebookVideo"
          autoplay
          @click="showInModal($refs.notebookVideo)"
        ></video>
        <p v-if="!isNotebookConnected" class="error-message">CCTV 연결 안됨</p>
      </div>
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
      isVideoConnected: true,
      isNotebookConnected: true,
    };
  },
  computed: {
    ...mapState("admin", ["image"]),
    isVideoElementConnected() {
      return this.$refs.videoElement && this.$refs.videoElement.srcObject;
    },
    isNotebookVideoConnected() {
      return this.$refs.notebookVideo && this.$refs.notebookVideo.srcObject;
    },
  },
  mounted() {
    this.initWebcam();
    this.checkConnectionStatus();
  },
  updated() {
    this.checkConnectionStatus();
  },
  methods: {
    checkConnectionStatus() {
      this.isVideoConnected = this.$refs.videoElement && this.$refs.videoElement.srcObject;
      this.isNotebookConnected = this.$refs.notebookVideo && this.$refs.notebookVideo.srcObject;
    },
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
          this.isNotebookConnected = !!notebookStream;
        } else {
          console.error("Notebook camera not found.");
        }

        const webcamStream = await navigator.mediaDevices.getUserMedia({ video: true });
        this.$refs.videoElement.srcObject = webcamStream; // 웹캠 비디오 요소
        this.isVideoConnected = !!webcamStream;
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
  height: 390px;
  border: 3px solid rgb(250, 100, 130); /* 빨간색 테두리 추가 */
  border-radius: 5px; /* 테두리 모서리 둥글게 */
}
.SubCctvContainer {
  display: flex;
  flex-direction: row;
  justify-content: center;
  box-shadow: 0px 0px 6px -1px black;
  background-color: rgb(55, 55, 55);
  border-radius: 10px;
  flex-grow: 1;
}
.CctvDiv {
  margin: 20px;
}
h2 {
  margin-top: 10px;
  text-align: center;
}
.error-message {
  text-align: center;
}
</style>
