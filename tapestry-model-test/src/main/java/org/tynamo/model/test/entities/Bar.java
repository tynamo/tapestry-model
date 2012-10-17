package org.tynamo.model.test.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.tynamo.descriptor.annotation.PropertyDescriptor;

@Entity
// @AssertNoOrphans(Wibble.class)
public class Bar implements IBar, Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	@PropertyDescriptor(searchable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *          The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public String getTransientProperty() {
		return "Hello World";
	}
}
