<template>
  <div class="registerClass">
    <el-form :model="ruleForm" class="formClass" :rules="rules" ref="ruleForm">
      <el-row :gutter="70" align="middle">
        <el-col span="12">
          <el-form-item>
            <el-radio-group v-model="ruleForm.role">
              <el-radio-button label="教师"></el-radio-button>
              <el-radio-button label="学生"></el-radio-button>
            </el-radio-group> </el-form-item
        ></el-col>
        <el-col span="12">
          <el-form-item prop="userName">
            <el-input
              v-model="ruleForm.userName"
              clearable
              placeholder="请输入用户名"
            ></el-input> </el-form-item
        ></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col span="12">
          <el-form-item prop="pwd">
            <el-input
              type="password"
              v-model="ruleForm.pwd"
              autocomplete="off"
              clearable
              placeholder="请输入密码"
            ></el-input> </el-form-item
        ></el-col>
        <el-col span="12">
          <el-form-item prop="confirmPwd">
            <el-input
              type="password"
              v-model="ruleForm.confirmPwd"
              autocomplete="off"
              clearable
              placeholder="请再次输入密码"
            ></el-input> </el-form-item
        ></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col span="12">
          <el-form-item prop="realName">
            <el-input
              v-model="ruleForm.realName"
              clearable
              placeholder="请输入真实姓名"
            ></el-input> </el-form-item
        ></el-col>
        <el-col span="12">
          <el-form-item prop="mail">
            <el-input
              v-model="ruleForm.mail"
              clearable
              placeholder="请输入邮箱"
            ></el-input> </el-form-item
        ></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col span="12">
          <el-form-item prop="id">
            <el-input
              v-model="ruleForm.id"
              clearable
              placeholder="请输入身份证号"
            ></el-input> </el-form-item
        ></el-col>
        <el-col span="12">
          <el-form-item prop="phone">
            <el-input
              v-model="ruleForm.phone"
              clearable
              placeholder="请输入手机号"
            ></el-input> </el-form-item
        ></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col span="12">
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')"
              >提交</el-button
            >
          </el-form-item>
        </el-col>
        <el-col span="12">
          <el-form-item>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import RegisterService from "../service/RegisterService";
import router from "../router/index";

export default {
  name: "login",
  data() {
    var confirmPassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.pwd) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    var existUser = (rule, value, callback) => {
      RegisterService.existUser(value).then(res => {
        res && res.data ? callback(new Error("用户名已存在，请重新输入")) : callback();
      })
    };
    return {
      ruleForm: {
        role: "教师",
        userName: "",
        pwd: "",
        confirmPwd: "",
        realName: "",
        mail: "",
        id: "",
        phone: "",
      },
      rules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 3,
            max: 10,
            message: "长度在 3 到 10 个字符",
          },
          {
            pattern: /^[a-zA-Z][a-zA-Z0-9]+$/,
            message: "只允许输入字母数字，只能以字母开头",
          },
          { validator: existUser, trigger: "blur" },
        ],
        pwd: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 8,
            max: 20,
            message: "长度在 8 到 20 个字符",
          },
        ],
        confirmPwd: [
          { required: true, message: "请再次输入密码", trigger: "blur" },
          { validator: confirmPassword, trigger: "blur" },
        ],
        realName: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
          {
            min: 3,
            max: 20,
            message: "长度在 3 到 20 个字符",
          },
        ],
        mail: [
          { required: true, message: "请输入邮箱" },
          {
            pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
            message: "邮箱格式错误",
          },
        ],
        id: [
          { required: true, message: "请输入身份证号" },
          { pattern: /^[0-9A-Z]+$/, message: "身份证格式不合法" },
          { max: 20, message: "长度不超过20" },
        ],
        phone: [
          { required: true, message: "请输入手机号" },
          { pattern: /^[0-9]+$/, message: "手机号只能是数字" },
          { max: 11, message: "最长只能11位" },
        ],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let data = {
            userName: this.ruleForm.userName,
            pwd: this.ruleForm.pwd,
            role: this.ruleForm.role,
            realName: this.ruleForm.realName,
            mail: this.ruleForm.mail,
            id: this.ruleForm.id,
            phone: this.ruleForm.phone,
          };
          RegisterService.register(data).then((res) => {
            if (res.data) {
              router.push("/");
              this.$message({
                message: "注册成功！请等待管理员审核！",
                type: "success",
                duration: 5000,
                showClose: true,
              });
            } else {
              this.$message.error("啊哦！出错了！");
            }
          });
        } else {
          this.$message.error("请检查输入");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>

<style scoped>
.registerClass {
  background: url("../assets/tree.png");
  background-size: 100% 100%;
  width: 100% !important;
  height: 100%;
  position: fixed;
  margin: -8px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.labelClass {
  color: black;
}
.el-row {
  margin-bottom: 20px;
}
</style>
