package com.xceptance.posters.loadtest.actions.account;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.posters.loadtest.validators.HeaderValidator;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Go to the Homepage
 */

public class NavigateToHomepage extends AbstractHtmlPageAction {
    /**
     * The header-brand-logo
     */
    private HtmlElement headerBrand;

    /**
     * Constructor.
     * 
     * @param previousAction
     *            The previously performed action
     */
    public NavigateToHomepage(final AbstractHtmlPageAction previousAction)
    {
        super(previousAction, null);
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the previous action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action.", page);
       
        // Remember the header-brand-logo.
        headerBrand = page.getHtmlElementById("header-brand-logo");
       
    }

    @Override
    protected void execute() throws Exception
    {
        // Click the logo to load the homepage.
        loadPageByClick(headerBrand);
    }

    @Override
    protected void postValidate() throws Exception
    {
        // Get the result of the action.
        final HtmlPage page = getHtmlPage();

        // Repeated basic checks - see action 'Homepage' for some more details how and when to use these validators.
        HttpResponseCodeValidator.getInstance().validate(page);
        ContentLengthValidator.getInstance().validate(page);
        HtmlEndTagValidator.getInstance().validate(page);

        HeaderValidator.getInstance().validate(page);

        // Get the homepage intro quote.
        final HtmlElement introElement = page.getHtmlElementById("intro-text-homepage");
        Assert.assertNotNull("Intro quote not found", introElement);

        // Get the content form the element.
        final String text = introElement.asNormalizedText();

        // Make sure we have the correct intro quote.
        Assert.assertEquals("Intro quote does not match", "Began with a simple idea \"SHATATATATA!\" - M. Scott", text);
    }
}
