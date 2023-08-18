<template>
  <div v-if="show" class="modal-overlay">
    <div class="modal-content">
      <video class="modal-video" ref="modalVideo" autoplay muted playsinline></video>
      <button class="modal-close-button" @click="$emit('close')">닫기</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    show: Boolean,
    videoStream: Object,
  },
  watch: {
    videoStream(newStream) {
      this.$nextTick(() => {
        if (this.$refs.modalVideo) {
          this.$refs.modalVideo.srcObject = newStream;
          this.$refs.modalVideo.play();
        }
      });
    },
  },
  mounted() {
    window.addEventListener("keydown", this.handleKeyDown);
  },
  beforeUnmount() {
    window.removeEventListener("keydown", this.handleKeyDown);
  },
  methods: {
    handleKeyDown(event) {
      if (event.key === "Escape" || event.key === "Enter") {
        this.$emit("close");
      }
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 200vh;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  width: 125vh;
  background: rgb(39, 40, 41);
  padding: 20px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 1%;
}
.modal-video {
  width: 100%;
  border-radius: 8px;
  background-color: black;
}
.modal-close-button {
  margin-top: 5px;
  background-color: rgb(55, 55, 55);
  border: none;
  color: #d2d2d2;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: 0.3s ease;
}

.modal-close-button:hover {
  background-color: rgba(85, 85, 85, 0.7);
}
</style>
