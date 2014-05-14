/**
 * Tuple
 *
 * Version 1.0.0
 *
 * Created on 14/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */
package visitor_practice;

public class Tuple<T1, T2> {
	public Tuple(T1 fst, T2 snd) {
		this.fst = fst;
		this.snd = snd;
	}

	public final T1 fst;
	public final T2 snd;
}
