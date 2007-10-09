package org.trails.record;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.apache.tapestry.util.io.DataSqueezerImpl;
import org.apache.tapestry.util.io.DataSqueezerUtil;
import org.trails.io.ClassAdaptor;


public class SqueezerDataEncoderTest extends TestCase
{

	SqueezerDataEncoder squeezerDataEncoder = new SqueezerDataEncoder();
	List list = new ArrayList();

	protected void setUp() throws Exception
	{
		super.setUp();
		DataSqueezerImpl dataSqueezer = DataSqueezerUtil.createUnitTestSqueezer();
		dataSqueezer.register(new ClassAdaptor());
		squeezerDataEncoder.setDataSqueezer(dataSqueezer);


		list.clear();
		list.add(1);
		list.add("alejandro");
		list.add(SqueezerDataEncoder.class);
		list.add(true);
	}


	public void testEncodePageChanges()
	{
		assertEquals("{1,Salejandro,Dorg.trails.record.SqueezerDataEncoder,T}",
			squeezerDataEncoder.encodePageChanges(list));

	}

	public void testDecodePageChanges()
	{
		List nuevos = squeezerDataEncoder.decodePageChanges("{1,Salejandro,Dorg.trails.record.SqueezerDataEncoder,T}");

		assertEquals(list.size(), nuevos.size());

		for (int i = 0; i < nuevos.size(); i++)
		{
			assertEquals(list.get(i), nuevos.get(i));
		}
	}
}
