package thread;

import java.util.Random;

import manager.Manager;

public class ThreadMem extends java.lang.Thread {
	private static final int PAGE_ALLOCATION_INTERVAL = 2000;
	private Manager manager;
	
	private int maxPages;
	
	public ThreadMem(Manager manager, int maxPages) {
		this.manager = manager;
		this.maxPages = maxPages;
		
		manager.alloc(this);
		System.out.println("Created " + this);
	}
	
	@Override
	public void run() {
		System.out.println("Running " + this);
	//	while(true) {
			try {
				int pageNum = generateRandomPageNumber();
				manager.request(this, pageNum);
				sleep(PAGE_ALLOCATION_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	//	}
	}
	
	private int generateRandomPageNumber() {
		Random random = new Random();
		
		return random.nextInt(maxPages);
	}
	
	@Override
	public String toString() {
		return "Thread[" + getId() + "]";
	}
}
