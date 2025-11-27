import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LibraryItemTest {

    @Test
    public void bookDaysOverdueAndFine() {
        Library lib = new Library();
        User u = new User(1, User.UserType.STUDENT, true);
        lib.addUser(u);
        Book b = new Book(lib.nextId(), "Test Book", "Author", "Genre", "Pub");
        lib.addItem(b);

        assertTrue(lib.borrowItem(u, b.getId(), 1));
        assertEquals(0, b.daysOverdue(22));
        assertFalse(b.isOnLoan() == false && b.daysOverdue(22) > 0);

        assertEquals(1, b.daysOverdue(23));
        double fine = b.returned(23);
        assertEquals(0.25, fine, 1e-9);
        assertFalse(b.isOnLoan());
    }
@Test
public void filmDaysOverdueAndFine() {
    Library lib = new Library();
    User u = new User(2, User.UserType.STUDENT, true);
    lib.addUser(u);
    Film f = new Film(lib.nextId(), "Test Film", "Genre", "Director", 2020, 100, "PG");
    lib.addItem(f);

    assertTrue(lib.borrowItem(u, f.getId(), 10));
    assertEquals(0, f.daysOverdue(17));
    double fineNo = lib.returnItem(f.getId(), 17);
    assertEquals(0.0, fineNo, 1e-9);

    assertTrue(lib.borrowItem(u, f.getId(), 50));
    assertEquals(1, f.daysOverdue(58));
    double fine = lib.returnItem(f.getId(), 58);
    assertEquals(5.0, fine, 1e-9);

    assertFalse(f.isOnLoan());
}


    @Test
    public void journalDaysOverdueAndFine() {
        Library lib = new Library();
        User u = new User(3, User.UserType.FACULTY, true);
        lib.addUser(u);

        Journal j = new Journal(lib.nextId(), "J1", "eissn", "Pub", "Issue", "url");
        lib.addItem(j);
        assertTrue(lib.borrowItem(u, j.getId(), 1));
        assertEquals(0, j.daysOverdue(15));
        double fineNo = j.returned(15);
        assertEquals(0.0, fineNo, 1e-9);

        assertTrue(lib.borrowItem(u, j.getId(), 30));
        assertEquals(2, j.daysOverdue(46));
        double fine = j.returned(46);
        assertEquals(2.0, fine, 1e-9);
        assertFalse(j.isOnLoan());
    }
}
