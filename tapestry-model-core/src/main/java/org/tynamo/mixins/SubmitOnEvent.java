package org.tynamo.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = "submitonevent.js")
public class SubmitOnEvent {

	@Environmental
	private JavaScriptSupport renderSupport;

	@Environmental
	private FormSupport formSupport;

	@InjectContainer
	private ClientElement container;

	@Inject
	private ComponentResources resources;

	@Inject
	private Request request;

	/**
	 * 
	 * Component event that triggers form submission; must be the name of REAL JavaScript
	 * 
	 * event (click, change, blur etc.)
	 */

	@Parameter(required = true, allowNull = false, defaultPrefix = "literal")
	private String monitorEvent;

	/**
	 * 
	 * Tapestry submit event name that will be simulated during form submission
	 */

	@Parameter(required = true, allowNull = false, defaultPrefix = "literal")
	private String submitEvent;

	/**
	 * 
	 * ID of the field that MUST have a value before form submission.
	 * 
	 * If field is empty, event is not triggered.
	 */

	@Parameter(required = false, allowNull = false, defaultPrefix = "literal")
	private String requiredField;

	/**
	 * 
	 * Form submit context
	 */

	@Parameter(required = false, allowNull = false, defaultPrefix = "prop")
	private Object[] submitContext;

	private static class ProcessFakeSubmission implements ComponentAction<SubmitOnEvent> {

		private static final long serialVersionUID = -2205200857980521346L;

		private String fakeSubmitName;

		public ProcessFakeSubmission(String fakeSubmitName) {
			this.fakeSubmitName = fakeSubmitName;
		}

		public void execute(SubmitOnEvent component) {
			component.processFakeSubmission(fakeSubmitName);
		}

	}

	public void afterRender() {
		final String formId = formSupport.getClientId();
		formSupport.store(this, new ProcessFakeSubmission("submit_" + container.getClientId()));
		renderSupport.addInitializerCall("submitOnChange", new JSONArray(monitorEvent, formId, container.getClientId(),
			requiredField));
	}

	private void processFakeSubmission(String elementName) {
		String value = request.getParameter(elementName);
		if (value == null) return;
		Runnable sendNotification = new Runnable() {
			public void run() {
				resources.triggerEvent(submitEvent, submitContext, null);
			}
		};
		formSupport.defer(sendNotification);
	}

}