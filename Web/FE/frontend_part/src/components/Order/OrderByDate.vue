<template>
  <div class="OrderDateContainer">
    <h1>기간별 조회</h1>
    <div id="ButtonContainer">
      <v-btn
      @click="getOrderDataWeek"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        일주일
      </v-btn>

      <v-btn @click="getOrderDataMonth"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        1개월
      </v-btn>

      <v-btn @click="getOrderData3Month"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        3개월
      </v-btn>

      <v-btn @click="getOrderData6Month"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        6개월
      </v-btn>

      <v-btn @click="getOrderDataYear"
      background-color='rgb(53, 53, 53)'
      variant="outlined">
        1년
      </v-btn>
    </div>
    <div class="OrderGraphContainer">
      
    <hr><hr>
    <Line :data="chartData"/>
    <Bar :data="chartData" />
    </div>
  </div>
</template>


<script>
import { Bar, Line } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale,PointElement,
  LineElement  } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, PointElement,
  LineElement)

export default {
    name: "OrderByDate",
    components:{
      Bar,
      Line
    },
    data: () => ({

      chartData: {
        labels: [ '0721', '0722', '0723', '0724', '0725', '0726', '0727'],
        datasets: [
          {
            label: '주문건수',
            backgroundColor: 'salmon',
            borderColor: 'salmon',
            color: 'red',
            data: [40, 20, 12, 25, 38, 50, 15]
          }
        ]
      }

    }),
    created(){
      const offset = new Date().getTimezoneOffset() * 60000;
      const today = new Date(Date.now() - offset);
      const end_day = today.toISOString();
      const week = new Date(Date.now() - (7 * 24 * 60 * 60 * 1000));
      const start_day = week.toISOString();

      const date = {
        end : end_day,
        start : start_day,
      }

      this.$store.dispatch("getOrderData", date);

    },
    methods:{
      getOrderDataWeek(){
          const offset = new Date().getTimezoneOffset() * 60000;
          const today = new Date(Date.now() - offset);
          const end_day = today.toISOString();
          const week = new Date(Date.now() - (7 * 24 * 60 * 60 * 1000));
          const start_day = week.toISOString();

          const date = {
            end : end_day,
            start : start_day,
          }

          this.$store.dispatch("getOrderData", date);
      },
      getOrderDataMonth(){
          const offset = new Date().getTimezoneOffset() * 60000;
          const today = new Date(Date.now() - offset);
          const end_day = today.toISOString();
          const month = new Date(today.setMonth(today.getMonth() - 1));
          const start_day = month.toISOString();

          console.log(end_day);
          console.log(start_day);

          const date = {
            end : end_day,
            start : start_day,
          }

          this.$store.dispatch("getOrderData", date);
      },
      getOrderData3Month(){
          const offset = new Date().getTimezoneOffset() * 60000;
          const today = new Date(Date.now() - offset);
          const end_day = today.toISOString();
          const month = new Date(today.setMonth(today.getMonth() - 3));
          const start_day = month.toISOString();

          console.log(end_day);
          console.log(start_day);

          const date = {
            end : end_day,
            start : start_day,
          }

          this.$store.dispatch("getOrderData", date);
      },
      getOrderData6Month(){
          const offset = new Date().getTimezoneOffset() * 60000;
          const today = new Date(Date.now() - offset);
          const end_day = today.toISOString();
          const month = new Date(today.setMonth(today.getMonth() - 6));
          const start_day = month.toISOString();

          console.log(end_day);
          console.log(start_day);

          const date = {
            end : end_day,
            start : start_day,
          }

          this.$store.dispatch("getOrderData", date);
      },
      getOrderDataYear(){
          const offset = new Date().getTimezoneOffset() * 60000;
          const today = new Date(Date.now() - offset);
          const end_day = today.toISOString();
          const month = new Date(today.setFullYear(today.getFullYear() - 1));
          const start_day = month.toISOString();

          console.log(end_day);
          console.log(start_day);

          const date = {
            end : end_day,
            start : start_day,
          }

          this.$store.dispatch("getOrderData", date);
      }


    }


}
</script>

<style>
.OrderDateContainer{
  margin: 20px;
}
.OrderGraphContainer{
  margin: 20px;
}
.graph{
  background-color: white;
  border: 2px solid #222;
  width: 300px;
  height: 300px;
}
</style>