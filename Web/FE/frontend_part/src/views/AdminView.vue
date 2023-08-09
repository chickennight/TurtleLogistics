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

export default {
  name: "AdminView",
  data: () => ({
    appHeight: 900,
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
  },
  components: {
    HeaderNav,
    SidebarNav,
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
