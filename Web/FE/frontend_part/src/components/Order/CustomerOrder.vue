<template>
  <div class="CustomerContainer">
    <div>
      <img class="MainTurtle" src="./MainTurtle.png" />
    </div>
    &nbsp;
    <div class="OrderForm">
      <v-sheet width="300" class="mx-auto">
        <h2>주문</h2>
        <v-form ref="form">
          <v-text-field
            v-model="order.customer_num"
            :rules="nameRules"
            label="수신자 이름"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="order.products[0].stock"
            :rules="nameRules"
            label="상품 A"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="order.products[1].stock"
            :rules="nameRules"
            label="상품 B"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="order.products[2].stock"
            :rules="nameRules"
            label="상품 C"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="order.address"
            :rules="nameRules"
            label="지역"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="order.detailAddress"
            :rules="nameRules"
            label="상세주소"
            color="warning"
            required
          ></v-text-field>

          <div class="d-flex flex-column">
            <v-btn color="success" class="mt-4" block @click="doOrder"> 주문하기 </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
  </div>
</template>

<script>
export default {
  name: "CustomerOrder",
  data: () => ({
    order: {
      customer_num: "",
      products: [
        { product_num: 1, stock: "" },
        { product_num: 2, stock: "" },
        { product_num: 3, stock: "" },
      ],
      address: "",
      detailAddress: "",
    },
  }),
  created() {},
  methods: {
    doOrder() {
      this.$store.dispatch("order/doOrder", this.order);
    },
  },
};
</script>

<style scoped>
.CustomerContainer {
  display: flex;
  flex-direction: column;
  height: 200vh;
  text-align: center;
}
.OrderForm * {
  background-color: rgb(53, 53, 53);
}
.MainTurtle {
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}
.test * {
  background-color: red !important;
}
</style>
