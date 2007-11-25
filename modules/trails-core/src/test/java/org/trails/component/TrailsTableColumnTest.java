package org.trails.component;

import org.apache.hivemind.Messages;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.components.BlockRenderer;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
import org.apache.tapestry.services.ExpressionEvaluator;
import org.apache.tapestry.valid.RenderString;
import org.jmock.Mock;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrailsTableColumnTest extends ComponentTest
{

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
		Map components = new HashMap();
		Mock messagesMock = new Mock(Messages.class);
		Mock renderSourceMock = new Mock(ITableRendererSource.class);
		Mock pageMock = new Mock(IPage.class);
		Mock containerMock = new Mock(IComponent.class);
		RenderString renderer = new RenderString("howdy");
		Mock cycleMock = new Mock(IRequestCycle.class);

		renderSourceMock.expects(once()).method("getRenderer").will(returnValue(renderer));
		column.setValueRendererSource((ITableRendererSource) renderSourceMock.proxy());

		assertEquals("no block address uses super.getValueRenderer", renderer,
				column.getValueRenderer(null, null, foo));

		Block fakeBlock = (Block) creator.newInstance(Block.class,
				new Object[]{"id", "dateColumnValue", "page", (IPage) pageMock.proxy() , "container", (IComponent) containerMock.proxy()});

		components.put("dateColumnValue", fakeBlock);

		cycleMock.expects(once()).method("getPage").with(eq("listPage")).will(returnValue(pageMock.proxy()));
		containerMock.expects(once()).method("getIdPath").will(returnValue("container"));
		containerMock.expects(once()).method("getMessages").will(returnValue(messagesMock.proxy()));
		messagesMock.expects(once()).method("getMessage").with(eq("date")).will(returnValue("date"));
		containerMock.expects(atLeastOnce()).method("getComponents").will(returnValue(components));
		pageMock.expects(atLeastOnce()).method("getPageName").will(returnValue("listPage"));
		pageMock.expects(once()).method("getNestedComponent").with(eq("container.dateColumnValue")).will(returnValue(fakeBlock));

		column = new TrailsTableColumn(descriptor, (ExpressionEvaluator) exprEvalMock.proxy());
		column.loadSettings((IComponent) containerMock.proxy());

		assertTrue("renders with block",
				column.getValueRenderer((IRequestCycle) cycleMock.proxy(), null, foo) instanceof BlockRenderer);
	}
}
