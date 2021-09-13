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
        <el-menu-item index="2" v-if="role==='教师'">组会文件上传</el-menu-item>
        <el-menu-item index="3" v-if="role==='教师'">学生论文信息</el-menu-item>
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
        <div v-if="showPapersList">
          <el-button type="primary" @click="handleAdd" size="mini"
            >添加论文信息<i class="el-icon-circle-plus el-icon--right"></i
          ></el-button>
          <el-button
            type="success"
            @click="handleExportTableAsExcel"
            size="mini"
            v-if="role==='教师'"
            >导出为Excel</el-button
          >
          <el-button type="primary" @click="handleExportTableAsWord" size="mini" v-if="role==='教师'"
            >导出为Word</el-button
          >
          <export-as-word v-show="false" ref="exportAsWord"></export-as-word>
          <el-input
            v-model="searchText"
            size="mini"
            style="width: 300px; float: right"
            placeholder="请输入 标题 | 作者 | 期刊名 搜索"
            prefix-icon="el-icon-search"
            @input="handleInput"
            clearable
            @clear="handleInput"
          >
          </el-input>
          <excel-table
            v-show="false"
            :tableData="showPaperInfoList"
            ref="excelTable"
          ></excel-table>
          <el-table
            id="userTable"
            class="userTable"
            :data="showPaperInfoList"
            stripe
            style="width: 100%; margin: 10px 0 0 0"
            height="500"
            border
            :header-cell-style="{ background: '#eee', color: 'cadetblue' }"
          >
            <el-table-column
              prop="title"
              label="标题"
              sortable
            ></el-table-column>
            <el-table-column
              prop="author"
              label="作者"
              sortable
            ></el-table-column>
            <el-table-column
              prop="journalTitle"
              label="期刊名"
              sortable
            ></el-table-column>
            <el-table-column
              prop="journalNo"
              label="卷号"
              sortable
            ></el-table-column>
            <el-table-column
              prop="publishTime"
              label="发表时间"
              sortable
              width="120"
            ></el-table-column>
            <el-table-column
              prop="pageNo"
              label="页码"
              sortable
            ></el-table-column>
            <el-table-column prop="filesName" label="附件列表" width="300">
              <template v-slot="slotScope">
                <div
                  v-for="(fileName, index) in slotScope.row.filesName"
                  :key="index"
                >
                  <el-button
                    v-if="fileName !== '暂无附件'"
                    type="text"
                    icon="el-icon-download"
                    size="mini"
                    @click="handleDownload(slotScope.row, fileName)"
                    >{{ fileName }}</el-button
                  >
                  <span v-else>暂无附件</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="operator" label="操作" width="100">
              <template v-slot="slotProps">
                <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  size="mini"
                  @click="handleEdit(slotProps.$index, slotProps.row)"
                ></el-button>
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  size="mini"
                  @click="handleDelete(slotProps.$index, slotProps.row)"
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else>
          <add-or-update-paper
            :formData="formData"
            :edit="isEdit"
            @showPaper="showPaper"
          ></add-or-update-paper>
        </div>
      </div>
      <div v-show="activeIndex === '2'">
        <el-input v-model="userName">用户名</el-input>
        <el-input v-model="paperTitle">文章标题</el-input>
        <upload-component @filesChange="filesChange"></upload-component>
        <el-button
          style="margin-left: 10px;"
          size="small"
          type="success"
          @click="submitUpload"
          >上传到服务器</el-button
        >
      </div>
      <div v-show="activeIndex === '3'">
        <all-students-paper-info></all-students-paper-info>
      </div>
      <div v-show="activeIndex === '4-1'" class="formClass">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
          <el-form-item prop="pwd">
            <el-input
              type="password"
              v-model="ruleForm.pwd"
              autocomplete="off"
              clearable
              placeholder="请输入新密码"
            ></el-input>
          </el-form-item>
          <el-form-item prop="confirmPwd">
            <el-input
              type="password"
              v-model="ruleForm.confirmPwd"
              autocomplete="off"
              clearable
              placeholder="请再次输入新密码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitNewPwd('ruleForm')"
              >确认</el-button
            >
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
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
import AddOrUpdatePaper from "../commonComponents/AddOrUpdatePaper.vue";
import UploadComponent from "../commonComponents/UploadComponent.vue";
import ExcelTable from "../commonComponents/ExcelTable.vue";
import axios from "axios";
import CommonFunc from "../../service/CommonFunc";
import ExportAsWord from "../commonComponents/ExportAsWord.vue";
import AllStudentsPaperInfo from './AllStudentsPaperInfo';

