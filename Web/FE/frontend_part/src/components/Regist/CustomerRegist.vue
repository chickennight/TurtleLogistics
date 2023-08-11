<template>
  <div class="CustomerContainer">
    <div>
      <img class="MainTurtle" src="./MainTurtle.png" />
    </div>
    &nbsp;
    <div class="CustomerRegistForm">
      <v-sheet width="300" class="mx-auto">
        <h2>회원가입</h2>
        <v-form ref="form">
          <v-text-field
            v-model="customer.customer_id"
            :rules="nameRules"
            label="아이디"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="customer.password"
            :rules="nameRules"
            label="비밀번호"
            type="password"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="customer.passwordCheck"
            :rules="nameRules"
            label="비밀번호 확인"
            type="password"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="customer.phone_number"
            :rules="nameRules"
            label="전화번호"
            required
          ></v-text-field>

          <v-select label="지역" :items="region" v-model="selectedRegionIndex"></v-select>

          <div class="d-flex flex-column">
            <v-btn color="success" class="mt-4" block @click="customerRegist"> 가입하기 </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
  </div>
</template>

<script>
export default {
  name: "CustomerRegist",
  data: () => ({
    customer: {
      customer_id: "",
      password: "",
      passwordCheck: "",
      phone_number: "",
      address: "",
    },
    nameRules: [(v) => !!v || "해당 칸을 입력해주세요"],
    select: null,
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
  methods: {
    customerRegist() {
      this.$store.dispatch("customer/customerRegist", this.customer);
    },
  },
  watch: {
    selectedRegionIndex(newIndex) {
      // 선택한 지역의 인덱스 값이 변경될 때마다 실행되는 함수
      if (newIndex !== null) {
        // newIndex가 null이 아니라면 (지역이 선택된 경우)
        this.customer.address = this.region.indexOf(newIndex) + 1; // 선택한 지역의 값을 order.address에 할당
      } else {
        this.customer.address = null; // 선택이 해제된 경우 order.address를 null로 설정
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
.CustomerRegistForm * {
  background-color: rgb(39, 40, 41);
}
.MainTurtle {
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}
</style>
