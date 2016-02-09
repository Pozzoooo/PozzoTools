package pozzo.apps.tools;

import android.util.Patterns;

import java.util.regex.Matcher;

/**
 * Utilities for Html manipulation.
 *
 * @author pozzo
 * @since 21/10/15.
 */
public class HtmlUtil {

	/**
	 * @return From text to html tag.
	 */
	public static String parseLink(String body) {
		StringBuilder newBody = new StringBuilder();
		Matcher matcher = Patterns.WEB_URL.matcher(body);
		int lastEnd = 0;
		while (matcher.find()) {
			if(matcher.start() > 0 && body.charAt(matcher.start()-1) == '@')
				continue;//Pulamos provavel email

			String link = body.substring(matcher.start(), matcher.end());
			link = "<a href=\"" + link + "\">" + link + "</a>";
			newBody.append(body.substring(lastEnd, matcher.start())).append(link);
			lastEnd = matcher.end();
		}
		if(lastEnd == 0)
			return body;

		newBody.append(body.substring(lastEnd, body.length()));
		return newBody.toString();
	}

	/**
	 * @return From text to html tag.
	 */
	public static String parseEmail(String body) {
		StringBuilder newBody = new StringBuilder();
		Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(body);
		int lastEnd = 0;
		while (matcher.find()) {
			String link = body.substring(matcher.start(), matcher.end());
			link = "<a href=\"mailto:" + link + "\">" + link + "</a>";
			newBody.append(body.substring(lastEnd, matcher.start())).append(link);
			lastEnd = matcher.end();
		}
		if(lastEnd == 0)
			return body;

		newBody.append(body.substring(lastEnd, body.length()));
		return newBody.toString();
	}

	/**
	 * @return From text to html tag.
	 */
	public static String parsePhone(String body) {
		StringBuilder newBody = new StringBuilder();
		Matcher matcher = Patterns.PHONE.matcher(body);
		int lastEnd = 0;
		while (matcher.find()) {
			if(matcher.start() > 0 && body.charAt(matcher.start()-1) != ' ')
				continue;

			String link = body.substring(matcher.start(), matcher.end());
			link = "<a href=\"tel:" + link + "\">" + link + "</a>";
			newBody.append(body.substring(lastEnd, matcher.start())).append(link);
			lastEnd = matcher.end();
		}
		if(lastEnd == 0)
			return body;

		newBody.append(body.substring(lastEnd, body.length()));
		return newBody.toString();
	}
}
