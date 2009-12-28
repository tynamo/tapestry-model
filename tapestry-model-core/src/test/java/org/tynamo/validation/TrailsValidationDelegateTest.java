package org.tynamo.validation;

import org.apache.tapestry.IForm;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IFieldTracking;
import org.jmock.Mock;
import org.tynamo.component.ComponentTest;

public class TrailsValidationDelegateTest extends ComponentTest
{

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
		assertFalse(delegate.getHasErrors());
		delegate.record(exception);
		assertTrue(delegate.getHasErrors());
	}

	public void testGetTrackingForField() throws Exception
	{

		IFieldTracking fieldTracking = delegate.getFieldTracking("Description");
		assertNotNull("now its there", fieldTracking);
		assertEquals("right one", "foo", fieldTracking.getFieldName());

	}


}
