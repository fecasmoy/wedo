/**
 * 
 */
package com.wedo.bserver.restful.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

import com.wedo.bserver.restful.resource.OrderResource;

public class OrderApplication extends Application {
	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createRoot() {
		Router router = new Router(getContext());

		// router.attach("", OrderResource.class);
		router.attach("/orders/{orderId}/{subOrderId}", OrderResource.class);
		return router;
	}

}
