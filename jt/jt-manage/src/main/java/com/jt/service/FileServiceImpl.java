package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

@Service
@PropertySource("classpath:/properties/iamge.properties")//加载配置文件
public class FileServiceImpl implements FileService {
//定磁盘路径
	@Value("${image.LocalDirPath}")
	private String LocalDirPath; //="F:/jt-images/";
	
	//定义虚拟路径地址
	@Value("${image.urlDirPath}")
	private String urlDirPath;  //="http://image.jt.com/";
	/**
	 * 实现思路: 1.校验图片类型 jpg/jpeg/png/gif.. 
	 * 2.木马.exe.jpg 校验是否为恶意程序 
	 * 3.分文件存储yyyy/MM/dd/
	 * 4.1.jpg 1.jpg防止文件重名 自定义文件名称uuid.类型
	 * 
	 * 
	 */
	@Override
	public ImageVO upload(MultipartFile uploadFile) {

		// 1.校验图片类型 jpg/jpeg/png/gif..
		// 1.获取图片名称 abc.jpg
		String fileName = uploadFile.getOriginalFilename();
		// 2.拆串 .jpg jpg 校验 正则表达式
		/*
		 * if ("jpg".equals(anObject)) {
		 * 
		 * }else if (condition) {
		 * 
		 * }
		 */
		fileName = fileName.toLowerCase();
		if (!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			return ImageVO.fail();
		}
		System.out.println("校验成功");

		// 3.校验是否为恶意程序 图片:高度/宽度/px
		try {
			BufferedImage bImage = ImageIO.read(uploadFile.getInputStream());
			int width = bImage.getWidth();
			int height = bImage.getHeight();
			if (width==0||height==0) {
				return ImageVO.fail();
			}
			System.out.println("表示是图片!!");
			
			
			
		//4.分文件存储yyyy/MM/dd/	
	   String dateDir=new SimpleDateFormat("yyyy/MM/dd/").format(new
			    Date());
	   //F:/jt-images/2019/10/01/
			String dirFilePath=LocalDirPath+dateDir;
//			System.out.println(dirFilePath);
			File dirFile=new File(dirFilePath);
			if (!dirFile.exists()) {
				//如果文件不存在,需要创建目录
				dirFile.mkdirs();
			}
			
		//动态生成文件名称uuid+文件后缀
			String uuid=UUID.randomUUID().toString();
			String fileType=fileName.substring(fileName.lastIndexOf("."));
			String realFileName=uuid+fileType;
			//D:XXXX/yyyy/mmm
			String realFilePath=dirFilePath+realFileName;
			uploadFile.transferTo(new File(realFilePath));
			
			//实现数据的回显
//			String url="http://域名/图片地址";
			String url=urlDirPath+dateDir+realFileName;
//			String url="https://img10.360buyimg.com/imgzone/jfs/t1/34811/29/7374/49114/5cc71f04Ec108633e/a474b497dd2884fa.jpg";
			//不能用本地路径 要生成一个虚拟路径
			ImageVO imageVO=new ImageVO(0, url, width, height);
			
			return imageVO;
		} catch (Exception e) {

			e.printStackTrace();
			return ImageVO.fail();
		}

	}

}
