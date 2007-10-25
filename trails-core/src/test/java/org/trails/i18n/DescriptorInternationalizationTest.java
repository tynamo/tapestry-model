/*
 * Created on 01/12/2005 by Eduardo Piva - eduardo@gwe.com.br
 *
 */
package org.trails.i18n;

import java.util.Locale;

import junit.framework.TestCase;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.ReflectionDescriptorFactory;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Bar;
import org.trails.test.Foo;

public class DescriptorInternationalizationTest extends TestCase
{

	private IPropertyDescriptor propertyDescriptor;
	private IClassDescriptor classDescriptor;
	private Locale pt = new Locale("pt");
	private Locale ptBR = new Locale("pt", "BR");
	private Locale en = Locale.ENGLISH;
	private TestLocaleHolder localeHolder;

	@Override
	protected void setUp() throws Exception
	{
		localeHolder = new TestLocaleHolder();
		SpringMessageSource messageSource = new SpringMessageSource();
		ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
		springMessageSource.setBasename("messagestest");
		messageSource.setMessageSource(springMessageSource);
		messageSource.setLocaleHolder(localeHolder);

		ReflectionDescriptorFactory descriptorFactory = new ReflectionDescriptorFactory();
		classDescriptor = descriptorFactory.buildClassDescriptor(Foo.class);
		propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Bar.class);
		classDescriptor.setDisplayName("Foo");
		propertyDescriptor.setName("number");
		propertyDescriptor.setDisplayName("Number");

		DescriptorInternationalization.aspectOf().setTrailsMessageSource(messageSource);
	}

	public void testGetMethodDisplayName()
	{

		localeHolder.setLocale(null);
		String displayName = propertyDescriptor.getDisplayName();
		assertEquals("Number", displayName);

		localeHolder.setLocale(en);
		displayName = propertyDescriptor.getDisplayName();
		assertEquals("i18n number", displayName);

		localeHolder.setLocale(pt);
		displayName = propertyDescriptor.getDisplayName();
		assertEquals("i18n ptnumber", displayName);

		localeHolder.setLocale(ptBR);
		displayName = propertyDescriptor.getDisplayName();
		assertEquals("i18n ptnumber", displayName);
	}

	public void testGetClassDisplayName()
	{
		localeHolder.setLocale(null);
		String displayName = classDescriptor.getDisplayName();
		assertEquals("Foo", displayName);

		localeHolder.setLocale(en);
		displayName = classDescriptor.getDisplayName();
		assertEquals("i18n Foo", displayName);

		localeHolder.setLocale(pt);
		displayName = classDescriptor.getDisplayName();
		assertEquals("i18n ptFoo", displayName);

		localeHolder.setLocale(ptBR);
		displayName = classDescriptor.getDisplayName();
		assertEquals("i18n ptFoo", displayName);
	}

	public void testGetClassPluralDisplayName()
	{
		localeHolder.setLocale(null);
		String displayName = classDescriptor.getPluralDisplayName();
		assertEquals("Foos", displayName);

		localeHolder.setLocale(en);
		displayName = classDescriptor.getPluralDisplayName();
		assertEquals("i18n Foo Plural", displayName);

		localeHolder.setLocale(pt);
		displayName = classDescriptor.getPluralDisplayName();
		assertEquals("i18n ptFoo Plural", displayName);

		localeHolder.setLocale(ptBR);
		displayName = classDescriptor.getPluralDisplayName();
		assertEquals("i18n ptFoo Plural", displayName);
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
		DescriptorInternationalization.aspectOf().setTrailsMessageSource(null);
	}
}
