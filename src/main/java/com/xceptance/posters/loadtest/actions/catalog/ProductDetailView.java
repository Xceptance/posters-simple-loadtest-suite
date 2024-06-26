package com.xceptance.posters.loadtest.actions.catalog;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.posters.loadtest.validators.HeaderValidator;
import com.xceptance.posters.loadtest.validators.NavBarValidator;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.HtmlPageUtils;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Opens the product detail page for a randomly chosen poster.
 */
public class ProductDetailView extends AbstractHtmlPageAction
{
    /**
     * The product detail link to follow
     */
    private HtmlElement productDetailLink;

    /**
     * Constructor
     * 
     * @param previousAction
     *            The previously performed action
     */
    public ProductDetailView(final AbstractHtmlPageAction previousAction)
    {
        super(previousAction, null);
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the current page.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        Assert.assertNotNull("Failed to get page from previous action", page);

        // Check the current page is a product overview category page...
        Assert.assertTrue("Product Overview element missing.", HtmlPageUtils.isElementPresent(page, "id('product-overview')"));

        // ...and we also see some poster's thumbnail images.
        HtmlPageUtils.findHtmlElements(page, "id('product-overview')//img[@class='card-img-top']");

        // Remember a random product's link URL.
        productDetailLink = HtmlPageUtils.findHtmlElementsAndPickOne(page, "id('product-overview')//div[@class='card-body']/a");
        Assert.assertNotNull("No matching product detail link found.", productDetailLink);
    }

    @Override
    protected void execute() throws Exception
    {
        // Click on the chosen product detail link to load the product detail page.
        loadPageByClick(productDetailLink);
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
        NavBarValidator.getInstance().validate(page);

        // Check it's a product detail page.
        // The product's name element in the headline is present.
        HtmlPageUtils.isElementPresent(page, "h3[id='product-detail-form-name']");

        // The product description is there in the right presentation (h3 - h4 -span).
        Assert.assertTrue("Product description is not there or not in the right presentation (h3 - h4 -span).",
                          HtmlPageUtils.isElementPresent(page, "id('product-detail-form-description-overview')"));
        Assert.assertTrue("Product description is not there or not in the right presentation (h3 - h4 -span).",
                          HtmlPageUtils.isElementPresent(page, "id('product-detail-form-description')"));
        // There is a price with the correct currency.
        final HtmlElement productPriceElement = HtmlPageUtils.findSingleHtmlElementByID(page, "product-detail-form-price");
        final String productPrice = productPriceElement.getTextContent();
        Assert.assertTrue("The price does not start with $", productPrice.startsWith("$"));

        // Product configuration elements are present.
        Assert.assertTrue("Product configuration element (finish matte) is not present.",
                          HtmlPageUtils.isElementPresent(page, "id('finish-matte')"));
        Assert.assertTrue("Product configuration element (finish gloss) is not present.",
                          HtmlPageUtils.isElementPresent(page, "id('finish-gloss')"));
        Assert.assertTrue("Product configuration element (size) is not present.", 
                          HtmlPageUtils.isElementPresent(page, "id('product-detail-form-size-selection')"));

        // 'Add to cart' button is available.
        Assert.assertTrue("'Add to cart' button is not available", HtmlPageUtils.isElementPresent(page, "id('btn-add-to-cart')"));
    }
}
