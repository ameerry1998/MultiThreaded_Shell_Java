package cs131.pa2.filter.concurrent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa2.filter.Filter;

/**
 * An abstract class that extends the Filter and implements the basic functionality of all filters. Each filter should
 * extend this class and implement functionality that is specific for that filter.
 * @author cs131a
 *
 */
public abstract class ConcurrentFilter extends Filter implements Runnable {
	/**
	 * The input queue for this filter
	 */
	protected LinkedBlockingQueue<String> input;
	/**
	 * The output queue for this filter
	 */
	protected LinkedBlockingQueue<String> output;
	
	/**
	 * a string to notify a filter it's at the end of the input
	 */
	String KillString = "Michael Jordan's mom was a super pumpkin#23,45!";
	/**
	 * a boolean that keeps track of whether a thread is alive
	 */
	boolean IAmDead = false;
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter concurrentNext = (ConcurrentFilter) nextFilter;
			this.next = concurrentNext;
			concurrentNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			concurrentNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	/**
	 * Gets the next filter
	 * @return the next filter
	 */
	public Filter getNext() {
		return next;
	}
	
	/**
	 * it runs the filter
	 * 
	 * */
	@Override
	public void run() {
		//System.out.println(Thread.currentThread()+" has started");
		while(!isDone()) {
			if(Thread.interrupted()) {
				IAmDead = true;
				break;
			}
			//System.out.println(Thread.currentThread()+" is processing");
			process();	
			if(Thread.interrupted()) {
				IAmDead = true;
				break;
			}
		}	
		IAmDead = true;
		//System.out.println(Thread.currentThread()+" has finished");
	}
	
	/**
	 * processes the input queue and writes the result to the output queue
	 */
	public void process(){
		while (!input.isEmpty()){
			String line = "";
			try {
				line = input.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String processedLine = processLine(line);
			if (processedLine != null){
				try {
					output.put(processedLine);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	
	@Override
	public boolean isDone() {
		//return input.size() == 0;
		//System.out.println(Thread.currentThread() + "IAmDead :" + IAmDead);
		if(IAmDead) return true;	
		else return false;
	}
	
	/*
	 * filter specific method 
	 * */
	protected abstract String processLine(String line);
	
}
