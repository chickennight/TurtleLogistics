<template>
    <div class="CustomerContainer">
    <div>
      <img class="MainTurtle" src="./MainTurtle.png">
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

          <v-text-field
            v-model="customer.address"
            :rules="nameRules"
            label="지역"
            required
         ></v-text-field>

          <div class="d-flex flex-column">
            <v-btn
              color="success"
              class="mt-4"
              block
              @click="customerRegist"
            >
              가입하기
            </v-btn>
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
      customer:{
        customer_id: '',
        password: '',
        passwordCheck: '',
        phone_number: '',
        address: '',
      },
        nameRules: [
            v => !!v || '해당 칸을 입력해주세요',
        ],
        select: null,
    }),
    methods: {
      customerRegist(){
        console.log(this.customer);
        this.$store.dispatch("customer/customerRegist", this.customer);
      }
    }
}
</script>

<style scoped>
.CustomerContainer{
  display : flex;
  flex-direction: column;
  height: 200vh;
  text-align: center;
}
.CustomerRegistForm *{
  background-color: rgb(53, 53, 53);
}
.MainTurtle{
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}

</style>