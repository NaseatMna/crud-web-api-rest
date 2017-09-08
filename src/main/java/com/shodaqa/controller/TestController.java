package com.shodaqa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Acer on 9/7/2017.
 */
@RestController
public class TestController {
	@RequestMapping(value = "/hello")
	public String hello(){
		return "Hello";
	}
}
