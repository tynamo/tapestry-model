/*
 * Generated file - Do not edit!
 */
package org.trails.test;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * BeanInfo class for Foo.
 * Note: The java beans specification does NOT state that a property mutator
 * can NOT exist with out an associated property accessor. Furthermore the
 * implmentation of the sun bdk will look for property mutators by them self.
 * The criteria for determining properties/accessors/mutators are determined
 * by the JavaBeans specification, the implementation of Introspector and the
 * implementation of the bdk not what the author or the framework thinks it
 * "should be".
 */

public class FooBeanInfo extends SimpleBeanInfo
{
   /** Description of the Field */
   protected BeanDescriptor bd = new BeanDescriptor(org.trails.test.Foo.class);
   /** Description of the Field */
   protected Image iconMono16;
   /** Description of the Field */
   protected Image iconColor16;
   /** Description of the Field */
   protected Image iconMono32;
   /** Description of the Field */
   protected Image iconColor32;

   /** Constructor for the FooBeanInfo object */
   public FooBeanInfo() throws java.beans.IntrospectionException
   {
      // setup bean descriptor in constructor. 
      bd.setName("Foo");

      Class infoSourceClass =  getBeanDescriptor().getBeanClass().isInterface()
         ? Object.class : getBeanDescriptor().getBeanClass().getSuperclass();
      BeanInfo info = Introspector.getBeanInfo(infoSourceClass);
      String order = info.getBeanDescriptor().getValue("propertyorder") == null
         ? "" : (String) info.getBeanDescriptor().getValue("propertyorder");
      PropertyDescriptor[] pd = getPropertyDescriptors();
      for (int i = 0; i != pd.length; i++)
      {
         if (order.indexOf(pd[i].getName()) == -1)
         {
            order = order + (order.length() == 0 ? "" : ":") + pd[i].getName();
         }
      }
      getBeanDescriptor().setValue("propertyorder", order);
   }

   /**
    * Gets the additionalBeanInfo
    *
    * @return   The additionalBeanInfo value
    */
   public BeanInfo[] getAdditionalBeanInfo()
   {
      Vector bi = new Vector();
      BeanInfo[] biarr = null;
      try
      {
      }
      catch (Exception e)
      {
         // Ignore it
      }
      return biarr;
   }

   /**
    * Gets the beanDescriptor
    *
    * @return   The beanDescriptor value
    */
   public BeanDescriptor getBeanDescriptor()
   {
      return bd;
   }

   /**
    * Gets the defaultPropertyIndex
    *
    * @return   The defaultPropertyIndex value
    */
   public int getDefaultPropertyIndex()
   {
      String defName = "";
      if (defName.equals(""))
      {
         return -1;
      }
      PropertyDescriptor[] pd = getPropertyDescriptors();
      for (int i = 0; i < pd.length; i++)
      {
         if (pd[i].getName().equals(defName))
         {
            return i;
         }
      }
      return -1;
   }

   /**
    * Gets the icon
    *
    * @param type  Description of the Parameter
    * @return      The icon value
    */
   public Image getIcon(int type)
   {
      if (type == BeanInfo.ICON_COLOR_16x16)
      {
         return iconColor16;
      }
      if (type == BeanInfo.ICON_MONO_16x16)
      {
         return iconMono16;
      }
      if (type == BeanInfo.ICON_COLOR_32x32)
      {
         return iconColor32;
      }
      if (type == BeanInfo.ICON_MONO_32x32)
      {
         return iconMono32;
      }
      return null;
   }

   /**
    * Gets the Property Descriptors
    *
    * @return   The propertyDescriptors value
    */
   public PropertyDescriptor[] getPropertyDescriptors() 
   {
      // create a property descriptor for each property
      // if no property name is specified (javabean.property name) the the introspector will guess the property name from the method name (at generation time)

      // do not ignore, bomb politely so use has chance to discover what went wrong...
      // I know that this is suboptimal solution, but swallowing silently is
      // even worse... Propose better solution!
      Vector descriptors = new Vector();

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("id", org.trails.test.Foo.class, "getId", "setId");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("name", org.trails.test.Foo.class, "getName", "setName");
         descriptor.setDisplayName("The Name");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("bar", org.trails.test.Foo.class, "getBar", "setBar");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("date", org.trails.test.Foo.class, "getDate", "setDate");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("number", org.trails.test.Foo.class, "getNumber", "setNumber");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("multiWordProperty", org.trails.test.Foo.class, "getMultiWordProperty", "setMultiWordProperty");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("bazzes", org.trails.test.Foo.class, "getBazzes", "setBazzes");
         descriptor.setExpert(true);
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("bings", org.trails.test.Foo.class, "getBings", "setBings");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("primitive", org.trails.test.Foo.class, "isPrimitive", "setPrimitive");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         PropertyDescriptor descriptor = new PropertyDescriptor("hidden", org.trails.test.Foo.class, "getHidden", "setHidden");
         descriptor.setHidden(true);
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }

      try
      {
         //explicit read only property
         PropertyDescriptor descriptor = new PropertyDescriptor("readOnly", org.trails.test.Foo.class, "getReadOnly", "setReadOnly");
         descriptors.add(descriptor);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }
      return (PropertyDescriptor[]) descriptors.toArray(new PropertyDescriptor[descriptors.size()]);
   }

   /**
    * Gets the methodDescriptors attribute ...
    *
    * @return   The methodDescriptors value
    */
   public MethodDescriptor[] getMethodDescriptors() {
      Vector descriptors = new Vector();
      MethodDescriptor descriptor = null;
      Method[] m;
      Method method;

      try {
         m = Class.forName("org.trails.test.Foo").getMethods();
      } catch (ClassNotFoundException e) {
         return new MethodDescriptor[0];
      }

      return (MethodDescriptor[]) descriptors.toArray(new MethodDescriptor[descriptors.size()]);
   }
}
