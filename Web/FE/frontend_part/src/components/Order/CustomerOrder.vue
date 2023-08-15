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
            v-model="this.customer_id"
            label="아이디"
            color="warning"
            readonly></v-text-field>

          <!-- selectDiv들을 동적으로 렌더링 -->
          <div
            v-for="(select, index) in selectDivs"
            :key="index"
            class="selectDiv">
            <v-select
              :v-model="select.product"
              :items="products"
              label="상품"
              class="productDiv"
              required></v-select>
            <v-select
              v-model="order.products[index].stock"
              :items="number"
              label="갯수"
              class="numberDiv"
              required></v-select>
            <!-- - 버튼 -->
          </div>

          <v-btn v-if="this.idxCount < 3" color="primary" @click="addSelectDiv"
            >+</v-btn
          >
          <v-btn v-if="this.idxCount > 0" color="error" @click="removeSelectDiv"
            >-</v-btn
          >

          <v-select
            label="지역"
            :items="region"
            v-model="selectedRegionIndex"></v-select>

          <v-text-field
            v-model="order.detailAddress"
            label="상세주소"
            color="warning"
            required></v-text-field>

          <div class="d-flex flex-column">
            <v-btn color="success" class="mt-4" block @click="doOrder">
              주문하기
            </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
  </div>
</template>

<script>
import HeaderNav from "@/components/common/HeaderNav.vue";
import { mapState } from "vuex";
export default {
  name: "CustomerOrder",
  components: {
    HeaderNav,
  },
  data: () => ({
    order: {
      customer_id: "",
      products: [
        { product_num: 1, stock: 0 },
        { product_num: 2, stock: 0 },
        { product_num: 3, stock: 0 },
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
    products: [
      "횡성 한우 부채살",
      "두루마리 휴지 30개입",
      "생수 2L 12개",
      "진라면 1봉(5개입)",
    ],
    number: [1, 2, 3],
    selectA: "선택 안함",
    selectB: "선택 안함",
    selectC: "선택 안함",
    selectDivs: [],
    idxCount: 0,
  }),
  computed: {
    ...mapState("customer", ["customer_id"]),
  },
  methods: {
    async doOrder() {
      this.order.customer_id = this.customer_id;
      if (
        this.order.address == "" ||
        this.order.detailAddress == "" ||
        (this.order.products[0].stock == 0 &&
          this.order.products[1].stock == 0 &&
          this.order.products[2].stock == 0)
      )
        alert("주문을 확인해주세요");
      else await this.$store.dispatch("order/doOrder", this.order);
      this.reset();
    },
    addSelectDiv() {
      // 새로운 selectDiv 객체를 생성하고 selectDivs 배열에 추가
      this.selectDivs.push({ product: "선택 안함", stock: 0 });
      this.idxCount += 1;
    },
    // - 버튼을 클릭할 때 실행되는 함수
    removeSelectDiv() {
      // 해당 index의 selectDiv를 배열에서 제거
      this.selectDivs.splice(this.idxCount - 1, 1);
      this.idxCount -= 1;
    },
    reset() {
      this.order.products = [
        { product_num: 1, stock: 0 },
        { product_num: 2, stock: 0 },
        { product_num: 3, stock: 0 },
      ];
      this.order.address = "";
      this.order.detailAddress = "";
      this.selectA = "선택 안함";
      this.selectB = "선택 안함";
      this.selectC = "선택 안함";
      this.selectedRegionIndex = null;
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
  height: 120vh;
  text-align: center;
}
.OrderForm * {
  background-color: rgb(39, 40, 41);
}
.MainTurtle {
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}

v-overlay-container * {
  background-color: black;
}
.selectDiv {
  display: flex;
  flex-direction: row;
}
.productDiv {
  width: 60%;
}
.numberDiv {
  width: 35%;
}
</style>
