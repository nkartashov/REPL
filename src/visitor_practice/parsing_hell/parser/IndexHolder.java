/**
 * IndexHolder
 *
 * Version 1.0.0
 *
 * Created on 23/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice.parsing_hell.parser;

public class IndexHolder {

	public void add(int sign) {
		if (holding < 2) {
			holding++;
		}
		last = (last + 1) % 2;
		holder[last] = sign;
	}

	public int getLast() {
		if (holding < 1) {
			throw new RuntimeException("Nothing to get");
		}
		return holder[last];
	}

	public int getSecondToLast() {
		if (holding < 2) {
			throw new RuntimeException("Nothing to get");
		}
		int i = last - 1 < 0 ? 1 : 0;
		return holder[i];
	}

	private int last = -1;
	private int holding = 0;
	private int[] holder = new int[2];
}
