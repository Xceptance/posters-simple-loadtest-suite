package com.xceptance.posters.loadtest.actions;

import java.net.URL;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.posters.loadtest.validators.HeaderValidator;
import com.xceptance.posters.loadtest.validators.NavBarValidator;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Loads the homepage from the given URL.<br/>
 * This is usually the starting point for most test cases.
 */
public class Homepage extends AbstractHtmlPageAction
{
    /**
     * The URL as string to fetch the data from.
     */
    private final String urlAsString;

    /**
     * The URL object.
     */
    private URL url;

    /**
     * Constructor. This will be called from a test case and while doing so the homepage's URL will be passed so that
     * the page can eventually be loaded.
     * 
     * @param urlAsString
     *            the URL to fetch the data from
     */
    public Homepage(final String urlAsString)
    {
        super(null);

        this.urlAsString = urlAsString;
    }

    @Override
    public void preValidate() throws Exception
    {
        // We have to check, whether the passed URL string is valid.
        Assert.assertNotNull("Url must not be null", urlAsString);

        // Use the java URL class to do the final validation since it will throw
        // an exception in case this is not a valid URL.
        // We do not have to deal with the exception, the framework will do it.
        url = new URL(urlAsString);
    }

    /**
     * Execute the request. Once pre-execution conditions have been meet, the execute method can be called to load the
     * page, in this case the homepage will be requested.
     */
    @Override
    protected void execute() throws Exception
    {
        // Load the page simply by firing the URL.
        // Always make sure that loadPage* methods are used.
        loadPage(url);
    }

    /**
     * Validate the correctness of the result. Once the homepage has been loaded, we can ensure that certain key
     * elements are present in our previous request's responses. For example, here we are validating that the proper
     * response code was sent, the length of the page is correct, an end tag is present, there is a headline on the
     * page. This is all being done with the help of validators. Validators are used when we need to check the same
     * thing after several different actions.
     */
    @Override
    protected void postValidate() throws Exception
    {
        // Get the result of the last action.
        final HtmlPage page = getHtmlPage();

        // First, we check all common criteria. This code can be bundled and
        // reused if needed. For the purpose of a
        // programming example, we leave it here as detailed as possible.

        // check the response code, the singleton instance validates for 200
        HttpResponseCodeValidator.getInstance().validate(page);

        // Check the content length, compare delivered content length to the
        // content length that was announced in the HTTP response header.
        ContentLengthValidator.getInstance().validate(page);

        // Check for complete HTML.
        HtmlEndTagValidator.getInstance().validate(page);

        // We can be pretty sure now, that the page fulfills the basic
        // requirements to be a valid page from our demo poster store.

        // Run more page specific tests now.

        // Check for the header.
        HeaderValidator.getInstance().validate(page);

        // Check the side navigation.
        NavBarValidator.getInstance().validate(page);

        // Get the homepage intro quote.
        final HtmlElement introElement = page.getHtmlElementById("intro-text-homepage");
        Assert.assertNotNull("Intro quote not found", introElement);

        // Get the content form the element.
        final String text = introElement.asNormalizedText();

        // Make sure we have the correct intro quote.
        Assert.assertEquals("Intro quote does not match", "Began with a simple idea \"SHATATATATA!\" - M. Scott", text);
    }
}
