package kwic_adt;

public class Main {
	
	private enum Action {search, print};
	
	///////////////// Settings
	private static final Action _action = Action.search;
	private static final String _keyword = "into";
	private static final int _context = 5;
	
	public static void main(String[] args) {
		if( args.length != 1 ) printErrorAndExit();
		
		LineStorage storage = new LineStorage();
		Input input = new Input();
		CircularShifter shifter = new CircularShifter();
		Alphabetizer alphabetizer = new Alphabetizer(shifter);
		Output out = new Output();
		
		input.parse(storage,args[0]);
		shifter.shift(storage);
		alphabetizer.alpha();
		
		if( _action == Action.print )
			out.print(alphabetizer);
		else
			out.searchAndPrint(alphabetizer, _keyword, _context);
	}
	
	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}
