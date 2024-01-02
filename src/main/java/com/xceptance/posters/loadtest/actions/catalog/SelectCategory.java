package com.xceptance.posters.loadtest.actions.catalog;

import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
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
 * Selects a random sub-category links and opens the related product overview page.
 */
public class SelectCategory extends AbstractHtmlPageAction
{
    /**
     * Chosen level-1 category.
     */
    private HtmlElement categoryLink;

    /**
     * Constructor
     * 
     * @param previousAction
     *            The previously performed action
     */
    public SelectCategory(final AbstractHtmlPageAction previousAction)
    {
        super(previousAction, null);
    }

    @Override
    public void preValidate() throws Exception
    {
        // Get the result of the action.
        final HtmlPage page = getPreviousAction().getHtmlPage();
        
        final String itemLinkSelector = "#header-categories .dropdown .dropdown-toggle";

        //List of all occurrences for the selector
        final DomNodeList<DomNode> foundElements = page.querySelectorAll(itemLinkSelector);

        // Get all drop down item links and select one randomly.
        categoryLink = (HtmlElement) HtmlPageUtils.pickOneRandomly(foundElements, false, false);

    }

    @Override
    protected void execute() throws Exception
    {
        // Click the link.
        loadPageByClick(categoryLink);
    }

    @Override
    protected void postValidate() throws Exception
    {
        // Get the result of the action.
        final HtmlPage page = getHtmlPage();

        // Basic checks that are part of the XLT API.
        HttpResponseCodeValidator.getInstance().validate(page);
        ContentLengthValidator.getInstance().validate(page);
        HtmlEndTagValidator.getInstance().validate(page);

        // Check for the header.
        HeaderValidator.getInstance().validate(page);

        // Check the side navigation.
        NavBarValidator.getInstance().validate(page);

        // The product over view element is present....
        Assert.assertTrue("Product over view element is bot present", HtmlPageUtils.isElementPresent(page, "id('productOverview')"));

        // ...and we also see some poster's thumbnail images.
        HtmlPageUtils.findHtmlElements(page, "id('productOverview')//img[@class='card-img-top']");
    }
}