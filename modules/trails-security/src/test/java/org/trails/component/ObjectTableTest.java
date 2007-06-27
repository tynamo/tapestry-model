/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.IPage;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;
import org.hibernate.criterion.DetachedCriteria;
import org.jmock.Mock;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.ReflectionDescriptorFactory;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.security.SecurityAuthorities;
import org.trails.security.test.FooSecured;
import org.trails.test.Foo;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectTableTest extends ComponentTest
{
	HibernateObjectTable objectTable;
	Mock psvcMock = new Mock(HibernatePersistenceService.class);
	Mock pageMock = new Mock(IPage.class);
	IPage page;
	SecurityAuthorities authorities;
	IClassDescriptor fooSecuredDescriptor;
	IdentifierDescriptor idSecured;
	IPropertyDescriptor nameSecured;
	IPropertyDescriptor fooFieldSecured;
	Map components = new HashMap();

	public void setUp() throws Exception
	{

		authorities = new SecurityAuthorities();

		page = (IPage) pageMock.proxy();
		objectTable = (HibernateObjectTable) creator.newInstance(HibernateObjectTable.class, new Object[]{
			"hibernatePersistenceService", psvcMock.proxy()
		});
		objectTable.setShowCollections(false);
		Block idColumnValue = (Block) creator.newInstance(Block.class);
		idColumnValue.setId("linkColumnValue");

		idColumnValue.setPage(page);
		idColumnValue.setContainer(objectTable);
		objectTable.addComponent(idColumnValue);
		objectTable.setPage(page);
		objectTable.setContainer(page);

		ReflectionDescriptorFactory descriptorFactory = new ReflectionDescriptorFactory();
		IClassDescriptor classDescriptor = descriptorFactory.buildClassDescriptor(Foo.class);
		//IClassDescriptor classDescriptor = new TrailsClassDescriptor(Foo.class);
		//fooSecuredDescriptor = new TrailsClassDescriptor(FooSecured.class);
		fooSecuredDescriptor = descriptorFactory.buildClassDescriptor(FooSecured.class);
		List propertyDescriptors = new ArrayList();
		List fooSecuredPropertyDescriptors = new ArrayList();
		IdentifierDescriptor idProp = new IdentifierDescriptor(Foo.class, classDescriptor.getPropertyDescriptor("id") );
		//IdentifierDescriptor idProp = new IdentifierDescriptor(Foo.class,"id", Integer.class);

		IPropertyDescriptor multiWordProp = new TrailsPropertyDescriptor(Foo.class, "multiWordProperty", String.class);


		IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "hidden", String.class);
		hiddenDescriptor.setHidden(true);

		IPropertyDescriptor summaryDescriptor = new TrailsPropertyDescriptor(Foo.class, "nonSummary", String.class);
		summaryDescriptor.setSummary(false);

		CollectionDescriptor bazzesDesriptor = new CollectionDescriptor(Foo.class, Set.class);
		bazzesDesriptor.setName("bazzes");

		idSecured = new IdentifierDescriptor(FooSecured.class, classDescriptor.getPropertyDescriptor("id") );
		//idSecured = new IdentifierDescriptor(FooSecured.class, "id", Integer.class);
		nameSecured = new IdentifierDescriptor(FooSecured.class, classDescriptor.getPropertyDescriptor("name") );
		//nameSecured = new TrailsPropertyDescriptor(FooSecured.class, "name", String.class);
		fooFieldSecured = new TrailsPropertyDescriptor(FooSecured.class, "Foo Field", String.class);

		propertyDescriptors.add(idProp);
		propertyDescriptors.add(multiWordProp);
		propertyDescriptors.add(hiddenDescriptor);
		propertyDescriptors.add(summaryDescriptor);
		propertyDescriptors.add(bazzesDesriptor);

		fooSecuredPropertyDescriptors.add(idSecured);
		fooSecuredPropertyDescriptors.add(nameSecured);
		fooSecuredPropertyDescriptors.add(fooFieldSecured);

		classDescriptor.setPropertyDescriptors(propertyDescriptors);
		fooSecuredDescriptor.setPropertyDescriptors(fooSecuredPropertyDescriptors);
		objectTable.setClassDescriptor(classDescriptor);
	}

	public void testGetColumns() throws Exception
	{
		pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(components));
		pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("fooPage"));
		pageMock.expects(atLeastOnce()).method("getIdPath").will(returnValue(null));
		List columns = objectTable.getColumns();
		assertEquals("2 columns", 2, columns.size());
		assertTrue(columns.get(0) instanceof TrailsTableColumn);
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		assertEquals("Id", idColumn.getDisplayName());

		Block fakeBlock = (Block) creator.newInstance(Block.class);
		components.put("multiWordPropertyColumnValue", fakeBlock);
		objectTable.setPropertyNames(new String[]{"multiWordProperty"});
		columns = objectTable.getColumns();
		assertEquals("1 column", 1, columns.size());
		TrailsTableColumn column = (TrailsTableColumn) columns.get(0);
		assertNotNull(column.getBlockAddress());

	}

	public void testGetBlockAddress() throws Exception
	{

		Block fakeBlock = (Block) creator.newInstance(Block.class);
		components.put("nameColumnValue", fakeBlock);
		pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(components));
		pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("listPage"));
		IPropertyDescriptor descriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		ComponentAddress address = objectTable.getBlockAddress(descriptor);
		assertEquals("listPage", address.getPageName());
		assertEquals("nameColumnValue", address.getIdPath());

		descriptor = new TrailsPropertyDescriptor(Foo.class, "wump", String.class);
		assertNull(objectTable.getBlockAddress(descriptor));
	}

	public void testGetLinkBlockAddress() throws Exception
	{
		Map components = new HashMap();
		pageMock.expects(atLeastOnce()).method("getIdPath").will(returnValue(null));
		pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(components));
		pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("listPage"));
		IPropertyDescriptor descriptor = new TrailsPropertyDescriptor(Foo.class, "id", Integer.class);
		ComponentAddress address = objectTable.getLinkBlockAddress(descriptor);
		assertEquals("linkColumnValue", address.getIdPath());
		Block fakeBlock = (Block) creator.newInstance(Block.class);
		components.put("idColumnValue", fakeBlock);
		address = objectTable.getLinkBlockAddress(descriptor);
		assertEquals("idColumnValue", address.getIdPath());
	}

	public void testGetColumnsWithSecurity() throws Exception
	{

		pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(components));
		pageMock.expects(atLeastOnce()).method("getIdPath").will(returnValue(null));
		pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("fooPage"));

		objectTable.setClassDescriptor(fooSecuredDescriptor);
		fooSecuredDescriptor.setAllowSave(false);
		nameSecured.setHidden(true);

		List columns = objectTable.getColumns();
		/* name must be hidden */
		assertEquals(columns.size(), 2);
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		TrailsTableColumn fooField = (TrailsTableColumn) columns.get(1);
		assertEquals("Id", idColumn.getDisplayName());
		assertEquals("Foo Field", fooField.getDisplayName());

		/* Id must be link because we can still delete the item */
		assertNotNull(idColumn.getBlockAddress());

		fooSecuredDescriptor.setAllowSave(true);
		fooSecuredDescriptor.setAllowRemove(false);

		columns = objectTable.getColumns();
		/* name must be hidden */
		assertEquals(columns.size(), 2);
		idColumn = (TrailsTableColumn) columns.get(0);
		fooField = (TrailsTableColumn) columns.get(1);

		/* Id must be link because we can still update/save the item */
		assertNotNull(idColumn.getBlockAddress());

		fooSecuredDescriptor.setAllowSave(false);
		fooSecuredDescriptor.setAllowRemove(false);

		columns = objectTable.getColumns();
		/* name must be hidden */
		assertEquals(columns.size(), 2);
		idColumn = (TrailsTableColumn) columns.get(0);
		fooField = (TrailsTableColumn) columns.get(1);

		/* Id can't be a link*/
		assertNull(idColumn.getBlockAddress());
	}

	public void testGetColumnWithSecurityAdminRole() throws Exception
	{
		pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("fooPage"));
		pageMock.expects(atLeastOnce()).method("getIdPath").will(returnValue(null));
		pageMock.expects(atLeastOnce()).method("getComponents").will(returnValue(components));
		objectTable.setClassDescriptor(fooSecuredDescriptor);

		List columns = objectTable.getColumns();
		/* name must be hidden */
		assertEquals(columns.size(), 3);
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		TrailsTableColumn nameField = (TrailsTableColumn) columns.get(1);
		TrailsTableColumn fooField = (TrailsTableColumn) columns.get(2);
		assertEquals("Id", idColumn.getDisplayName());
		assertEquals("Foo Field", fooField.getDisplayName());
		assertEquals("Name", nameField.getDisplayName());

		/* Id must be a link */
		assertNotNull(idColumn.getBlockAddress());
	}

	public void testGetTableSource() throws Exception
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
		objectTable.setCriteria(criteria);
		HibernateTableModel tableModel = (HibernateTableModel) objectTable.getSource();

		assertNotNull(tableModel.getPersistenceService());
		assertEquals(criteria, tableModel.getCriteria());

		objectTable.setCriteria(null);
		List instances = new ArrayList();
		objectTable.setInstances(instances);
		assertEquals(instances, objectTable.getSource());
	}

	public void testGetIdentifierProperty() throws Exception
	{
		assertEquals("right id prop", "id", objectTable.getIdentifierProperty());
	}
}