package cs131.pa2.filter.concurrent;

/**
 * The filter for printing in the console
 * 
 * @author cs131a
 *
 */
public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}

	public void process() {
		while (!isDone()) {
			try {
				processLine(input.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String processLine(String line) {
		if (line.equals(KillString)) {
			IAmDead = true;
			return null;
		}
		System.out.println(line);
		return null;
	}
}
