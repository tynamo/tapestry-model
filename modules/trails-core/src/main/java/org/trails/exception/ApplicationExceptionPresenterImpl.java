package org.trails.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.error.ExceptionPresenterImpl;
import org.trails.TrailsRuntimeException;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage.PageType;

/* kaosko 2007-06-18:
 * I would have really liked to implement the exception presenter only as a handler in pipeline
 * (as described at http://mail-archives.apache.org/mod_mbox/tapestry-dev/200606.mbox/%3C000a01c6906b$6ba3b8b0$6601a8c0@CARMANI9300%3E)
 * It required some changed in Hivemind core that were already implemented in Hivemind 1.1.2 which was never
 * released even though it apparently came very close. I've inquired about the status of 1.1.2 on Hivemind list
 */
public class ApplicationExceptionPresenterImpl extends ExceptionPresenterImpl {
	private static final Log log = LogFactory.getLog(ApplicationExceptionPresenterImpl.class);
	private PageResolver pageResolver;
	
	public enum DefaultPage{UnknownEntity};
	
	public void presentException(IRequestCycle cycle, Throwable throwable) {
		if (throwable.getCause() == null || !(throwable.getCause() instanceof TrailsRuntimeException)) {
			super.presentException(cycle, throwable);
			return;
		}
		TrailsRuntimeException trailsRuntimeException = (TrailsRuntimeException)throwable.getCause();

		if (log.isWarnEnabled())
		{
			if (trailsRuntimeException.getEntityType() == null)
			{
				log.warn("Trails specific exception happened while handling unknown entity, caused by: " + throwable.getCause().getMessage());
			} else
			{
				log.warn("Trails specific exception happened while handling entity type " + trailsRuntimeException.getEntityType().getName() + ", caused by: " + throwable.getCause().getMessage());
			}
		}
		log.debug("The problem was caused by: ", throwable.getCause());
		IPage iPage = pageResolver.resolvePage(cycle, trailsRuntimeException.getEntityType(), PageType.Exception);
		
		setExceptionPageName(iPage.getPageName());
		super.presentException(cycle, throwable.getCause() );
	}

	public void setPageResolver(PageResolver pageResolver) {
		this.pageResolver = pageResolver;
	}
}
