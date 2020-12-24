package com.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.ICustomerDao;
import com.springboot.app.models.dao.IInvoiceDao;
import com.springboot.app.models.dao.IProductDao;
import com.springboot.app.models.entity.Customer;
import com.springboot.app.models.entity.Invoice;
import com.springboot.app.models.entity.Product;

@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerDao customerDao;
	@Autowired
	private IInvoiceDao invoiceDao;

	@Autowired
	private IProductDao productDao;

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		return (List<Customer>) customerDao.findAll();
	}

	@Override
	@Transactional
	public void saveCustomer(Customer customer) {
		customerDao.save(customer);
	}

	@Override
	@Transactional(readOnly = true)
	public Customer findByID(Long id) {
		return customerDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		customerDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Customer> findAll(Pageable pageable) {
		return customerDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> finName(String term) {

		return productDao.findByNameLikeIgnoreCase("%".concat(term).concat("%"));
	}

	@Override
	@Transactional
	public void saveInvoice(Invoice invoice) {
		invoiceDao.save(invoice);
	}

	@Override
	@Transactional(readOnly = true)
	public Product findProductById(Long id) {
		return productDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Invoice findInvoiceById(Long id) {
		return invoiceDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteInvoice(Long id) {
		invoiceDao.deleteById(id);
		;
	}

	@Override
	@Transactional(readOnly = true)
	public Invoice fetchByIdWithCustomerWithInvoiceItemWithProduct(Long id) {
		return invoiceDao.fetchByIdWithCustomerWithInvoiceItemWithProduct(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Customer fetchByIdWithInvoices(Long id) {
		return customerDao.fetchByIdWithInvoices(id);
	}

}
