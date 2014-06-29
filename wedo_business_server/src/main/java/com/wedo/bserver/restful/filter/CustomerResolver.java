/**
 * 
 */
package com.wedo.bserver.restful.filter;

import org.restlet.util.Resolver;

/**
 * @author Administrator
 * 
 */
public class CustomerResolver extends Resolver<char[]> {

	/**
	 * Returns the value that corresponds to the given name.
	 */
	@Override
	public char[] resolve(String name) {
		// Could have a look into a database, LDAP directory, etc.
		if ("login".equals(name)) {
			return "secret".toCharArray();
		}

		return null;
	}

}
