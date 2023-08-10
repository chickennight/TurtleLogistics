<template>
  <div id="adminMainContainer" :style="{ height: appHeight + 'px' }">
    <sidebar-nav></sidebar-nav>
    <div class="adminSubContainer">
      <header-nav></header-nav>
      <router-view @childContentHeightChanged="updateAppHeight" />
    </div>
  </div>
</template>

<script>
import HeaderNav from "@/components/common/HeaderNav.vue";
import SidebarNav from "@/components/common/SidebarNav.vue";
// import AdminMainView from "@/components/Admin/AdminMainView.vue";
import { mapState } from "vuex";

export default {
  name: "AdminView",
  data: () => ({
    appHeight: 900,
    previousMachineLog: [],
    errorImg: "/Error_BluePrint/error_nukki.png",
    myTimer: null,
  }),
  methods: {
    updateAppHeight(childContentHeight) {
      // 자식 컴포넌트의 내용 높이에 따라 App.vue의 높이를 동적으로 변경
      if (childContentHeight > 800) {
        this.appHeight = childContentHeight + 300;
      } else {
        this.appHeight = 1200;
      }
    },

    sendMessage(machineDetail) {
      this.$store.dispatch("admin/SendSMS", machineDetail);
    },
    changeImg(machine_id) {
      this.errorImg = `/Error_BluePrint/BluePrint_${machine_id}.PNG`;
      this.$store.state.errorImg = `/Error_BluePrint/BluePrint_${machine_id}.PNG`;
    },
    async getMachineStatus() {
      if (this.machineStatus[`로그`] != null) {
        this.previousMachineLog = [...this.machineStatus[`로그`]];
      } else {
        this.previousMachineLog = [];
      }
      await this.$store.dispatch("machine/getMachineStatus");
    },
  },
  components: {
    HeaderNav,
    SidebarNav,
  },
  async mounted() {
    await this.getMachineStatus();
    this.myTimer = setInterval(async () => {
      await this.getMachineStatus(); // 매 초마다 새 데이터를 가져옵니다.
      let addedLogs;
      // previousMachineLog와 machineLog를 비교하여 새로운 로그를 찾습니다.
      if (this.machineStatus[`로그`] != null) {
        addedLogs = this.machineStatus[`로그`].filter(
          (log) => !this.previousMachineLog.some((prevLog) => prevLog.log_num === log.log_num)
        );
        this.previousMachineLog = [...this.machineStatus[`로그`]];

        // addedLogs가 비어있지 않으면, 새로운 로그가 추가되었음을 의미합니다.
        if (addedLogs.length > 0) {
          const plainAddedLogs = addedLogs.map((log) => ({ ...log }));
          // 새로운 로그에 대해 원하는 동작을 수행합니다.
          this.changeImg(plainAddedLogs[0].machine_id);
          alert(
            `${plainAddedLogs[0].machine_id} 공정에 이상이 발생했습니다. 확인 후 메뉴얼에 따라 조치해주시기 바랍니다`
          );

          switch (plainAddedLogs[0].machine_id) {
            case 1000:
              this.sendMessage(
                "[주문 컨베이어 벨트] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 1010:
              this.sendMessage(
                "[1차 피스톤] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 1020:
              this.sendMessage(
                "[2차 피스톤] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 1030:
              this.sendMessage(
                "[3차 피스톤] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 2000:
              this.sendMessage(
                "[분류 컨베이어 벨트] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 2100:
              this.sendMessage(
                "[카메라 모듈] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 2010:
              this.sendMessage(
                "[1차 가름막] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 2020:
              this.sendMessage(
                "[2차 가름막] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
            case 2030:
              this.sendMessage(
                "[3차 가름막] 오류 발생 / 에러 내용 : " + plainAddedLogs[0].error_message
              );
              break;
          }
        }
      }
    }, 1000);
  },
  computed: {
    ...mapState("machine", ["machineStatus"]),
  },
};
</script>

<style scoped>
#adminMainContainer {
  display: flex;
  width: 100%;
  height: 1500px;
}

.adminSubContainer {
  flex-direction: column;
  width: 100%;
}
</style>
