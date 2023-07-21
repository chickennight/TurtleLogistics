<template>
    <sidebar-nav></sidebar-nav>
  <header-nav></header-nav>
  <admin-main-view></admin-main-view>
</template>

<script>
import HeaderNav from "@/components/common/HeaderNav.vue"
import SidebarNav from "@/components/common/SidebarNav.vue"
import AdminMainView from "@/components/Admin/AdminMainView.vue"

export default {
    name: "AdminView",
    
    components: {
      HeaderNav,
      SidebarNav,
      AdminMainView,
  },

}
</script>

<style>

</style>