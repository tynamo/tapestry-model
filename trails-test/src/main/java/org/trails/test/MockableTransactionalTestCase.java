package org.trails.test;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.constraint.IsInstanceOf;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.component.ComponentTest;
import org.trails.persistence.PersistenceService;

public abstract class MockableTransactionalTestCase extends AbstractTransactionalSpringContextTests {
	protected PersistenceService persistenceService;

	private MockObjectTestCase mockDelegate = new MockObjectTestCase() {};

	protected Mock cycleMock = mock(IRequestCycle.class);
	protected Mock infrastructureMock = mock(Infrastructure.class);
	protected Mock webRequestMock = mock(WebRequest.class);
	protected Mock webResponseMock = mock(WebResponse.class);

	protected String[] getConfigLocations(){
		return new String[]{"applicationContext-test.xml", "seed-data-test.xml"};
	}

	public void onSetUpBeforeTransaction() throws Exception {
		persistenceService = (PersistenceService) applicationContext.getBean("persistenceService");
	}

	protected void onTearDownAfterTransaction() {
		verify();
	}


	/**
	 * Operations delegation to componentTestDelegate
	 */
	protected <T> T buildTrailsPage(Class<T> pageClass)
	{
		return getComponentTest().buildTrailsPage(pageClass);
	}

	// Operations delegating to mockDelegate 

	public Mock mock(Class mockedType) {return mockDelegate.mock(mockedType);}

	public InvocationMatcher once() {return mockDelegate.once();}
	public InvocationMatcher atLeastOnce() {return mockDelegate.atLeastOnce();}

	public IsInstanceOf isA(Class operandClass){return mockDelegate.isA(operandClass);}

	public void verify() {mockDelegate.verify();}

	public Stub returnValue(Object o) {return mockDelegate.returnValue(o);}
	public Stub returnValue(boolean result){return mockDelegate.returnValue(result);}
	public Stub returnValue(byte result){return mockDelegate.returnValue(result);}
	public Stub returnValue(char result){return mockDelegate.returnValue(result);}
	public Stub returnValue(short result){return mockDelegate.returnValue(result);}
	public Stub returnValue(int result){return mockDelegate.returnValue(result);}
	public Stub returnValue(long result){return mockDelegate.returnValue(result);}
	public Stub returnValue(float result){return mockDelegate.returnValue(result);}
	public Stub returnValue(double result){return mockDelegate.returnValue(result);}

	public IsEqual eq(Object operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(boolean operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(byte operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(char operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(short operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(int operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(long operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(float operand) {return mockDelegate.eq(operand);}
	public IsEqual eq(double operand) {return mockDelegate.eq(operand);}


	/**
	 * Little hook to override the default ComponentTest delegate.
	 *
	 * @return
	 */
	protected ComponentTest getComponentTest() {
		return new ComponentTest(){};
	}
}
