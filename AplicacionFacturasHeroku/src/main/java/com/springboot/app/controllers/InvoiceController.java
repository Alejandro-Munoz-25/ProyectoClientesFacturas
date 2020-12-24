package com.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.entity.Customer;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.InvoiceItem;
import com.springboot.app.models.entity.Product;
import com.springboot.app.models.service.ICustomerService;

@Secured(value = "ROLE_ADMIN") // todos los métodos necesitan tener el role admin
@Controller
@RequestMapping(path = "/invoice")
@SessionAttributes("invoice") 
public class InvoiceController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	ICustomerService customerService;
	@Autowired
	private MessageSource messagesSources;

	@GetMapping("/form/")
	public String redirect(@RequestParam(name = "item_Id[]", required = false) Long[] itemId, Model model,
			Locale locale, @Valid Invoice invoice, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("title", messagesSources.getMessage("text.factura.form.titulo", null, locale));
			return "invoice/form";
		}
		if (itemId == null || itemId.length <= 0) {
			model.addAttribute("title", messagesSources.getMessage("text.factura.form.titulo", null, locale));
			model.addAttribute("error", messagesSources.getMessage("text.factura.flash.lineas.error", null, locale));
			return "invoice/form";
		}
		model.addAttribute("title", messagesSources.getMessage("text.factura.form.titulo", null, locale));
		return "invoice/form";
	}

	@GetMapping(value = "/form/{customerId}")
	public String createInvoice(@PathVariable(value = "customerId") Long customerId, Model model,
			RedirectAttributes flash, Locale locale) {
		Customer customer = null;
		if (customerId > 0 && customerId != null) {
			customer = customerService.findByID(customerId);
			if (customer == null) {
				flash.addFlashAttribute("error",
						messagesSources.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/list";
			}
		} else {
			flash.addFlashAttribute("error", messagesSources.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/list";
		}
		Invoice invoice = new Invoice();
		invoice.setCustomer(customer);

		model.addAttribute("title", messagesSources.getMessage("text.factura.form.titulo", null, locale));
		model.addAttribute("invoice", invoice);
		return "invoice/form";
	}

//@ResponseBody suprime el cargar una vista de thymeleaf y toma el resultado convertido a json
	@GetMapping(value = "/load-products/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> productList(@PathVariable String term) {
		List<Product> listProducts = customerService.finName(term);
		for (int i = 0; i < listProducts.size(); i++) {
			log.info("Producto[" + i + "]: " + listProducts.get(i).getName());
		}
		return listProducts;
	}

//	@PostMapping(value = "/form/{id}")
	@PostMapping(value = "/form/")
	public String save(@RequestParam(name = "item_Id[]", required = false) Long[] itemId,
			@RequestParam(name = "amount[]", required = false) Integer[] amount, @Valid Invoice invoice,
			BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Locale locale) {
		if (result.hasErrors()) {
			model.addAttribute("title", messagesSources.getMessage("text.factura.form.titulo", null, locale));
			return "invoice/form";
		}
		if (itemId == null || itemId.length <= 0 || amount.length <= 0) {
			model.addAttribute("title", messagesSources.getMessage("text.factura.form.titulo", null, locale));
			model.addAttribute("error", messagesSources.getMessage("text.factura.flash.lineas.error", null, locale));
			return "invoice/form";
		}

		for (int i = 0; i < itemId.length; i++) {
			Product product = customerService.findProductById(itemId[i]);
			InvoiceItem line = new InvoiceItem();
			List<Integer> listAmount = new ArrayList<Integer>(Arrays.asList(amount));
			List<Long> listItemsId = new ArrayList<Long>(Arrays.asList(itemId));
			log.info("lista	" + listAmount.toString());
			if (listAmount.get(i) == null || listAmount.get(i) <= 0) {
				listAmount.remove(i);
				listItemsId.remove(i);
				amount = listAmount.stream().toArray(n -> new Integer[n]);
				itemId = listItemsId.stream().toArray(n -> new Long[n]);
			}
			if (amount.length <= 0) {
				model.addAttribute("title", messagesSources.getMessage("text.factura.form.titulo", null, locale));
				model.addAttribute("error",
						messagesSources.getMessage("text.factura.flash.lineas.error", null, locale));
				return "invoice/form";
			}
			line.setAmount(amount[i]);
			line.setProduct(product);
			invoice.addItemInvoice(line);
			log.info("Id: ".concat(itemId[i].toString().concat(", amount:	").concat(amount[i].toString())));
		}
		customerService.saveInvoice(invoice);
		status.setComplete();
		flash.addFlashAttribute("success",
				messagesSources.getMessage("text.factura.flash.crear.success", null, locale));
		return "redirect:/detail/" + invoice.getCustomer().getId();
	}

	@GetMapping(value = "/detail/{id}")
	public String detailInvoice(@PathVariable(name = "id") Long id, Model model, RedirectAttributes flash,
			Locale locale) {

		Invoice invoice = customerService.fetchByIdWithCustomerWithInvoiceItemWithProduct(id);// customerService.findInvoiceById(id);
		if (invoice == null) {
			flash.addAttribute("error", messagesSources.getMessage("text.factura.flash.db.error", null, locale));
			return "redirect:/list";
		}
		model.addAttribute("invoice", invoice);
		String title = String.format(messagesSources.getMessage("text.factura.ver.titulo", null, locale),
				invoice.getDescription());
		model.addAttribute("title", title);
		return "invoice/detail";
	}

	@GetMapping(value = "/delete/{id}")
	public String deleteInvoice(@PathVariable long id, RedirectAttributes flash, Model model, Locale locale) {
		if (id > 0) {
			Invoice invoice = customerService.findInvoiceById(id);
			if (invoice != null) {
				customerService.deleteInvoice(id);
				flash.addFlashAttribute("success",
						messagesSources.getMessage("text.factura.flash.eliminar.success", null, locale));
				return "redirect:/detail/" + invoice.getCustomer().getId();
			} else {
				flash.addFlashAttribute("error",
						messagesSources.getMessage("text.factura.flash.db.error", null, locale));
				return "redirect:/list";
			}
		}
		return "redirect:/list";
	}
}
