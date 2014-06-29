/**
 * 
 */
package com.wedo.bserver.restful.resource;

import java.util.List;

import org.fecasmoy.restfultest.eyedemo.dao.CustomerDAO;
import org.fecasmoy.restfultest.eyedemo.entity.Customer;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * @author Administrator
 * 
 */
public class CustomersResource extends Resource {
	private CustomerDAO customerDAO;

	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		Customer customer = new Customer();
		customer.setName(form.getFirstValue("name"));
		customer.setAddress(form.getFirstValue("address"));
		customer.setRemarks("This is an example which receives request with put method and save data");

		customerDAO.saveCustomer(customer);
	}

	@Override
	public void init(Context context, Request request, Response response) {
		super.init(context, request, response);
	}

	public CustomersResource() {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	public CustomersResource(Context context, Request request, Response response) {
		super(context, request, response);

		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	public Representation represent(Variant variant) {
		List<Customer> list = customerDAO.getAllCustomers();
		Representation representation = new StringRepresentation(
				"--------------------", MediaType.TEXT_PLAIN);
		return representation;
	}

	@Override
	public boolean allowPut() {
		return true;
	}

	@Override
	public void storeRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		Customer customer = new Customer();
		customer.setName(form.getFirstValue("name"));
		customer.setAddress(form.getFirstValue("address"));
		customer.setRemarks("This is an example which receives request with put method and save data");

		customerDAO.saveCustomer(customer);
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
}
