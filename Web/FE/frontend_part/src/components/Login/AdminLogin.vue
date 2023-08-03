<template>
  <div class="loginForm">
    <div><img class="MainTurtle" src="./MainTurtle.png"></div>
    &nbsp;
    <v-sheet width="300" class="mx-auto">
        <h2 >로그인</h2>
        <v-form fast-fail @submit.prevent>
        <v-text-field
            v-model="admin.admin_id"
            label="아이디"
            :rules="nameRules"
            required
            @keyup.enter="doAdminLogin"
        ></v-text-field>

        <v-text-field
            v-model="admin.password"
            label="비밀번호"
            type="password"
            :rules="nameRules"
            required
            @keyup.enter="doAdminLogin"
        ></v-text-field>

        <v-btn 
          block 
          class="mt-2"
          @click="doAdminLogin" 
          >
          로그인
        </v-btn>
        </v-form>
        &nbsp;
        <div class="registDiv">
          <router-link :to="{ name: 'AdminRegist' }">관리자 등록</router-link>
        </div>
    </v-sheet>
  </div>
</template>

<script>

export default {
    name: "AdminLogin",
    data: () => ({
      admin: {
        admin_id: "",
        password: "",
      },
      nameRules: [
        v => !!v || '해당 칸을 입력해주세요',
      ],
    }),
    methods:{
      doAdminLogin(){
        console.log(this.admin.admin_id);
        console.log(this.admin.password);
        
        this.$store.dispatch("admin/adminLogin", this.admin);

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