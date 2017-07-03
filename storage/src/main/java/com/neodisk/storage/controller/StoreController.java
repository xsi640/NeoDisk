package com.neodisk.storage.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.neodisk.commons.UUIDUtils;
import com.neodisk.service.StoreService;

@Controller
public class StoreController {
	@Autowired
	private StoreService storeService;

	@RequestMapping("/file")
	public String file() {
		return "/file";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public String handleFileUpload(HttpServletRequest request) throws IOException, Exception{
		storeService.save(UUIDUtils.randomUUIDString(), request.getInputStream());
		return "successed";
	}
}
