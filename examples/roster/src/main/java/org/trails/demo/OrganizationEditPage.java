package org.trails.demo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.page.EditPage;


public abstract class OrganizationEditPage extends EditPage {


    public DetachedCriteria getDirectorsWithoutOrganizationCriteria() {
        String identifier = "id";
        String inverseProperty = "organization";

        DetachedCriteria criteria = DetachedCriteria.forClass(Director.class);
        Organization organization = (Organization) getModel();

        if (organization != null && organization.getDirector() != null) {
            criteria.add(
                    Restrictions.disjunction()
                            .add(Restrictions.isNull(inverseProperty))
                            .add(Restrictions.eq(inverseProperty + "." + identifier, organization.getId())));
        } else {
            criteria.add(Restrictions.isNull(inverseProperty));
        }

        return criteria;
    }

}