package com.xceptance.posters.loadtest.actions.account;

import com.xceptance.posters.loadtest.util.Account;
import com.xceptance.posters.loadtest.validators.HeaderValidator;
import org.junit.Assert;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.HtmlPageUtils;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Fill in and submit the registration form.
 */
public class Register extends AbstractHtmlPageAction
{
    /**
     * The registration form.
     */
    private HtmlForm registrationForm;

    /**
     * The button to submit the registration form.
     */
    private HtmlElement createAccountButton;

    /**
     * The account to register.
     */
    private final Account account;

    /**
     * Constructor.
     * 
     * @param previousAction
     *            The previously performed action
     * @param accountData
     *            The account data used to register new account
     */
    public Register(final AbstractHtmlPageAction previousAction, final Account accountData)
    {
        super(previousAction, null);
        account = accountData;
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the previous action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action.", page);

        // Check that the registration form is available.
        Assert.assertTrue("Registration form not found", HtmlPageUtils.isElementPresent(page, "id('formRegister')"));

        // Remember the registration form.
        registrationForm = HtmlPageUtils.findSingleHtmlElementByID(page, "formRegister");

        // Check that the create account button is available.
        Assert.assertTrue("Create account button not found", HtmlPageUtils.isElementPresent(page, "id('btnRegister')"));

        // Remember the create account button.
        createAccountButton = HtmlPageUtils.findSingleHtmlElementByID(page, "btnRegister");
    }

    @Override
    protected void execute() throws Exception
    {
        // Fill in the form.
        HtmlPageUtils.setInputValue(registrationForm, "lastName", account.getLastName());
        HtmlPageUtils.setInputValue(registrationForm, "firstName", account.getFirstName());
        HtmlPageUtils.setInputValue(registrationForm, "eMail", account.getEmail());
        HtmlPageUtils.setInputValue(registrationForm, "password", account.getPassword());
        HtmlPageUtils.setInputValue(registrationForm, "passwordAgain", account.getPassword());

        // Submit the registration form
        loadPageByClick(createAccountButton);
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

        // Check that the account was successfully created.
        final boolean isAccountCreated = page.asXml()
                                             .contains("Your account has been created. Log in with your email address and password.");
        Assert.assertTrue("Registration failed.", isAccountCreated);

        // Check that it's the sign in page.
        Assert.assertTrue("Sign in form not found.", HtmlPageUtils.isElementPresent(page, "id('formLogin')"));
        Assert.assertTrue("Link to register not found.", HtmlPageUtils.isElementPresent(page, "id('linkRegister')"));
        
        //The log in selector 
        String logInButtonSelector = "#btnCartOverviewForm .goToLogin";
        
        //List of all occurrences for the selector
        DomNodeList<DomNode> foundElements = page.querySelectorAll(logInButtonSelector);
        
        //Making sure that there is exactly one occurrence for our specified selector
        Assert.assertEquals("No or too many elements found for Selector: " + logInButtonSelector + " -", 1, foundElements.size());

        // Check that no customer is logged in.
        Assert.assertTrue("Customer is logged in after registration.", !foundElements.isEmpty());
        
    }
}
