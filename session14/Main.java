package session14;
import session14.dao.OrderDAO;
import session14.dao.ReportDAO;

import java.util.Scanner;

public class Main {

	private static OrderDAO orderDAO = new OrderDAO();
	private static ReportDAO reportDAO = new ReportDAO();
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		while (true) {
			System.out.println("\n===== FLASH SALE SYSTEM =====");
			System.out.println("1. Place Order");
			System.out.println("2. View Top Buyers");
			System.out.println("3. View Revenue");
			System.out.println("4. Stress Test (50 Threads)");
			System.out.println("5. Exit");

			System.out.print("Choose: ");
			String choice = sc.nextLine();

			switch (choice) {
				case "1":
					placeOrderMenu();
					break;
				case "2":
					viewTopBuyers();
					break;
				case "3":
					viewRevenue();
					break;
				case "4":
					stressTest();
					break;
				case "5":
					System.out.println("Bye!");
					return;
				default:
					System.out.println("Invalid choice!");
			}
		}
	}

	// ================== 1. PLACE ORDER ==================
	private static void placeOrderMenu() {
		try {
			System.out.print("User ID: ");
			int userId = Integer.parseInt(sc.nextLine());

			System.out.print("Product ID: ");
			int productId = Integer.parseInt(sc.nextLine());

			System.out.print("Quantity: ");
			int quantity = Integer.parseInt(sc.nextLine());

			orderDAO.placeOrder(userId, productId, quantity);

		} catch (Exception e) {
			System.out.println("Invalid input!");
		}
	}

	// ================== 2. TOP BUYERS ==================
	private static void viewTopBuyers() {
		try {
			reportDAO.getTopBuyers();
		} catch (Exception e) {
			System.out.println("Error loading report!");
		}
	}

	// ================== 3. REVENUE ==================
	private static void viewRevenue() {
		try {
			reportDAO.getRevenue(); // bạn cần thêm hàm này
		} catch (Exception e) {
			System.out.println("Error loading revenue!");
		}
	}

	// ================== 4. STRESS TEST ==================
	private static void stressTest() {
		System.out.println("Running 50 threads...");

		for (int i = 1; i <= 50; i++) {
			int userId = i;

			new Thread(() -> {
				orderDAO.placeOrder(userId, 1, 1);
			}).start();
		}
	}
}