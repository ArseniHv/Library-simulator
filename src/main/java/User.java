import java.util.HashSet;
import java.util.Set;

public class User {
    public enum UserType { STUDENT, FACULTY }

    private final int id;
    private final UserType type;
    private final boolean returnsOnTime;
    private final Set<Integer> borrowedItemIds = new HashSet<>();

    public User(int id, UserType type, boolean returnsOnTime) {
        this.id = id;
        this.type = type;
        this.returnsOnTime = returnsOnTime;
    }

    public int getId() { return id; }
    public UserType getType() { return type; }
    public boolean returnsOnTime() { return returnsOnTime; }

    public void borrowItem(int itemId) { borrowedItemIds.add(itemId); }
    public void returnItem(int itemId) { borrowedItemIds.remove(itemId); }

    public int getBorrowedCountByClass(Class<?> clazz, Library library) {
        int cnt = 0;
        for (int id : borrowedItemIds) {
            LibraryItem it = library.getItemById(id);
            if (it != null && clazz.isInstance(it)) cnt++;
        }
        return cnt;
    }

    public int totalBorrowed() { return borrowedItemIds.size(); }
    public Set<Integer> getBorrowedItemIds() { return borrowedItemIds; }
}