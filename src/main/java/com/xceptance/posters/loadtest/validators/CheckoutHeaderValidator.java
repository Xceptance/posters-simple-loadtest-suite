package com.xceptance.posters.loadtest.validators;

import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.xlt.api.util.HtmlPageUtils;

/**
 * Checks for the correct header elements during checkout.
 */
public class CheckoutHeaderValidator
{
    /**
     * Make a stateless singleton available.
     */
    private static final CheckoutHeaderValidator instance = new CheckoutHeaderValidator();

    /**
     * Checks the poster store header elements.
     * 
     * @param page The page to check.
     */
    public void validate(final HtmlPage page) throws Exception
    {
        // Validate presence of some basic elements in the header:
        // The brand logo
        Assert.assertTrue("Brand not found.", HtmlPageUtils.isElementPresent(page, "id('header-brand')"));
        // The showUserMenu button
        Assert.assertTrue("Cart overview in header not found.", HtmlPageUtils.isElementPresent(page, "id('show-user-menu')"));
    }

    /**
     * The instance for easy reuse. Possible because this validator is stateless.
     * 
     * @return the instance
     */
    public static CheckoutHeaderValidator getInstance()
    {
        return instance;
    }
}