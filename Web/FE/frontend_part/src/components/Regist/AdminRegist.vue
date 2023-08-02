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
            :rules="id_rules"
            label="아이디"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="admin.password"
            :rules="pw_rules"
            label="비밀번호"
            type="password"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="admin.passwordCheck"
            :rules="pwcheck_rules"
            :hint="checkMsg"
            @keyup="CheckPwd"
            label="비밀번호 확인"
            type="password"
            color="warning"
            required
          ></v-text-field>
          

          <v-text-field
            v-model="admin.phone_number"
            :rules="phone_rules"
            label="전화번호"
            required
         ></v-text-field>

          <v-text-field
            v-model="admin.adminCode"
            :rules="code_rules"
            type="password"
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
      id_rules: [
          v => !!v || '해당 칸을 입력해주세요',
          v => !(v && v.length <= 7) || '아이디를 8자 이상으로 설정해주세요.',
      ],
      pw_rules: [
          v => !!v || '해당 칸을 입력해주세요',
          v => !(v && v.length <= 7) || '비밀번호를 8자 이상으로 설정해주세요.',
          v => /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/.test(v) || '비밀번호를 영어, 숫자, 특수문자의 조합으로 설정해주세요.',
      ],
      pwcheck_rules: [
          v => !!v || '해당 칸을 입력해주세요',
      ],
      phone_rules: [
          v => !!v || '해당 칸을 입력해주세요',
          v => !(v && v.length != 11) || '전화번호가 적합하지 않습니다.',
      ],
      code_rules: [
          v => !!v || '해당 칸을 입력해주세요',
          v => (v == 'a204') || '관리자 코드가 일치하지 않습니다.',
      ],
      select: null,
      checkMsg : "동일한 비밀번호를 입력해주세요."
    }),
    methods: {
      adminRegist(){

        if(this.admin.adminCode == "a204"){
          this.$store.dispatch("admin/adminRegist", this.admin);
        }
        else{
          alert("관리자 코드가 일치하지 않습니다. 다시 확인해주세요.");
        }
      },
      CheckPwd() {
        if (this.admin.passwordCheck === "") {
          this.checkMsg = "비밀번호를 확인해주세요.";
        } else if (this.admin.passwordCheck == this.admin.password) {
          this.checkMsg = "비밀번호가 일치합니다.";
        } else {
          this.checkMsg = "비밀번호가 일치하지 않습니다.";
        }
      },
    },
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