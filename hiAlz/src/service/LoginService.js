import axios from "axios";

function login(userName, pwd) {
  let data = {
    userName,
    pwd,
  };
  return axios.post("/login", data);
}

export default {
  login,
};
