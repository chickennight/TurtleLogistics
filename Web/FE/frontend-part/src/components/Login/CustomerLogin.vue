<template>
    <div class="loginForm">
    <div><img class="MainTurtle" src="./MainTurtle.png"></div>
    &nbsp;
    <v-sheet width="300" class="mx-auto">
        <h2 >로그인</h2>
        <v-form fast-fail @submit.prevent>
        <v-text-field
            v-model="customer.customer_id"
            label="아이디"
            :rules="firstNameRules"
        ></v-text-field>

        <v-text-field
            v-model="customer.password"
            label="비밀번호"
            type="password"
            :rules="lastNameRules"
        ></v-text-field>

        <v-btn @click="doCustomerLogin" block class="mt-2">로그인</v-btn>
        </v-form>
        &nbsp;
        <div class="registDiv"><router-link :to="{name: 'CustomerRegist' }">회원가입</router-link></div>
    </v-sheet>
  </div>
</template>

<script>
export default {
  name: "CustomerLogin",
    data: () => ({
        customer:{
          customer_id: '',
          password: '',
        },
      firstNameRules: [
        value => {
          if (value?.length > 3) return true

          return 'First name must be at least 3 characters.'
        },
      ],
      lastNameRules: [
        value => {
          if (/[^0-9]/.test(value)) return true

          return 'Last name can not contain digits.'
        },
      ],
    }),
    methods:{
      doCustomerLogin(){
        console.log(this.customer);
        this.$store.dispatch("customerLogin", this.customer);
      }
    }
}
</script>

<style scoped>
.loginForm{
  width: 100%;
  height: 1000px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  
}
.loginForm *{
  color: white;
  background-color: rgb(53, 53, 53);
  text-align: center;
}
.registDiv{
  text-align: center;
  text-decoration: none;
}
.MainTurtle{
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}
</style>