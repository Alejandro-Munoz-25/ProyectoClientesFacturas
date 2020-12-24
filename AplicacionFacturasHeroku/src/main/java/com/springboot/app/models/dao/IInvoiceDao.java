package com.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Invoice;

public interface IInvoiceDao extends CrudRepository<Invoice, Long> {
	@Query("select f from Invoice f join fetch f.customer c join fetch f.items i join fetch i.product where f.id=?1")
	public Invoice fetchByIdWithCustomerWithInvoiceItemWithProduct(Long id);
}
