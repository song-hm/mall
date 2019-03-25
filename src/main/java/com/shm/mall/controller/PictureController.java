package com.shm.mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shm.mall.utils.FastDFSClient;
import com.shm.mall.utils.JsonUtils;

/**
 * 图片上传的controller
 * @author SHM
 *
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVICE_URL}")
	private String IMAGE_SERVICE_URL;
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		try {
			//得到图片上传的服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//得到文件扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//得到一个图片的地址和文件名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//补充为一个完整的URL
			url = IMAGE_SERVICE_URL + url;
			//封装到map中返回
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			String json = JsonUtils.objectToJson(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			String json = JsonUtils.objectToJson(result);
			return json;
		}
		
	}
}
