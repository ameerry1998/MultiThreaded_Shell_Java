package cs131.pa2.filter.concurrent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;
/**
 * The filter for redirecting the output to a file 
 * @author cs131a
 *
 */
public class RedirectFilter extends ConcurrentFilter {
	private FileWriter fw;
	
	/**
	 * The contructor of the redirect filter
	 * @param line the parameters of where to redirect
	 * @throws Exception throws an exception when there is error with the parameters, or when
	 * the specified path is not found (required for the case where we give a path of a directory outside
	 * of the current directory) 
	 */
	public RedirectFilter(String line) throws Exception {
		super();
		String[] param = line.split(">");
		if(param.length > 1) {
			if(param[1].trim().equals("")) {
				System.out.printf(Message.REQUIRES_PARAMETER.toString(), line.trim());
				throw new Exception();
			}
			try {
				fw = new FileWriter(new File(ConcurrentREPL.currentWorkingDirectory + Filter.FILE_SEPARATOR + param[1].trim()));
			} catch (IOException e) {
				System.out.printf(Message.FILE_NOT_FOUND.toString(), line);	//shouldn't really happen but just in case
				throw new Exception();
			}
		} else {
			System.out.printf(Message.REQUIRES_INPUT.toString(), line);
			throw new Exception();
		}
	}
	
	public void process() {
		while(!isDone()) {
			try {
				processLine(input.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * processes one line from the input and writes it to the output file
	 * @param line the line as got from the input queue
	 * @return not used, always returns null
	 */
	public String processLine(String line) {
		if(line.equals(KillString)) {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			IAmDead = true;
			return KillString;
		}
		try {
			fw.append(line + "\n");
			/*if(isDone()) {
				
			}*/
		} catch (IOException e) {
			System.out.printf(Message.FILE_NOT_FOUND.toString(), line);
		}
		return null;
	}
}
