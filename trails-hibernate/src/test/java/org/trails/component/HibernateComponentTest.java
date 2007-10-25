package org.trails.component;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.trails.descriptor.DescriptorService;
import org.trails.i18n.SpringMessageSource;
import org.trails.i18n.TestLocaleHolder;
import org.trails.i18n.TrailsMessageSource;
import org.trails.page.HibernateEditPage;

public abstract class HibernateComponentTest extends ComponentTest
{
	private TestLocaleHolder localeHolder = new TestLocaleHolder(Locale.ENGLISH);

	protected void setUp() throws Exception
	{
		super.setUp();
		localeHolder.setLocale(threadLocale.getLocale());
	}

	protected HibernateEditPage buildEditPage()
	{
		DescriptorService descriptorService = (DescriptorService) descriptorServiceMock.proxy();
		SpringMessageSource messageSource = new SpringMessageSource();
		ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
		springMessageSource.setBasename("messagestest");
		messageSource.setLocaleHolder(localeHolder);
		messageSource.setMessageSource(springMessageSource);

		HibernateEditPage editPage = (HibernateEditPage) creator.newInstance(HibernateEditPage.class,
			new Object[]{
				"persistenceService", persistenceMock.proxy(),
				"descriptorService", descriptorServiceMock.proxy(),
				"callbackStack", callbackStack,
				"hibernateValidationDelegate", delegate,
				"resourceBundleMessageSource", messageSource
			});
		return editPage;
	}

	public TrailsMessageSource getNewTrailsMessageSource()
	{
		SpringMessageSource messageSource = new SpringMessageSource();
		ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
		springMessageSource.setBasename("messages");
		messageSource.setLocaleHolder(localeHolder);
		messageSource.setMessageSource(springMessageSource);
		return messageSource;
	}
}
