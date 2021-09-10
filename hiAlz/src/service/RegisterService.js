import axios from "axios";

function register(data) {
  return axios.post("/register", data);
}

function existUser(userName) {
  return axios.get("/existUser", { params: { userName } });
}

export default {
    register,
    existUser
};
