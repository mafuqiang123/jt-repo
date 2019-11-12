package com.jt.ceshi;

class HoldLockThread implements Runnable{

	private String lockA;
	private String lockB;
	
	
	public HoldLockThread(String lockA, String lockB) {
		this.lockA = lockA;
		this.lockB = lockB;
	}


	@Override
	public void run() {

		synchronized (lockA) {
			System.out.println(Thread.currentThread().getName()+"\t自己持有:"+lockA+",但是却在抢"+lockB);
			
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (lockB) {
			System.out.println(Thread.currentThread().getName()+"\t自己持有:"+lockB+"但是却在枪"+lockA);	
			}
		}
	}
}

public class Sisuo {
	
	public static void main(String[] args) {
		
		String lockA="lockA";
		String lockB="lockB";
		
		new Thread(new HoldLockThread(lockA,lockB)).start();
		new Thread(new HoldLockThread(lockB,lockA)).start();
		
	}
	
}
