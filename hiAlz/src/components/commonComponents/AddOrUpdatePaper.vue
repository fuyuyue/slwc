<template>
  <el-card class="box-card" shadow="hover">
    <template v-slot:header>
      <span>论文信息</span>
    </template>
    <el-form
      :model="ruleForm"
      class="formClass"
      :rules="rules"
      ref="ruleForm"
      label-width="auto"
      label-position="left"
    >
      <el-form-item prop="title" label="标题">
        <el-input
          v-model="ruleForm.title"
          clearable
          placeholder="请输入标题"
          :disabled="edit"
        ></el-input>
      </el-form-item>
      <el-form-item prop="author" label="作者">
        <el-input
          v-model="ruleForm.author"
          clearable
          placeholder="请输入作者"
        ></el-input>
      </el-form-item>
      <el-form-item prop="journalTitle" label="期刊名">
        <el-input
          v-model="ruleForm.journalTitle"
          clearable
          placeholder="请输入期刊名"
        ></el-input>
      </el-form-item>
      <el-form-item prop="journalNo" label="卷号">
        <el-input
          v-model="ruleForm.journalNo"
          clearable
          placeholder="请输入卷号"
        ></el-input>
      </el-form-item>
      <el-form-item prop="pageNo" label="页码">
        <el-input
          v-model="ruleForm.pageNo"
          clearable
          placeholder="请输入页码"
        ></el-input>
      </el-form-item>
      <el-form-item prop="publishTime" label="发表时间">
        <el-date-picker
          v-model="ruleForm.publishTime"
          type="date"
          placeholder="选择日期"
          :editable="canEdit"
          format="YYYY年MM月DD日"
          value-format="YYYY-MM-DD"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="附件">
        <upload-component
          :files="fileList"
          @filesChange="filesChange"
        ></upload-component>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" round @click="handleDone">提交</el-button>
        <el-button type="danger" round @click="handleCancel">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import HomeService from "../../service/HomeService";
import UploadComponent from "./UploadComponent.vue";

export default {
  components: { UploadComponent },
  props: {
    formData: Object,
    edit: Boolean,
  },
  data() {
    return {
      ruleForm: {
        title: this.formData.title,
        author: this.formData.author,
        journalTitle: this.formData.journalTitle,
        journalNo: this.formData.journalNo,
        pageNo: this.formData.pageNo,
        publishTime: this.formData.publishTime,
      },
      userName: "",
      canEdit: false,
      fileList: [],
    };
  },
  methods: {
    filesChange(files) {
      this.fileList = files;
    },
    hasFiles() {
      if (this.fileList.length === 0) return false;
      for (let file of this.fileList) {
        if (file.raw) {
          return true;
        }
      }
      return false;
    },
    handleDone() {
      let form = new FormData();
      form.append("title", this.ruleForm.title);
      form.append("author", this.ruleForm.author);
      form.append("journalTitle", this.ruleForm.journalTitle);
      form.append("journalNo", this.ruleForm.journalNo);
      form.append("pageNo", this.ruleForm.pageNo);
      form.append("publishTime", this.ruleForm.publishTime);
      form.append("userName", this.userName);
      let filesPathToBack = "";
      this.fileList.forEach((file) => {
        form.append("files", file.raw);
        filesPathToBack += this.userName + "/" + this.ruleForm.title + "/" + file.name + ";";
      });
      form.append("filesPath", filesPathToBack);
      HomeService.modifyPaperInfo(form, this.hasFiles()).then((res) => {
        if (res.data) {
          this.$message({
            message: "操作成功！",
            type: "success",
            showClose: true,
          });
          this.$emit("showPaper");
        }
      });
    },
    handleCancel() {
      this.$confirm("修改会失效，确认返回？", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          this.$refs["ruleForm"].resetFields();
          this.$emit("showPaper");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    initFileList() {
      if (!this.formData.filesPath) return;
      let filePaths = this.formData.filesPath.split(";");
      this.formData.filesName.forEach((name) => {
        this.fileList.push({
          name: name,
          url: filePaths.find((path) => path.includes(name)),
        });
      });
    },
  },
  mounted() {
    let localInfo =
      sessionStorage.getItem("slwcUserInfo") ||
      localStorage.getItem("slwcUserInfo");
    let userInfo = JSON.parse(localInfo);
    this.userName = userInfo.userName;
    this.initFileList();
  },
};
</script>
