package ${package}.integration;

import org.apache.tapestry5.test.AbstractIntegrationTestSuite;
import org.testng.annotations.Test;

public class IntegrationTests extends AbstractIntegrationTestSuite
{
	@Test
	public void integration_test() throws Exception
	{
		open(BASE_URL);
		assertEquals("Tynamo!", getTitle());


	}
}