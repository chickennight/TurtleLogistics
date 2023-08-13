<template>
  <div v-if="isOpen" class="modal-overlay" @click="close">
    <div class="modal" @click.stop>
      <slot></slot>
      <button @click="close">확인</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isOpen: {
      type: Boolean,
      required: true,
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
  background: rgb(20, 20, 20);
  padding: 20px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: auto;
  max-width: 70vh;
  z-index: 1001;
}

.modal-content {
  background: black;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.modal-close-button {
  margin-top: 10px;
}
</style>
