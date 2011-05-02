package kwic_adt;

public class Main {
	
	private enum Action {search, print};
	
	///////////////// Settings
	private static final Action _action = Action.search;
	private static final String _keyword = "topic";
	private static final int _context = 5;
	
	public static void main(String[] args) {
		if( args.length != 1 ) printErrorAndExit();
		
		LineStorage storage = new LineStorage();
		IndexStorage indexStorage = new IndexStorage();
		Input input = new Input();
		CircularShifter shifter = new CircularShifter();
		Alphabetizer alphabetizer = new Alphabetizer();
		Output out = new Output();
		
		input.parse(storage,args[0]);
		shifter.shift(storage,indexStorage);
		alphabetizer.alpha(indexStorage);
		
		if( _action == Action.print )
			out.print(storage,indexStorage);
		else
			out.searchAndPrint(storage,indexStorage, _keyword, _context);
	}
	
	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}
