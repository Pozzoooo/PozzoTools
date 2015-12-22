package pozzo.apps.pozzotools;

import android.test.AndroidTestCase;

/**
 * Test our HTML utils.
 *
 * Created by Pozzo on 22/12/15.
 */
public class HtmlUtilTest extends AndroidTestCase {

    public void testParseLink() throws Exception {
        String pureText = "Nao eh link, ehlink.com www.link.com e soh nao@link.com";
		String expectedHtml = "Nao eh link, <a href=\"ehlink.com\">ehlink.com</a> " +
				"<a href=\"www.link.com\">www.link.com</a> e soh nao@link.com";

        String parsedText = HtmlUtil.parseLink(pureText);
        assertEquals(expectedHtml, parsedText);
    }

    public void testParseEmail() throws Exception {
		String pureText = "Nada nada, link.com <a href=\"link\">link</a> email@hot.com naomail@poxa " +
				"fim";
		String expectedHtml = "Nada nada, link.com <a href=\"link\">link</a> " +
				"<a href=\"mailto:email@hot.com\">email@hot.com</a> naomail@poxa fim";

		String parsedText = HtmlUtil.parseEmail(pureText);
		assertEquals(expectedHtml, parsedText);
    }

    public void testParsePhone() throws Exception {
		String pureText = "Um telefone como este (46)8405-0547 ou este 35381133 ou 9898-9898";
		String expectedHtml = "Um telefone como este " +
				"<a href=\"tel:(46)8405-0547\">(46)8405-0547</a> ou este " +
				"<a href=\"tel:35381133\">35381133</a> ou <a href=\"tel:9898-9898\">9898-9898</a>";

		String parsedText = HtmlUtil.parsePhone(pureText);
		assertEquals(expectedHtml, parsedText);
    }
}
