package org.tynamo.examples.simple.entities;

import org.apache.tapestry5.beaneditor.ReorderProperties;
import org.tynamo.descriptor.annotation.ClassDescriptor;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.descriptor.annotation.beaneditor.BeanModels;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@ClassDescriptor(hasCyclicRelationships = true)
@BeanModels({
		@BeanModel(reorder = "id, name, parent, children") // == @ReorderProperties("id, name, parent, children")
})
public class TreeNode
{
	private Integer id;

	private String name;

	private TreeNode parent;

	private Set<TreeNode> children = new HashSet<TreeNode>();

	@OneToMany(mappedBy = "parent")
	public Set<TreeNode> getChildren()
	{
		return children;
	}

	public void setChildren(Set<TreeNode> children)
	{
		this.children = children;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@ManyToOne
	public TreeNode getParent()
	{
		return parent;
	}

	public void setParent(TreeNode parent)
	{
		this.parent = parent;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TreeNode treeNode = (TreeNode) o;

		return getId() != null ? getId().equals(treeNode.getId()) : treeNode.getId() == null;

	}

	public int hashCode()
	{
		return (getId() != null ? getId().hashCode() : 0);
	}

	@Override
	public String toString()
	{
		return getName();
	}


}
