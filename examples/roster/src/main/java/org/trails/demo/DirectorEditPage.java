package org.trails.demo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.page.EditPage;


public abstract class DirectorEditPage extends EditPage {


    public DetachedCriteria getOrganizationsWithoutDirectorCriteria() {
        String identifier = "id";
        String inverseProperty = "director";
        DetachedCriteria criteria = DetachedCriteria.forClass(Organization.class);
        Director director = (Director) getModel();

        if (director != null && director.getOrganization() != null) {
            criteria.add(
                    Restrictions.disjunction()
                            .add(Restrictions.isNull(inverseProperty))
                            .add(Restrictions.eq(inverseProperty + "." + identifier, director.getId())));
        } else {
            criteria.add(Restrictions.isNull(inverseProperty));
        }

        return criteria;
    }
}
