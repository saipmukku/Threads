package processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class OrdersProcessor {

	public static void main(String args[]) {
		
		Object key = new Object();
		Orders orders = new Orders();
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter item's data file name: ");
		String systemFileName = input.next();
		System.out.println("Enter 'y' for multiple threads, any other character otherwise: ");
		String multipleThreads = input.next();
		System.out.println("Enter number of orders to process: ");
		int ordersToProcess = input.nextInt();
		System.out.println("Enter order's base filename: ");
		String baseName = input.next();
		System.out.println("Enter result's filename: ");
		String resultsName = input.next();
		
		if(multipleThreads.equals("y")) {
			
			ArrayList<Thread> threads = new ArrayList<Thread>();
			System.out.println("Creating threads: ");

			for(int j = 0; j < ordersToProcess; j++) {
				
				threads.add(new Thread(new PurchaseOrder(key, orders, baseName + (j + 1) + ".txt", systemFileName)));
				
			}
			
			long startTime = System.currentTimeMillis();
			
			for(Thread thread : threads) {
				
				thread.start();
				
			}
			
			for(Thread thr : threads) {
				
				try {
					
					thr.join();
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
			long endTime = System.currentTimeMillis();
			
			System.out.println("Processing time (msec): " + (endTime - startTime));
			
			File file = new File(resultsName);
			
			BufferedWriter writer;
			
			try {
				
				writer = new BufferedWriter(new FileWriter(resultsName));
				writer.write(orders.finalSummary());
				writer.close();
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
				
			}
			
		} else {
			
			PurchaseOrder purchase;
			long startTime = System.currentTimeMillis();
			
			for(int i = 0; i < ordersToProcess; i++) {
				
				purchase = new PurchaseOrder(key, orders, baseName + (i + 1) + ".txt", systemFileName);
				System.out.println("Reading order for client with id: " + purchase.clientId);
				purchase.fillOrder();
				
			}
			
			long endTime = System.currentTimeMillis();
			System.out.println("Processing time (msec): " + (endTime - startTime));
			
			File file = new File(resultsName);
			
			BufferedWriter writer;
			
			try {
				
				writer = new BufferedWriter(new FileWriter(resultsName));
				writer.write(orders.finalSummary());
				writer.close();
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
				
			}
			
		}
		
	}
	
}