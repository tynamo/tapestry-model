/*
 * Created on 01/12/2005 by Eduardo Piva - eduardo@gwe.com.br
 *
 */
package org.tynamo.i18n;

import java.util.Locale;

import org.tynamo.component.ComponentTest;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.IDescriptor;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.ReflectionDescriptorFactory;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.test.Bar;
import org.tynamo.test.Foo;

public class DescriptorInternationalizationTest extends ComponentTest
{

	private IPropertyDescriptor propertyDescriptor;
	private TrailsClassDescriptor classDescriptor;
	private Locale pt = new Locale("pt");
	private Locale ptBR = new Locale("pt", "BR");
	private Locale en = Locale.ENGLISH;

	@Override
	protected void setUp() throws Exception
	{
		// no need to call super.setUp();

		TrailsMessageSource messageSource = getNewTrailsMessageSource();

		ReflectionDescriptorFactory descriptorFactory = new ReflectionDescriptorFactory();
		classDescriptor = descriptorFactory.buildClassDescriptor(Foo.class);
		propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Bar.class);
		classDescriptor.setDisplayName("Foo");
		propertyDescriptor.setName("number");
		propertyDescriptor.setDisplayName("Number");

		DescriptorInternationalization.aspectOf().setTrailsMessageSource(messageSource);
	}

	private void displayNameTest(Locale locale, IDescriptor descriptor, String expected)
	{
		threadLocale.setLocale(locale);
		String displayName = descriptor.getDisplayName();
		assertEquals(expected, displayName);
	}

	private void pluralDisplayNameTest(Locale locale, TrailsClassDescriptor descriptor, String expected)
	{
		threadLocale.setLocale(locale);
		String displayName = descriptor.getPluralDisplayName();
		assertEquals(expected, displayName);
	}

	public void testPropertyDescriptorDisplayNameWithoutI18N()
	{
		DescriptorInternationalization.aspectOf().setTrailsMessageSource(null);
		displayNameTest(en, propertyDescriptor, "Number");
	}

	public void testPropertyDescriptorDisplayName()
	{
		displayNameTest(en, propertyDescriptor, "i18n number");
		displayNameTest(pt, propertyDescriptor, "i18n ptnumber");
		displayNameTest(ptBR, propertyDescriptor, "i18n ptnumber");
	}

	public void testClassDescriptorDisplayNameWithoutI18N()
	{
		DescriptorInternationalization.aspectOf().setTrailsMessageSource(null);
		displayNameTest(en, classDescriptor, "Foo");
	}

	public void testClassDescriptorDisplayName()
	{
		displayNameTest(en, classDescriptor, "i18n Foo");
		displayNameTest(pt, classDescriptor, "i18n ptFoo");
		displayNameTest(ptBR, classDescriptor, "i18n ptFoo");
	}

	public void testClassDescriptorPluralDisplayNameWithoutI18N()
	{
		DescriptorInternationalization.aspectOf().setTrailsMessageSource(null);
		pluralDisplayNameTest(en, classDescriptor, "Foos");
	}

	public void testGetClassPluralDisplayName()
	{
		pluralDisplayNameTest(en, classDescriptor, "i18n Foo Plural");
		pluralDisplayNameTest(pt, classDescriptor, "i18n ptFoo Plural");
		pluralDisplayNameTest(ptBR, classDescriptor, "i18n ptFoo Plural");
		String displayName;
	}
}
