package FedExTracker;

import java.util.Scanner;

public class FedEx {

	public static void main(String[] args) throws Exception {
		FedExDatabase database = new FedExDatabase();
		int menuChoice = 0;
		Scanner scan = new Scanner(System.in);
		
		displayMenu();
		menuChoice = scan.nextInt();
		
		while(menuChoice != 4) {
			switch(menuChoice) {
				case 1:
					try {
						database.addOrder();
					}
					catch(Exception e) {
						throw e;
					}
					break;
				case 2:
					try {
						System.out.println("Enter Tracking Number: ");
						database.displayOrder(scan.nextInt());
					}
					catch(Exception e) {
						throw e;
					}
					break;
				case 3:
					try {
						database.loadTrackNumbers();
					}
					catch(Exception e) {
						throw e;
					}
					break;
			}
			displayMenu();
			menuChoice = scan.nextInt();
		}
		scan.close();
	}

	
	public static void displayMenu() {
		// Create menu for user
		System.out.println("\tMenu\n");
		System.out.println("1. Add Order");
		System.out.println("2. Look Up Order Details");
		System.out.println("3. Look Up List of Track Numbers");
		System.out.println("4. Exit");
		System.out.println("\nInput your choice: ");
	}
}
