<template>
  <div id="adminMainContainer">
  <sidebar-nav></sidebar-nav>
    <div class="adminSubContainer">
      <header-nav></header-nav>
      <admin-main-view></admin-main-view>
    </div>
  </div>
</template>

<script>
import HeaderNav from "@/components/common/HeaderNav.vue";
import SidebarNav from "@/components/common/SidebarNav.vue";
import AdminMainView from "@/components/Admin/AdminMainView.vue";

export default {
  name: "AdminView",

  components: {
    HeaderNav,
    SidebarNav,
    AdminMainView,
  },
};
</script>

<style>
#adminMainContainer{
  display : flex;

  height: 150vh;
}

.adminSubContainer{
  flex-direction: column;

  width: 100%;
}

</style>
