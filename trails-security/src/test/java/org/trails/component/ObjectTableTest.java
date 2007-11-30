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
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.IAdvancedTableColumn;
import org.apache.tapestry.contrib.table.model.common.BlockTableRendererSource;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.util.ComponentAddress;
import org.apache.hivemind.Messages;
import org.hibernate.criterion.DetachedCriteria;
import org.jmock.Mock;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
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


public class ObjectTableTest extends MockObjectTestCase
{

	Creator creator = new Creator();
	HibernateObjectTable objectTable;
	Messages messages;
	HibernatePersistenceService persistenceService;
	IPage page;
	IRequestCycle cycle;
	IClassDescriptor classDescriptor;

	SecurityAuthorities authorities;
	IClassDescriptor fooSecuredDescriptor;
	IdentifierDescriptor idSecured;
	IPropertyDescriptor nameSecured;
	IPropertyDescriptor fooFieldSecured;
	Map components = new HashMap();

	public void setUp() throws Exception
	{

		persistenceService = mock(HibernatePersistenceService.class);
		page = mock(IPage.class);
		cycle = mock(IRequestCycle.class);
		messages = mock(Messages.class);

		authorities = new SecurityAuthorities();
		objectTable = (HibernateObjectTable) creator.newInstance(HibernateObjectTable.class,
				new Object[]{"id", "myObjectTable", "hibernatePersistenceService", persistenceService });

		objectTable.setShowCollections(false);

		Block idColumnValue = (Block) creator.newInstance(Block.class,
				new Object[]{"id", "linkColumnValue", "page", page, "container", objectTable});

		objectTable.addComponent(idColumnValue);
		objectTable.setPage(page);
		objectTable.setContainer(page);

		ReflectionDescriptorFactory descriptorFactory = new ReflectionDescriptorFactory();
		classDescriptor = descriptorFactory.buildClassDescriptor(Foo.class);

		fooSecuredDescriptor = descriptorFactory.buildClassDescriptor(FooSecured.class);
		List propertyDescriptors = new ArrayList();
		List fooSecuredPropertyDescriptors = new ArrayList();
		IdentifierDescriptor idProp = new IdentifierDescriptor(Foo.class, classDescriptor.getPropertyDescriptor("id"));
		//IdentifierDescriptor idProp = new IdentifierDescriptor(Foo.class,"id", Integer.class);

		IPropertyDescriptor multiWordProp = new TrailsPropertyDescriptor(Foo.class, "multiWordProperty", String.class);


		IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "hidden", String.class);
		hiddenDescriptor.setHidden(true);

		IPropertyDescriptor summaryDescriptor = new TrailsPropertyDescriptor(Foo.class, "nonSummary", String.class);
		summaryDescriptor.setSummary(false);

		CollectionDescriptor bazzesDesriptor = new CollectionDescriptor(Foo.class, "bazzes", Set.class);

		idSecured = new IdentifierDescriptor(FooSecured.class, classDescriptor.getPropertyDescriptor("id"));
		//idSecured = new IdentifierDescriptor(FooSecured.class, "id", Integer.class);
		nameSecured = new IdentifierDescriptor(FooSecured.class, classDescriptor.getPropertyDescriptor("name"));
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

		checking(new Expectations()
		{
			{
				atLeast(1).of(page).getComponents(); will(returnValue(components));
				atLeast(1).of(page).getPageName(); will(returnValue("fooPage"));
				atLeast(1).of(page).getIdPath(); will(returnValue("fooPageIdPath"));
				atLeast(1).of(page).getMessages(); will(returnValue(messages));
				atLeast(1).of(messages).getMessage("multiWordProperty"); will(returnValue("multiWordProperty"));
				atLeast(1).of(messages).getMessage("Id"); will(returnValue("Id"));
			}
		});

		objectTable.prepareForRender(cycle);
		List columns = objectTable.getColumns();
		assertEquals("2 columns", 2, columns.size());
		assertTrue(columns.get(0) instanceof TrailsTableColumn);
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		assertEquals("Id", idColumn.getDisplayName());

