package com.xceptance.posters.loadtest.actions.account;

import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;


import com.xceptance.posters.loadtest.util.Account;
import com.xceptance.posters.loadtest.validators.HeaderValidator;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.HtmlPageUtils;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Fill in and submit the sign-in form. <br>
 * The previous action should be {@link GoToSignIn}. The resulting page is the homepage.
 */
public class Login extends AbstractHtmlPageAction
{
    /**
     * The sign-in form.
     */
    private HtmlForm signInForm;

    /**
     * The button to submit the sign-in form.
     */
    private HtmlElement signInButton;
    
    /**
     * The account to log in.
     */
    private final Account account;

    /**
     * Constructor
     * 
     * @param previousAction
     *            the previously performed action
     * @param account
     *            the account to log in
     */
    public Login(final AbstractHtmlPageAction previousAction, final Account account)
    {
        super(previousAction, null);
        this.account = account;
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the previous action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action.", page);

        // Check that the sign-in form is available.
        Assert.assertTrue("Sign-in form not found", HtmlPageUtils.isElementPresent(page, "id('form-login')"));

        // Remember the sign-in form.
        signInForm = HtmlPageUtils.findSingleHtmlElementByID(page, "form-login");

        // Check that the sign-in button is available.
        Assert.assertTrue("Sign-in button not found", HtmlPageUtils.isElementPresent(page, "id('btn-sign-in')"));

        // Remember the sign-in button.
        signInButton = HtmlPageUtils.findSingleHtmlElementByID(page, "btn-sign-in");
        
        HtmlPageUtils.findSingleHtmlElementByID(page, "header-brand");
    }

    @Override
    protected void execute() throws Exception
    {
        // Fill in the form.
        HtmlPageUtils.setInputValue(signInForm, "email", account.getEmail());
        HtmlPageUtils.setInputValue(signInForm, "password", account.getPassword());

        // Submit the registration form.
        loadPageByClick(signInButton);
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
        
        final String accountButtonSelector = "#go-to-account-overview";

        //List of all occurrences for the selector
        final DomNodeList<DomNode> foundElements = page.querySelectorAll(accountButtonSelector);
        
        //Making sure that there is exactly one occurrence for our specified selector
        Assert.assertEquals("No or too many elements found for Selector: " + accountButtonSelector + " -", 1, foundElements.size());

        // Check that it's the user page by looking for the title.
        final HtmlElement blogNameElement = page.getHtmlElementById("title-account-overview");
        Assert.assertNotNull("Quote not found", blogNameElement);

        // Check the title.
        Assert.assertEquals("User page title not found", "My Account", blogNameElement.asNormalizedText());
        
        // After validating the user page, navigate back to the homepage.
        navigateToHomepage();
    }

    private void navigateToHomepage() throws Exception
    {
        // Get the result of the action.
        final HtmlPage page = getHtmlPage();
        

        // Print out the URL of the page.
        System.out.println("Cuuurrent page URL: " + page.getUrl().toString());


        // Find the header brand element.
        final HtmlElement headerBrand = page.getFirstByXPath("//a[@id='header-brand']");

        // Click on the header brand to navigate to the homepage.
        loadPageByClick(headerBrand);
        

        // Print out the URL of the page.
        System.out.println("Cuuurrent page URL: " + page.getUrl().toString());

    }


   
}
