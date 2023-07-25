<template>
  <div id="adminMainContainer">
    <sidebar-nav></sidebar-nav>
    <div class="adminSubContainer">
      <header-nav></header-nav>
      <router-view/>
    </div>
  </div>
</template>

<script>
import HeaderNav from "@/components/common/HeaderNav.vue";
import SidebarNav from "@/components/common/SidebarNav.vue";
// import AdminMainView from "@/components/Admin/AdminMainView.vue";

export default {
  name: "AdminView",

  components: {
    HeaderNav,
    SidebarNav,

  },
};
</script>

<style>
#adminMainContainer{
  display : flex;
  width: 100%;
  height: 200vh;
}

.adminSubContainer{
  flex-direction: column;
  width: 100%;
}

</style>
