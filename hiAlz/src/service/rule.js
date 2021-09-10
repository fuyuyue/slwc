function validateUserName(rule, value, callback) {
  if (value === "") {
    callback(new Error("请输入用户名"));
  } else {
    if (this.ruleForm.userName !== "") {
      this.$refs.ruleForm.validateField("checkUserName");
    }
    callback();
  }
}

function validatePass(rule, value, callback) {
  if (value === "") {
    callback(new Error("请输入密码"));
  } else {
    if (this.ruleForm.pwd !== "") {
      this.$refs.ruleForm.validateField("checkPass");
    }
    callback();
  }
}

function validatePass2(rule, value, callback) {
  if (value === "") {
    callback(new Error("请再次输入密码"));
  } else if (value !== this.ruleForm.pwd) {
    callback(new Error("两次输入密码不一致!"));
  } else {
    callback();
  }
}

export default {
  validateUserName,
  validatePass,
  validatePass2,
};
