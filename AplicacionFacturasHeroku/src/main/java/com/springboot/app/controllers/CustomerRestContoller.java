package com.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.models.service.ICustomerService;
import com.springboot.app.view.xml.CustomeList;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestContoller {

	@Autowired
	private ICustomerService customerService;

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public CustomeList list() {
		return new CustomeList(customerService.findAll());
	}
}