export default {
  components: {
    AddOrUpdatePaper,
    UploadComponent,
    ExcelTable,
    ExportAsWord,
    AllStudentsPaperInfo,
  },
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
    return {
      debounceFunc: CommonFunc.debounce(this.searchTable, 600),
      dialogVisible: false,
      paperInfoList: [],
      showPaperInfoList: [],
      activeIndex: "1",
      userName: "",
      role: "",
      searchText: "",
      showPapersList: true,
      isEdit: false,
      formData: {},
      paperTitle: "testTitle",
      fileList: [],
      ruleForm: {
        pwd: "",
        confirmPwd: "",
      },
      rules: {
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
      },
    };
  },
  methods: {
    showExitDialog() {
      this.dialogVisible = true;
    },
    comfirmExit() {
      this.dialogVisible = false;
      this.clearUserState();
    },
    cancelExit() {
      this.dialogVisible = false;
      this.activeIndex = '1';
    },
    handleClose() {
      this.dialogVisible = false;
      this.activeIndex = '1';
    },
    filesChange(fileList) {
      this.fileList = fileList;
    },
    submitUpload() {
      let form = new FormData();
      form.append("userName", this.userName);
      form.append("paperTitle", this.paperTitle);
      this.fileList.forEach((file) => {
        form.append("files", file.raw);
      });
      HomeService.upload(form).then((res) => {
        if (res.data) {
          alert("upload");
        }
      });
    },
    handleExportTableAsWord() {
      this.$refs["exportAsWord"].exportWord(this.paperInfoList);
    },
    handleInput() {
      this.debounceFunc();
    },
    searchTable() {
      this.showPaperInfoList = this.paperInfoList.filter((paper) => {
        return (
          this.searchText === "" ||
          paper.title.includes(this.searchText) ||
          paper.author.includes(this.searchText) ||
          paper.journalTitle.includes(this.searchText)
        );
      });
    },
    handleExportTableAsExcel() {
      this.$refs["excelTable"].ExportTableAsExcel();
    },
    handleDownload(fileInfo, fileName) {
      const filePath = this.userName + "/" + fileInfo.title + "/" + fileName;
      const a = document.createElement("a");
      a.setAttribute("download", fileName);
      a.setAttribute("target", "_blank");
      a.setAttribute(
        "href",
        axios.defaults.baseURL + "/download?filePath=" + filePath
      );
      a.click();
    },
    handleSelect(key) {
      this.activeIndex = key;
    },
    handleAdd() {
      this.isEdit = false;
      this.formData = {};
      this.showPapersList = false;
    },
    initPaperList() {
      HomeService.getPaperList(this.userName).then((res) => {
        if (res && res.data) {
          this.paperInfoList = res.data.map((data) => {
            data.filesName = [];
            if (!data.filesPath) {
              data.filesName.push("暂无附件");
            } else {
              let files = data.filesPath.split(";");
              files.forEach((file) => {
                if (file) {
                  let names = file.split("/");
                  data.filesName.push(names[names.length - 1]);
                }
              });
            }
            return data;
          });
          this.showPaperInfoList = this.paperInfoList.map((paper) => paper);
        }
      });
    },
    showPaper() {
      this.initPaperList();
      this.showPapersList = true;
    },
    handleEdit(index, row) {
      this.isEdit = true;
      this.formData = row;
      this.showPapersList = false;
    },
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除该数据, 是否继续?", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          let data = {
            userName: this.userName,
            title: row.title,
          };
          HomeService.deletePaperInfo(data).then((res) => {
            if (res) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.paperInfoList.splice(index, 1);
              this.showPaperInfoList = this.paperInfoList.map((paper) => paper);
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
    submitNewPwd(ruleFormName) {
      this.$confirm("重置密码后需要重新登录, 是否继续?", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
      })
        .then(() => {
          this.$refs[ruleFormName].validate((valid) => {
            if (valid) {
              let data = {
                pwd: this.ruleForm.pwd,
                userName: this.userName,
              };
              HomeService.updatePwd(data).then((res) => {
                if (res) {
                  this.$message({
                    type: "success",
                    message: "重置密码成功，请重新登录!",
                  });
                  this.clearUserState();
                } else {
                  this.$message.error("重置密码失败！请重试");
                }
              });
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
    clearUserState() {
      if (sessionStorage.getItem("slwcUserInfo")) {
        sessionStorage.removeItem("slwcUserInfo");
      }
      if (localStorage.getItem("slwcUserInfo")) {
        localStorage.removeItem("slwcUserInfo");
      }
      this.$router.push("/login");
    },
  },
  mounted() {
    let userInfo =
      sessionStorage.getItem("slwcUserInfo") ||
      localStorage.getItem("slwcUserInfo");
    this.userName = JSON.parse(userInfo).userName;
    this.role = JSON.parse(userInfo).role;
    this.initPaperList();
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
