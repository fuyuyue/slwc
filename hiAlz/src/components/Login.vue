<template>
  <div class="loginClass">
    <el-form :model="ruleForm" class="formClass" :rules="rules" ref="ruleForm">
      <el-form-item
        ><h2 style="color: white">欢迎您，上理微创人</h2></el-form-item
      >
      <el-form-item prop="userName">
        <el-input
          v-model="ruleForm.userName"
          clearable
          placeholder="请输入用户名"
          @change="handleChange"
        ></el-input>
      </el-form-item>
      <el-form-item prop="pwd">
        <el-input
          type="password"
          v-model="ruleForm.pwd"
          autocomplete="off"
          clearable
          placeholder="请输入密码"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-checkbox v-model="rememberPwd" style="color: black"
          >记住密码</el-checkbox
        >
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')"
          >登录</el-button
        >
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
      <el-form-item
        ><el-button type="text" @click="register"
          >没有账号？点击注册！</el-button
        ></el-form-item
      >
    </el-form>
  </div>
</template>

<script>
import LoginService from "../service/LoginService";
import router from "../router/index";

export default {
  name: "login",
  data() {
    return {
      rememberPwd: false,
      ruleForm: {
        role: "",
        userName: "",
        pwd: "",
      },
      rules: {
        userName: [
          { required: true, message: "请输入用户名" },
          {
            min: 3,
            max: 10,
            message: "长度在 3 到 10 个字符",
          },
          {
            pattern: /^[a-zA-Z][a-zA-Z0-9]+$/,
            message: "只允许输入字母数字，只能以字母开头",
          },
        ],
        pwd: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 8,
            max: 20,
            message: "长度在 8 到 20 个字符",
          },
        ],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          LoginService.login(this.ruleForm.userName, this.ruleForm.pwd).then(
            (res) => {
              if (res.data === "error") {
                this.$message.error("用户名密码错误！");
                return false;
              }
              let status = res.data.split(";")[0];
              this.ruleForm.role = res.data.split(";")[1];
              switch (status) {
                case "Audit":
                  this.$message({
                    message: "正在审核中，请等待或联系管理员杨昳津老师",
                    type: "warning",
                  });
                  break;
                case "Approved":
                  router.push("/home");
                  if (this.rememberPwd) {
                    localStorage.setItem(
                      "slwcUserInfo",
                      JSON.stringify(this.ruleForm)
                    );
                  }
                  sessionStorage.setItem(
                    "slwcUserInfo",
                    JSON.stringify(this.ruleForm)
                  );
                  break;
              }
            }
          );
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    register() {
      router.push("/register");
    },
    handleChange() {
      let localInfo =
        sessionStorage.getItem("slwcUserInfo") ||
        localStorage.getItem("slwcUserInfo");
      if (
        localInfo &&
        JSON.parse(localInfo).userName === this.ruleForm.userName
      ) {
        this.ruleForm = JSON.parse(localInfo);
      }
    },
  },
};
</script>

<style scoped>
.loginClass {
  background: url("../assets/beach.png");
  background-size: 100% 100%;
  width: 100% !important;
  height: 100%;
  position: fixed;
  margin: -8px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.formClass {
  margin: 0 auto;
  /* width: 400px;
  height: 300px; */
  /* position: relative;
  top: 20%; */
  text-align: center;
}
.labelClass {
  color: black;
}
</style>
