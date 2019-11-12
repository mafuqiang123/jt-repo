package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@RestController
public class FileController {

	@Autowired
	private  FileService fileService;
	/**
	 * 文件上传成功之后,返回json 数据
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("/file")//http://localhost:8091/pic/upload?dir=image
	public String file(MultipartFile fileImage) throws Exception {
		//1.获取图片名称
				String fileName = fileImage.getOriginalFilename();
				
				//2.创建文件对象,指定上传目录
				File dir = new File("F:/jt-images/");
				if(!dir.exists()) {
					//如果文件不存在,创建目录
					dir.mkdirs();
				}
				
				String path = "F:/jt-images/"+fileName;
				File file = new File(path);
				
				//3.利用工具API执行输出操作
				fileImage.transferTo(file);
				return "文件上传成功!!!!!";
	}
	
	
	
	@RequestMapping("/pic/upload")
	public ImageVO fileUpload(MultipartFile uploadFile) {
		return fileService.upload(uploadFile);
		
	}
	
	
	
	
	
	
	
	
	

}
