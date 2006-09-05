package org.trails.descriptor;

public class TrailsMethodDescriptor extends TrailsDescriptor implements IMethodDescriptor
{
    private String name;
    
    private Class[] argumentTypes;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // constructors
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public TrailsMethodDescriptor(IMethodDescriptor methodDescriptor)
    {
        super(methodDescriptor);
    }
    
    public TrailsMethodDescriptor(String name, Class returnType, Class[] argumentTypes)
    {
        super(returnType);
        this.name = name;
        this.argumentTypes = argumentTypes;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // bean setters/getters
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /* (non-Javadoc)
     * @see org.trails.descriptor.IMethodDescriptor#getArgumentTypes()
     */
    public Class[] getArgumentTypes()
    {
        return argumentTypes;
    }
    

    /* (non-Javadoc)
     * @see org.trails.descriptor.IMethodDescriptor#setArgumentTypes(java.lang.Class[])
     */
    public void setArgumentTypes(Class[] argumentTypes)
    {
        this.argumentTypes = argumentTypes;
    }
    

    /* (non-Javadoc)
     * @see org.trails.descriptor.IMethodDescriptor#getName()
     */
    public String getName()
    {
        return name;
    }
    

    /* (non-Javadoc)
     * @see org.trails.descriptor.IMethodDescriptor#setName(java.lang.String)
     */
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Object clone()
    {
        return new TrailsMethodDescriptor(this);
    }
 
    
}
