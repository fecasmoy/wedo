/**
 * 
 */
package com.wedo.bserver.restful.filter;

import org.restlet.Context;
import org.restlet.Guard;
import org.restlet.data.ChallengeScheme;

/**
 * @author Administrator
 * 
 */
public class CustomerGuardFilter extends Guard {

	public CustomerGuardFilter() {
		this(Context.getCurrent(), ChallengeScheme.HTTP_DIGEST, "realm");
	}

	public CustomerGuardFilter(Context context, ChallengeScheme scheme,
			String realm) {
		super(context, scheme, realm);
	}

}