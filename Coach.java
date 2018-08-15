package com.multiconsumer;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFolder;
/*
 * Top Level try to surround exception with try catches instead of throwing them
 */

public class Coach{
	private int numPlayers;
	private BlockingQueue<BoxFolder> toProcess;
	private BlockingQueue<Player> players;
	private BoxFolder root;
	private BoxDeveloperEditionAPIConnection client;
	
	public Coach(BoxFolder start, BoxDeveloperEditionAPIConnection user) throws InterruptedException {
		this.numPlayers = 100;
		this.root = start;
		this.client = user;
		this.toProcess = new LinkedBlockingQueue<BoxFolder>(10000000);
		toProcess.add(root);
		this.players = new LinkedBlockingQueue<Player>(this.numPlayers);
		recruitPlayers(this.numPlayers);
	}
	public Coach(BoxFolder start, BoxDeveloperEditionAPIConnection user, int n) throws InterruptedException {
		this.numPlayers = n;
		this.root = start;
		this.client = user;
		this.toProcess = new LinkedBlockingQueue<BoxFolder>(10000000);
		toProcess.add(root);
		this.players = new LinkedBlockingQueue<Player>(this.numPlayers+1);
		recruitPlayers(this.numPlayers);
	}
	
	public void putPlayerOnBench(Player bob) throws InterruptedException {
		this.players.put(bob);
	}
	
	public void addFoldersToBeProcessed(Collection<BoxFolder> stuff) {
		toProcess.addAll(stuff);
	}
	
	public BlockingQueue<BoxFolder> getFoldersToBeProcessed() {
		return toProcess;
	}
	
	public void recruitPlayers(int total) throws InterruptedException {
		for(int i = 0; i < total; i++) {
			Player averageJoe = new Player(this,client);
			this.players.put(averageJoe);
			//System.out.println(i);
		}
	}
	
	public void traverse() throws InterruptedException {
		//System.out.println("yo");
		//System.out.println(toProcess.size() + " " + players.size());
		//System.out.println(numPlayers);
		ExecutorService executor = Executors.newFixedThreadPool(1000);
		while(true) {
			if(toProcess.size() != 0 && players.size() != 0) {
				//System.out.println("helloooooo");
				Player readyFreddie = players.poll();
				readyFreddie.setTask(toProcess.poll());
				//System.out.println("hello "+ toProcess.size() + " " + players.size());
				executor.execute(readyFreddie);
				//int a = 1;if (a ==1)return;
				Thread.currentThread().sleep(10);
			}
			if(toProcess.size() == 0 && players.size() == numPlayers) { //get there eventually
				System.out.println("done");
				executor.shutdown();
				return;
			}
			//System.out.println("hello "+ toProcess.size() + " " + players.size());
		}
		//System.out.println("what " +toProcess.size() + " " + players.size());
	}
}
