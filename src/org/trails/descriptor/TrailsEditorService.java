package org.trails.descriptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry.util.ComponentAddress;

public class TrailsEditorService implements EditorService
{
    private Map editorMap;
    
    private ComponentAddress defaultEditor;

    public TrailsEditorService()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public Map getEditorMap()
    {
        return editorMap;
    }

    /**
     * This a map where the keys are ognl expressions and the values are
     * component address.
     * 
     * @param editorMap
     */
    public void setEditorMap(Map editorMap)
    {
        //System.out.println("map type: " + editorMap.getClass().getName());
        this.editorMap = editorMap;
    }

    /**
     * @param descriptor
     * @return The first component address in the editorMap whose key evaluates
     *         to true for descriptor. This will be used to load an editor for
     *         the descriptor. Returns default editor if no match is found.
     * @see org.trails.descriptor.EditorService#findEditor(org.trails.descriptor.IPropertyDescriptor)
     */
    public ComponentAddress findEditor(IPropertyDescriptor descriptor)
    {
        for (Iterator iter = editorMap.entrySet().iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry) iter.next();
            try
            {
                if (((Boolean) Ognl.getValue((String) entry.getKey(),
                        descriptor)).booleanValue())
                {
                    return (ComponentAddress) entry.getValue();
                }
            } catch (OgnlException e)
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        return getDefaultEditor();
    }

    public ComponentAddress getDefaultEditor()
    {
        return defaultEditor;
    }

    public void setDefaultEditor(ComponentAddress defaultEditor)
    {
        this.defaultEditor = defaultEditor;
    }

}
