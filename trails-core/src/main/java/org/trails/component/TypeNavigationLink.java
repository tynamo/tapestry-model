/*
 * Created on Jan 30, 2005
 *
 * Copyright 2004 Chris Nelson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.component;

import org.apache.tapestry.annotations.InjectObject;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TypeNavigationLink extends Link
{
    @InjectObject("spring:descriptorService")
    public abstract DescriptorService getDescriptorService();

    public Class getType()
    {
        try
        {
            return Class.forName(getTypeName());
        }catch (ClassNotFoundException ce)
        {
            throw new TrailsRuntimeException(ce);
        }
    }

    public IClassDescriptor getClassDescriptor()
    {
        return getDescriptorService().getClassDescriptor(getType());
    }

    public abstract void setTypeName(String typeName);

}
