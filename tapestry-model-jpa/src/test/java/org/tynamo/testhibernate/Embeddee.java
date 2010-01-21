package org.tynamo.testhibernate;

import org.tynamo.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Embeddee {

    private String title;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyDescriptor(displayName = "The Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Added to expose a bug
    @Transient
    public boolean isTrue() {
        return true;
    }
}
