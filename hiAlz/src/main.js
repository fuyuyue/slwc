import { createApp } from "vue";
import ElementPlus from "element-plus";
import "element-plus/lib/theme-chalk/index.css";
import App from "./App.vue";
import axios from "axios";
import router from "./router";

//设置element-plus语言
// import lang from "element-plus/lib/locale/lang/zh-cn";
// import "dayjs/locale/zh-cn";
// import locale from "element-plus/lib/locale";
// locale.use(lang);

// axios.defaults.baseURL = "http://192.168.1.6:8082";
axios.defaults.baseURL = "http://localhost:8082";
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8"; //配置请求头信息。
axios.defaults.headers.common["accessToken"] =
  "FA4C308D5E8F6409E01344ADDAEB4C71";

router.beforeEach((to, from, next) => {
  let isAuthenticated =
    sessionStorage.getItem("slwcUserInfo") ||
    localStorage.getItem("slwcUserInfo");
  if (to.name === "register") next();
  else if (to.fullPath !== "/" && !isAuthenticated) next({ name: "login" });
  else next();
});

const app = createApp(App).use(router);
app.use(ElementPlus);
app.mount("#app");
