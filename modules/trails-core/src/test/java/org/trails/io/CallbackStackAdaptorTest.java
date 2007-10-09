package org.trails.io;

import junit.framework.TestCase;


public class CallbackStackAdaptorTest extends TestCase
{

	String string = "http://www.trailsframework.org/Customizing+pages#Customizingpages-CustomizedTrailspages@Uhttp://svn.trails.codehaus.org/@Uhttp://www.ohloh.net/projects/336?p=Trails+Framework@Uhttp://localhost:8080/trailsPage.svc?&classDescriptor=YDorg.trails.demo.Car&pageType=List@Uhttp://forum.java.sun.com/thread.jspa?threadID=651046&messageID=3828775@Uhttp://svn.trails.codehaus.org/@Uhttp://www.ohloh.net/projects/336?p=Trails+Framework@Uhttp://localhost:8080/trailsPage.svc?&classDescriptor=YDorg.trails.demo.Car&pageType=List@Uhttp://forum.java.sun.com/thread.jspa?threadID=651046&messageID=3828775@Uhttp://svn.trails.codehaus.org/@Uhttp://www.ohloh.net/projects/336?p=Trails+Framework@Uhttp://localhost:8080/trailsPage.svc?&classDescriptor=YDorg.trails.demo.Car&pageType=List";

	public void testCompressUncompress() {
		CallbackStackAdaptor adaptor = new CallbackStackAdaptor();
		String compressed = adaptor.compress(string);
		assertEquals(string, adaptor.uncompress(compressed));
	}
}
