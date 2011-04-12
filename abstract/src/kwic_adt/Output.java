package kwic_adt;

import java.util.List;

public class Output {

	public void print(Alphabetizer alpha) {
		int size = alpha.getLineCount();
		
		for( int i = 0; i < size; ++i ) {
			// printing each line
			System.out.println(alpha.getLine(i));
		}
	}

	public void searchAndPrint(Alphabetizer alpha, String keyword, int context) {
		Search search = new Search(alpha);
		List<String> res = search.doSearch(keyword, context);
		
		for( String s : res ) {
			System.out.println(s);
		}
	}
}
