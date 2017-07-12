package com.neodisk.storage.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neodisk.api.APIMsgType;
import com.neodisk.exceptions.NeoException;
import com.neodisk.message.ResponseMessage;
import com.neodisk.message.body.None;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseMessage<None> defaultErrorHandler(HttpServletRequest req, Exception e) {
		NeoException ex = new NeoException("unknow exception", APIMsgType.inner_store_unknow_error);
		return new ResponseMessage<None>(ex);
	}

	@ExceptionHandler(IOException.class)
	@ResponseBody
	public ResponseMessage<None> handleIOException(HttpServletRequest request, Exception e) {
		NeoException ex = new NeoException("io exception", APIMsgType.inner_io_error);
		return new ResponseMessage<None>(ex);
	}

	@ExceptionHandler(NeoException.class)
	@ResponseBody
	public ResponseMessage<None> handleNeoException(HttpServletRequest request, NeoException e) {
		return new ResponseMessage<None>(e);
	}

	@ExceptionHandler(FileUploadException.class)
	@ResponseBody
	public ResponseMessage<None> handleFileUploadException(HttpServletRequest request, Exception e) {
		NeoException ex = new NeoException("file upload exception", APIMsgType.inner_store_error);
		return new ResponseMessage<None>(ex);
	}
}
