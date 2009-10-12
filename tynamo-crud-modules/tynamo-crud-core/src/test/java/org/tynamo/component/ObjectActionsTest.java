/*
 * Created on 21/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.tynamo.component;

import org.apache.tapestry.test.Creator;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.page.EditPage;
import org.tynamo.test.Foo;

public class ObjectActionsTest extends ComponentTest
{

	private ObjectActions objectActions;
	private Creator creator = new Creator();
	private TrailsClassDescriptor descriptor;
	private EditPage editPage;

	@Override
	public void setUp() throws Exception
	{
		objectActions = (ObjectActions) creator.newInstance(ObjectActions.class);
		descriptor = new TrailsClassDescriptor(Foo.class);
		descriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, "id", Integer.class));
		editPage = buildEditPage();
		editPage.setClassDescriptor(descriptor);
		objectActions.setPage(editPage);
	}

	public void testShowRemoveButton()
	{

		Foo foo = new Foo();
		foo.setId(1);

		editPage.setModel(foo);

		descriptor.setAllowRemove(true);
		assertTrue(objectActions.isShowRemoveButton());

		descriptor.setAllowRemove(false);
		assertTrue(!objectActions.isShowRemoveButton());

		foo.setId(null);
		editPage.setModel(foo);
		editPage.setModelNew(true);

		descriptor.setAllowRemove(true);
		assertTrue(!objectActions.isShowRemoveButton());

		descriptor.setAllowRemove(false);
		assertTrue(!objectActions.isShowRemoveButton());
	}

}
