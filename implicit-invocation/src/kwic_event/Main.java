package kwic_event;

public class Main {
	
private enum Action {search, print};
	
	///////////////// Settings
	private static final Action _action = Action.search;
	private static final String _keyword = "into";
	private static final int _context = 5;
	
	public static void main(String[] args) {
		if( args.length != 1 ) printErrorAndExit();
		
		run(args[0]);
	}

	public static void run(String path) {
		LineStorage input = new LineStorage();
		IndexStorage indexes = new IndexStorage();
		
		Input inputModule = new Input();
	    CircularShifter shifter = new CircularShifter(indexes);
	    Alphabetizer alphabetizer = new Alphabetizer();
	    Output output = new Output();
	    Search search = new Search();
	    
	    input.addObserver(shifter);
	    indexes.addObserver(alphabetizer);

	    inputModule.parse(input, path);
	    
	    if( _action == Action.print ) {
	    	output.print(input,indexes);
	    } else if( _action == Action.search ) {
	    	search.search(_keyword,_context,input,indexes);
	    }
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}
