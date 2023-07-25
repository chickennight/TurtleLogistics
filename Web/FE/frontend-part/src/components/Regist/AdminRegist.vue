<template>
  <div class="AdminContainer">
    <div>
      <img class="MainTurtle" src="./MainTurtle.png">
    </div>
    &nbsp;
    <div class="AdminRegistForm">
      <v-sheet width="300" class="mx-auto">
        <h2>관리자 등록</h2>
        <v-form ref="form">
          <v-text-field
            v-model="admin.admin_id"
            :rules="nameRules"
            label="아이디"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="admin.password"
            :rules="nameRules"
            label="비밀번호"
            type="password"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="admin.passwordCheck"
            :rules="nameRules"
            label="비밀번호 확인"
            type="password"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="admin.phone_number"
            :rules="nameRules"
            label="전화번호"
            required
         ></v-text-field>

          <v-text-field
            v-model="admin.adminCode"
            :rules="nameRules"
            label="관리자 코드"
            required
         ></v-text-field>

          <div class="d-flex flex-column">
            <v-btn
              color="success"
              class="mt-4"
              block
              @click="adminRegist"
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
    name: "AdminRegist",
    data: () => ({
      admin:{
        admin_id: '',
        password: '',
        passwordCheck: '',
        phone_number: '',
        adminCode: '',
      },
        nameRules: [
            v => !!v || '해당 칸을 입력해주세요',
        ],
        select: null,
    }),
    methods: {
      adminRegist(){
        console.log(this.admin.adminCode);
        if(this.admin.adminCode == "a204"){
          this.$store.dispatch("adminRegist", this.admin);
        }
        else{
          alert("관리자 코드가 일치하지 않습니다. 다시 확인해주세요.");
        }
      }
    }
}
</script>

<style scoped>
.AdminContainer{
  display : flex;
  flex-direction: column;
  height: 200vh;
  text-align: center;
}
.AdminRegistForm *{
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