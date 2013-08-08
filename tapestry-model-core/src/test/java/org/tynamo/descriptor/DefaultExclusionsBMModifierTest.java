package org.tynamo.descriptor;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;
import org.tynamo.PageType;
import org.tynamo.internal.services.DefaultExclusionsBMModifier;
import org.tynamo.services.DescriptorService;
import org.tynamo.test.Baz;
import org.tynamo.test.Foo;

import java.util.Collections;
import java.util.Set;

public class DefaultExclusionsBMModifierTest extends TapestryTestCase
{

	@Test
	void exclude()
	{

		BeanModel model = mockBeanModel();
		DescriptorService descriptorService = newMock(DescriptorService.class);

		TynamoClassDescriptorImpl classDescriptor = new TynamoClassDescriptorImpl(Foo.class);
		IdentifierDescriptor idProp = new IdentifierDescriptorImpl(Foo.class, "id", String.class);
		TynamoPropertyDescriptor multiWordProp =
				new TynamoPropertyDescriptorImpl(Foo.class, "multiWordProperty", String.class);

		CollectionDescriptor bazzesDescriptor = new CollectionDescriptor(Foo.class, "bazzes", Set.class);
		bazzesDescriptor.setElementType(Baz.class);

		Collections.addAll(classDescriptor.getPropertyDescriptors(), idProp, multiWordProp, bazzesDescriptor);

		expect(model.exclude("id", "bazzes")).andReturn(model);
		expect(model.getBeanType()).andReturn(Foo.class);
		expect(descriptorService.getClassDescriptor(Foo.class)).andReturn(classDescriptor);

		replay();

		DefaultExclusionsBMModifier modifier = new DefaultExclusionsBMModifier(descriptorService);
		modifier.modify(model, PageType.LIST.getContextKey());

		verify();

	}
}
