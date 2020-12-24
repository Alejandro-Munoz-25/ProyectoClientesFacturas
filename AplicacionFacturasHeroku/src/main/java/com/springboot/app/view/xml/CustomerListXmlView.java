package com.springboot.app.view.xml;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.springboot.app.models.entity.Customer;

@Component("list.xml")
public class CustomerListXmlView extends MarshallingView {
	@Autowired
	public CustomerListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.remove("title");
		model.remove("page");
		@SuppressWarnings("unchecked")
		List<Customer> customers = (List<Customer>) model.get("customers");
		model.put("customersList", new CustomeList(customers));
		model.remove("customers");
		super.renderMergedOutputModel(model, request, response);
	}

}
