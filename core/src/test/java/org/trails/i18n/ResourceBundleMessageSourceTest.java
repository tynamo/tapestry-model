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
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Bar;
import org.trails.test.Foo;
import org.trails.test.TestTest;

public class ResourceBundleMessageSourceTest extends TestCase {

	private DefaultTrailsResourceBundleMessageSource messageSource;
	private IClassDescriptor classDescriptor;
	private IClassDescriptor secondClassDescriptor;
	private IClassDescriptor thirdClassDescriptor;
	private IPropertyDescriptor numberPropertyDescriptor;
	private IPropertyDescriptor namePropertyDescriptor;
	private Locale pt = new Locale("pt");
	private Locale ptBR = new Locale("pt", "BR");
	private Locale en = Locale.ENGLISH;	
	
	@Override
	protected void setUp() throws Exception {
        messageSource = new DefaultTrailsResourceBundleMessageSource();
        ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
        springMessageSource.setBasename("messagestest");
        messageSource.setMessageSource(springMessageSource);
        
        classDescriptor = new TrailsClassDescriptor(Foo.class);
        secondClassDescriptor = new TrailsClassDescriptor(Bar.class);
        thirdClassDescriptor = new TrailsClassDescriptor(TestTest.class);
        numberPropertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Bar.class);
        namePropertyDescriptor = new TrailsPropertyDescriptor(Foo.class, Bar.class);
        numberPropertyDescriptor.setName("number");
        numberPropertyDescriptor.setDisplayName("number");
        namePropertyDescriptor.setName("name");
        namePropertyDescriptor.setDisplayName("name");
        
	}
	
	public void testGetDisplayName() {
		String value;
		
		value = messageSource.getDisplayName(numberPropertyDescriptor, pt, "Default");
		assertEquals(value, "i18n ptnumber");
		
		value = messageSource.getDisplayName(numberPropertyDescriptor, ptBR, "Default");
		assertEquals(value, "i18n ptnumber");

		value = messageSource.getDisplayName(numberPropertyDescriptor, en, "Default");
		assertEquals(value, "i18n number");
		
		value = messageSource.getDisplayName(namePropertyDescriptor, en, "Default");
		assertEquals(value, "i18n name");

		value = messageSource.getDisplayName(namePropertyDescriptor, pt, "Default");
		assertEquals(value, "i18n ptname");
		
		value = messageSource.getDisplayName(namePropertyDescriptor, ptBR, "Default");
		assertEquals(value, "i18n ptname");

		value = messageSource.getDisplayName(classDescriptor, pt, "Default");
		assertEquals(value, "i18n ptFoo");

		value = messageSource.getDisplayName(classDescriptor, ptBR, "Default");
		assertEquals(value, "i18n ptFoo");

		value = messageSource.getDisplayName(classDescriptor, en, "Default");
		assertEquals(value, "i18n Foo");

		value = messageSource.getDisplayName(secondClassDescriptor, pt, "Default");
		assertEquals(value, "i18n ptBar");

		value = messageSource.getDisplayName(secondClassDescriptor, ptBR, "Default");
		assertEquals(value, "i18n ptBar");

		value = messageSource.getDisplayName(secondClassDescriptor, en, "Default");
		assertEquals(value, "i18n Bar");

		value = messageSource.getDisplayName(thirdClassDescriptor, pt, "Default");
		assertEquals(value, "Default");
		
		value = messageSource.getDisplayName(thirdClassDescriptor, ptBR, "Default");
		assertEquals(value, "Default");

		value = messageSource.getDisplayName(thirdClassDescriptor, en, "Default");
		assertEquals(value, "Default");

	}
	
	public void testGetPluralDisplayName() {
		String value;
		
		value = messageSource.getPluralDislayName(classDescriptor, pt, "Default");
		assertEquals(value, "i18n ptFoo Plural");

		value = messageSource.getPluralDislayName(classDescriptor, ptBR, "Default");
		assertEquals(value, "i18n ptFoo Plural");

		value = messageSource.getPluralDislayName(classDescriptor, en, "Default");
		assertEquals(value, "i18n Foo Plural");

		value = messageSource.getPluralDislayName(secondClassDescriptor, pt, "Default");
		assertEquals(value, "i18n ptBar Plural");

		value = messageSource.getPluralDislayName(secondClassDescriptor, ptBR, "Default");
		assertEquals(value, "i18n ptBar Plural");

		value = messageSource.getPluralDislayName(secondClassDescriptor, en, "Default");
		assertEquals(value, "i18n Bar Plural");

		value = messageSource.getPluralDislayName(thirdClassDescriptor, pt, "Default");
		assertEquals(value, "Default");		

		value = messageSource.getPluralDislayName(thirdClassDescriptor, ptBR, "Default");
		assertEquals(value, "Default");		
	}
	
	public void testGetMessage() {
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
	
	public void testGetMessageWithParameter() {
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

	public void testGetMessageWithDefaultValue() {
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
	
	public void testGetMessageWithDefaultValueAndParameter() {
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
