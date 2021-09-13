# slwc
vue+element+springboot;简易文件上传下载;导出为excel/word

数据表：

CREATE TABLE `paper` (
  `title` varchar(200) NOT NULL,
  `author` varchar(200) DEFAULT NULL,
  `journal_title` varchar(200) DEFAULT NULL,
  `publish_time` date DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  `journal_no` varchar(45) DEFAULT NULL,
  `page_no` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `userinfo` (
  `id` varchar(20) NOT NULL DEFAULT '0',
  `user_name` varchar(45) DEFAULT NULL,
  `pwd` varchar(45) DEFAULT NULL,
  `role` varchar(10) DEFAULT NULL,
  `role_manager` varchar(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `real_name` varchar(45) DEFAULT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `fileInfo` (
  `user_name` varchar(45) NOT NULL,
  `paper_title` varchar(200) NOT NULL,
  `file_path` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`user_name`,`paper_title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
