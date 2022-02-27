import com.google.zxing.NotFoundException;
import functions.FileProcessor;
import functions.QRCodeUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ultronxr
 * @date 2022/02/25 17:24
 */
@Slf4j
public class MainMethod {

    private static final ArrayList<String> SCAN_RESULT_LIST = new ArrayList<>();

    public static final ArrayList<String> SCAN_ERROR_FILEPATH_LIST = new ArrayList<>();


    public static void main(String[] args) {
        if(args.length == 0) {
            String workPath = System.getProperty("user.dir");
            log.info("当前工作目录：{}", workPath);
            FileProcessor.findImageFileRecursively(new File(workPath));
            doScanQRCode();
            doSummary();
        } else if(args.length == 1) {
            log.info("当前工作目录：{}", args[0]);
            FileProcessor.findImageFileRecursively(new File(args[0]));
            doScanQRCode();
            doSummary();
        } else {
            log.info("当前工作目录：{}", (Object) args);
            for(String arg : args) {
                FileProcessor.clearImageFilepathList();
                FileProcessor.findImageFileRecursively(new File(arg));
                doScanQRCode();
            }
            doSummary();
        }
    }

    private static void doScanQRCode() {
        for(String filepath : FileProcessor.getImageFilepathList()) {
            try {
                String scanResult = QRCodeUtils.decoderByFilePath(filepath);
                if(null != scanResult) {
                    SCAN_RESULT_LIST.add(scanResult);
                    log.info("二维码扫描完成。文件路径：{}", filepath);
                }
            } catch (NotFoundException ex) {
                SCAN_ERROR_FILEPATH_LIST.add(filepath);
                log.warn("二维码扫描失败！文件路径：{}", filepath);
            } catch (IOException ex) {
                ex.printStackTrace();
                SCAN_ERROR_FILEPATH_LIST.add(filepath);
                log.warn("文件读取失败！文件路径：{}", filepath);
            }
        }
    }

    private static void doSummary() {
        log.info("=".repeat(20));
        log.info("共扫描二维码文件数量：{}", SCAN_RESULT_LIST.size() + SCAN_ERROR_FILEPATH_LIST.size());
        log.info("扫描完成数量：{}", SCAN_RESULT_LIST.size());
        log.info("扫描失败/文件读取错误数量：{}", SCAN_ERROR_FILEPATH_LIST.size());
        log.info("扫描完成结果请查看scan_result.txt文件；扫描失败结果请查看scan_error.txt文件；扫描日志请查看log.txt文件。");
        log.info("=".repeat(20));

        writeScanResultAndScanError();
    }

    private static void writeScanResultAndScanError() {
        try {
            FileWriter fileWriter = new FileWriter("scan_result.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(String result : SCAN_RESULT_LIST) {
                bufferedWriter.write(result);
            }
            bufferedWriter.flush();
            fileWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
            log.info("输出“扫描完成结果”文件完成。");

            fileWriter = new FileWriter("scan_error.txt", false);
            bufferedWriter = new BufferedWriter(fileWriter);
            for(String result : SCAN_ERROR_FILEPATH_LIST) {
                bufferedWriter.write(result);
            }
            bufferedWriter.flush();
            fileWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
            log.info("输出“扫描失败结果”文件完成。");
        } catch (IOException ex) {
            ex.printStackTrace();
            log.warn("输出扫描结果文件发生异常！");
        }
    }

}
