package org.trails.demo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.page.EditPage;

/**
 * These custom pages override the AssociationSelect component criteria
 * parameter with a new criteria that properly  considers the @OneToOne
 * use case.
 *
 * EXAMPLE one-to-one association (Organization<-->Director) :
 * The "director" select combo in OrganizationEditPage will show
 * only directors without organizations, and vice versa the
 * "organization" select combo in DirectorEditPage will show only
 * organizations without directors. So, when you select a director for an
 * organization, that same director won't be available in the select
 * combo for other organizations.
 *
 * @author kenneth.colassi            nhhockeyplayer@hotmail.com
 */
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
