package com.xceptance.posters.loadtest.actions.account;

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
 * Browses to the registration form.
 */
public class GoToRegistrationForm extends AbstractHtmlPageAction
{
    /**
     * The link to the registration form.
     */
    private HtmlElement registerLink;

    /**
     * Constructor.
     * 
     * @param previousAction
     *            The previously performed action
     */
    public GoToRegistrationForm(final AbstractHtmlPageAction previousAction)
    {
        super(previousAction, null);
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the previous action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action.", page);

        // Check that the registration link is available.
        Assert.assertTrue("Registration link not found.", HtmlPageUtils.isElementPresent(page, "id('go-to-registration')"));

        // Remember the registration link.
        registerLink = HtmlPageUtils.findSingleHtmlElementByID(page, "go-to-registration");
    }

    @Override
    protected void execute() throws Exception
    {
        // Load the registration page by clicking the link.
        loadPageByClick(registerLink);
    }

    @Override
    protected void postValidate() throws Exception
    {
        // get the result of this action
        final HtmlPage page = getHtmlPage();

        // Basic checks - see action 'Homepage' for some more details how and when to use these validators
        HttpResponseCodeValidator.getInstance().validate(page);
        ContentLengthValidator.getInstance().validate(page);
        HtmlEndTagValidator.getInstance().validate(page);

        HeaderValidator.getInstance().validate(page);

        // Check that it's the registration page.
        Assert.assertTrue("Registration form not found.", HtmlPageUtils.isElementPresent(page, "id('form-register')"));
        Assert.assertTrue("Button to create account not found.", HtmlPageUtils.isElementPresent(page, "id('btn-register')"));
    }
}
