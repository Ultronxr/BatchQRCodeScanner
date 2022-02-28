package cn.ultronxr.batchqrcodescanner.functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * @author Ultronxr
 * @date 2022/02/27 15:24
 * 文件处理相关方法封装
 */
public class FileProcessor {

    /** 所有图片文件的绝对路径列表 */
    private static final ArrayList<String> IMAGE_FILEPATH_LIST = new ArrayList<>();


    public static ArrayList<String> getImageFilepathList() {
        return IMAGE_FILEPATH_LIST;
    }

    public static void clearImageFilepathList() {
        IMAGE_FILEPATH_LIST.clear();
    }

    /**
     * 递归获取所有图片文件的绝对路径
     * @param file 根路径文件/目录
     */
    public static void findImageFileRecursively(File file) {
        if(null == file) {
            return;
        }
        if(file.isDirectory()) {
            File[] fileList = file.listFiles();
            if(fileList != null) {
                for(File tempFile : fileList) {
                    if(tempFile.isDirectory()) {
                        findImageFileRecursively(tempFile);
                    } else if(isImageFile(tempFile)) {
                        IMAGE_FILEPATH_LIST.add(tempFile.getAbsolutePath());
                    }
                }
            }
        } else if(isImageFile(file)) {
            IMAGE_FILEPATH_LIST.add(file.getAbsolutePath());
        }
    }

    /**
     * 判断是否是图片文件
     * @param file 待判断文件
     * @return true - 是； false - 否
     */
    public static boolean isImageFile(File file) {
        if(null == file) {
            return false;
        }
        String mimetype = null;
        try {
            mimetype = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimetype != null && mimetype.split("/")[0].equals("image");
    }

}
