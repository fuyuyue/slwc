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
        <el-menu-item index="1">个人中心</el-menu-item>
        <el-submenu index="4">
          <template v-slot:title>
            <i class="el-icon-setting"></i>
            <span>欢迎您，{{ userName }}</span>
          </template>
          <el-menu-item index="4-1">修改密码</el-menu-item>
          <el-menu-item index="4-2"
            ><el-button type="text" @click="showExitDialog"
              >退出登录</el-button
            ></el-menu-item
          >
        </el-submenu>
      </el-menu>
    </el-header>
    <el-main>
      <div v-show="activeIndex === '1'">
        <el-input
          v-model="searchText"
          size="mini"
          style="width: 300px; float: right;  margin: 0 0 10px 0"
          placeholder="请输入 用户名 | 真实姓名 搜索"
          prefix-icon="el-icon-search"
          @input="handleInput"
          clearable
          @clear="handleInput"
        >
        </el-input>
        <el-table
          :data="usersList"
          stripe
          style="width: 100%;"
          height="500"
          border
          :header-cell-style="{ background: '#eee', color: 'cadetblue' }"
        >
          <el-table-column prop="userName" label="用户名"></el-table-column>
          <el-table-column prop="realName" label="真实姓名"></el-table-column>
          <el-table-column prop="mail" label="邮箱"></el-table-column>
          <el-table-column prop="id" label="身份证号"></el-table-column>
          <el-table-column prop="phone" label="手机号"></el-table-column>
          <el-table-column prop="status" label="状态" sortable width="100px">
            <template v-slot="slotProps">
              {{ slotProps.row.status === "Approved" ? "已审批" : "未审批" }}
            </template>
          </el-table-column>
          <el-table-column prop="operator" label="操作" width="280px">
            <template v-slot="slotProps">
              <el-button
                :disabled="slotProps.row.status === 'Approved'"
                size="mini"
                type="primary"
                @click="handleApprove(slotProps.$index, slotProps.row)"
                >审批</el-button
              >
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(slotProps.$index, slotProps.row)"
                >删除</el-button
              >
              <el-button
                v-if="slotProps.row.role === '学生'"
                size="mini"
                :disabled="slotProps.row.roleManager === 'Y'"
                @click="handleSetManager(slotProps.$index, slotProps.row)"
                >{{
                  slotProps.row.roleManager === "Y"
                    ? "已是库存管理员"
                    : "设为库存管理员"
                }}</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="activeIndex === '4-1'" class="formClass">
        <reset-pwd :userName="userName"></reset-pwd>
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
import ResetPwd from "../commonComponents/ResetPwd.vue";
import CommonFunc from "../../service/CommonFunc";

export default {
  components: { ResetPwd },
  data() {
    return {
      debounceFunc: CommonFunc.debounce(this.searchTable, 600),
      needApprovedList: [],
      usersList: [],
      activeIndex: "1",
      userName: "",
      role: "",
      dialogVisible: false,
      searchText: "",
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
          this.usersList.find((user) => user.id === row.id).status = "Approved";
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
              this.usersList.splice(index, 1);
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
    handleSetManager(index, row) {
      this.$confirm("此操作将自动取消原库存管理员的资格, 是否继续?", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          HomeService.setRoleManager(row.id).then((res) => {
            if (res) {
              this.$message({
                type: "success",
                message: "设置成功!",
              });
              this.needApprovedList
                .filter((user) => user.roleManager === "Y")
                .forEach((user) => (user.roleManager = "N"));
              this.usersList
                .filter((user) => user.roleManager === "Y")
                .forEach((user) => (user.roleManager = "N"));
              this.needApprovedList[index].roleManager = "Y";
              this.usersList[index].roleManager = "Y";
            } else {
              this.$message.error("设置失败！");
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
    handleInput() {
      this.debounceFunc();
    },
    searchTable() {
      this.usersList = this.needApprovedList.filter((userInfo) => {
        return (
          this.searchText === "" ||
          userInfo.userName.includes(this.searchText) ||
          userInfo.realName.includes(this.searchText)
        );
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
        this.usersList = res.data.filter((data) => {
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
