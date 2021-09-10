<template>
  <el-table
    id="userTable"
    class="userTable"
    :data="tableData"
    stripe
    style="width: 100%"
    height="500"
    border
    :header-cell-style="{ background: '#eee', color: 'cadetblue' }"
  >
    <el-table-column prop="title" label="标题" sortable></el-table-column>
    <el-table-column prop="author" label="作者" sortable></el-table-column>
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
    ></el-table-column>
    <el-table-column
      prop="pageNo"
      label="页码"
      sortable
    ></el-table-column>
  </el-table>
</template>

<script>
import FileSaver from "file-saver";
import XLSX from "xlsx";

export default {
  props: {
      tableData: Array,
  },
  methods: {
    ExportTableAsExcel() {
      /* generate workbook object from table */
      let wb = XLSX.utils.table_to_book(document.querySelector("#userTable"));
      /* get binary string as output */
      let wbout = XLSX.write(wb, {
        bookType: "xlsx",
        bookSST: true,
        type: "array",
      });
      try {
        FileSaver.saveAs(
          new Blob([wbout], { type: "application/octet-stream" }),
          "论文信息表.xlsx"
        );
      } catch (e) {
        if (typeof console !== "undefined") console.log(e, wbout);
      }
      return wbout;
    },
  }
};
</script>
