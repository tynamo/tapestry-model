package org.tynamo.descriptor;

public class EmbeddedDescriptor extends TynamoPropertyDescriptorImpl implements IdentifierDescriptor
{
	private static final long serialVersionUID = 1L;

	private boolean identifier;

	private boolean generated;

	private TynamoClassDescriptor embeddedClassDescriptor;

	public EmbeddedDescriptor(Class beanType, TynamoPropertyDescriptor descriptor, TynamoClassDescriptor embeddedClassDescriptor)
	{
		super(beanType, descriptor);
		this.embeddedClassDescriptor = embeddedClassDescriptor;
	}

	private EmbeddedDescriptor(EmbeddedDescriptor dto)
	{
		super(dto);
	}

	public boolean isIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(boolean identifier)
	{
		this.identifier = identifier;
	}

	public boolean isGenerated()
	{
		return generated;
	}

	public void setGenerated(boolean generated)
	{
		this.generated = generated;
	}

	public TynamoClassDescriptor getEmbeddedClassDescriptor()
	{
		return embeddedClassDescriptor;
	}

	public void setEmbeddedClassDescriptor(TynamoClassDescriptor embeddedClassDescriptor)
	{
		this.embeddedClassDescriptor = embeddedClassDescriptor;
	}

	@Override
	public boolean isEmbedded()
	{
		return true;
	}

	@Override
	public Object clone()
	{
		return new EmbeddedDescriptor(this);
	}

}
