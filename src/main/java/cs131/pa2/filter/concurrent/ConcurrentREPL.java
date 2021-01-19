package cs131.pa2.filter.concurrent;

import java.awt.List;
import java.util.ArrayList;
import java.util.Scanner;

import cs131.pa2.filter.Message;

/**
 * The main implementation of the REPL loop (read-eval-print loop). It reads
 * commands from the user, parses them, executes them and displays the result.
 * 
 * @author cs131a
 *
 */
public class ConcurrentREPL {
	/**
	 * the path of the current working directory
	 */
	static String currentWorkingDirectory;

	/**
	 * The main method that will execute the REPL loop
	 * 
	 * @param args not used
	 */

	public static void main(String[] args) {
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		ArrayList<Thread> commandThreads = new ArrayList<Thread>();
		int CommandCounter = 0;
		ArrayList<String> commandStrings = new ArrayList<String>();
		// keep track of background commands you've run and the order they're in
		// (ArrayList)
		// global counter for how many commands you've run so far
		// keep track of the command strings themselves
		// associate the command strings with the order they've been run and the threads
		// that that command corresponds with (at least the last one)
		while (true) {
			// obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if (command.equals("repl_jobs")) {
				for (int i = 0; i < commandStrings.size() && commandStrings.get(i) != null; i++) {
					if (commandThreads.get(i) != null) {
						if (!commandThreads.get(i).isAlive()) {
							continue;
						}
					}
					System.out.println(commandStrings.get(i));
				}
			}

			else if (command.startsWith("kill")) {
				if (command.equals("kill")) {
					System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));
					continue;
				}
				int commandNum = 0;
				try {
					commandNum = Integer.parseInt(command.substring(5));
					if (commandThreads.get(commandNum) != null) {
						if (!commandThreads.get(commandNum).isAlive())
							commandThreads.get(commandNum).interrupt();

					}
				} catch (NumberFormatException e) {
					System.out.print(Message.INVALID_PARAMETER.with_parameter(command));
				}
			}

			else if (command.equals("exit")) {
				break;
			} else if (!command.trim().equals("")) {
				// building the filters list from the command
				ConcurrentFilter filterlist;
				if(command.contains("&")) {
				 filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command.substring(0,command.indexOf("&")));
				}
				else {
				 filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				}
				CommandCounter++;
				// keeping track of the Strings
				if (command.contains("&")) {
					commandStrings.add("\t" + CommandCounter + ". " + command);			
				}
				Thread t = null;
				Thread lastThread = null;
				while (filterlist != null) {
					t = new Thread(filterlist);
					t.start();
					filterlist = (ConcurrentFilter) filterlist.getNext();
//					if (filterlist == null) {
//					}
				}
				lastThread = t;
				commandThreads.add(t);
				// System.out.println(lastThread.getName());
				if (lastThread != null && !(command.contains("&"))) {
					try {
						lastThread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
