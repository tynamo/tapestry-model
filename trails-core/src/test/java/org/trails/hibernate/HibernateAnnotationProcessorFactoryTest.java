package org.trails.hibernate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.DeclarationVisitor;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class HibernateAnnotationProcessorFactoryTest extends MockObjectTestCase
{
	public void testProcess() throws Exception
	{
		// set up the environment options so we can read the existing config
		Map<String, String> options = new HashMap<String, String>();

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

		// put the output file in temp as well
		File destFile = new File(tempDir + File.separator + "destConfig.xml");
		options.put("-AconfigFile=" + configFile.getAbsolutePath(), null);
		options.put("-AdestFile=" + destFile.getAbsolutePath(), null);

		Mock apEnvMock = new Mock(AnnotationProcessorEnvironment.class);
		apEnvMock.expects(atLeastOnce()).method("getOptions").will(returnValue(options));

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
				DeclarationVisitor visitor = (DeclarationVisitor) invocation.parameterValues.get(0);
				visitor.visitClassDeclaration((ClassDeclaration) classDeclarationMock.proxy());
				return null;
			}

			public StringBuffer describeTo(StringBuffer buffer)
			{
				buffer.append("Accept declaration visitor");
				return buffer;
			}

		});
		Set<Declaration> declarations = new HashSet<Declaration>();
		declarations.add((Declaration) declarationMock.proxy());
		apEnvMock.expects(once()).method("getDeclarationsAnnotatedWith")
			.with(isA(AnnotationTypeDeclaration.class))
			.will(returnValue(declarations));

		Set<AnnotationTypeDeclaration> annTypeDecls = new HashSet<AnnotationTypeDeclaration>();
		Mock annTypeDeclMock = new Mock(AnnotationTypeDeclaration.class);
		annTypeDecls.add((AnnotationTypeDeclaration) annTypeDeclMock.proxy());

		AnnotationProcessorEnvironment apEnv = (AnnotationProcessorEnvironment) apEnvMock.proxy();

		HibernateAnnotationProcessorFactory factory = new HibernateAnnotationProcessorFactory();
		AnnotationProcessor processor = factory.getProcessorFor(annTypeDecls, apEnv);

		processor.process();
		// Apt processor might not flush the file immediately - deal with it
		processor = null;
		System.gc();

		// Since the move to www.hibernate.org was completed, the DTD is not anymore available on hibernate.sourceforg.net
		// FIXME is there really no better way to provide a DTD from the filesystem if you don't want to/can't change the id???
		// From http://tersesystems.com/post/6000058.jhtml		
		EntityResolver resolver = new EntityResolver()
		{
			public InputSource resolveEntity(String publicId, String systemId)
			{
				if (publicId.equals("-//Hibernate/Hibernate Configuration DTD 3.0//EN"))
				{
					InputStream in = getClass().getClassLoader().getResourceAsStream("hibernate-configuration-3.0.dtd");
					return new InputSource(in);
				}
				return null;
			}
		};

		SAXReader reader = new SAXReader();
		reader.setEntityResolver(resolver);

		//SAXReader reader = new SAXReader();
		//reader.setIncludeExternalDTDDeclarations(false);
		Document doc = reader.read(destFile);
		assertNotNull(doc.selectSingleNode("//mapping[@class='org.trails.Foo']"));
		assertNotNull(doc.selectSingleNode("//mapping[@class='org.trails.security.domain.ShouldBePreserved']"));
		assertNull(doc.selectSingleNode("//mapping[contains(@class, 'ShouldBeRemoved')]"));

		configFile.delete();
		destFile.delete();
	}

}
