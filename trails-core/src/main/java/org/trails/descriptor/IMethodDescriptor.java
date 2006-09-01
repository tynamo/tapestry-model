
package org.trails.descriptor;




public interface IMethodDescriptor extends IDescriptor {

    public abstract Class[] getArgumentTypes();

    public abstract void setArgumentTypes(Class[] argumentTypes);

    public abstract String getName();

    public abstract void setName(String name);

}