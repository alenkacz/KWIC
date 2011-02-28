package kwic_adt;

public class Main {
	
	public static void main(String[] args) {
		if( args.length != 1 ) printErrorAndExit();
		
		LineStorage storage = new LineStorage();
		Input input = new Input();
		CircularShifter shifter = new CircularShifter();
		Alphabetizer alphabetizer = new Alphabetizer();
		Output out = new Output();
		
		input.parse(storage,args[0]);
		shifter.shift(storage);
		alphabetizer.alpha(shifter);
		out.print();
	}
	
	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}
