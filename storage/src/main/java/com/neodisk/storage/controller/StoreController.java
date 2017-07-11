package com.neodisk.storage.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.neodisk.api.APIMsgType;
import com.neodisk.exceptions.NeoException;
import com.neodisk.message.ResponseMessage;
import com.neodisk.message.body.None;
import com.neodisk.mongo.store.domain.StoreInfo;
import com.neodisk.storage.service.StoreService;

@RestController
public class StoreController {
	@Autowired
	private StoreService storeService;

	@RequestMapping(value = "/storage/upload/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseMessage<None> upload(HttpServletRequest request, @PathVariable("id") String id)
			throws IOException, NeoException, FileUploadException {

		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iterator = upload.getItemIterator(request);
		if (iterator.hasNext()) {
			FileItemStream item = iterator.next();
			if (!item.isFormField()) {
				InputStream inputStream = item.openStream();
				storeService.save(id, inputStream);
			}
		}
		return new ResponseMessage<None>();
	}

	@RequestMapping(value = "/storage/download/{id}", method = RequestMethod.GET)
	public void download(HttpServletResponse response, @PathVariable("id") String id) throws IOException, NeoException {
		OutputStream outputStream = response.getOutputStream();
		storeService.read(id, outputStream);
	}
	
	@RequestMapping(value = "/storage/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") String id){
		storeService.delete(id);
	}

	@RequestMapping(value = "/storage/{id}", method = RequestMethod.GET)
	public ResponseMessage<StoreInfo> get(@PathVariable("id") String id) throws NeoException{
		StoreInfo storeInfo = storeService.get(id);
		if(storeInfo == null){
			throw new NeoException("store not found. id:" + id, APIMsgType.inner_store_not_found);
		}
		return new ResponseMessage<StoreInfo>(storeInfo);
	}
}
