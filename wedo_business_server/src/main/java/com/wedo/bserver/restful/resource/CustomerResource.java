/**
 * 
 */
package com.wedo.bserver.restful.resource;

import org.fecasmoy.restfultest.eyedemo.dao.CustomerDAO;
import org.fecasmoy.restfultest.eyedemo.entity.Customer;
import org.restlet.Context;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class CustomerResource extends Resource {
	String customerId = "";
	private CustomerDAO customerDAO;

	@Override
	public void init(Context context, Request request, Response response) {
		super.init(context, request, response);
		// -------------basic 认证
		ChallengeResponse challengeResponse = request.getChallengeResponse();
		if (challengeResponse != null) {
			String userName = challengeResponse.getIdentifier();
			String password = new String(request.getChallengeResponse()
					.getSecret());

			// here is to get password from database through user name, suppose
			// the password is "tiger"
			if (!"tiger".equals(password)) {
				response.setEntity("User name and password are not match",
						MediaType.TEXT_PLAIN);
				setModifiable(false);
			}
		}
		// -------------basic 认证
		customerId = (String) request.getAttributes().get("custId");
	}

	public CustomerResource() {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	public CustomerResource(Context context, Request request, Response response) {
		super(context, request, response);

		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	public Representation getRepresentation(Variant variant) {
		String userMsg = customerDAO.getCustomerById(customerId);
		Representation representation = new StringRepresentation(userMsg,
				MediaType.TEXT_PLAIN);
		return representation;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@Override
	public boolean allowPut() {
		return true;
	}

	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public boolean allowDelete() {
		return true;
	}

	@Override
	public void storeRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		Customer customer = new Customer();
		customer.setName(form.getFirstValue("name"));
		customer.setRemarks("This is an example which receives request with put method and save data");

		customerDAO.saveCustomer(customer);
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		Customer customer = new Customer();
		customer.setId(customerId);
		customer.setName(form.getFirstValue("name"));
		customer.setRemarks("This is an example which receives request with post method and update data");

		customerDAO.updateCustomer(customer);
	}

	@Override
	public void removeRepresentations() throws ResourceException {
		customerDAO.deleteCustomer(customerId);
	}
}