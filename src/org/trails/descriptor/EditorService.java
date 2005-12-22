package org.trails.descriptor;

import org.apache.tapestry.util.ComponentAddress;

public interface EditorService
{

    /**
     * @return the component address for a block to display to edit the
     * field for this descriptor. 
     */
    public abstract ComponentAddress findEditor(IPropertyDescriptor descriptor);

    public void setDefaultEditor(ComponentAddress defaultEditor);
    
    public ComponentAddress getDefaultEditor();
}