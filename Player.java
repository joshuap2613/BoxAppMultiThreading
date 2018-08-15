package com.multiconsumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem.Info;

public class Player implements Runnable {

	private BoxDeveloperEditionAPIConnection client;
	private Coach boss;
	private BoxFolder task;
	
	public Player(Coach Boss, BoxDeveloperEditionAPIConnection user) {
		this.client = user;
		this.boss = Boss;
		this.task = null;
	}
	
	public void setTask(BoxFolder Task) {
		task = Task;
	}
	
	
	public Player(BoxFolder Task,Coach Boss, 
			BoxDeveloperEditionAPIConnection user) {
		this.client = user;
		this.boss = Boss;
		this.task = Task;
	}

	public void run() {
		/**
		 * Serving token one by one in a infinite loop.
		 * The Loop will break while there are no more
		 * token to serve
		 */
		Collection<BoxFolder> yo = new ArrayList<BoxFolder>();
		try { 
			System.out.println("Name: " + task.getInfo().getName()+" ID: "+task.getID());
			//System.out.println("Children");
			for(Info info: task.getChildren()) {
				BoxFolder.Info subfolder = (BoxFolder.Info) info;
				yo.add(new BoxFolder(client,subfolder.getID()));
				//System.out.println("Name: " + subfolder.getName()+" ID: "+subfolder.getID());
			}
		} catch (ClassCastException f) {
			//doNothing
		}
		boss.addFoldersToBeProcessed(yo);
		//System.out.println(boss.getFoldersToBeProcessed().size());
		try {
			putMeBackInCoach();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void putMeBackInCoach() throws InterruptedException {
		boss.putPlayerOnBench(this);
	}
}