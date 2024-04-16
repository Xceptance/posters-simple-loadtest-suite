package com.xceptance.posters.loadtest.actions.order;

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
 * Opens the cart overview page.
 */
public class ViewCart extends AbstractHtmlPageAction
{
    /**
     * Link to shopping cart page.
     */
    private HtmlElement viewCartLink;

    /**
     * Constructor
     * 
     * @param previousAction The previously performed action
     */
    public ViewCart(final AbstractHtmlPageAction previousAction)
    {
        super(previousAction, null);
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the previous action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action.", page);

        // Check if the mini cart menu is present
        if (HtmlPageUtils.isElementPresent(page, "id('mini-cart-menu')"))
        {
            // Check if the cart overview link is available.
            if (HtmlPageUtils.isElementPresent(page, "id('mini-cart-menu')//div[@class='link-button']/a"))
            {
                // Remember cart overview link.
                viewCartLink = HtmlPageUtils.findSingleHtmlElementByXPath(page, "id('mini-cart-menu')//div[@class='link-button']/a");
            }
            else
            {
                // Set viewCartLink to null to indicate that it is not available
                viewCartLink = null;
            }
        }
        else
        {
            // Set viewCartLink to null to indicate that it is not available
            viewCartLink = null;
        }
    }

    @Override
    protected void execute() throws Exception
    {
        // Load the cart overview page if viewCartLink is available
        if (viewCartLink != null)
        {
            loadPageByClick(viewCartLink);
        }
    }

    @Override
    protected void postValidate() throws Exception
    {
        // If viewCartLink is null, it means the mini cart menu is not present, so we
        // skip validation
        if (viewCartLink != null)
        {
            // Get the result of the action.
            final HtmlPage page = getHtmlPage();

            // Basic checks - see action 'Homepage' for some more details how and when to
            // use these validators.
            HttpResponseCodeValidator.getInstance().validate(page);
            ContentLengthValidator.getInstance().validate(page);
            HtmlEndTagValidator.getInstance().validate(page);

            HeaderValidator.getInstance().validate(page);

            // Check that it's the cart overview page.
            Assert.assertTrue("Title not found", HtmlPageUtils.isElementPresent(page, "id('cart-title')"));
            Assert.assertTrue("Total price not found", HtmlPageUtils.isElementPresent(page, "id('order-sub-total-value')"));
            Assert.assertTrue("Checkout button not found", HtmlPageUtils.isElementPresent(page, "id('btn-start-checkout')"));
        }
    }
}
