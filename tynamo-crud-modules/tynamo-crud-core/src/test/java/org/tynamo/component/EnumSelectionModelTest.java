package org.tynamo.component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import junit.framework.TestCase;
import org.tynamo.test.Gazonk;

public class EnumSelectionModelTest extends TestCase
{
	EnumPropertySelectionModel selectionModel;
	EnumPropertySelectionModel nullableSelectionModel;

	public void setUp()
	{
		selectionModel = new EnumPropertySelectionModel(Gazonk.Origin.class);
		nullableSelectionModel = new EnumPropertySelectionModel(Gazonk.Origin.class, true);
	}

	public void testGetValue() throws Exception
	{
		assertEquals("right value", "AMERICA", selectionModel.getValue(1));
		assertEquals("none value", AbstractPropertySelectionModel.DEFAULT_NONE_VALUE, nullableSelectionModel.getValue(0));
		assertEquals("right value", "AFRICA", nullableSelectionModel.getValue(1));
	}

	public void testNoneLabelValue() throws Exception
	{
		nullableSelectionModel.setNoneLabel("Any");
		assertEquals("Any", nullableSelectionModel.getLabel(0));
	}

	public void testWithList() throws Exception
	{
		List<Gazonk.Origin> instances = new ArrayList<Gazonk.Origin>();
		instances.addAll(EnumSet.of(Gazonk.Origin.AFRICA, Gazonk.Origin.AMERICA));
		selectionModel = new EnumPropertySelectionModel(instances, false);
		assertEquals(2, selectionModel.getOptionCount());
	}

	public void testGetLabel() throws Exception
	{
		assertEquals("right label", "AMERICA", selectionModel.getLabel(1));
		assertEquals("none value", EnumPropertySelectionModel.DEFAULT_NONE_LABEL, nullableSelectionModel.getLabel(0));
		assertEquals("right label", "AFRICA", nullableSelectionModel.getLabel(1));
	}

	public void testGetAnimalLabel() throws Exception
	{
		selectionModel = new EnumPropertySelectionModel(Gazonk.Animal.class, false);
		selectionModel.setLabelProperty("sound");
		assertEquals("Meow", selectionModel.getLabel(0));
		assertEquals("Ruff!", selectionModel.getLabel(1));
	}

	public void testTranslateValue() throws Exception
	{
		Gazonk.Origin origin = (Gazonk.Origin) selectionModel.translateValue("AMERICA");
		assertEquals("got right origin", Gazonk.Origin.AMERICA, origin);
		assertEquals("should be null", null, nullableSelectionModel.translateValue(EnumPropertySelectionModel.DEFAULT_NONE_VALUE));
		assertEquals("correct origin", Gazonk.Origin.AFRICA, nullableSelectionModel.translateValue("AFRICA"));
	}

	public void testTranslateValueWithToString() throws Exception
	{
		EnumPropertySelectionModel animalSelectionModel = new EnumPropertySelectionModel(Gazonk.Animal.class, false);
		Gazonk.Animal kitty = (Gazonk.Animal) animalSelectionModel.translateValue("Cat");
		assertEquals(Gazonk.Animal.CAT, kitty);
	}

	public void testGetOptionCount() throws Exception
	{
		assertEquals(5, selectionModel.getOptionCount());
		assertEquals(6, nullableSelectionModel.getOptionCount());
	}
}
