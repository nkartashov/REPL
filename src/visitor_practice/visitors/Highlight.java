/**
 * Highlight
 *
 * Version 1.0.0
 *
 * Created on 14/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.visitors;

public class Highlight {
	public Highlight(int start, int length, String style) {
		this.start = start;
		this.length = length;
		this.style = style;
	}

	public final int start;
	public final int length;
	public final String style;
}
