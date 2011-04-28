package org.tynamo.descriptor;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;
import org.tynamo.test.Baz;
import org.tynamo.test.Foo;
import org.tynamo.util.BeanModelUtils;

import java.util.Collections;
import java.util.Set;

public class BeanModelUtilsTest extends TapestryTestCase
{

	@Test
	void exclude()
	{

		BeanModel model = mockBeanModel();
		expect(model.exclude("id", "bazzes")).andReturn(model);

		replay();

		TynamoClassDescriptorImpl classDescriptor = new TynamoClassDescriptorImpl(Foo.class);
		IdentifierDescriptor idProp = new IdentifierDescriptorImpl(Foo.class, "id", String.class);
		TynamoPropertyDescriptor multiWordProp =
				new TynamoPropertyDescriptorImpl(Foo.class, "multiWordProperty", String.class);

		CollectionDescriptor bazzesDescriptor = new CollectionDescriptor(Foo.class, "bazzes", Set.class);
		bazzesDescriptor.setElementType(Baz.class);

		Collections.addAll(classDescriptor.getPropertyDescriptors(), idProp, multiWordProp, bazzesDescriptor);

		BeanModelUtils.exclude(model, classDescriptor);

		verify();

	}
}
