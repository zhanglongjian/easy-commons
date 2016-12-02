package cn.com.easy.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片压缩
 * 
 * @author zhanglj 2016年3月12日下午11:02:13
 * 
 */
public class ImgCompressUtils {

	// 图片处理
	public static String compressPic(File file, String outputDir, String outputFileName, int height, int width, boolean isgp) {
		try {
			// 获得源文件
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				return null;
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (isgp == true) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null)) / (double) width + 0.1;
					double rate2 = ((double) img.getHeight(null)) / (double) height + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = width; // 输出的图片宽度
					newHeight = height; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = new FileOutputStream(outputDir + File.separator + outputFileName);
				// JPEGImageEncoder可适用于其他图片类型的转换
				// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				// encoder.encode(tag);
				out.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outputFileName;
	}
}
