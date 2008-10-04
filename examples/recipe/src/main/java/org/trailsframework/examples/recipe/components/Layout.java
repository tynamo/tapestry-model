package org.trailsframework.examples.recipe.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Environment;


//@IncludeJavaScriptLibrary("context:scripts/global.js")
@IncludeStylesheet({"context:styles/andreas01/theme.css"})//, "context:styles/tapestryskin/print.css"})
public class Layout {

	@Inject
	private Environment environment;

	@Inject
	private Context context;

	@Inject
	private RenderSupport renderSupport;


	@Property
	@Parameter(required = true)
	private String title;

//
//	@InjectContainer
//	@Property
//	private BasePage page;

//	
//	private String message;
//	
//	private String type;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
	private String heading;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
	private String menu;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String bodyId;

//	void setupRender() {
//		renderSupport.addScriptLink(globalLibrary);
//	}

	public String getCssTheme() {
		return "simplicity";
	}
}
