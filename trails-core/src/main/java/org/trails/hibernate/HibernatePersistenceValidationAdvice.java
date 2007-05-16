package org.trails.hibernate;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;


public class HibernatePersistenceValidationAdvice implements MethodBeforeAdvice
{

	public HibernatePersistenceValidationAdvice()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable
	{
		//System.out.println("in advice");
	}

}
