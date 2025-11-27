public abstract class LibraryItem {
    protected int id;
    protected String title;
    protected User borrower;
    protected int loanDay = -1;

    public LibraryItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() { return id; }
    public boolean isOnLoan() { return borrower != null; }

    public void borrow(User user, int day) {
        this.borrower = user;
        this.loanDay = day;
    }

    public int daysOverdue(int day) {
        if (!isOnLoan()) return 0;
        int due = loanDay + getLoanPeriodDaysForBorrower();
        return Math.max(0, day - due);
    }

    public double returned(int day) {
        int overdue = daysOverdue(day);
        double fine = overdue * getFinePerDay();
        this.borrower = null;
        this.loanDay = -1;
        return fine;
    }

    public abstract int getLoanPeriodDaysForBorrower();
    public abstract double getFinePerDay();
}