		Block fakeBlock = (Block) creator.newInstance(Block.class,
				new Object[]{"id", "multiWordPropertyColumnValue", "page", page, "container", page});

		components.put("multiWordPropertyColumnValue", fakeBlock);
		objectTable.setPropertyNames(new String[]{"multiWordProperty"});

		objectTable.prepareForRender(cycle);
		columns = objectTable.getColumns();
		assertEquals("1 column", 1, columns.size());
		TrailsTableColumn column = (TrailsTableColumn) columns.get(0);
		assertNotNull(column);
	}

	public void testGetBlockAddress() throws Exception
	{
		String fakeBlockName = "nameColumnValue";

		checking(new Expectations()
		{
			{
				atLeast(1).of(page).getComponents(); will(returnValue(components));
				atLeast(1).of(page).getPageName(); will(returnValue("fooPage"));
				atLeast(1).of(page).getIdPath(); will(returnValue("fooPageIdPath"));
				atLeast(1).of(page).getMessages(); will(returnValue(messages));
				atLeast(1).of(messages).getMessage("multiWordProperty"); will(returnValue("multiWordProperty"));
				atLeast(1).of(messages).getMessage("Id"); will(returnValue("Id"));
				atLeast(1).of(messages).getMessage("name"); will(returnValue("name"));
			}
		});

		Block fakeBlock = (Block) creator.newInstance(Block.class, new Object[]{"id", fakeBlockName, "page", page, "container", page});
		components.put(fakeBlockName, fakeBlock);

		IPropertyDescriptor descriptor = new TrailsPropertyDescriptor(Foo.class, "name", String.class);
		classDescriptor.getPropertyDescriptors().add(descriptor);

		objectTable.prepareForRender(null);
		List<ITableColumn> columns = objectTable.getColumns();
		assertEquals("3 columns", 3, columns.size());

		IAdvancedTableColumn column = null;

		for (ITableColumn aux : columns)
		{
			if (aux.getColumnName().equals("name"))
			{
				column = (IAdvancedTableColumn) aux;
				break;
			}
		}

		assertNotNull(column);

		ComponentAddress address = ((BlockTableRendererSource) column.getValueRendererSource()).getBlockAddress();
		assertEquals("fooPage", address.getPageName());
		assertEquals("fooPageIdPath." + fakeBlockName, address.getIdPath());
	}

	public void testGetLinkBlockAddress() throws Exception
	{
		checking(new Expectations()
		{
			{
				atLeast(1).of(page).getComponents(); will(returnValue(components));
				atLeast(1).of(page).getPageName(); will(returnValue("fooPage"));
				atLeast(1).of(page).getIdPath(); will(returnValue("fooPageIdPath"));
				atLeast(1).of(page).getMessages(); will(returnValue(messages));
				atLeast(1).of(messages).getMessage("multiWordProperty"); will(returnValue("multiWordProperty"));
				atLeast(1).of(messages).getMessage("Id"); will(returnValue("Id"));
			}
		});
		
		objectTable.prepareForRender(cycle);
		List columns = objectTable.getColumns();
		assertEquals("2 columns", 2, columns.size());
		assertTrue(columns.get(0) instanceof TrailsTableColumn);
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		assertEquals("Id", idColumn.getDisplayName());

		ComponentAddress address = ((BlockTableRendererSource) idColumn.getValueRendererSource()).getBlockAddress();

		assertNotNull(address);
		assertEquals("fooPageIdPath.myObjectTable.linkColumnValue", address.getIdPath());

		String fakeBlockName = "idColumnValue";
		Block fakeBlock = (Block) creator.newInstance(Block.class, new Object[]{"id", fakeBlockName, "page", page, "container", page});
		components.put(fakeBlockName, fakeBlock);

		objectTable.prepareForRender(null);
		columns = objectTable.getColumns();
		assertEquals("2 columns", 2, columns.size());
		assertTrue(columns.get(0) instanceof TrailsTableColumn);
		idColumn = (TrailsTableColumn) columns.get(0);
		assertEquals("Id", idColumn.getDisplayName());

		address = ((BlockTableRendererSource) idColumn.getValueRendererSource()).getBlockAddress();
		assertNotNull(address);
		assertEquals("fooPageIdPath.idColumnValue", address.getIdPath());
	}


	public void testGetColumnsWithSecurity() throws Exception
	{

		checking(new Expectations()
		{
			{
				atLeast(1).of(page).getComponents(); will(returnValue(components));
				atLeast(1).of(page).getPageName(); will(returnValue("fooPage"));
				atLeast(1).of(page).getIdPath(); will(returnValue("fooPageIdPath"));
				atLeast(1).of(page).getMessages(); will(returnValue(messages));
				atLeast(1).of(messages).getMessage("Foo Field"); will(returnValue("Foo Field"));
				atLeast(1).of(messages).getMessage("Id"); will(returnValue("Id"));
			}
		});

		objectTable.setClassDescriptor(fooSecuredDescriptor);
		fooSecuredDescriptor.setAllowSave(false);
		nameSecured.setHidden(true);

		objectTable.prepareForRender(cycle);
		List columns = objectTable.getColumns();

		/* name must be hidden */
		assertEquals(2, columns.size());
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		TrailsTableColumn fooField = (TrailsTableColumn) columns.get(1);
		assertEquals("Id", idColumn.getDisplayName());
		assertEquals("Foo Field", fooField.getDisplayName());

		/* Id must be link because we can still delete the item */
		assertTrue(idColumn.getValueRendererSource() instanceof BlockTableRendererSource);

		fooSecuredDescriptor.setAllowSave(true);
		fooSecuredDescriptor.setAllowRemove(false);

		objectTable.prepareForRender(cycle);
		columns = objectTable.getColumns();

		/* name must be hidden */
		assertEquals(columns.size(), 2);
		idColumn = (TrailsTableColumn) columns.get(0);
		fooField = (TrailsTableColumn) columns.get(1);

		/* Id must be link because we can still update/save the item */
		assertTrue(idColumn.getValueRendererSource() instanceof BlockTableRendererSource);

		fooSecuredDescriptor.setAllowSave(false);
		fooSecuredDescriptor.setAllowRemove(false);

		objectTable.prepareForRender(cycle);
		columns = objectTable.getColumns();
		/* name must be hidden */
		assertEquals(columns.size(), 2);
		idColumn = (TrailsTableColumn) columns.get(0);
		fooField = (TrailsTableColumn) columns.get(1);

		/* Id can't be a link*/
		assertFalse(idColumn.getValueRendererSource() instanceof BlockTableRendererSource);
	}

	public void testGetColumnWithSecurityAdminRole() throws Exception
	{
		checking(new Expectations()
		{
			{
				atLeast(1).of(page).getComponents(); will(returnValue(components));
				atLeast(1).of(page).getPageName(); will(returnValue("fooPage"));
				atLeast(1).of(page).getIdPath(); will(returnValue("fooPageIdPath"));
				atLeast(1).of(page).getMessages(); will(returnValue(messages));
				atLeast(1).of(messages).getMessage("Id"); will(returnValue("Id"));
				atLeast(1).of(messages).getMessage("Name"); will(returnValue("Name"));
				atLeast(1).of(messages).getMessage("Foo Field"); will(returnValue("Foo Field"));
			}
		});

		objectTable.setClassDescriptor(fooSecuredDescriptor);

		objectTable.prepareForRender(cycle);
		List columns = objectTable.getColumns();

		/* name must be hidden */
		assertEquals(3, columns.size());
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		TrailsTableColumn nameField = (TrailsTableColumn) columns.get(1);
		TrailsTableColumn fooField = (TrailsTableColumn) columns.get(2);
		assertEquals("Id", idColumn.getDisplayName());
		assertEquals("Foo Field", fooField.getDisplayName());
		assertEquals("Name", nameField.getDisplayName());

		/* Id must be a link */
		assertTrue(idColumn.getValueRendererSource() instanceof BlockTableRendererSource);
	}

	public void testGetTableSource() throws Exception
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
		objectTable.setCriteria(criteria);
		assertTrue(objectTable.getSource() instanceof HibernateTableModel);
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