/*
 * Created on 01/12/2005 by Eduardo Piva - eduardo@gwe.com.br
 *
 */
package org.trails.i18n;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.hivemind.service.ThreadLocale;
import org.jmock.Mock;
import org.jmock.core.Stub;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.StubSequence;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.servlet.TrailsApplicationServlet;
import org.trails.test.Bar;
import org.trails.test.Foo;

public class DescriptorInternationalizationTest extends TestCase {

	private IPropertyDescriptor propertyDescriptor;
	private IClassDescriptor classDescriptor;
	private Locale pt = new Locale("pt");
	private Locale ptBR = new Locale("pt", "BR");
	private Locale en = Locale.ENGLISH;	
	private ApplicationContext appContext;

	@Override
	protected void setUp() throws Exception {
		// appContext will initialize the aspect
        appContext = new ClassPathXmlApplicationContext(
        "applicationContext-test.xml");
        classDescriptor = new TrailsClassDescriptor(Foo.class);
        propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Bar.class);
        classDescriptor.setDisplayName("Foo");
        propertyDescriptor.setName("number");
        propertyDescriptor.setDisplayName("number");
		TrailsApplicationServlet.setCurrentLocale(null);
        localeHolderMock = new Mock(LocaleHolder.class);
        DescriptorInternationalization.aspectOf().setLocaleHolder(
                (LocaleHolder)localeHolderMock.proxy());
        localeHolderMock.expects(new InvokeAtLeastOnceMatcher()).method("getLocale")
        .will(new StubSequence(new Stub[] {
                new ReturnStub(null),
                new ReturnStub(en),
                new ReturnStub(pt),
                new ReturnStub(ptBR)
        }));        
	}
	
    Mock localeHolderMock;
    
	public void testGetMethodDisplayName() {

		String displayName = propertyDescriptor.getDisplayName();
		assertEquals(displayName, "Number");
		
		TrailsApplicationServlet.setCurrentLocale(en);
		displayName = propertyDescriptor.getDisplayName();
		assertEquals(displayName, "i18n number");

		TrailsApplicationServlet.setCurrentLocale(pt);
		displayName = propertyDescriptor.getDisplayName();
		assertEquals(displayName, "i18n ptnumber");

		TrailsApplicationServlet.setCurrentLocale(ptBR);
		displayName = propertyDescriptor.getDisplayName();
		assertEquals(displayName, "i18n ptnumber");
	}
	
	public void testGetClassDisplayName() {
		String displayName = classDescriptor.getDisplayName();
		assertEquals(displayName, "Foo");
		
		TrailsApplicationServlet.setCurrentLocale(en);
		displayName = classDescriptor.getDisplayName();
		assertEquals(displayName, "i18n Foo");
		
		TrailsApplicationServlet.setCurrentLocale(pt);
		displayName = classDescriptor.getDisplayName();
		assertEquals(displayName, "i18n ptFoo");

		TrailsApplicationServlet.setCurrentLocale(ptBR);
		displayName = classDescriptor.getDisplayName();
		assertEquals(displayName, "i18n ptFoo");
	}
	
	public void testGetClassPluralDisplayName() {
		String displayName = classDescriptor.getPluralDisplayName();
		assertEquals(displayName, "Foos");
		
		TrailsApplicationServlet.setCurrentLocale(en);
		displayName = classDescriptor.getPluralDisplayName();
		assertEquals(displayName, "i18n Foo Plural");
		
		TrailsApplicationServlet.setCurrentLocale(pt);
		displayName = classDescriptor.getPluralDisplayName();
		assertEquals(displayName, "i18n ptFoo Plural");

		TrailsApplicationServlet.setCurrentLocale(ptBR);
		displayName = classDescriptor.getPluralDisplayName();
		assertEquals(displayName, "i18n ptFoo Plural");
	}

	@Override
	protected void tearDown() throws Exception
	{
		TrailsApplicationServlet.setCurrentLocale(null);
	}
	
	
	
}
