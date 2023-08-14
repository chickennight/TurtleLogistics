<template>
  <div v-if="isVisible" class="modal-overlay">
    <div class="modal">
      <h2>{{ title }}</h2>
      <p v-html="message"></p>
      <button class="modal-close-button" @click="close">확인</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isVisible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: "Alert",
    },
    message: {
      type: String,
      default: "",
    },
  },
  methods: {
    close() {
      this.$emit("close");
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
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal {
  background: rgb(39, 40, 41);
  border: 2px solid rgb(250, 100, 130);
  padding: 20px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  width: 500px;
  height: 200px;
  max-width: 70vh;
  z-index: 1001;
}

.modal-content {
  background: rgb(39, 40, 41);
  padding: 20px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.modal-close-button {
  background-color: rgb(55, 55, 55);
  border: none;
  color: #d2d2d2;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  align-self: center;
  transition: 0.3s ease;
}

.modal-close-button:hover {
  background-color: rgba(85, 85, 85, 0.7);
}
</style>
