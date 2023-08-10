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
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: black;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.modal-video {
  width: 100%;
  height: auto;
  background-color: black;
}

.modal-close-button {
  margin-top: 10px;
}
</style>
