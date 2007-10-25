package org.trails.test;

import org.trails.i18n.DescriptorInternationalization;

public class TestUtils
{

	public static void cleanDescriptorInternationalizationAspect()
	{
		DescriptorInternationalization.aspectOf().setTrailsMessageSource(null);
	}
}
