package ${package}.pages;


import org.apache.tapestry5.Link;
import org.trailsframework.hibernate.pages.HibernateEditPage;
import org.trailsframework.util.DisplayNameUtils;

public class Edit extends HibernateEditPage
{
	public String getTitle()
	{
		return getMessages().format("org.trails.i18n.edit", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}


	public Link back()
	{
		return getResources().createPageLink(Show.class, false, getClassDescriptor().getType(), getBean());
	}
}
