<template>
  <div class="loginForm">
    <div>
      <img class="MainTurtle" src="./MainTurtle.png" />
    </div>
    &nbsp;
    <v-sheet width="300" class="mx-auto">
      <h2>로그인</h2>
      <v-form fast-fail @submit.prevent>
        <v-text-field
          v-model="customer.customer_id"
          label="아이디"
          :rules="nameRules"
          @keyup.enter="doCustomerLogin"
        ></v-text-field>

        <v-text-field
          v-model="customer.password"
          label="비밀번호"
          type="password"
          :rules="nameRules"
          @keyup.enter="doCustomerLogin"
        ></v-text-field>

        <v-btn @click="doCustomerLogin" block class="mt-2">로그인</v-btn>
      </v-form>
    </v-sheet>
    <div class="registDiv">
      <v-btn block class="mt-2" router-link :to="{ name: 'AdminRegist' }"> 회원가입 </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  name: "CustomerLogin",
  data: () => ({
    customer: {
      customer_id: "",
      password: "",
    },
    nameRules: [(v) => !!v || "해당 칸을 입력해주세요"],
  }),
  methods: {
    doCustomerLogin() {
      this.$store.dispatch("customer/customerLogin", this.customer);
    },
  },
};
</script>

<style scoped>
.loginForm {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background-image: url("@/assets/closeConveyorBelt.svg");
  background-position: left 44% bottom 120%;
  background-size: 150%;
  background-repeat: no-repeat;
}
.loginForm * {
  background-color: rgb(39, 40, 41);
  text-align: center;
}
.registDiv {
  color: rgb(210, 210, 210) !important;
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  width: 300px;
}

.MainTurtle {
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}

.mt-2 {
  border: 3px solid rgb(21, 21, 21);
  font-size: 22px;
  font-weight: bold;
  padding: 7%;
}
</style>
