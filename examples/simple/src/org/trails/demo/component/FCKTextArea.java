package org.trails.demo.component;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.AbstractComponent;

public abstract class FCKTextArea extends AbstractComponent
{

    public FCKTextArea()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

    }

    public Map getSymbols()
    {
        HashMap symbols = new HashMap();
        symbols.put("editor", this);
        return symbols;
    }
}
