package org.trails.validation;

import org.apache.tapestry.IForm;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.RenderString;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Baz;
import org.trails.test.Foo;

public class TrailsValidationDelegateTest extends ComponentTest
{

	public TrailsValidationDelegateTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	Mock componentMock;

	public void setUp() throws Exception
	{
		Mock formMock = new Mock(IForm.class);
		componentMock = new Mock(IFormComponent.class);

//        TextField textField = (TextField)creator.newInstance(TextField.class);
//        textField.setForm((IForm)formMock.proxy());
//        textField.setName("foo");
//        Field[] fields = textField.getClass().getDeclaredFields();
//        Field displayNameField = textField.getClass().getDeclaredField("_$displayName");
//        displayNameField.setAccessible(true);
//        displayNameField.set(textField, "Description");

		IForm form = (IForm) formMock.proxy();
		//componentMock.expects(atLeastOnce()).method("getForm").will(returnValue(form));
		componentMock.expects(atLeastOnce()).method("getName").will(
			returnValue("foo"));
		componentMock.expects(atLeastOnce()).method("getDisplayName").will(returnValue("Description"));

		formMock.expects(atLeastOnce()).method("getName").will(returnValue("fooForm"));


		delegate.setFormComponent((IFormComponent) componentMock.proxy());


		delegate.recordFieldInputValue("foodly");


	}

	public void testRecordException() throws Exception
	{

		Exception exception = new Exception("error occurred.");
		Assert.assertFalse(delegate.getHasErrors());
		delegate.record(exception);
		Assert.assertTrue(delegate.getHasErrors());
	}

	public void testGetTrackingForField() throws Exception
	{

		IFieldTracking fieldTracking = delegate.getFieldTracking("Description");
		Assert.assertNotNull("now its there", fieldTracking);
		Assert.assertEquals("right one", "foo", fieldTracking.getFieldName());

	}

	public void testRecordInvalidStateException() throws Exception
	{
		//record second field input with different description
		// this mimics how Tapestry reuses the same component but
		// with different field names and display names
		componentMock.expects(atLeastOnce()).method("getDisplayName").will(
			returnValue("Description"));
		componentMock.reset();
		componentMock.expects(atLeastOnce()).method("getDisplayName").will(
			returnValue("OtherProperty"));
		componentMock.expects(atLeastOnce()).method("getName").will(
			returnValue("bar"));
		delegate.recordFieldInputValue("foodly");

		TrailsClassDescriptor descriptor = new TrailsClassDescriptor(Baz.class, "Baz");
		descriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "description", String.class));
		InvalidValue invalidValue = new InvalidValue("is too long", Baz.class, "description", "blarg", new Baz());
		InvalidStateException invalidStateException = new InvalidStateException(new InvalidValue[]{invalidValue});

		delegate.record(descriptor, invalidStateException);
		Assert.assertTrue(delegate.getHasErrors());
		IFieldTracking fieldTracking = (IFieldTracking)
			delegate.getFieldTracking("Description");
		Assert.assertTrue(fieldTracking.isInError());
		Assert.assertEquals("right field tracking", "foo", fieldTracking.getFieldName());
		RenderString renderString = (RenderString) fieldTracking.getErrorRenderer();
		Assert.assertEquals("Description is too long", renderString.getString());


		IFieldTracking nonErrorTracking = (IFieldTracking)
			delegate.getFieldTracking("OtherProperty");
		Assert.assertFalse(nonErrorTracking.isInError());

	}


	public void testRecordInvalidStateExceptionWithoutPropertyDescriptor() throws Exception
	{

		TrailsClassDescriptor descriptor = new TrailsClassDescriptor(Baz.class, "Baz");
		InvalidValue invalidValue = new InvalidValue("Is not a valid entity", Baz.class, "foo", "blarg", new Baz());
		InvalidStateException invalidStateException = new InvalidStateException(new InvalidValue[]{invalidValue});
		delegate.record(descriptor, invalidStateException);

		Assert.assertTrue(delegate.getHasErrors());
		Assert.assertFalse(delegate.getErrorRenderers().isEmpty());

		IFieldTracking fieldTracking = delegate.getFieldTracking("Description");
		Assert.assertTrue(fieldTracking.isInError());
		Assert.assertEquals("right field tracking", "foo", fieldTracking.getFieldName());
		RenderString renderString = (RenderString) fieldTracking.getErrorRenderer();
		Assert.assertEquals("Is not a valid entity", renderString.getString());

		IFieldTracking nonErrorTracking = delegate.getFieldTracking("OtherProperty");
		renderString = (RenderString) nonErrorTracking.getErrorRenderer();
		Assert.assertTrue(nonErrorTracking.isInError());
		Assert.assertEquals("Is not a valid entity", renderString.getString());


		nonErrorTracking = delegate.getFieldTracking("OtherOne");
		renderString = (RenderString) nonErrorTracking.getErrorRenderer();
		Assert.assertTrue(nonErrorTracking.isInError());
		Assert.assertEquals("Is not a valid entity", renderString.getString());

		renderString = (RenderString) delegate.getErrorRenderers().get(0);
		Assert.assertEquals("Is not a valid entity", renderString.getString());
	}

}