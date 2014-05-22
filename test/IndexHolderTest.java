import org.junit.Assert;
import org.junit.Test;
import visitor_practice.parsing_hell.parser.IndexHolder;

/**
 * IndexHolderTest
 *
 * Version 1.0.0
 *
 * Created on 23/05/2014
 *
 * The following text is protected by GPLv2 licence
 * (http://www.gnu.org/licenses/gpl-2.0.html)
 */


public class IndexHolderTest {
	@Test
	public void OneAndOnlyTest() {
		IndexHolder indexHolder = new IndexHolder();
		for (int i = 0; i < 10; i++) {
			indexHolder.add(i);
			Assert.assertEquals(i, indexHolder.getLast());
			if (i > 0) {
				Assert.assertEquals(i - 1, indexHolder.getSecondToLast());
			}
		}

	}
}
