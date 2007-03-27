package org.trails.demo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.page.EditPage;

/**
 * This custom page override the AssociationSelect component criteria
 * parameter with a new criteria that properly  considers the
 *
 * @OneToOne use case.
 *
 * EXAMPLE:
 *         one-to-one association
 *
 *                 Organization-<>-----Director
 *
 * The "director" select combo in DirectorEdit Page will show
 * only directors without organizations.
 *
 * @author kenneth.colassi            nhhockeyplayer@hotmail.com
 */
public abstract class DirectorEdit extends EditPage {


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
