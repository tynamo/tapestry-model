package org.trails.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.services.ExpressionEvaluator;
import org.jmock.Mock;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

import junit.framework.TestCase;

public class TrailsTableColumnTest extends ComponentTest
{

    public TrailsTableColumnTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public void testGetColumnValue() throws Exception
    {
        TrailsPropertyDescriptor descriptor = 
            new TrailsPropertyDescriptor(Foo.class, "date", Date.class);
        descriptor.setFormat("MM/dd/yyyy");
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse("6/1/2005");
        Foo foo = new Foo();
        foo.setDate(date);
        Mock exprEvalMock = new Mock(ExpressionEvaluator.class);
        exprEvalMock.expects(atLeastOnce()).method("read").with(same(foo), eq("date")).will(
                onConsecutiveCalls(returnValue(date), returnValue(null)));
        //exprEvalMock.expects(atLeastOnce()).method("read").with(null, eq("date")).will(returnValue(null));
        TrailsTableColumn column  = new TrailsTableColumn(descriptor, (ExpressionEvaluator)exprEvalMock.proxy());
  
        assertEquals("06/01/2005", column.getColumnValue(foo));
        
        foo.setDate(null);
        assertEquals(null, column.getColumnValue(foo));
    }
}
