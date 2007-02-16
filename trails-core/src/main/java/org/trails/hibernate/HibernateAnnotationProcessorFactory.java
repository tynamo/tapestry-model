package org.trails.hibernate;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.util.DTDEntityResolver;

import java.io.PrintWriter;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HibernateAnnotationProcessorFactory implements AnnotationProcessorFactory
{
	public static final String SECURITY_PACKAGE = "org.trails.security";
	
	public static final String configFileOption = "configFile";
	
	public static final String destFileOption = "destFile";
	
    public HibernateAnnotationProcessorFactory()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public Collection<String> supportedOptions()
    {
        return null;
    }

    public Collection<String> supportedAnnotationTypes()
    {
        return Arrays.asList("javax.persistence.Entity");
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> decls, AnnotationProcessorEnvironment env)
    {
        return new HibernateAnnotationProcessor(env, decls);
    }
    
    public class HibernateAnnotationProcessor implements AnnotationProcessor
    {
        private AnnotationProcessorEnvironment env;
        private Set<AnnotationTypeDeclaration> annotationTypeDeclarations;
        
        public HibernateAnnotationProcessor(
                AnnotationProcessorEnvironment env, 
                Set<AnnotationTypeDeclaration> annTypeDecls)
        {
            this.env = env;
            this.annotationTypeDeclarations = annTypeDecls;
        }
        
        Element sessionFactoryElement;
        
        public void process()
        {
        	String configTemplateFilePath = getOptionValue(configFileOption);
            
            System.out.println(configTemplateFilePath);
            try
            {
                SAXReader reader = new SAXReader();
                reader.setValidation(false);
                reader.setEntityResolver(new DTDEntityResolver());
                reader.setIncludeExternalDTDDeclarations(false);
                reader.setIncludeInternalDTDDeclarations(false);
                // Create file first, this is more reliable if there are spaces in the path
                File configTemplateFile = new File(configTemplateFilePath);
                Document doc = reader.read(configTemplateFile);
                sessionFactoryElement = (Element)doc.getRootElement().selectSingleNode("//session-factory");
                for (Iterator iter = sessionFactoryElement.selectNodes("mapping[not(starts-with(@class, '" + SECURITY_PACKAGE + "'))]").iterator(); iter.hasNext();)
                {
                    Element element = (Element) iter.next();
                    sessionFactoryElement.remove(element);
                }
                List listenerElements = sessionFactoryElement.elements("listener");
                sessionFactoryElement.elements().removeAll(listenerElements);
                
                for (AnnotationTypeDeclaration annotationTypeDecl : annotationTypeDeclarations)
                {
                    System.out.println(annotationTypeDecl);
                    for (Declaration declaration : env.getDeclarationsAnnotatedWith(annotationTypeDecl))
                    {
                        declaration.accept(new SimpleDeclarationVisitor() 
                        {
                            public void visitClassDeclaration(ClassDeclaration classDeclaration)
                            {
                                System.out.println(classDeclaration.getQualifiedName());
                                sessionFactoryElement.addElement("mapping").setAttributeValue("class", 
                                        classDeclaration.getQualifiedName());
                            }                       
                        });
                    }
                    sessionFactoryElement.elements().addAll(listenerElements);
                }
                String filename = getOptionValue(destFileOption);
                System.out.println("Creating destFile: " + filename);
                File f = new File(filename);
                f.getParentFile().mkdirs();

                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter(new PrintWriter(f), format);
                writer.write(doc);
                writer.close();
                
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }

		private String getOptionValue(String option)
		{
			// This is a hack due to a bug in apt
            for (String key : env.getOptions().keySet())
			{
				if (key.startsWith("-A" + option))
				{
					return key.split("=")[1];
				}
			}
			return null;
		}
        
    }
}
