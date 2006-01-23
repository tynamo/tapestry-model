/*
 * Created on 21/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.trails.component;

import junit.framework.TestCase;

import org.apache.tapestry.IPage;
import org.apache.tapestry.test.Creator;
import org.jmock.cglib.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.hibernate.HasAssignedIdentifier;
import org.trails.hibernate.Interceptable;
import org.trails.page.EditPage;
import org.trails.page.ModelPage;
import org.trails.test.Foo;

public class ObjectActionsTest extends ComponentTest {

	private ObjectActions objectActions;
	private Creator creator = new Creator();
	private IClassDescriptor descriptor;
	private EditPage editPage;
	private Mock modelPage;
	
	@Override
	public void setUp() throws Exception {
		objectActions = (ObjectActions)creator.newInstance(ObjectActions.class);
		editPage = buildEditPage();
		descriptor = new TrailsClassDescriptor(Foo.class);
		descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").withAnyArguments().will(returnValue(descriptor));
		objectActions.setPage(editPage);
	}
	
	public void testShowRemoveButton() {
		Interceptable interceptable = new HasAssignedIdentifier() {
			public boolean isSaved() {
				return true;
			}
			public void onLoad() {}
			public void onSave() {}};
			
		editPage.setModel(interceptable);

		descriptor.setAllowRemove(true);
		assertTrue(objectActions.isShowRemoveButton());
		
		descriptor.setAllowRemove(false);
		assertTrue(!objectActions.isShowRemoveButton());

		interceptable = interceptable = new HasAssignedIdentifier() {
			public boolean isSaved() {
				return false;
			}
			public void onLoad() {}
			public void onSave() {}};
		editPage.setModel(interceptable);

		descriptor.setAllowRemove(true);
		assertTrue(!objectActions.isShowRemoveButton());
		
		descriptor.setAllowRemove(false);
		assertTrue(!objectActions.isShowRemoveButton());
	}
	
}
