//package br.ufrj.dcc.so;
//package mainP;
import manager.*;
import memory.*;
import thread.*;

public class Main {
	private static final int VERY_LONG_TIME = 100;
	private static final int MAX_THREAD_COUNT = 10;
	private static final int MAX_PAGE_COUNT = 50;
	private static final int FRAME_COUNT = 64;
	private static final int THREAD_CREATION_INTERVAL = 500;

	public static void main(String[] args) throws InterruptedException {
		Memory memory = new Memory(FRAME_COUNT);
		Manager manager = new Manager(memory, MAX_PAGE_COUNT);
		
		for (int i = 0; i < MAX_THREAD_COUNT; i++) {
			ThreadMem thread = new ThreadMem(manager, MAX_PAGE_COUNT);
			thread.start();
			
			java.lang.Thread.sleep(THREAD_CREATION_INTERVAL);
		}
		
	/*	while (true) {
			java.lang.Thread.sleep(VERY_LONG_TIME);
		} */
	}
}
