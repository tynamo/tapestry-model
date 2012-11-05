package org.tynamo.model.elasticsearch.descriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.common.collect.Lists;
import org.tynamo.descriptor.extension.DescriptorExtension;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchField;

public class ElasticSearchFieldDescriptor implements DescriptorExtension {

	private List<ElasticSearchField> annotations = Lists.newArrayList();

	public ElasticSearchFieldDescriptor(Member member) {
		if (member instanceof Field)
			this.annotations = Lists.newArrayList(((Field) member).getAnnotation(ElasticSearchField.class));
		else if (member instanceof Method)
			this.annotations = Lists.newArrayList(((Method) member).getAnnotation(ElasticSearchField.class));

		// FIXME disable multifield support for now, not implemented.
		// if (field.getAnnotation(ElasticSearchField.class) != null) {
		// this.annotations = Lists.newArrayList(field.getAnnotation(ElasticSearchField.class));
		// } else if (field.getAnnotation(ElasticSearchMultiField.class) != null) {
		// this.annotations = Lists.newArrayList(field.getAnnotation(ElasticSearchMultiField.class).value());
		// }
	}

	public String type() {
		return annotations.get(0).type();
	}

	public Collection<ElasticSearchField> getFields() {
		return annotations;
	}

	public boolean hasType() {
		return hasField() && getField().type().length() > 0;
	}

	public ElasticSearchField getField() {
		return annotations.get(0);
	}

	public boolean isMultiField() {
		return annotations.size() > 1;
	}

	public boolean hasField() {
		return annotations.isEmpty() == false;
	}
}
