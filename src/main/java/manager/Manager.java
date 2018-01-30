package manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import memory.Frame;
import memory.Memory;
import memory.PageTable;
import thread.ThreadMem;

public class Manager {
	protected Memory memory;
	protected Map<ThreadMem, PageTable> pageTables;
	protected Map<ThreadMem, Queue<Integer>> requestQueues;
	protected int maxPageCount;

	public Manager(Memory memory, int maxPageCount) {
		this.memory = memory;
		this.maxPageCount = maxPageCount;

		pageTables = new HashMap<ThreadMem, PageTable>();
		requestQueues = new HashMap<ThreadMem, Queue<Integer>>();
	}

	public void alloc(ThreadMem thread) {
		pageTables.put(thread, new PageTable(maxPageCount));
		requestQueues.put(thread, new LinkedList<Integer>());
	}

	public void request(ThreadMem thread, int pageNum) {
		System.out.println(thread + " requested page " + pageNum);

		PageTable pageTable = pageTables.get(thread);
		Queue<Integer> queue = requestQueues.get(thread);

		Frame pageFrame = pageTable.getPageFrame(pageNum);

		if (pageFrame == null) {
			if (pageTable.getTotalPagesInMemory() < 300) {
				fetchPage(thread, pageNum, pageTable, queue);
			} else {
				fetchPageLRU(thread, pageNum, pageTable, queue);
			}
		} else {
			updateUsage(thread, pageNum, queue);
		}
		System.out.println(thread + " Page Table:");
		System.out.println(pageTable);
	}

	private void updateUsage(ThreadMem thread, int pageNum, Queue<Integer> queue) {
		queue.remove(pageNum);
		queue.add(pageNum);
		System.out.println("Page " + pageNum + " for " + thread
				+ " already in memory");
	}

	private void fetchPageLRU(ThreadMem thread, int pageNum, PageTable pageTable,
			Queue<Integer> queue) {
		Integer lruPageNumber = queue.remove();

		Frame usedFrame = pageTable.getPageFrame(lruPageNumber);
		pageTable.removePage(lruPageNumber);
		pageTable.setPageFrame(usedFrame, pageNum);

		queue.add(pageNum);

		System.out.println("Page " + pageNum + " for " + thread
				+ " brought to memory in " + usedFrame + ", replacing Page "
				+ lruPageNumber);
	}

	private void fetchPage(ThreadMem thread, int pageNum, PageTable pageTable,
			Queue<Integer> queue) {
		try {
			Frame nextFreeFrame = getNextFreeFrame();
			nextFreeFrame.setFree(false);
			pageTable.setPageFrame(nextFreeFrame, pageNum);

			queue.add(pageNum);

			System.out.println("Page " + pageNum + " for " + thread
					+ " brought to memory in " + nextFreeFrame);
		} catch (OutOfMemoryException e) {
			System.out.println("Out of Memory!");
		}
	}

	private Frame getNextFreeFrame() throws OutOfMemoryException {
		for (Frame frame : memory.getFrames()) {
			if (frame.isFree()) {
				return frame;
			}
		}

		throw new OutOfMemoryException();
	}
}
