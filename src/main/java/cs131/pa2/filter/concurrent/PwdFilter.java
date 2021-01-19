package cs131.pa2.filter.concurrent;
/**
 * The filter for pwd command
 * @author cs131a
 *
 */
public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	public void process() {
		try {
			output.put(processLine(""));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			IAmDead = true;
			output.put(KillString);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}
}
