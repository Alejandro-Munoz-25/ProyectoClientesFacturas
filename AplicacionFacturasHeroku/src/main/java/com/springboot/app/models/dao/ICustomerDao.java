package com.springboot.app.models.dao;


import org.springframework.data.jpa.repository.Query;

//import java.util.List;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.app.models.entity.Customer;


public interface ICustomerDao extends PagingAndSortingRepository<Customer, Long> {
	@Query("select c from Customer c left join fetch c.invoices i where c.id=?1")
	public Customer fetchByIdWithInvoices(Long id);

}
