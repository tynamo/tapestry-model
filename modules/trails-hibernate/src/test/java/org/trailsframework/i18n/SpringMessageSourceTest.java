/*
 * Created on 01/12/2005 by Eduardo Piva - eduardo@gwe.com.br
 *
 */
package org.trailsframework.i18n;

import java.util.Locale;

import junit.framework.TestCase;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.testhibernate.Bar;
import org.trails.testhibernate.Foo;
import org.trails.testhibernate.BarCollector;

public class SpringMessageSourceTest extends TestCase
{

	private SpringMessageSource messageSource;
	private TestLocaleHolder localeHolder;
	private TrailsClassDescriptor classDescriptor;
	private TrailsClassDescriptor secondClassDescriptor;
	private TrailsClassDescriptor thirdClassDescriptor;
	private IPropertyDescriptor numberPropertyDescriptor;
	private IPropertyDescriptor namePropertyDescriptor;
	private Locale pt = new Locale("pt");
	private Locale ptBR = new Locale("pt", "BR");
	private Locale en = Locale.ENGLISH;

	@Override
	protected void setUp() throws Exception
	{
		localeHolder = new TestLocaleHolder();
		messageSource = new SpringMessageSource();
		ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
		springMessageSource.setBasename("messagestest");
		messageSource.setMessageSource(springMessageSource);
		messageSource.setLocaleHolder(localeHolder);

		classDescriptor = new TrailsClassDescriptor(Foo.class);
		secondClassDescriptor = new TrailsClassDescriptor(Bar.class);
		thirdClassDescriptor = new TrailsClassDescriptor(BarCollector.class);
		numberPropertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Bar.class);
		namePropertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Bar.class);
		numberPropertyDescriptor.setName("number");
		numberPropertyDescriptor.setDisplayName("number");
		namePropertyDescriptor.setName("name");
		namePropertyDescriptor.setDisplayName("name");

	}

	private void displayNameTest(IDescriptor descriptor, Locale locale, String key, String expectedResult)
	{
		localeHolder.setLocale(locale);
		assertEquals(expectedResult, messageSource.getDisplayName(descriptor, key));
	}

	private void pluralDisplayNameTest(TrailsClassDescriptor descriptor, Locale locale, String key, String expectedResult)
	{
		localeHolder.setLocale(locale);
		assertEquals(expectedResult, messageSource.getPluralDislayName(descriptor, key));
	}

	public void testGetDisplayName()
	{
		displayNameTest(numberPropertyDescriptor, pt, "Default", "i18n ptnumber");
		displayNameTest(numberPropertyDescriptor, ptBR, "Default", "i18n ptnumber");
		displayNameTest(numberPropertyDescriptor, en, "Default", "i18n number");
		displayNameTest(namePropertyDescriptor, en, "Default", "i18n name");
		displayNameTest(namePropertyDescriptor, pt, "Default", "i18n ptname");
		displayNameTest(namePropertyDescriptor, ptBR, "Default", "i18n ptname");
		displayNameTest(classDescriptor, pt, "Default", "i18n ptFoo");
		displayNameTest(classDescriptor, ptBR, "Default", "i18n ptFoo");
		displayNameTest(classDescriptor, en, "Default", "i18n Foo");
		displayNameTest(secondClassDescriptor, pt, "Default", "i18n ptBar");
		displayNameTest(secondClassDescriptor, ptBR, "Default", "i18n ptBar");
		displayNameTest(secondClassDescriptor, en, "Default", "i18n Bar");
		displayNameTest(thirdClassDescriptor, pt, "Default", "Default");
		displayNameTest(thirdClassDescriptor, ptBR, "Default", "Default");
		displayNameTest(thirdClassDescriptor, en, "Default", "Default");
	}

	public void testGetPluralDisplayName()
	{
		pluralDisplayNameTest(classDescriptor, pt, "Default", "i18n ptFoo Plural");
		pluralDisplayNameTest(classDescriptor, ptBR, "Default", "i18n ptFoo Plural");
		pluralDisplayNameTest(classDescriptor, en, "Default", "i18n Foo Plural");
		pluralDisplayNameTest(secondClassDescriptor, pt, "Default", "i18n ptBar Plural");
		pluralDisplayNameTest(secondClassDescriptor, ptBR, "Default", "i18n ptBar Plural");
		pluralDisplayNameTest(secondClassDescriptor, en, "Default", "i18n Bar Plural");
		pluralDisplayNameTest(thirdClassDescriptor, pt, "Default", "Default");
		pluralDisplayNameTest(thirdClassDescriptor, ptBR, "Default", "Default");
	}

	public void testGetMessage()
	{
		String value;

		value = messageSource.getMessage("idon'texist", en);
		assertNull(value);

		value = messageSource.getMessage("idon'texist", pt);
		assertNull(value);

		value = messageSource.getMessage("idon'texist", ptBR);
		assertNull(value);

		value = messageSource.getMessage("name", pt);
		assertEquals(value, "i18n ptname");

		value = messageSource.getMessage("name", en);
		assertEquals(value, "i18n name");

		value = messageSource.getMessage("name", ptBR);
		assertEquals(value, "i18n ptname");
	}

	public void testGetMessageWithParameter()
	{
		Object[] args = new Object[]{"test"};
		String value;

		value = messageSource.getMessage("org.trails.component.newlink", args, en);
		assertEquals(value, "New test");

		value = messageSource.getMessage("org.trails.component.newlink", args, pt);
		assertEquals(value, "Adicionar test");

		value = messageSource.getMessage("org.trails.component.newlink", args, ptBR);
		assertEquals(value, "Adicionar test");

		value = messageSource.getMessage("idon'texist", args, en);
		assertNull(value);

		value = messageSource.getMessage("idon'texist", args, pt);
		assertNull(value);

		value = messageSource.getMessage("idon'texist", args, ptBR);
		assertNull(value);
	}

	public void testGetMessageWithDefaultValue()
	{
		String value;

		value = messageSource.getMessageWithDefaultValue("idon'texist", en, "DefaultMessage");
		assertEquals(value, "DefaultMessage");

		value = messageSource.getMessageWithDefaultValue("idon'texist", pt, "DefaultMessage");
		assertEquals(value, "DefaultMessage");

		value = messageSource.getMessageWithDefaultValue("idon'texist", ptBR, "DefaultMessage");
		assertEquals(value, "DefaultMessage");

		value = messageSource.getMessageWithDefaultValue("name", pt, "DefaultMessage");
		assertEquals(value, "i18n ptname");

		value = messageSource.getMessageWithDefaultValue("name", en, "DefaultMessage");
		assertEquals(value, "i18n name");

		value = messageSource.getMessageWithDefaultValue("name", ptBR, "DefaultMessage");
		assertEquals(value, "i18n ptname");
	}

	public void testGetMessageWithDefaultValueAndParameter()
	{
		Object[] args = new Object[]{"test"};
		String value;

		value = messageSource.getMessageWithDefaultValue("org.trails.component.newlink", args, en, "DefaultMessage");
		assertEquals(value, "New test");

		value = messageSource.getMessageWithDefaultValue("org.trails.component.newlink", args, pt, "DefaultMessage");
		assertEquals(value, "Adicionar test");

		value = messageSource.getMessageWithDefaultValue("org.trails.component.newlink", args, ptBR, "DefaultMessage");
		assertEquals(value, "Adicionar test");

		value = messageSource.getMessageWithDefaultValue("idon'texist", args, en, "DefaultMessage");
		assertEquals(value, "DefaultMessage");

		value = messageSource.getMessageWithDefaultValue("idon'texist", args, pt, "DefaultMessage");
		assertEquals(value, "DefaultMessage");

		value = messageSource.getMessageWithDefaultValue("idon'texist", args, ptBR, "DefaultMessage");
		assertEquals(value, "DefaultMessage");
	}

}
