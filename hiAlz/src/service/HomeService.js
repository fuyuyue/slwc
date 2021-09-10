import axios from "axios";

function getRegisterList() {
  return axios.get("/home/get-register-list");
}

function approve(id) {
  return axios.post("/home/approve", id);
}

function deleteById(id) {
  return axios.post("/home/delete", id);
}

function getPaperList(userName) {
  return axios.get("/home/userHome/get-paper-list", { params: { userName } });
}

function modifyPaperInfo(data, hasFiles) {
  if (hasFiles) {
    return axios.post("/home/userHome/modify-paper", data);
  } else {
    return axios.post("/home/userHome/modify-paper-no-files", data);
  }
}

function deletePaperInfo(data) {
  return axios.post("/home/userHome/delete-paper", data);
}

function upload(data) {
  return axios.post("/upload", data);
}

function updatePwd(data) {
  return axios.post("/home/updatePwd", data);
}

function getAllStudentsPaperListByCondition(data) {
  return axios.get("/home/userHome/get-all-student-paper-list", {
    params: { ...data },
  });
}

export default {
  getRegisterList,
  approve,
  deleteById,
  getPaperList,
  modifyPaperInfo,
  upload,
  deletePaperInfo,
  updatePwd,
  getAllStudentsPaperListByCondition,
};
