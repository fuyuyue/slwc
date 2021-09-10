package com.slwc.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;

public class FileUtils {

    public final static String fileOriginPath = System.getProperty("user.dir") + File.separator + "FileSpace";

    public static void saveFileByIo(FileInputStream fis, String userName, String paperTitle, String fileName) throws IOException {
        String fileSpace = userName + File.separator + paperTitle + File.separator + fileName;
        String filePath = fileOriginPath + File.separator + fileSpace;
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().isDirectory()) {
            boolean mkdirRes = file.getParentFile().mkdirs();
            if (!mkdirRes) {
                throw new IOException("io err");
            }
        }
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    public static String getFileName(String filePath) {
        String[] pathElement = filePath.split(File.separator);
        String fileName = "";
        if (pathElement.length > 0) {
            fileName = pathElement[pathElement.length - 1];
        }
        return fileName;
    }

    public static void download(final HttpServletResponse response, String filePath) throws IOException {
        String fileName = getFileName(filePath);
        String realPath = FileUtils.fileOriginPath + File.separator + filePath;
        File file = new File(realPath);
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFileDirectory(String userName, String paperTitle) {
        String fileSpace = userName + File.separator + paperTitle + File.separator;
        String filePath = fileOriginPath + File.separator + fileSpace;
        File fileDir = new File(filePath);
        if (fileDir.exists() && fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            if (files == null) return false;
            for (File file : files) {
                if (!file.delete()) {
                    return false;
                }
            }
            return fileDir.delete();
        }
        return false;
    }

    public static boolean deleteOneFile(String userName, String paperTitle, String fileName) {
        String fileSpace = userName + File.separator + paperTitle + File.separator;
        String filePath = fileOriginPath + File.separator + fileSpace + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
