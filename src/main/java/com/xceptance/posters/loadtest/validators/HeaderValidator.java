package com.xceptance.posters.loadtest.validators;

import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.xlt.api.util.HtmlPageUtils;

/**
 * Checks for the correct header elements (non-checkout pages).
 */
public class HeaderValidator
{
    /**
     * Make a stateless singleton available.
     */
    private static final HeaderValidator instance = new HeaderValidator();

    /**
     * Checks the poster store header elements.
     * 
     * @param page The page to check.
     */
    public void validate(final HtmlPage page) throws Exception
    {
        // Validate presence of some basic elements in the header:
        // The brand logo
        Assert.assertTrue("Brand not found.", HtmlPageUtils.isElementPresent(page, "//img[@class ='shop-logo']"));
        // The search form
        Assert.assertTrue("Search form not found.", HtmlPageUtils.isElementPresent(page, "id('header-search-form')"));
        // The search input
        Assert.assertTrue("Search input field not found.", HtmlPageUtils.isElementPresent(page, "id('header-search-text')"));
        // The search button
        Assert.assertTrue("Search button not found.", HtmlPageUtils.isElementPresent(page, "id('header-search-button')"));
        // The cart overview
        Assert.assertTrue("Cart overview in header not found.", HtmlPageUtils.isElementPresent(page, "id('header-cart-overview')"));
    }

    /**
     * The instance for easy reuse. Possible because this validator is stateless.
     * 
     * @return the instance
     */
    public static HeaderValidator getInstance()
    {
        return instance;
    }
}
