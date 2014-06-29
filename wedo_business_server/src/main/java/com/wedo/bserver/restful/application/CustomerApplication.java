/**
 * 
 */
package com.wedo.bserver.restful.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

import com.wedo.bserver.restful.resource.CustomerResource;

//public class CustomerApplication extends Application {
//	@Override
//	public synchronized Restlet createRoot() {
//		Router router = new Router(getContext());
//
//		router.attach("", CustomerResource.class);
////		router.attach("/orders/{orderId}", CustomerResource.class);
//		router.attach("/customers/{custId}", CustomerResource.class);
//		return router;
//	}
//}
public class CustomerApplication extends Application{
	@Override
	public synchronized Restlet createRoot() {
		Router router = new Router(getContext());
		
//		router.attach("", CustomerResource.class);
		router.attach("/customers/{custId}", CustomerResource.class);
		return router;
	}
}