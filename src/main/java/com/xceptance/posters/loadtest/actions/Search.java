package com.xceptance.posters.loadtest.actions;

import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;

import com.xceptance.posters.loadtest.util.SearchOption;
import com.xceptance.posters.loadtest.validators.HeaderValidator;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.HtmlPageUtils;
import com.xceptance.xlt.api.validators.ContentLengthValidator;
import com.xceptance.xlt.api.validators.HtmlEndTagValidator;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;

/**
 * Enter the given search phrase in the site's search bar and submit the form.
 */
public class Search extends AbstractHtmlPageAction {
	/**
	 * Search phrase.
	 */
	private final String phrase;

	/**
	 * Search form.
	 */
	private HtmlForm searchForm;

	/**
	 * Search option ({@link SearchOption#HITS} or {@link SearchOption#NO_HITS} ).
	 */
	private final SearchOption searchOption;

	/**
	 * Constructor.
	 * 
	 * @param previousAction The previously performed action
	 * @param phrase         The search phrase
	 * @param option         The search option that defines if we expect a hit or a
	 *                       no-hit
	 */
	public Search(final AbstractHtmlPageAction previousAction, final String phrase, final SearchOption option) {
		super(previousAction, null);
		this.phrase = phrase;
		searchOption = option;
	}

	/**
	 * Validation prior to execution.
	 * 
	 * @throws Exception if some of the required input elements couldn't be found.
	 */
	@Override
	public void preValidate() throws Exception {
		// Get the current page.
		final HtmlPage page = getPreviousAction().getHtmlPage();
		Assert.assertNotNull("Failed to get page from previous action.", page);

		// Check that the search form is available.
		Assert.assertTrue("Search form not found.", HtmlPageUtils.isElementPresent(page, "id('header-search-form')"));

		// Remember the search form.
		searchForm = HtmlPageUtils.findSingleHtmlElementByXPath(page, "id('header-search-form')");
	}

	/**
	 * Executes the search. Primarily this includes the input of the search phrase
	 * and a click on the proper search button.
	 * 
	 * @throws Exception if some of the inputs have become invalid or setting the
	 *                   value attribute of the search input field has failed.
	 */
	@Override
	protected void execute() throws Exception {
		// Fill the search form with the given phrase.
		HtmlPageUtils.setInputValue(searchForm, "searchText", phrase);

		// Submit the search.
		loadPageByFormSubmit(searchForm);
	}

	/**
	 * Validation after search has become complete.
	 * 
	 * @throws Exception if no search result block element could be found
	 */
	@Override
	protected void postValidate() throws Exception {
		// Get the result of the action.
		final HtmlPage page = getHtmlPage();

		// Basic checks - see action 'Homepage' for some more details how and when to
		// use these validators.
		HttpResponseCodeValidator.getInstance().validate(page);
		ContentLengthValidator.getInstance().validate(page);
		HtmlEndTagValidator.getInstance().validate(page);

		HeaderValidator.getInstance().validate(page);

		// Check that the desired option result was achieved.
		switch (searchOption) {
		case HITS:
			Assert.assertNotNull("Expected at least one hit for '" + phrase + "'.",
					HtmlPageUtils.findSingleHtmlElementByID(page, "productOverview"));
			break;

		case NO_HITS:
			Assert.assertFalse("Search phrase '" + phrase + "' should result in no hits.",
					HtmlPageUtils.isElementPresent(page, "productOverview"));
			break;

		default:
			Assert.fail("Unknown search option.");
			break;
		}
	}
}
