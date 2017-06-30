package com.neodisk.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.neodisk.service.StoreService;

@Controller
public class StoreController {
	@Autowired
	private StoreService storeService;
	
	
}
