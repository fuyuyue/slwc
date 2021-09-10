import { createRouter, createWebHashHistory } from "vue-router";

import Login from "../components/Login";
import Register from "../components/Register";
import HomePage from "../components/homeComponents/HomePage";
import AdminHome from "../components/homeComponents/AdminHome";
import UserHome from "../components/homeComponents/UserHome";
import AddOrUpdatePaper from "../components/commonComponents/AddOrUpdatePaper"

const routes = [
  {
    name: "login",
    path: "/",
    component: Login,
  },
  {
    name: "register",
    path: "/register",
    component: Register,
  },
  {
    name: "home",
    path: "/home",
    component: HomePage,
    children: [
      {
        path: "/adminHome",
        component: AdminHome,
      },
      {
        path: "/userHome",
        component: UserHome,
        children: [
          {
            path: '/addOrUpdatePaper',
            component: AddOrUpdatePaper,
          }
        ]
      },
    ],
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
