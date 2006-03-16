package org.trails.hibernate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.DeclarationVisitor;

public class HibernateAnnotationProcessorFactoryTest extends MockObjectTestCase
{
	public void testProcess() throws Exception
	{
		// set up the environment options so we can read the existing config
		Map<String,String> options = new HashMap<String,String>();
		
		// create a hibernate.cfg.xml in temp dir
		String tempDir = File.createTempFile("foo", "txt").getParentFile().getAbsolutePath();
		File configFile = new File(tempDir + File.separator + "hibernate.cfg.xml");
		FileOutputStream outStream = new FileOutputStream(configFile);
		
		// copy the test file to the temp dir so the processor will be able to find it
		InputStream inStream = this.getClass().getResourceAsStream("processorTest.xml");
		byte[] buf = new byte[1024];
		int len;
		while ((len = inStream.read(buf)) > 0)
		{
			outStream.write(buf, 0, len);
		}
		inStream.close();
		outStream.close();
		
		options.put("-AconfigFile=" + configFile.getAbsolutePath(), null);
		
		
		Mock apEnvMock = new Mock(AnnotationProcessorEnvironment.class);
		apEnvMock.expects(atLeastOnce()).method("getOptions").will(returnValue(options));
		
		// create the filer so we can get back the new config xml file 
		// the processor will produce
		Mock filerMock = new Mock(Filer.class);
		StringWriter stringWriter = new StringWriter();
		filerMock.expects(once()).method("createTextFile").withAnyArguments()
			.will(returnValue(new PrintWriter(stringWriter)));
		apEnvMock.expects(atLeastOnce()).method("getFiler").will(returnValue((Filer)filerMock.proxy()));
		// This stuff is way overcomplicated (apt has a crappy API!)
		// The bottom line here is that the 
		// class declaration representing the annotated class will be received by a 
		// visitor who will ask it for its fully qualified class name.
		
		Mock declarationMock = new Mock(Declaration.class);
		final Mock classDeclarationMock = new Mock(ClassDeclaration.class);
		classDeclarationMock.expects(atLeastOnce()).will(returnValue("org.trails.Foo"));
		declarationMock.expects(once()).method("accept").will(new Stub() 
		{

			public Object invoke(Invocation invocation) throws Throwable
			{
				DeclarationVisitor visitor = (DeclarationVisitor)invocation.parameterValues.get(0);
				visitor.visitClassDeclaration((ClassDeclaration)classDeclarationMock.proxy());
				return null;
			}

			public StringBuffer describeTo(StringBuffer buffer)
			{
				buffer.append("Accept declaration visitor");
				return buffer;
			}
			
		});
		Set<Declaration> declarations = new HashSet<Declaration>();
		declarations.add((Declaration)declarationMock.proxy());
		apEnvMock.expects(once()).method("getDeclarationsAnnotatedWith")
			.with(isA(AnnotationTypeDeclaration.class))
			.will(returnValue(declarations));

		Set<AnnotationTypeDeclaration> annTypeDecls = new HashSet<AnnotationTypeDeclaration>();
		Mock annTypeDeclMock = new Mock(AnnotationTypeDeclaration.class);
		annTypeDecls.add((AnnotationTypeDeclaration)annTypeDeclMock.proxy());

		AnnotationProcessorEnvironment apEnv = (AnnotationProcessorEnvironment)apEnvMock.proxy();
		
		HibernateAnnotationProcessorFactory factory = new HibernateAnnotationProcessorFactory();
		AnnotationProcessor processor = factory.getProcessorFor(annTypeDecls, apEnv);
			
		processor.process();
		
		String xml = stringWriter.toString();
		assertTrue("has foo mapping", xml.indexOf("mapping class=\"org.trails.Foo\"") > -1);
		assertTrue("has existing trails mapping", xml.indexOf("mapping class=\"org.trails.security.domain.ShouldBePreserved\"") > -1);
		assertTrue("existing app entries are removed", xml.indexOf("ShouldBeRemoved") == -1);
		
		configFile.delete();
	}

}
