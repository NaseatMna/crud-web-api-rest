package com.shodaqa.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shodaqa.models.Test;
import com.shodaqa.models.forms.TestForm;
import com.shodaqa.services.TestService;
import com.shodaqa.utils.NullAndEnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;

import static com.shodaqa.common.CoreConstants.DEFAULT_CONTENT_TYPE;
import static com.shodaqa.helpers.response.ResponseHelpers.responseSuccess;

/**
 * Created by Acer on 9/7/2017.
 */
@RestController
@RequestMapping(value = "${version}/areas", produces = DEFAULT_CONTENT_TYPE)
public class TestController extends BaseController{

	@Autowired
	private TestService testService;

	@RequestMapping(method = RequestMethod.POST)
	public String createTest(HttpServletRequest request, HttpServletResponse response,
							 @RequestBody TestForm testForm)throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
		Test test = new Test();
		NullAndEnumUtils.getInstance().copyProperties(test,testForm);
		testService.save(test);
		return responseSuccess(request,response);


	}
}
