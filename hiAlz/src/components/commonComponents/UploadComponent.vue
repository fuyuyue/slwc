<template>
  <el-upload
    drag
    action="/upload"
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :file-list="fileList"
    :limit="3"
    :auto-upload="false"
    :on-change="handleChange"
    multiple
  >
    <i class="el-icon-upload"></i>
    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
    <template v-slot:tip>
      <div class="el-upload__tip">
        单个文件不超过20Mb
      </div>
    </template>
  </el-upload>
</template>

<script>
export default {
  props: {
    files: Array,
  },
  data() {
    return {
      fileList: this.files,
    };
  },
  methods: {
    handleChange(file, fileList) {
      // 限制上传文件的大小
      const isLt = file.size / 1024 / 1024 / 20 <= 1;
      if (!isLt) {
        this.$message.error("上传文件大小不得大于20MB!");
        for (var i = 0; i < fileList.length; i++) {
          if (fileList[i].uid == file.uid) {
            fileList.splice(i, 1);
          }
        }
      }
      this.fileList = fileList;
      this.$emit("filesChange", this.fileList);
    },
    handleRemove(file) {
      for (var i = 0; i < this.fileList.length; i++) {
        if (this.fileList[i].name == file.name) {
          this.fileList.splice(i, 1);
        }
      }
    },
    handlePreview(file) {
      console.log(file);
    },
  },
};
</script>
