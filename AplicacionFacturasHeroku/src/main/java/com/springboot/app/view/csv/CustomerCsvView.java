package com.springboot.app.view.csv;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.springboot.app.models.entity.Customer;

@Component(value = "list.csv")
public class CustomerCsvView extends AbstractView {

	public CustomerCsvView() {
		setContentType("text/csv");
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv \"");

		@SuppressWarnings("unchecked")
		List<Customer> customersList = (List<Customer>) model.get("customers");
		response.setContentType(getContentType());

		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		// Nombres de los atrinutos de la clase
		String[] header = { "id", "name", "surname", "email", "createAt" };
		beanWriter.writeHeader(header);

		for (Customer customer : customersList) {
			beanWriter.write(customer, header);
		}
		beanWriter.close();
	}

}
