/**
 * Preprocessor
 *
 * Version 1.0.0
 *
 * Created on 08/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell;

public class Preprocessor {
	public String preprocess(String input) {
		StringBuilder builder = new StringBuilder();
		for (char ch: input.toCharArray()) {
			if (!Character.isWhitespace(ch)) {
				builder.append(ch);
			}
		}
		return builder.toString();
	}
}
