import com.google.zxing.NotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author Ultronxr
 * @date 2022/02/25 17:24
 */
public class MainMethod {

    public static void main(String[] args) {
        ArrayList<String> resultList = new ArrayList<>();
        ArrayList<String> errorFileNameList = new ArrayList<>();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("summary.txt", true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assert fileWriter != null;
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        if(args.length == 1) {
            Path path = Path.of(args[0]);
            if(Files.isDirectory(path)) {
                File file = path.toFile();
                File[] tempFiles = file.listFiles();
                assert tempFiles != null;
                for(File tempFile : tempFiles) {
                    try {
                        assert !tempFile.isDirectory();
                        bufferedWriter.write("正在扫描二维码文件：" + tempFile.getName() + "\n");
                        String result = QRCodeUtils.decoderByImageFile(tempFile);
                        if(result != null) {
                            resultList.add(result);
                            bufferedWriter.write("二维码扫描完成。\n");
                        }
                    } catch (IOException | NotFoundException ex) {
                        errorFileNameList.add(tempFile.getName());
                        try {
                            bufferedWriter.write("二维码扫描失败！\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        try {
            bufferedWriter.write("所有文件扫描完成。统计结果如下：\n");
            bufferedWriter.write("总扫描文件数量：" + (resultList.size()+errorFileNameList.size()) + "\n");
            bufferedWriter.write("二维码扫描失败文件数量及列表：" + errorFileNameList.size() + "\n");
            for(String errorFile : errorFileNameList) {
                bufferedWriter.write(errorFile + "\n");
            }
            bufferedWriter.write("二维码扫描完成文件数量及结果：" + resultList.size() + "\n");
            for(String result : resultList) {
                bufferedWriter.write(result + "\n");
            }
            bufferedWriter.flush();
            fileWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
