package com.xceptance.posters.loadtest.validators;

import java.util.Iterator;
import java.util.List;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.xlt.api.util.HtmlPageUtils;

/**
 * Checks for the correct header elements.
 */
public class NavBarValidator
{
    /**
     * Make a stateless singleton available.
     */
    private static final NavBarValidator instance = new NavBarValidator();

    /**
     * Checks the poster store side navigation elements
     * 
     * @param page
     *            the page to check
     */
    public void validate(final HtmlPage page) throws Exception
    {

        // Check that the category menu contains at least two top categories
        // For this purpose we get a list of all top categories and check the
        // size of the list
        final List<HtmlElement> topCategories = HtmlPageUtils.findHtmlElements(page, "id('categoryMenu')/ul/li[@class='has-dropdown']");
        Assert.assertTrue("There are less then two top categories in the side nav.", topCategories.size() >= 2);

        // Check that each top category has at least one drop down item
        // category
        for (final Iterator<HtmlElement> iterator = topCategories.iterator(); iterator.hasNext();)
      {
            final HtmlElement htmlElement = iterator.next();
            // relative xpath to address the first sibling after the top
            // category that is a drop down item
            Assert.assertTrue("Top category is not followed by a level-1 category.",!page.querySelectorAll("#categoryMenu .dropdown-menu").isEmpty());
        }

    }

    /**
     * The instance for easy reuse. Possible because this validator is stateless.
     * 
     * @return the instance
     */
    public static NavBarValidator getInstance()
    {
        return instance;
    }
}
