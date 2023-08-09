<template>
  <div class="CustomerContainer">
    <header-nav></header-nav>
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

          <v-select label="지역" :items="region" v-model="selectedRegionIndex"></v-select>

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
import HeaderNav from "@/components/common/HeaderNav.vue";
export default {
  name: "CustomerOrder",
  components: {
    HeaderNav,
  },
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
    region: [
      "서울특별시",
      "부산광역시",
      "경기도",
      "대구광역시",
      "인천광역시",
      "광주광역시",
      "대전광역시",
      "울산광역시",
      "세종특별자치시",
      "강원도",
      "충청북도",
      "충청남도",
      "전라북도",
      "전라남도",
      "경상북도",
      "경상남도",
      "제주특별자치도",
    ],
    selectedRegionIndex: null,
  }),
  created() {},
  methods: {
    doOrder() {
      console.log(this.order);
      this.$store.dispatch("order/doOrder", this.order);
    },
  },
  watch: {
    selectedRegionIndex(newIndex) {
      // 선택한 지역의 인덱스 값이 변경될 때마다 실행되는 함수
      if (newIndex !== null) {
        // newIndex가 null이 아니라면 (지역이 선택된 경우)
        this.order.address = this.region.indexOf(newIndex) + 1; // 선택한 지역의 값을 order.address에 할당
      } else {
        this.order.address = null; // 선택이 해제된 경우 order.address를 null로 설정
      }
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
v-overlay-container * {
  background-color: black;
}
</style>
