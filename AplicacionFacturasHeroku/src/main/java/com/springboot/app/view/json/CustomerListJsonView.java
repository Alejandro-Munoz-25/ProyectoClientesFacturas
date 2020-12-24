package com.springboot.app.view.json;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.springboot.app.models.entity.Customer;

@Component("list.json")
public class CustomerListJsonView extends MappingJackson2JsonView {

	@Override
	protected Object filterModel(Map<String, Object> model) {
		model.remove("page");
		model.remove("title");
		@SuppressWarnings("unchecked")
		List<Customer> customers = (List<Customer>) model.get("customers");
		model.remove("customers");
		model.put("customersList", customers);
		return super.filterModel(model);
	}

}
