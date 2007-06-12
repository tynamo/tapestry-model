/*
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
package org.trails.test;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.trails.security.annotation.ViewRequiresAssociation;
import org.trails.security.domain.User;


/**
 * @javabean.class name="Foo"
 * @hibernate.class table="FOO"
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@ViewRequiresAssociation("owner")
public class SecureFoo
{
    
    private Integer id;
    private String name;
		private User owner;

    /**
     * @javabean.property
     * @hibernate.id generator-class="assigned"
     */
    @Id
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @hibernate.property not-null="true"
     * @javabean.property displayName="The Name"
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    public User getOwner()
    {
        return owner;
    }
    public void setOwner(User owner)
    {
        this.owner = owner;
    }
}
