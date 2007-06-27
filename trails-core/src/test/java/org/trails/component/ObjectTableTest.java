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
import org.jmock.Mock;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.ReflectionDescriptorFactory;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.persistence.PersistenceService;
import org.trails.test.Foo;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectTableTest extends ComponentTest
{
	ObjectTable objectTable;
	Mock psvcMock = new Mock(PersistenceService.class);
	Mock pageMock = new Mock(IPage.class);
	IPage page;
	Map components = new HashMap();

	public void setUp() throws Exception
	{

		page = (IPage) pageMock.proxy();
		objectTable = (ObjectTable) creator.newInstance(ObjectTable.class, new Object[]{
			"persistenceService", psvcMock.proxy()
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
		
		List propertyDescriptors = new ArrayList();
		IdentifierDescriptor idProp = new IdentifierDescriptor(Foo.class, classDescriptor.getPropertyDescriptor("id") );

		IPropertyDescriptor multiWordProp = new TrailsPropertyDescriptor(Foo.class, "multiWordProperty", String.class);


		IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "hidden", String.class);
		hiddenDescriptor.setHidden(true);

		IPropertyDescriptor summaryDescriptor = new TrailsPropertyDescriptor(Foo.class, "nonSummary", String.class);
		summaryDescriptor.setSummary(false);

		CollectionDescriptor bazzesDesriptor = new CollectionDescriptor(Foo.class, Set.class);
		bazzesDesriptor.setName("bazzes");

		propertyDescriptors.add(idProp);
		propertyDescriptors.add(multiWordProp);
		propertyDescriptors.add(hiddenDescriptor);
		propertyDescriptors.add(summaryDescriptor);
		propertyDescriptors.add(bazzesDesriptor);

		classDescriptor.setPropertyDescriptors(propertyDescriptors);
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

	public void testGetTableSource() throws Exception
	{
/*		 //@todo: reimplement this test without hibernate
		DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
		objectTable.setCriteria(criteria);
		TrailsTableModel tableModel = (TrailsTableModel) objectTable.getSource();

		assertNotNull(tableModel.getPersistenceService());
		assertEquals(criteria, tableModel.getCriteria());

		objectTable.setCriteria(null);
		List instances = new ArrayList();
		objectTable.setInstances(instances);
		assertEquals(instances, objectTable.getSource());
		*/
	}

	public void testGetIdentifierProperty() throws Exception
	{
		assertEquals("right id prop", "id", objectTable.getIdentifierProperty());
	}
}
