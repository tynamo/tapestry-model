package org.trails.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.components.BlockRenderer;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.util.ComponentAddress;
import org.apache.tapestry.valid.RenderString;
import org.jmock.Mock;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

public class TrailsTableColumnTest extends ComponentTest
{

	public TrailsTableColumnTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	TrailsTableColumn column;
	Mock exprEvalMock;
	TrailsPropertyDescriptor descriptor;
	Foo foo;
	Date date;

	public void setUp() throws Exception
	{
		exprEvalMock = new Mock(ExpressionEvaluator.class);
		descriptor = new TrailsPropertyDescriptor(Foo.class, "date", Date.class);
		column = new TrailsTableColumn(descriptor, (ExpressionEvaluator) exprEvalMock.proxy());
		date = new SimpleDateFormat("MM/dd/yyyy").parse("6/1/2005");
		foo = new Foo();
		foo.setDate(date);
	}

	public void testGetColumnValue() throws Exception
	{

		descriptor.setFormat("MM/dd/yyyy");


		exprEvalMock.expects(atLeastOnce()).method("read").with(same(foo), eq("date")).will(
			onConsecutiveCalls(returnValue(date), returnValue(null)));
		//exprEvalMock.expects(atLeastOnce()).method("read").with(null, eq("date")).will(returnValue(null));

		assertEquals("06/01/2005", column.getColumnValue(foo));

		foo.setDate(null);
		assertEquals(null, column.getColumnValue(foo));
	}

	public void testGetValueRenderer() throws Exception
	{
		column = new TrailsTableColumn(descriptor, (ExpressionEvaluator) exprEvalMock.proxy());
		Mock renderSourceMock = new Mock(ITableRendererSource.class);

		RenderString renderer = new RenderString("howdy");
		renderSourceMock.expects(once()).method("getRenderer").will(returnValue(renderer));
		column.setValueRendererSource((ITableRendererSource) renderSourceMock.proxy());

		assertEquals("no block address uses super.getValueRenderer", renderer,
			column.getValueRenderer(null, null, foo));

		ComponentAddress blockAddress = new ComponentAddress("listPage", "dateColumn");
		column = new TrailsTableColumn(descriptor, (ExpressionEvaluator) exprEvalMock.proxy(), blockAddress);
		Block fakeBlock = (Block) creator.newInstance(Block.class);
		Mock cycleMock = new Mock(IRequestCycle.class);
		Mock pageMock = new Mock(IPage.class);
		cycleMock.expects(once()).method("getPage").with(eq("listPage")).will(returnValue(pageMock.proxy()));
		pageMock.expects(once()).method("getNestedComponent").with(eq("dateColumn")).will(returnValue(fakeBlock));

		assertTrue("renders with block",
			column.getValueRenderer((IRequestCycle) cycleMock.proxy(), null, foo) instanceof BlockRenderer);
	}
}
