/**
 * 
 */
package com.wedo.bserver.restful.resource;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * 
 * @author ajax
 * @version 1.0
 */
public class OrderResource extends Resource {
	String orderId = "";
	String subOrderId = "";

	public OrderResource(Context context, Request request, Response response) {
		super(context, request, response);
		orderId = (String) request.getAttributes().get("orderId");
		subOrderId = (String) request.getAttributes().get("subOrderId");
		// This representation has only one type of representation.
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	public Representation getRepresentation(Variant variant) {
		Representation representation = new StringRepresentation(
				"the order id is : " + orderId + " and the sub order id is : "
						+ subOrderId, MediaType.TEXT_PLAIN);
		return representation;
	}
}
