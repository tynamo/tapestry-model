package org.tynamo.services;

import org.apache.tapestry5.func.Predicate;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;
import org.tynamo.descriptor.extension.EnumReferenceDescriptor;

public class TynamoDataTypeAnalyzerPredicates
{
	public static final Predicate<TynamoPropertyDescriptor> nonVisual = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.isNonVisual();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> readOnly = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.isReadOnly();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> richText = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.isRichText();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> password = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.getName().toLowerCase().endsWith("password");
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> date = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.isDate();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> number = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.isNumeric();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> longtext = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor descriptor)
		{
			return descriptor.isString() & descriptor.isLarge();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> generatedId = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor descriptor)
		{
			return descriptor.isIdentifier() && ((IdentifierDescriptor) descriptor).isGenerated();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> assignedId = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor descriptor)
		{
			return descriptor.isIdentifier() && descriptor.isString() && !((IdentifierDescriptor) descriptor).isGenerated();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> enumi = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.supportsExtension(EnumReferenceDescriptor.class);
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> blob = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.supportsExtension(BlobDescriptorExtension.class);
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> manyToOne = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.isObjectReference();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> manyToMany = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor descriptor)
		{
			return descriptor.isCollection() && !((CollectionDescriptor) descriptor).isChildRelationship();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> composition = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor descriptor)
		{
			return descriptor.isCollection() && ((CollectionDescriptor) descriptor).isChildRelationship();
		}
	};

	public static final Predicate<TynamoPropertyDescriptor> embedded = new Predicate<TynamoPropertyDescriptor>()
	{
		public boolean accept(TynamoPropertyDescriptor tynamoPropertyDescriptor)
		{
			return tynamoPropertyDescriptor.isEmbedded();
		}
	};

}
