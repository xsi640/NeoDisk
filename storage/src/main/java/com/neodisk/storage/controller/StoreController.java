package com.neodisk.storage.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.neodisk.api.APIMsgType;
import com.neodisk.commons.TypeParserUtils;
import com.neodisk.exceptions.NeoException;
import com.neodisk.message.ResponseMessage;
import com.neodisk.message.body.None;
import com.neodisk.mongo.store.domain.Store;
import com.neodisk.storage.service.StoreService;

@Controller
@RequestMapping("/store/**")
public class StoreController {

	@Autowired
	private StoreService storeService;

	@RequestMapping(name = "/get", method = RequestMethod.GET)
	public ResponseMessage<Store> get(@RequestParam(name = "id", required = true) String id) throws NeoException {
		Store s = storeService.get(id);
		if (s == null) {
			throw new NeoException("store not find. id:" + id, APIMsgType.inner_store_not_found);
		}
		return ResponseMessage.create(s);
	}

	@RequestMapping(name = "/delete", method = RequestMethod.GET)
	public ResponseMessage<None> delete(@RequestParam(name = "id", required = true) String id) {
		storeService.delete(id);
		return ResponseMessage.createNoneBody();
	}

	@RequestMapping(name = "/save", method = RequestMethod.POST)
	public ResponseMessage<Store> save(@RequestParam(name = "id", required = true) String id,
			@RequestParam(name = "length", required = true) long length) {
		Store s = storeService.save(id, length);
		return ResponseMessage.create(s);
	}

	public ResponseMessage<Store> finish(@RequestParam(name = "id", required = true) String id) throws NeoException {
		Store s = storeService.finish(id);
		return ResponseMessage.create(s);
	}

	@RequestMapping(name = "/upload", method = RequestMethod.GET)
	public ResponseMessage<Store> upload(@RequestParam(name = "id", required = true) String id,
			HttpServletRequest request) throws NeoException, IOException {
		long length = request.getContentLengthLong();
		Store s = storeService.upload(id, length, request.getInputStream());
		return ResponseMessage.create(s);
	}

	@RequestMapping(name = "/download", method = RequestMethod.GET)
	public void download(@RequestParam(name = "id", required = true) String id,
						 HttpServletRequest request,
						 HttpServletResponse response) throws NeoException, IOException {
		long position = TypeParserUtils.toLong(request.getHeader("pos"));
		storeService.download(id, position, response.getOutputStream());
	}
}
