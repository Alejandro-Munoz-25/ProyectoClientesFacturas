package com.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.entity.Customer;
import com.springboot.app.models.service.ICustomerService;
import com.springboot.app.models.service.IUploadFileService;
import com.springboot.app.util.paginator.PageRender;
import com.springboot.app.view.xml.CustomeList;

@Controller
@SessionAttributes("customer")
public class CustomerController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private MessageSource messagesSources;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IUploadFileService uploadFileService;
	/*-----------------------------------------------------------------------------------*/
	/*----------------------------------Método List-------------------------------------*/
	/*-----------------------------------------------------------------------------------*/

	@RequestMapping(value = { "/list-rest" }, method = RequestMethod.GET)
	@ResponseBody
	public CustomeList listRest() {
		return new CustomeList(customerService.findAll());
	}

	@RequestMapping(value = { "/list", "/" }, method = RequestMethod.GET)
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "format", defaultValue = "html") String format, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale) {

		if (authentication != null) {
			logger.info("Hi	,".concat(authentication.getName().toString()));
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.info("Forma estatica SecurityContextHolder.getContext().getAuthentication(); Hi	,"
					.concat(auth.getName().toString()));
		}

		if (hasRole("ROLE_ADMIN")) {
			logger.info("Hi	".concat(auth.getName()).concat(" you have access"));
		} else {
			logger.info("Hi	".concat(auth.getName()).concat(" you don't have access"));
		}

		if (format.equals("html")) {
			Pageable pagerequest = PageRequest.of(page, 4);
			Page<Customer> customersPage = customerService.findAll(pagerequest);
			PageRender<Customer> pageRender = new PageRender<>("/list", customersPage);
			model.addAttribute("title", messagesSources.getMessage("text.cliente.listar.titulo", null, locale));
			model.addAttribute("customers", customersPage);
			model.addAttribute("page", pageRender);
		} else {
			model.addAttribute("customers", customerService.findAll());
		}
		return "list";
	}

	/*-----------------------------------------------------------------------------------*/
	/*--------------------------Metodos Formulario---------------------------------------*/
	/*-----------------------------------------------------------------------------------*/
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String create(Map<String, Object> model, Locale locale) {

		Customer customer = new Customer();
		model.put("title", messagesSources.getMessage("text.cliente.form.titulo", null, locale));
		model.put("customer", customer);
		return "form";
	}

	@GetMapping("/form/")
	public String redirectAfterPost(@Valid Customer customer, BindingResult result, Model model, Locale locale) {
		if (result.hasErrors()) {
			model.addAttribute("title", messagesSources.getMessage("text.cliente.form.titulo", null, locale));
			return "form";
		}
		model.addAttribute("title", messagesSources.getMessage("text.cliente.form.titulo", null, locale));
		return "form";
	}

	@Secured(value = "ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String find(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash,
			Locale locale) {
		Customer customer = null;
		if (id > 0) {
			customer = customerService.findByID(id);
			if (customer == null) {
				flash.addFlashAttribute("error",
						messagesSources.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/list";
			}
		} else {
			flash.addFlashAttribute("error", messagesSources.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/list";
		}
		model.put("title", messagesSources.getMessage("text.cliente.form.titulo", null, locale));
		model.put("customer", customer);
		return "form";
	}

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form/", method = RequestMethod.POST)
	public String edit(@Valid Customer customer, BindingResult result, Model model,
			@RequestParam(name = "file") MultipartFile file, RedirectAttributes flash, SessionStatus status,
			Locale locale) {

		if (result.hasErrors()) {
			model.addAttribute("title", messagesSources.getMessage("text.cliente.form.titulo", null, locale));
			return "form";
		}
		if (!file.isEmpty()) {
			if (customer.getId() != null && customer.getId() > 0 && customer.getPhoto() != null
					&& customer.getPhoto().length() > 0) {
				uploadFileService.delete(customer.getPhoto());
			}
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", messagesSources
					.getMessage("text.cliente.flash.foto.subir.success", null, locale).concat(" " + uniqueFilename));
			customer.setPhoto(uniqueFilename);
		} else {
			customer.setPhoto("");
		}
		customer.setName(customer.getName().trim().replaceAll(" +", " "));
		customer.setSurname(customer.getSurname().trim().replaceAll(" +", " "));
		String message = (customer.getId() != null)
				? messagesSources.getMessage("text.cliente.flash.editar.success", null, locale)
				: messagesSources.getMessage("text.cliente.flash.crear.success", null, locale);
		customerService.saveCustomer(customer);
		status.isComplete();
		flash.addFlashAttribute("success", message);

		return "redirect:/list";
	}

	@Secured(value = "ROLE_ADMIN")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {

		if (id > 0) {
			Customer customer = customerService.findByID(id);
			customerService.delete(id);
			flash.addFlashAttribute("success",
					messagesSources.getMessage("text.cliente.flash.eliminar.success", null, locale));
			if (uploadFileService.delete(customer.getPhoto())) {
				String mensajeFotoEliminar = String.format(
						messagesSources.getMessage("text.cliente.flash.foto.eliminar.success", null, locale),
						customer.getPhoto());
				flash.addFlashAttribute("info", mensajeFotoEliminar);
			}
		} else {
			return "redirect:/list";
		}
		return "redirect:/list";
	}

	/*-----------------------------------------------------------------------------------*/
	/*--------------------------Método para almacenar imagen-----------------------------*/
	/*-----------------------------------------------------------------------------------*/
	@Secured(value = { "ROLE_USER", "ROLE_ADMIN" })
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {

		Resource resource = null;
		try {
			resource = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/*-----------------------------------------------------------------------------------*/
	/*----------------------------Metodos Detalles---------------------------------------*/
	/*-----------------------------------------------------------------------------------*/
	@Secured(value = "ROLE_USER")
	@GetMapping(value = "detail/{id}")
	public String detail(@PathVariable(name = "id") Long id, Map<String, Object> model, RedirectAttributes flash,
			Locale locale) {
		Customer customer = customerService.fetchByIdWithInvoices(id);// customerService.findByID(id);
		if (customer == null) {
			flash.addAttribute("error", messagesSources.getMessage(" text.cliente.flash.id.error)", null, locale));
			return "redirect:/list";
		}
		model.put("title", messagesSources.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ")
				.concat(customer.getName().concat(" ").concat(customer.getSurname())));
		model.put("customer", customer);
		return "detail";
	}
	/*-----------------------------------------------------------------------------------*/
	/*--------------------------- Método validador de roles------------------------------*/
	/*-----------------------------------------------------------------------------------*/

	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		if (auth == null) {
			return false;
		}
//		Todo rol debe de implementar GrantedAuthority 
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority(role));
	}

}
