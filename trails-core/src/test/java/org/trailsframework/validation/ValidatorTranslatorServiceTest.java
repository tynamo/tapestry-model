package org.trails.validation;

import java.util.Date;

import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.form.translator.FormatTranslator;
import org.apache.tapestry.form.translator.NumberTranslator;
import org.apache.tapestry.form.translator.Translator;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.StringValidator;
import org.jmock.MockObjectTestCase;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

public class ValidatorTranslatorServiceTest extends MockObjectTestCase
{

	ValidatorTranslatorService valTransService = new ValidatorTranslatorService();

	public void testGetValidator() throws Exception
	{
		IPropertyDescriptor numberDescriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
		NumberValidator validator = (NumberValidator) valTransService.getValidator(numberDescriptor);
		assertEquals("right value type", Double.class,
			validator.getValueTypeClass());
		IPropertyDescriptor descriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		descriptor.setLength(55);
		descriptor.setRequired(true);

		StringValidator stringValidator =
			(StringValidator) valTransService.getValidator(descriptor);
		assertTrue("is required", stringValidator.isRequired());

	}

	public void testGetTranslator() throws Exception
	{
		IPropertyDescriptor numberDescriptor = new TrailsPropertyDescriptor(Foo.class, "number", Double.class);
		Translator translator = valTransService.getTranslator(numberDescriptor);
		assertTrue(translator instanceof NumberTranslator);
		assertNotNull(((FormatTranslator) translator).getPattern());

		IPropertyDescriptor dateDescriptor = new TrailsPropertyDescriptor(Foo.class, "date", Date.class);
		translator = valTransService.getTranslator(dateDescriptor);
		assertNotNull(((FormatTranslator) translator).getPattern());
		dateDescriptor.setFormat("MM/dd/yyyy");
		translator = valTransService.getTranslator(dateDescriptor);
		assertTrue(translator instanceof DateTranslator);
		assertEquals("MM/dd/yyyy",
			((DateTranslator) translator).getPattern());

	}

}
