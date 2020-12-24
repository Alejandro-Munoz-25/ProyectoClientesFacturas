package com.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.models.entity.Customer;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.Product;

public interface ICustomerService {

	public List<Customer> findAll();

	public Page<Customer> findAll(Pageable pageable);

	public void saveCustomer(Customer customer);

	public Customer findByID(Long id);

	public void delete(Long id);

	public List<Product> finName(String term);

	public void saveInvoice(Invoice invoice);

	public Product findProductById(Long id);

	public Invoice findInvoiceById(Long id);

	public void deleteInvoice(Long id);

	public Invoice fetchByIdWithCustomerWithInvoiceItemWithProduct(Long id);

	public Customer fetchByIdWithInvoices(Long id);
}
