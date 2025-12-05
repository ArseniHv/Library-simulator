import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Library lib = new Library();

        for (int i = 1; i <= 67; i++) {
            lib.addUser(new User(i, User.UserType.STUDENT, true));
        }
        for (int i = 68; i <= 100; i++) {
            lib.addUser(new User(i, User.UserType.STUDENT, false));
        }

        lib.loadBooksFromResource("/books.csv");
        lib.loadJournalsFromResource("/jlist.csv");
        lib.loadMoviesFromResource("/movies.csv");

        lib.addObserver(new EmailNotifier());

        OverdueObserver oneTime = new OverdueObserver() {
            private boolean fired = false;
            @Override
            public void notifyOverdue(User user, LibraryItem item, int daysLate) {
                if (!fired) {
                    System.out.println("[One-time observer] First overdue notification: user " + user.getId() + ", item " + item.getTitle());
                    fired = true;
                    lib.removeObserver(this);
                }
            }
        };
        lib.addObserver(oneTime);

        Map<String, Object> result = lib.runSimulation(365);

        System.out.println("Items loaded: 100 users, items = " + result.get("itemsCount"));
        System.out.println("Simulation over 365 days finished.");
        System.out.println("Total fines collected: $" + result.get("totalFines"));
        System.out.println("Total borrow events: " + result.get("totalBorrowEvents"));
        System.out.println("Total return events: " + result.get("totalReturnEvents"));
        System.out.println("Total items in library: " + result.get("itemsCount"));
    }
}
