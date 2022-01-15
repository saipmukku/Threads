package processor;

import java.text.NumberFormat;
import java.util.*;

// FIX THE QUANTITY COUNTER

public class Orders {
	// Keys are integers representing client IDs, values are in a tree
	// map, and are sorted by name, and map to ArrayLists that have the
	// information for all of the orders.
	private TreeMap<Integer, TreeMap<String, ArrayList<String>>> orders;
	
	public Orders() {
		
		orders = new TreeMap<Integer, TreeMap<String, ArrayList<String>>>();
		
		// This map contains all the orders for all of the clients. The values
		// are also a map, but the key are the names of the items, and the
		// information associated with that item.
		
	}
	
	public TreeMap<Integer, TreeMap<String, ArrayList<String>>> getOrders(){
		
		return orders;
		
	}
	
	// Do not call until the threaded calls are complete.
	public String finalSummary() {
		
		TreeMap<String, ArrayList<String>> summary = new TreeMap<String, ArrayList<String>>();
		String result = "";
		double grandTotal = 0;
		
		for(Integer i : orders.keySet()) {
			
			double orderTotal = 0;
			result += "----- Order details for client with Id: " + i + " -----\n";
			
			for(String j : orders.get(i).keySet()) {
				
				result += "Item's name: " + j + ", Cost per item: ";
				double cost = Double.parseDouble(orders.get(i).get(j).get(0));
				result += NumberFormat.getCurrencyInstance().format(cost);
				int quantity = Integer.parseInt(orders.get(i).get(j).get(1));
				result += ", Quantity: " + quantity;
				result += ", Cost: " + NumberFormat.getCurrencyInstance().format(cost * quantity) + "\n";
				
				if(summary.get(j) == null) {
					
					summary.put(j, new ArrayList<String>());
					summary.get(j).add(orders.get(i).get(j).get(0));
					summary.get(j).add(orders.get(i).get(j).get(1));
					
				} else {
					
					int initialQuantity = Integer.parseInt(summary.get(j).remove(1));
					int addedQuantity = Integer.parseInt(orders.get(i).get(j).get(1));
					String turnIntoString = "" + (initialQuantity + addedQuantity);
					summary.get(j).add(turnIntoString);
					
				}
				
				orderTotal += cost * quantity;
				
			}
			
			result += "Order Total: " + NumberFormat.getCurrencyInstance().format(orderTotal) + "\n";
			grandTotal += orderTotal;
			
		}
		
		result += "***** Summary of all orders *****";
		
		for(String g : summary.keySet()) {
			
			result += "\nSummary - Item's name: " + g;
			result += ", Cost per item: " + NumberFormat.getCurrencyInstance().format(Double.parseDouble(summary.get(g).get(0)));
			result += ", Number sold: " + ((int) Double.parseDouble(summary.get(g).get(1))) + ", Item's Total: ";
			double firstPrice = Double.parseDouble(summary.get(g).get(0));
			double secondPrice = Double.parseDouble(summary.get(g).get(1));
			result += NumberFormat.getCurrencyInstance().format(firstPrice * secondPrice);
			
		}
		
		return result + "\nSummary Grand Total: " + NumberFormat.getCurrencyInstance().format(grandTotal) + "\n";
		
	}

}