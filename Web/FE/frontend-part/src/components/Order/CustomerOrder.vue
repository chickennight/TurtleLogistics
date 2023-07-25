<template>
  <div class="CustomerContainer">
    <div>
      <img class="MainTurtle" src="./MainTurtle.png">
    </div>
    &nbsp;
    <div class="OrderForm">
      <v-sheet width="300" class="mx-auto">
        <h2>주문</h2>
        <v-form ref="form">
          <v-text-field
            v-model="name"
            :rules="nameRules"
            label="수신자 이름"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="productA"
            :rules="nameRules"
            label="상품 A"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="productB"
            :rules="nameRules"
            label="상품 B"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="productC"
            :rules="nameRules"
            label="상품 C"
            color="warning"
            required
          ></v-text-field>

          <v-text-field
            v-model="region"
            :rules="nameRules"
            label="지역"
            color="warning"
            required
          ></v-text-field>

          <div class="d-flex flex-column">
            <v-btn
              color="success"
              class="mt-4"
              block
              @click="validate"
            >
              주문하기
            </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
</div>
</template>

<script>
export default {
    name : 'CustomerOrder',
}
</script>

<style scoped>
.CustomerContainer{
  display : flex;
  flex-direction: column;
  height: 200vh;
  text-align: center;
}
.OrderForm *{
  background-color: rgb(53, 53, 53);
}
.MainTurtle{
  height: 150px;
  width: 150px;
  object-fit: contain;
  margin: auto;
  display: block;
}
.test *{
  background-color: red !important;
}
</style>