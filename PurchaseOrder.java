package processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PurchaseOrder implements Runnable {
	
	private Orders allOrders;
	private DecimalFormat decimal = new DecimalFormat();
	private TreeMap<String, Integer> orders;
	private Object key;
	private String file;
	private	String priceFile;
	protected int clientId;
	
	public PurchaseOrder(Object k, Orders modifyingOrders, String fileName, String fileForPrice) {
		
		allOrders = modifyingOrders;
		file = fileName;
		priceFile = fileForPrice;
		key = k;
		orders = new TreeMap<String, Integer>();
		getOrderQuantities(fileName);
		getClientId(fileName);
		
	}
	
	public void getClientId(String fileName) {
		
		File file = new File(fileName);
		
		if(!(file.exists())) return;
		
		Scanner input = null;
		
		try {
			
			input = new Scanner(file);
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		
		}
		
		input.next();
		
		clientId = Integer.parseInt(input.next());
		
	}
	
	public void fillOrder() {

		TreeMap<String, Integer> thisMap = getOrderQuantities(file);
		
		for(String str : thisMap.keySet()) thisMap.put(str, thisMap.get(str) / 2);
			
		for(String i : thisMap.keySet()) {
			
			ArrayList<String> info = new ArrayList<String>();
			
			info.add(getCostOfItem(i));
			info.add(((Integer) (thisMap.get(i))).toString());
			double cost = Double.parseDouble(getCostOfItem(i));
			int quantity = thisMap.get(i);
			String nextAddition = NumberFormat.getCurrencyInstance().format(cost * quantity);
			info.add(nextAddition);
			
			synchronized(key) {
				
				if(allOrders.getOrders().get(clientId) == null) {
					
					allOrders.getOrders().put(clientId, new TreeMap<String, ArrayList<String>>());
					
				}
				
				allOrders.getOrders().get(clientId).put(i, info);
				
			}
			
		}
		// ArrayList has info for each item in this order: cost, quantity, and total cost.
	}
	
	public String getCostOfItem(String item) {
		
		File file = new File(priceFile);
		Scanner input = null;
		
		try {
			
			input = new Scanner(file);
			
		} catch (FileNotFoundException e) {
			
			System.out.println("Not possible");
		
		}

		String next = "";

		while(input.hasNext() && !(next.equals(item))) {
			
			next = input.next();
			
		}
		
		if(input.hasNext()) {

			next = input.next();
			return "" + decimal.format(Double.parseDouble(next));
			
		}
		
		return null;
		
	}
	
	public TreeMap<String, Integer> getOrderQuantities(String fileName) {
		
		File file = new File(fileName);
		
		if(!(file.exists())) return null;
		
		Scanner input = null;
		
		try {
			
			input = new Scanner(file);
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		
		}
		
		input.next();
		
		while(input.hasNext()) {
			
			String nextString = "";
			
			if(input.hasNext()) {
				
				input.next();
			
			}
			
			if(input.hasNext()) {
			
				nextString = input.next();
				
			}
			
			if(orders.get(nextString) == null && !nextString.equals("")) {
				
				orders.put(nextString, 1);
				
			} else if(!nextString.equals("")){
				
				orders.put(nextString, orders.get(nextString) + 1);
				
			}
			
		}

		return orders;

	}

	public void run() {
		
		System.out.println("Reading order for client with id: " + clientId);
		fillOrder();
		
	}

}