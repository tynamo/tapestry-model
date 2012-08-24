package org.tynamo.model.test.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

@Entity

// @ValidateUniqueness(property = "name")
public class BarCollector {
	private List<Bar> bars = new ArrayList<Bar>();

  private Integer id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ElementCollection
//	@CollectionTable(name = "Bar", joinColumns = {@JoinColumn(name="barCollector_id")})	
	@OrderColumn(name = "barOrder")
	public List<Bar> getBars() {
		return bars;
	}

	public void setBars(List<Bar> bars) {
		this.bars = bars;
	}

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


}
