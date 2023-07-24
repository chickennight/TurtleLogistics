<template>
    <div class="loginForm">
    <div>로고</div>
    <v-sheet width="300" class="mx-auto">
        <v-form fast-fail @submit.prevent>
        <v-text-field
            v-model="customerId"
            label="아이디"
            :rules="firstNameRules"
        ></v-text-field>

        <v-text-field
            v-model="customerPassword"
            label="비밀번호"
            type="password"
            :rules="lastNameRules"
        ></v-text-field>

        <v-btn href="/customer" block class="mt-2">로그인</v-btn>
        </v-form>
        &nbsp;
        <div class="registDiv">회원가입</div>
    </v-sheet>
  </div>
</template>

<script>
export default {
  name: "CustomerLogin",
      data: () => ({
      customerId: '',
      firstNameRules: [
        value => {
          if (value?.length > 3) return true

          return 'First name must be at least 3 characters.'
        },
      ],
      customerPassword: '',
      lastNameRules: [
        value => {
          if (/[^0-9]/.test(value)) return true

          return 'Last name can not contain digits.'
        },
      ],
    }),
    methods:{
      doAdminLogin(){
        
      }
    }
}
</script>

<style>

</style>