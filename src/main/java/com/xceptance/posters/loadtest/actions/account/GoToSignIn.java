package com.xceptance.posters.loadtest.actions.account;

import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.posters.loadtest.validators.HeaderValidator;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.HtmlPageUtils;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Browse to the sign in page.
 */
public class GoToSignIn extends AbstractHtmlPageAction {
	/**
	 * The sign in button.
	 */
	private HtmlElement signInButton;

	/**
	 * Constructor.
	 * 
	 * @param previousAction The previously performed action
	 */
	public GoToSignIn(final AbstractHtmlPageAction previousAction) {
		super(previousAction, null);
	}

	@Override
	public void preValidate() throws Exception {
		// Get the result of the previous action.
		final HtmlPage page = getPreviousAction().getHtmlPage();
		Assert.assertNotNull("Failed to get page from previous action.", page);

		// The sign in selector
		final String signInButtonSelector = "#header-customer-menus #go-to-login";

		// List of all occurrences for the selector
		final DomNodeList<DomNode> foundElements = page.querySelectorAll(signInButtonSelector);

		// Making sure that there is exactly one occurrence for our specified selector
		Assert.assertEquals("No or too many elements found for Selector: " + signInButtonSelector + " -", 1,
				foundElements.size());

		// Remember the sign in button.
		signInButton = (HtmlElement) foundElements.get(0);

	}

	@Override
	protected void execute() throws Exception {
		// Click the button to load the sign in page.
		loadPageByClick(signInButton);
	}

	@Override
	protected void postValidate() throws Exception {
		// Get the result of the action.
		final HtmlPage page = getHtmlPage();

		// Repeated basic checks - see action 'Homepage' for some more details how and
		// when to use these validators.
		HttpResponseCodeValidator.getInstance().validate(page);
		ContentLengthValidator.getInstance().validate(page);
		HtmlEndTagValidator.getInstance().validate(page);

		HeaderValidator.getInstance().validate(page);

		// Check that it's the sign in page.
		Assert.assertTrue("Sign in form not found.", HtmlPageUtils.isElementPresent(page, "id('formLogin')"));
		Assert.assertTrue("Link to register not found.", HtmlPageUtils.isElementPresent(page, "id('linkRegister')"));
	}
}
