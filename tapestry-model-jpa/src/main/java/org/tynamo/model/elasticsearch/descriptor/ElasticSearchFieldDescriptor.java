package org.tynamo.model.elasticsearch.descriptor;

import org.tynamo.descriptor.extension.DescriptorExtension;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchField;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ElasticSearchFieldDescriptor implements DescriptorExtension {

	private List<ElasticSearchField> annotations;

	public ElasticSearchFieldDescriptor(ElasticSearchField... annotations)
	{
		this.annotations = Arrays.asList(annotations);
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
		return !annotations.isEmpty();
	}
}
