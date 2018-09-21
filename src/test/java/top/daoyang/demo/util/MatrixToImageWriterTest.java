package top.daoyang.demo.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import static org.junit.Assert.*;

public class MatrixToImageWriterTest {

    @Test
    public void writeToFile() throws WriterException, IOException {
        String text = "https://qr.alipay.com/bax0448093rk881u2sju00c2"; // 二维码内容
        int width = 300; // 二维码图片宽度
        int height = 300; // 二维码图片高度
        String format = "jpg";// 二维码的图片格式

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");	// 内容所使用字符集编码

        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        // 生成二维码
        File outputFile = new File("D:" + File.separator + "new.jpg");
//        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);

    }
}