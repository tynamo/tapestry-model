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

import org.apache.hivemind.Messages;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IPage;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.contrib.table.model.IAdvancedTableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.common.BlockTableRendererSource;
import org.apache.tapestry.test.Creator;
import org.apache.tapestry.util.ComponentAddress;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.trails.descriptor.*;
import org.trails.persistence.PersistenceService;
import org.trails.test.Foo;

import java.util.*;

public class ObjectTableTest extends MockObjectTestCase
{
	Creator creator = new Creator();
	ObjectTable objectTable;

	IPage page;
	Messages messages;
	PersistenceService persistenceService;

	Map components = new HashMap();
	IClassDescriptor classDescriptor;

	public void setUp() throws Exception
	{
		page = mock(IPage.class);
		messages = mock(Messages.class);
		persistenceService = mock(PersistenceService.class);

		ReflectionDescriptorFactory descriptorFactory = new ReflectionDescriptorFactory();
		classDescriptor = descriptorFactory.buildClassDescriptor(Foo.class);

		List propertyDescriptors = new ArrayList();
		IdentifierDescriptor idProp = new IdentifierDescriptor(Foo.class, classDescriptor.getPropertyDescriptor("id"));

		IPropertyDescriptor multiWordProp = new TrailsPropertyDescriptor(Foo.class, "multiWordProperty", String.class);

		IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "hidden", String.class);
		hiddenDescriptor.setHidden(true);

		IPropertyDescriptor summaryDescriptor = new TrailsPropertyDescriptor(Foo.class, "nonSummary", String.class);
		summaryDescriptor.setSummary(false);

		CollectionDescriptor bazzesDesriptor = new CollectionDescriptor(Foo.class, "bazzes", Set.class);

		propertyDescriptors.add(idProp);
		propertyDescriptors.add(multiWordProp);
		propertyDescriptors.add(hiddenDescriptor);
		propertyDescriptors.add(summaryDescriptor);
		propertyDescriptors.add(bazzesDesriptor);

		classDescriptor.setPropertyDescriptors(propertyDescriptors);

		objectTable = (ObjectTable) creator.newInstance(ObjectTable.class,
				new Object[]{"id", "myTable", "persistenceService", persistenceService, "page", page, "container", page, "classDescriptor", classDescriptor});
		objectTable.setShowCollections(false);

		Block idColumnValue = (Block) creator.newInstance(Block.class,
				new Object[]{"id", "linkColumnValue", "page", page, "container", objectTable});

		objectTable.addComponent(idColumnValue);

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

		objectTable.prepareForRender(null);
	}

	public void testGetColumns() throws Exception
	{
		List columns = objectTable.getColumns();
		assertEquals("2 columns", 2, columns.size());
		assertTrue(columns.get(0) instanceof TrailsTableColumn);
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		assertEquals("Id", idColumn.getDisplayName());

		Block fakeBlock = (Block) creator.newInstance(Block.class);
		components.put("multiWordPropertyColumnValue", fakeBlock);

		PropertyUtils.write(objectTable, "propertyNames", Arrays.asList("multiWordProperty"));

		columns = objectTable.getColumns();
		assertEquals("2 column", 2, columns.size());
		TrailsTableColumn column = (TrailsTableColumn) columns.get(0);
		assertNotNull(column);
	}

	public void testGetBlockAddress() throws Exception
	{
		String fakeBlockName = "nameColumnValue";

		checking(new Expectations()
		{
			{
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
		List columns = objectTable.getColumns();
		assertEquals("2 columns", 2, columns.size());
		assertTrue(columns.get(0) instanceof TrailsTableColumn);
		TrailsTableColumn idColumn = (TrailsTableColumn) columns.get(0);
		assertEquals("Id", idColumn.getDisplayName());

		ComponentAddress address = ((BlockTableRendererSource) idColumn.getValueRendererSource()).getBlockAddress();

		assertNotNull(address);
		assertEquals("fooPageIdPath.myTable.linkColumnValue", address.getIdPath());

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

	public void testGetIdentifierProperty() throws Exception
	{
		assertEquals("right id prop", "id", objectTable.getIdentifierProperty());
	}
}
