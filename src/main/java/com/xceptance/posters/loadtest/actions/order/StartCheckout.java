package com.xceptance.posters.loadtest.actions.order;

import com.xceptance.posters.loadtest.validators.CheckoutHeaderValidator;
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
 * Starts the checkout.
 */
public class StartCheckout extends AbstractHtmlPageAction
{
    /**
     * The checkout link.
     */
    private HtmlElement checkoutLink;

    /**
     * Constructor
     * 
     * @param previousAction
     *            The previously performed action
     */
    public StartCheckout(final AbstractHtmlPageAction previousAction)
    {
        super(previousAction, null);
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the previous action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action.", page);
        
        //The item quantity in mini cart selector 
        final String itemQuantity = "#miniCartMenu .font-bold";
        
        //List of all occurrences for the selector
        final DomNodeList<DomNode> foundElements = page.querySelectorAll(itemQuantity);
        
        //Making sure that there is exactly one occurrence for our specified selector
        Assert.assertEquals("No or too many elements found for Selector: " + itemQuantity + " -", 1, foundElements.size());
        
        final HtmlElement cartItemQuantity = (HtmlElement) foundElements.get(0);
        
        // Check that the cart is not empty.
        final boolean cartIsEmpty = cartItemQuantity.asText().matches(".*: 0.*");
        Assert.assertFalse("Cart must not be empty for checkout.", cartIsEmpty);

        // Check that the checkout link is available.
        Assert.assertTrue("Checkout link not found.", HtmlPageUtils.isElementPresent(page, "id('btnStartCheckout')"));

        // Remember the checkout link.
        checkoutLink = HtmlPageUtils.findSingleHtmlElementByID(page, "btnStartCheckout");
    }

    @Override
    protected void execute() throws Exception
    {
        // Start the checkout.
        loadPageByClick(checkoutLink);
    }

    @Override
    protected void postValidate() throws Exception
    {
        // Get the result of the action
        final HtmlPage page = getHtmlPage();

        // Basic checks - see action 'Homepage' for some more details how and when to use these validators.
        HttpResponseCodeValidator.getInstance().validate(page);
        ContentLengthValidator.getInstance().validate(page);
        HtmlEndTagValidator.getInstance().validate(page);

        CheckoutHeaderValidator.getInstance().validate(page);

        // Check that it's the page to enter or select a shipping address.
        Assert.assertTrue("Title not found.", HtmlPageUtils.isElementPresent(page, "id('titleDelAddr')"));

        // Check that the form to enter a new shipping address is available.
        Assert.assertTrue("Form to enter shipping address not found.", HtmlPageUtils.isElementPresent(page, "id('formAddDelAddr')"));
    }
}
