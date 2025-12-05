public abstract sealed class LibraryItem implements Searchable, Comparable<LibraryItem>
    permits Book, Journal, Film {
    protected final int id;
    protected final String title;
    protected User borrower;
    protected int loanDay = -1;
    protected final int year;

    protected LibraryItem(int id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public boolean isOnLoan() { return borrower != null; }

    public void borrow(User user, int day) {
        this.borrower = user;
        this.loanDay = day;
    }

    public int daysOverdue(int day) {
        if (!isOnLoan()) return 0;
        int due = loanDay + getLoanPeriod(borrower);
        return Math.max(0, day - due);
    }

    public double returned(int day) {
        int overdue = daysOverdue(day);
        double fine = overdue * getDailyOverdueFee();
        this.borrower = null;
        this.loanDay = -1;
        return fine;
    }

    protected String[] getSearchableFields() {
        return new String[] { title };
    }

    @Override
    public boolean matches(String keyword) {
        if (keyword == null) return false;
        String k = keyword.toLowerCase();
        for (String s : getSearchableFields()) {
            if (s != null && s.toLowerCase().contains(k)) return true;
        }
        if (year > 0 && String.valueOf(year).contains(k)) return true;
        return false;
    }

    @Override
    public int compareTo(LibraryItem other) {
        if (other == null) return 1;
        int t = this.title.compareToIgnoreCase(other.title);
        if (t != 0) return t;
        int typeOrder = Integer.compare(typeOrder(), other.typeOrder());
        if (typeOrder != 0) return typeOrder;
        return Integer.compare(this.year, other.year);
    }

    protected abstract int typeOrder();
    public abstract int getLoanPeriod(User user);
    public abstract double getDailyOverdueFee();
}
