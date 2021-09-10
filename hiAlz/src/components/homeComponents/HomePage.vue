<template>
  <div id="home">
    <router-view />
  </div>
</template>

<script>
export default {
  name: "home",
  mounted() {
    let localInfo =
      sessionStorage.getItem("slwcUserInfo") ||
      localStorage.getItem("slwcUserInfo");
    let userInfo = JSON.parse(localInfo);
    if (userInfo.userName === "admin") {
      this.$router.push("/adminHome");
    } else {
      this.$router.push("/userHome");
    }
  },
};
</script>
