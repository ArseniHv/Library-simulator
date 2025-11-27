import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Library {
    private final Map<Integer, LibraryItem> items = new HashMap<>();
    private final List<User> users = new ArrayList<>();
    private int nextItemId = 1;
    private final Random random = new Random();

    public double alphaBook = 0.05;
    public double alphaJournal = 0.08;
    public double alphaFilm = 0.05;
    public double betaReturn = 0.02;

    public Library() {}

    public void addUser(User u) { users.add(u); }
    public List<User> getUsers() { return users; }

    public int addItem(LibraryItem item) {
        items.put(item.getId(), item);
        return item.getId();
    }

    public LibraryItem getItemById(int id) { return items.get(id); }

    public int nextId() { return nextItemId++; }

    public void loadBooksFromResource(String resourcePath) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String header = br.readLine();
                if (header == null) return;
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    String[] tok = line.split(";");
                    String title = tok.length>0?tok[0]:"";
                    String author = tok.length>1?tok[1]:"";
                    String genre = tok.length>2?tok[2]:"";
                    String publisher = tok.length>4?tok[4]:(tok.length>3?tok[3]:"");
                    Book b = new Book(nextId(), title, author, genre, publisher);
                    addItem(b);
                }
            }
        }
    }

    public void loadJournalsFromResource(String resourcePath) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String header = br.readLine();
                if (header == null) return;
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    String[] tok = line.split(";");
                    String title = tok.length>0?tok[0]:"";
                    String eissn = tok.length>1?tok[1]:"";
                    String publisher = tok.length>2?tok[2]:"";
                    String latestIssue = tok.length>3?tok[3]:"";
                    String url = tok.length>4?tok[4]:"";
                    Journal j = new Journal(nextId(), title, eissn, publisher, latestIssue, url);
                    addItem(j);
                }
            }
        }
    }

    public void loadMoviesFromResource(String resourcePath) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String header = br.readLine();
                if (header == null) return;
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    String[] tok = line.split(";");
                    String title = tok.length > 1 ? tok[1] : "";
                    String genre = tok.length > 2 ? tok[2] : "";
                    String director = tok.length > 4 ? tok[4] : "";
                    int year = (tok.length > 6 && tok[6].matches("\\d+")) ? Integer.parseInt(tok[6]) : 0;
                    int runtime = (tok.length > 7 && tok[7].matches("\\d+")) ? Integer.parseInt(tok[7]) : 0;
                    String rating = tok.length > 8 ? tok[8] : "";
                    Film f = new Film(nextId(), title, genre, director, year, runtime, rating);
                    addItem(f);
                }
            }
        }
    }

    public boolean borrowItem(User user, int itemId, int day) {
        LibraryItem it = items.get(itemId);
        if (it == null) return false;
        if (it.isOnLoan()) return false;

        if (user.getType() == User.UserType.STUDENT) {
            if (it instanceof Film) {
                int moviesOut = user.getBorrowedCountByClass(Film.class, this);
                if (moviesOut >= 1) return false;
            }
            if (it instanceof Book) {
                int booksOut = user.getBorrowedCountByClass(Book.class, this);
                if (booksOut >= 3) return false;
            }
            if (it instanceof Journal) {
                int journalsOut = user.getBorrowedCountByClass(Journal.class, this);
                if (journalsOut >= 3) return false;
            }
        }

        it.borrow(user, day);
        user.borrowItem(itemId);
        return true;
    }

    public double returnItem(int itemId, int day) {
        LibraryItem it = items.get(itemId);
        if (it == null || !it.isOnLoan()) return 0.0;
        User borrower = it.borrower;
        double fine = it.returned(day);
        if (borrower != null) borrower.returnItem(itemId);
        return fine;
    }

    public Map<String, Object> runSimulation(int days) {
        Map<String, Object> stats = new HashMap<>();
        double totalFines = 0.0;
        int totalBorrowEvents = 0;
        int totalReturnEvents = 0;

        List<LibraryItem> allItems = new ArrayList<>(items.values());

        for (int day = 1; day <= days; day++) {
            for (User u : users) {
                if (random.nextDouble() < alphaBook) {
                    List<LibraryItem> availBooks = new ArrayList<>();
                    for (LibraryItem it : allItems) if (!it.isOnLoan() && it instanceof Book) availBooks.add(it);
                    if (!availBooks.isEmpty()) {
                        LibraryItem pick = availBooks.get(random.nextInt(availBooks.size()));
                        if (borrowItem(u, pick.getId(), day)) totalBorrowEvents++;
                    }
                }
                if (random.nextDouble() < alphaJournal) {
                    List<LibraryItem> availJ = new ArrayList<>();
                    for (LibraryItem it : allItems) if (!it.isOnLoan() && it instanceof Journal) availJ.add(it);
                    if (!availJ.isEmpty()) {
                        LibraryItem pick = availJ.get(random.nextInt(availJ.size()));
                        if (borrowItem(u, pick.getId(), day)) totalBorrowEvents++;
                    }
                }
                if (random.nextDouble() < alphaFilm) {
                    List<LibraryItem> availF = new ArrayList<>();
                    for (LibraryItem it : allItems) if (!it.isOnLoan() && it instanceof Film) availF.add(it);
                    if (!availF.isEmpty()) {
                        LibraryItem pick = availF.get(random.nextInt(availF.size()));
                        if (borrowItem(u, pick.getId(), day)) totalBorrowEvents++;
                    }
                }

                Set<Integer> borrowedIds = new HashSet<>(u.getBorrowedItemIds());
                for (int itemId : borrowedIds) {
                    LibraryItem it = items.get(itemId);
                    if (it == null) continue;

                    boolean returnedToday = false;

                    if (u.returnsOnTime()) {
                        int dueCheck = (day - it.loanDay);
                        if (dueCheck == it.getLoanPeriodDaysForBorrower()) {
                            double fine = returnItem(itemId, day);
                            totalFines += fine;
                            totalReturnEvents++;
                            returnedToday = true;
                        }
                    }

                    if (!returnedToday) {
                        int overdueCheck = (day - it.loanDay) - it.getLoanPeriodDaysForBorrower();
                        if (overdueCheck > 0) {
                            if (random.nextDouble() < betaReturn) {
                                double fine = returnItem(itemId, day);
                                totalFines += fine;
                                totalReturnEvents++;
                            }
                        }
                    }
                }
            }
        }

        stats.put("totalFines", totalFines);
        stats.put("totalBorrowEvents", totalBorrowEvents);
        stats.put("totalReturnEvents", totalReturnEvents);
        stats.put("itemsCount", items.size());
        return stats;
    }
}
