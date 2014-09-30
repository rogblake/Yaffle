package org.oatesonline.sandbox.resource;

	import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

	/**
	 * Resource which has only one representation.
	 * 
	 */
	public class HelloWorld extends ServerResource {

	    @Get
	    public String represent() {
	        return "hello, world (from the cloud!)";
	    }

	}
