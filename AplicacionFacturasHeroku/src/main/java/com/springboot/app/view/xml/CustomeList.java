package com.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.springboot.app.models.entity.Customer;

@XmlRootElement(name = "customers")
public class CustomeList {
	@XmlElement(name = "customer")
	public List<Customer> customers;

	public CustomeList() {
	}

	public CustomeList(List<Customer> customers) {
		this.customers = customers;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

}
