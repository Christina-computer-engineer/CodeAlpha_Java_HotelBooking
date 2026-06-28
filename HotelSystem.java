import java.io.*;
import java.util.*;

class Room {
    int id;
    String type;
    boolean booked;

    Room(int id, String type) {
        this.id = id;
        this.type = type;
        this.booked = false;
    }
}

class Booking {
    String name;
    int roomId;
    String type;
    double amount;
    String paymentMethod;
    String txnId;

    Booking(String name, int roomId, String type,
            double amount, String paymentMethod, String txnId) {
        this.name = name;
        this.roomId = roomId;
        this.type = type;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.txnId = txnId;
    }
}

class HotelManager {

    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Booking> bookings = new ArrayList<>();

    final String FILE = "bookings.txt";

    HotelManager() {
        for (int i = 1; i <= 3; i++) rooms.add(new Room(i, "Standard"));
        for (int i = 4; i <= 6; i++) rooms.add(new Room(i, "Deluxe"));
        for (int i = 7; i <= 9; i++) rooms.add(new Room(i, "Suite"));
    }

    // ROOM PRICE
    double getPrice(String type) {
        if (type.equals("Standard")) return 1000;
        if (type.equals("Deluxe")) return 2000;
        return 3000;
    }

    // VIEW ROOMS
    void showRooms() {
        System.out.println("\n--- ROOMS ---");
        for (Room r : rooms) {
            System.out.println("Room " + r.id +
                    " | " + r.type +
                    " | " + (r.booked ? "Booked" : "Available"));
        }
    }

    // PAYMENT SIMULATION
    String pay(double amount, String method) {

        System.out.println("\nProcessing payment...");
        System.out.println("Method: " + method);
        System.out.println("Amount: ₹" + amount);

        String txnId = "TXN" + (int)(Math.random() * 100000);

        System.out.println("Payment Successful!");
        System.out.println("Transaction ID: " + txnId);

        return txnId;
    }

    // BOOK ROOM
    void bookRoom(String name, String type, String method) {

        for (Room r : rooms) {
            if (!r.booked && r.type.equalsIgnoreCase(type)) {

                double amount = getPrice(type);
                String txnId = pay(amount, method);

                r.booked = true;

                bookings.add(new Booking(name, r.id, type, amount, method, txnId));

                System.out.println("\nRoom Booked Successfully!");
                System.out.println("Room No: " + r.id);
                return;
            }
        }

        System.out.println("No rooms available!");
    }

    // CANCEL
    void cancel(int id) {

        for (Iterator<Booking> it = bookings.iterator(); it.hasNext();) {

            Booking b = it.next();

            if (b.roomId == id) {

                it.remove();

                for (Room r : rooms) {
                    if (r.id == id) r.booked = false;
                }

                System.out.println("Booking cancelled!");
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    // VIEW BOOKINGS (RECEIPT STYLE)
    void showBookings() {

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n===== BOOKING DETAILS =====");

        for (Booking b : bookings) {
            System.out.println("\n--------------------");
            System.out.println("Name: " + b.name);
            System.out.println("Room ID: " + b.roomId);
            System.out.println("Room Type: " + b.type);
            System.out.println("Amount Paid: ₹" + b.amount);
            System.out.println("Payment Method: " + b.paymentMethod);
            System.out.println("Transaction ID: " + b.txnId);
        }
    }

    // SAVE FILE
    void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {

            for (Booking b : bookings) {
                pw.println(b.name + "," + b.roomId + "," + b.type + ","
                        + b.amount + "," + b.paymentMethod + "," + b.txnId);
            }

            System.out.println("Data saved!");

        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    // LOAD FILE
    void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");

                Booking b = new Booking(
                        d[0],
                        Integer.parseInt(d[1]),
                        d[2],
                        Double.parseDouble(d[3]),
                        d[4],
                        d[5]
                );

                bookings.add(b);

                for (Room r : rooms) {
                    if (r.id == b.roomId) r.booked = true;
                }
            }

            System.out.println("Data loaded!");

        } catch (Exception e) {
            System.out.println("No previous data.");
        }
    }
}

public class HotelSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HotelManager hm = new HotelManager();

        hm.load();

        while (true) {

            System.out.println("\n===== HOTEL SYSTEM =====");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Save & Exit");
            System.out.print("Enter choice: ");

            int ch;

            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (ch) {

                case 1:
                    hm.showRooms();
                    break;

                case 2:

                    System.out.print("Enter name: ");
                    String name = sc.nextLine();

                    System.out.println("Room Type:");
                    System.out.println("1. Standard");
                    System.out.println("2. Deluxe");
                    System.out.println("3. Suite");

                    int t = Integer.parseInt(sc.nextLine());

                    String type =
                            (t == 1) ? "Standard" :
                            (t == 2) ? "Deluxe" : "Suite";

                    System.out.print("Payment Method (UPI/Card/Cash): ");
                    String method = sc.nextLine();

                    hm.bookRoom(name, type, method);
                    break;

                case 3:

                    System.out.print("Enter Room ID: ");
                    int id = Integer.parseInt(sc.nextLine());

                    hm.cancel(id);
                    break;

                case 4:
                    hm.showBookings();
                    break;

                case 5:
                    hm.save();
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
