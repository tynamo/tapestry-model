package org.trails.test.functional;

import java.io.IOException;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SecurityTest extends FunctionalTest
{
	
	public void testAdminLogin() throws Exception
	{
		login("admin", "admin");
		assertXPathPresent(startPage, "//a[contains(text(), 'List Users')]");
	}

	protected void login(String userName, String password) throws IOException
	{
		HtmlPage loginPage = startPage;
		HtmlForm loginForm = (HtmlForm) loginPage.getAllForms().get(0);
		loginForm.getInputByName("j_username").setValueAttribute(userName);
		loginForm.getInputByName("j_password").setValueAttribute(password);
		startPage = (HtmlPage) loginForm.submit();
	}

	public void testUserLogin() throws Exception
	{
		login("user", "user");
		assertXPathPresent(startPage,
				"//a[contains(text(), 'List Secure Things')]");
		assertXPathNotPresent(startPage, "//a[contains(text(), 'List Users')]");
	}


	
}
