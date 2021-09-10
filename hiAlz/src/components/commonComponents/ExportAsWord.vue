<template>
  <div class="threeLevelMain">
    <!-- 报价容器 -->
    <div class="quoteContainer">
      <!-- 报价顶部信息 -->
      <div class="quote_info clearfix">
        <h3 class="h3_title">报价单</h3>
        <div class="quote_itemBox">
          <div class="quote_item">
            <span class="quote_label">客户名称：</span>
            <p class="quote_p">{{ form.custName }}</p>
          </div>
          <div class="quote_item">
            <span class="quote_label">联系方式：</span>
            <p class="quote_p">{{ form.phoneNumber }}</p>
          </div>
        </div>
        <div class="quote_itemBox">
          <div class="quote_item">
            <span class="quote_label">项目要求：</span>
            <p class="quote_p">{{ form.projectRequirement }}</p>
          </div>
          <div class="quote_item">
            <span class="quote_label">备注：</span>
            <p class="quote_p">{{ form.remark }}</p>
          </div>
        </div>
      </div>
      <!-- 设备选取表格 -->
      <div class="quote_table clearfix">
        <el-table
          border
          class="table_domQuote"
          ref="tableDomQuote"
          size="small"
          :data="tableData"
          style="width: 100%"
        >
          <el-table-column
            prop="number"
            width="80"
            label="序号"
            align="center"
          ></el-table-column>
          <el-table-column
            label="设备名称"
            prop="name"
            align="center"
          ></el-table-column>
          <el-table-column
            label="数量"
            prop="num"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="salePrice"
            label="销售单价"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="saleTotal"
            label="销售合计"
            align="center"
          ></el-table-column>
          <el-table-column
            label="备注"
            prop="remark"
            align="center"
          ></el-table-column>
          <template v-slot:append>
            <div class="quoteTable">
              <span class="quoteTable_sp1">合计：</span>
              <span class="quoteTable_sp1">{{ form.totalPrice }}</span>
            </div>
          </template>
        </el-table>
      </div>
    </div>
    <!-- 审核备注容器 -->
    <div class="reasonBox clearfix">
      <div class="title_checkReason">审核备注：</div>
      <div class="checkReasonMain">
        <div class="p_box">
          <p class="p_checkReason">{{ form.checkReason }}</p>
        </div>
      </div>
    </div>
    <!-- 底部按钮容器 -->
    <div class="botmBtnContainer">
      <el-button @click="exportWord" size="small" type="primary"
        >导出word</el-button
      >
      <!-- <el-button @click="exportExcelClick" size="small" type="primary">导出excel</el-button> -->
    </div>
  </div>
</template>
<script>
import docxtemplater from "docxtemplater";
import PizZip from "pizzip";
import JSZipUtils from "jszip-utils";
import { saveAs } from "file-saver";

export default {
  name: "home",
  data() {
    return {
      // 表单对象
      form: {
        custName: "杰斯", // 客户姓名
        phoneNumber: "138xxxxxxxx", // 联系方式
        projectRequirement: "为了更美好的明天而战", // 项目要求
        totalPrice: 140, // 合计报价
        remark: "QEWARAEQAAAAAAAAA", // 备注
        checkReason: "同意", // 审核备注
      },
      // 表格信息
      tableData: [],
    };
  },
  created() {
    this.tableData = [
      {
        number: 1, // 序号
        name: "设备1", // 设备名称
        num: 1, // 数量
        salePrice: 10, // 销售单价
        saleTotal: 10, // 销售合计
        remark: "啦啦啦", // 备注
      },
      {
        number: 2, // 序号
        name: "设备2", // 设备名称
        num: 2, // 数量
        salePrice: 20, // 销售单价
        saleTotal: 40, // 销售合计
        remark: "啦啦啦", // 备注
      },
      {
        number: 3, // 序号
        name: "设备3", // 设备名称
        num: 3, // 数量
        salePrice: 30, // 销售单价
        saleTotal: 90, // 销售合计
        remark: "啦啦啦", // 备注
      },
    ];
  },
  methods: {
    // 点击导出word
    exportWord: function(formData) {
      // let _this = this;
      // 读取并获得模板文件的二进制内容
      JSZipUtils.getBinaryContent("input.docx", function(error, content) {
        // model.docx是模板。我们在导出的时候，会根据此模板来导出对应的数据
        // 抛出异常
        if (error) {
          throw error;
        }
        // 创建一个PizZip实例，内容为模板的内容
        let zip = new PizZip(content);
        // 创建并加载docxtemplater实例对象
        let doc = new docxtemplater().loadZip(zip);
        // 只有数组，适配input.docx中{#table}
        let table = [],
          index = 1;
        for (let data of formData) {
          data.index = index;
          data.publishTime = data.publishTime.split("-")[0];
          data.journalNo = data.pageNo
            ? data.journalNo + ", " + data.pageNo
            : data.journalNo;
          data.journalTitle = data.journalNo
            ? data.journalTitle + ", " + data.publishTime + ", " + data.journalNo
            : data.journalTitle + ", " + data.publishTime;
          table.push(data);
          index++;
        }
        // 设置模板变量的值
        doc.setData({
          table: table,
          // ..._this.form,
          // table: _this.tableData
        });

        try {
          // 用模板变量的值替换所有模板变量
          doc.render();
        } catch (error) {
          // 抛出异常
          let e = {
            message: error.message,
            name: error.name,
            stack: error.stack,
            properties: error.properties,
          };
          console.log(JSON.stringify({ error: e }));
          throw error;
        }

        // 生成一个代表docxtemplater对象的zip文件（不是一个真实的文件，而是在内存中的表示）
        let out = doc.getZip().generate({
          type: "blob",
          mimeType:
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        });
        // 将目标文件对象保存为目标类型的文件，并命名
        saveAs(out, "论文信息.docx");
      });
    },
  },
};
</script>
