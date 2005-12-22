package org.trails.descriptor;

public interface IDescriptor extends Cloneable
{
    /**
     * @return
     */
    public boolean isHidden();
    
    public void setHidden(boolean hidden);
    
    /**
     * @return
     */
    public String getDisplayName();
    
    public void setDisplayName(String displayName);
    
    public Object clone();
}