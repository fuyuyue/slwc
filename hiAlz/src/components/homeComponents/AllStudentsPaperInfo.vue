<template>
  <el-input
    v-model="searchText"
    size="mini"
    style="width: 300px; float: right; margin: 0 0 10px 0"
    placeholder="请输入 标题 | 作者 | 期刊名 搜索"
    suffix-icon="el-icon-search"
    @input="handleInput"
  >
  </el-input>
  <el-table
    :data="paperInfoList"
    stripe
    style="width: 100%; margin: 10px 0 0 0"
    height="500"
    border
    :header-cell-style="{ background: '#eee', color: 'cadetblue' }"
  >
    <el-table-column
      prop="publisher"
      label="上传者"
      sortable
      :filters="publisherFilter"
      :filter-method="filterMethod"
    ></el-table-column>
    <el-table-column prop="title" label="标题" sortable></el-table-column>
    <el-table-column prop="author" label="作者" sortable></el-table-column>
    <el-table-column
      prop="journalTitle"
      label="期刊名"
      sortable
    ></el-table-column>
    <el-table-column prop="journalNo" label="卷号" sortable></el-table-column>
    <el-table-column
      prop="publishTime"
      label="发表时间"
      sortable
      width="120"
      :filters="publishTimeFilter"
      :filter-method="filterMethod"
    ></el-table-column>
    <el-table-column prop="pageNo" label="页码" sortable></el-table-column>
    <el-table-column prop="filesName" label="附件列表" width="300">
      <template v-slot="slotScope">
        <div v-for="(fileName, index) in slotScope.row.filesName" :key="index">
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
  </el-table>
  <el-pagination
    background
    @current-change="handleCurrentChange"
    v-model="currentPage3"
    :page-size="pageSize"
    layout="prev, pager, next, jumper"
    :total="totalNum"
  >
  </el-pagination>
</template>

<script>
import HomeService from "../../service/HomeService";
import axios from "axios";
import CommonFunc from "../../service/CommonFunc";

export default {
  data() {
    return {
      paperInfoList: [],
      searchText: "",
      debounceFunc: CommonFunc.debounce(this.searchTable, 600),
      userName: "",
      role: "",
      publisherFilter: [],
      publishTimeFilter: [],
      totalNum: 1000,
      pageSize: 20,
    };
  },
  methods: {
    initPaperList(pageIndex) {
      let data = {
        pageIndex: pageIndex,
        searchContent: this.searchText,
      }
      HomeService.getAllStudentsPaperListByCondition(data).then((res) => {
        if (res && res.data) {
          let publisherSet = new Set();
          let publishTimeSet = new Set();
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
            if (!publisherSet.has(data.publisher)) {
              this.publisherFilter.push({
                text: data.publisher,
                value: data.publisher,
              });
            }
            publisherSet.add(data.publisher);
            if (!publishTimeSet.has(data.publishTime)) {
              this.publishTimeFilter.push({
                text: data.publishTime,
                value: data.publishTime,
              });
            }
            publishTimeSet.add(data.publishTime);
            return data;
          });
        }
      });
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
    handleInput() {
      this.debounceFunc();
    },
    filterMethod(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
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
