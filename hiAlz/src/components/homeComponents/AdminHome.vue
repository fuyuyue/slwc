<template>
  <el-container>
    <el-header>
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="1">处理中心</el-menu-item>
        <el-menu-item index="2">消息中心</el-menu-item>
        <el-submenu index="3">
          <template v-slot:title>
            <i class="el-icon-setting"></i>
            <span>欢迎您，{{ userName }}</span>
          </template>
          <el-menu-item index="3-1"
            ><el-button type="text" @click="showExitDialog"
              >退出登录</el-button
            ></el-menu-item
          >
        </el-submenu>
      </el-menu>
    </el-header>
    <el-main>
      <div v-show="activeIndex === '1'">
        <el-table
          :data="needApprovedList"
          stripe
          style="width: 100%"
          height="250"
          border
          :header-cell-style="{ background: '#eee', color: 'cadetblue' }"
        >
          <el-table-column prop="userName" label="用户名"></el-table-column>
          <el-table-column prop="realName" label="真实姓名"></el-table-column>
          <el-table-column prop="mail" label="邮箱"></el-table-column>
          <el-table-column prop="id" label="身份证号"></el-table-column>
          <el-table-column prop="phone" label="手机号"></el-table-column>
          <el-table-column prop="status" label="状态" sortable>
            <template v-slot="slotProps">
              {{ slotProps.row.status === "Approved" ? "已审批" : "未审批" }}
            </template>
          </el-table-column>
          <el-table-column prop="operator" label="操作">
            <template v-slot="slotProps">
              <el-button
                :disabled="slotProps.row.status === 'Approved'"
                size="mini"
                @click="handleApprove(slotProps.$index, slotProps.row)"
                >审批</el-button
              >
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(slotProps.$index, slotProps.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="activeIndex === '2'">
        <infinity-scroll></infinity-scroll>
      </div>
      <el-dialog
        title="提示"
        v-model="dialogVisible"
        width="30%"
        :before-close="handleClose"
      >
        <span>是否确认退出登录？</span>
        <template v-slot:footer class="dialog-footer">
          <el-button @click="cancelExit">取 消</el-button>
          <el-button type="primary" @click="comfirmExit">确 定</el-button>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script>
import HomeService from "../../service/HomeService";
import InfinityScroll from "../commonComponents/InfinityScroll.vue";

export default {
  components: { InfinityScroll },
  data() {
    return {
      needApprovedList: [],
      activeIndex: "1",
      userName: "",
      role: "",
      dialogVisible: false,
    };
  },
  methods: {
    showExitDialog() {
      this.dialogVisible = true;
    },
    handleClose() {
      this.dialogVisible = false;
      this.activeIndex = "1";
    },
    comfirmExit() {
      this.dialogVisible = false;
      this.clearUserState();
    },
    cancelExit() {
      this.dialogVisible = false;
      this.activeIndex = "1";
    },
    clearUserState() {
      if (sessionStorage.getItem("slwcUserInfo")) {
        sessionStorage.removeItem("slwcUserInfo");
      }
      if (localStorage.getItem("slwcUserInfo")) {
        localStorage.removeItem("slwcUserInfo");
      }
      this.$router.push("/login");
    },
    handleSelect(key) {
      this.activeIndex = key;
    },
    handleApprove(index, row) {
      HomeService.approve(row.id).then((res) => {
        if (res) {
          this.$message({
            message: "审批成功！",
            type: "success",
            duration: 2000,
            showClose: true,
          });
          this.needApprovedList.find((user) => user.id === row.id).status =
            "Approved";
        }
      });
    },
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除该数据, 是否继续?", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          HomeService.deleteById(row.id).then((res) => {
            if (res) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.needApprovedList.splice(index, 1);
            } else {
              this.$message.error("删除失败！");
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
  },
  mounted() {
    let userInfo =
      sessionStorage.getItem("slwcUserInfo") ||
      localStorage.getItem("slwcUserInfo");
    this.userName = JSON.parse(userInfo).userName;
    this.role = JSON.parse(userInfo).role;
    HomeService.getRegisterList().then((res) => {
      if (res && res.data) {
        this.needApprovedList = res.data.filter((data) => {
          return data.userName !== "admin";
        });
      }
    });
  },
};
</script>

<style scoped>
.el-menu--horizontal .el-submenu {
  float: right;
}
.formClass {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10%;
}
</style>
