package com.xceptance.posters.loadtest.actions.account;

import com.xceptance.posters.loadtest.validators.HeaderValidator;
import org.junit.Assert;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.HtmlPageUtils;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Log out.
 */
public class Logout extends AbstractHtmlPageAction
{
    /**
     * The logout link.
     */
    HtmlElement logoutLink;

    /**
     * Constructor.
     * 
     * @param previousAction
     *            The previously performed action
     */
    public Logout(final AbstractHtmlPageAction previousAction)
    {
        super(previousAction, null);
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the previous action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action.", page);

        final String accountButtonSelector = "#btnCartOverviewForm .goToAccountOverview";

        //List of all occurrences for the selector
        final DomNodeList<DomNode> foundElements = page.querySelectorAll(accountButtonSelector);
        
        //Making sure that there is exactly one occurrence for our specified selector
        Assert.assertEquals("No or too many elements found for Selector: " + accountButtonSelector + " -", 1, foundElements.size());
        
        final String logoutLinkSelector = "#btnCartOverviewForm .goToLogout";

        //Making sure that there is exactly one occurrence for our specified selector
        Assert.assertEquals("No or too many elements found for Selector: " + logoutLinkSelector + " -", 1, page.querySelectorAll(logoutLinkSelector).size());

        // Remember logout link.
        logoutLink = (HtmlElement) page.querySelectorAll(logoutLinkSelector).get(0);
    }

    @Override
    protected void execute() throws Exception
    {
        // Log out by clicking the link.
        loadPageByClick(logoutLink);
    }

    @Override
    protected void postValidate() throws Exception
    {
        // Get the result of the action.
        final HtmlPage page = getHtmlPage();

        // Basic checks - see action 'Homepage' for some more details how and when to use these validators.
        HttpResponseCodeValidator.getInstance().validate(page);
        ContentLengthValidator.getInstance().validate(page);
        HtmlEndTagValidator.getInstance().validate(page);

        HeaderValidator.getInstance().validate(page);

        //The sign in selector 
        String signInButtonSelector = "#btnCartOverviewForm .goToLogin";
        
        //List of all occurrences for the selector
        DomNodeList<DomNode> foundElements = page.querySelectorAll(signInButtonSelector);
        
        //Making sure that there is exactly one occurrence for our specified selector
        Assert.assertEquals("No or too many elements found for Selector: " + signInButtonSelector + " -", 1, foundElements.size());

        // Check that it's the home page.
        final HtmlElement blogNameElement = page.getHtmlElementById("intro");
        Assert.assertNotNull("Quote not found", blogNameElement);

        // Check the intro quote.
        Assert.assertEquals("Quote does not match", "Began with a simple idea \"SHATATATATA!\" - M. Scott", blogNameElement.asText());
    }
}
