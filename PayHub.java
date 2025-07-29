import java.io.*;
import java.util.*;

public class PayHub {
    private static final String USER_FILE = "data/users.txt";
    private static final String TRANSACTION_FILE = "data/transactions.txt";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
            System.out.println("================================================");
            System.out.println("             || WELCOME TO PAYHUB ||            ");
            System.out.println("================================================");
        while (true) {
            System.out.println("------------------------------------------------");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("------------------------------------------------");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
    
            if (choice == 1) {
                registerUser(scanner);
                pause(scanner);
            } else if (choice == 2) {
                loginUser(scanner);
                pause(scanner);
            } else if (choice == 3) {
                System.out.println("------------------------------------------------");
                System.out.println("|      Thank you for using PayHub,Goodbye!     |");
                System.out.println("------------------------------------------------");
                break;
            } else {
                System.out.println("Invalid option! Please Try Again");
                pause(scanner);
            }
        }
    
        scanner.close();
    }
    
    private static void registerUser(Scanner scanner) {
        System.out.println("------------------ Registration ----------------");
        System.out.println("------------------------------------------------");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        if (isUserExists(username)) {
            System.out.println("Username already taken. Try another!");
            return;
        }

        if (!contact.matches("\\d{10}")) {
            System.out.println("Invalid contact number! Must be 10 digits.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("Invalid email format!");
            return;
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(USER_FILE, true)))) {
            out.println(username + "," + password + "," + contact + "," + email + ",1000");
            System.out.println("User registered successfully!");
            System.out.println("");
            pause(scanner);
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    private static boolean isUserExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    private static void loginUser(Scanner scanner) {
        System.out.println("-------------------- Login ---------------------");
        System.out.println("------------------------------------------------");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("Admin login successful.");
            System.out.println("");
            adminMenu(scanner);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    System.out.println("Login successful.");
                    System.out.println("");
                    userMenu(scanner, username);
                    return;
                }
            }
            System.out.println("Invalid username or password!");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void userMenu(Scanner scanner, String username) {
        while (true) {
            System.out.println("------------------------------------------------");
            System.out.println("------------------ User Menu -------------------");
            System.out.println("1. Check Balance");
            System.out.println("2. Transfer Money");
            System.out.println("3. Transaction History");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    double balance = getBalance(username);
                    if (balance != -1) {
                        System.out.println("Current Balance: Rs." + balance);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 2:
                    transferMoney(scanner, username);
                    break;
                case 3:
                    showTransactionHistory(username);
                    break;
                case 4:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static double getBalance(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return Double.parseDouble(parts[4]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return -1;
    }

    private static void transferMoney(Scanner scanner, String sender) {
        System.out.println("------------------------------------------------");
        System.out.println("--------------- Transfer Money -----------------");

        System.out.print("Enter recipient username: ");
        String recipient = scanner.nextLine();

        if (!isUserExists(recipient)) {
            System.out.println("Recipient does not exist.");
            return;
        }

        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }


        System.out.print("Enter password to confirm: ");
        String password = scanner.nextLine();

        if (!verifyPassword(sender, password)) {
            System.out.println("Incorrect password. Transaction denied.");
            return;
        }

        List<String> users = new ArrayList<>();
        boolean success = false;

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(sender)) {
                    double balance = Double.parseDouble(parts[4]);
                    if (balance >= amount) {
                        parts[4] = String.valueOf(balance - amount);
                        success = true;
                    } else {
                        System.out.println("Insufficient balance.");
                        return;
                    }
                } else if (parts[0].equals(recipient)) {
                    double balance = Double.parseDouble(parts[4]);
                    parts[4] = String.valueOf(balance + amount);
                }
                users.add(String.join(",", parts));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        if (success) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
                for (String user : users) {
                    bw.write(user + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTION_FILE, true))) {
                bw.write(sender + " sent Rs." + amount + " to " + recipient + "\n");
            } catch (IOException e) {
                System.out.println("Error writing transaction: " + e.getMessage());
            }

            System.out.println("Transaction successful.");
            System.out.println("");
            pause(scanner);
        }
    }

    private static boolean verifyPassword(String username, String inputPassword) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(inputPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    private static void showTransactionHistory(String username) {
        boolean found = false;
        System.out.println("------------------------------------------------");
        System.out.println("-------------- Transaction History -------------");
        System.out.println("Transaction History for " + username + ":");
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Exact matching logic:
                if (line.startsWith(username + " sent Rs.") || line.endsWith("to " + username)) {
                    System.out.println(line);
                    found = true;
                    System.out.println("");
                }
            }
            if (!found) {
                System.out.println("No transactions found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction file: " + e.getMessage());
        }
    }
    
    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("------------------------------------------------");
            System.out.println("------------------ Admin Panel -----------------");
            System.out.println("1. View All Users");
            System.out.println("2. View All Transactions");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllUsers();
                    pause(scanner);
                    break;
                case 2:
                    viewAllTransactions();
                    pause(scanner);
                    break;
                case 3:
                    System.out.println("Exiting admin panel.");
                    return;
                
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewAllUsers() {
        System.out.println("------------------------------------------------");
        System.out.println("--------------- Registered Users ---------------");
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    System.out.println("Username: " + parts[0] + " | Contact: " + parts[2] + " | Email: " + parts[3] + " | Balance: Rs." + parts[4]);
                    System.out.println("");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    private static void viewAllTransactions() {
        System.out.println("------------------------------------------------");
        System.out.println("--------------- All Transactions ---------------");
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                System.out.println("");
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
    }
    
    private static void pause(Scanner scanner) {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    
}
