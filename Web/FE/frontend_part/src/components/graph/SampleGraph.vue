<template>
  <div>
    <Line :data="chartData" style="color:white" />
  </div>
</template>

<script>
import { Line } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale,PointElement,
  LineElement  } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, PointElement,
  LineElement)

export default {
    name: "SampleGraph",
        components:{
      Line
    },
    data: () => ({

      chartData: {
        labels: [ 'January', 'February', 'March'],
        datasets: [
          {
            label: 'Data One',
            borderColor: 'white',
            backgroundColor: 'white',
            data: [40, 20, 12]
          }
        ]
      }

    }),
}
</script>

<style>

</style>