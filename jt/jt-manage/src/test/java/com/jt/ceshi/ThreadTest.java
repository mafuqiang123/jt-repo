package com.jt.ceshi;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;




public class ThreadTest {

	public static void main(String[] args) {

//		ExecutorService threadPool=
//	new ThreadPoolExecutor(3, 5, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(3), Executors.defaultThreadFactory(), 
//			new AbortPolicy());
		
		
	ExecutorService threadpool=new ThreadPoolExecutor(3, 5, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3)
			,Executors.defaultThreadFactory(),new AbortPolicy());
		
		
		
		
		
		for (int i = 0; i <9; i++) {
			threadpool.execute(new Runnable() {
			
			@Override
			public void run() {
		System.out.println("我爱你"+Thread.currentThread().getName());
			}
		});
		}
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
