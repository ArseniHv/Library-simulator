public final class Book extends LibraryItem implements Loanable {
    private final String author;
    private final String genre;
    private final String publisher;

    public Book(int id, String title, String author, String genre, String publisher) {
        super(id, title, 0);
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
    }

    @Override
    public int getLoanPeriod(User user) {
        return 21;
    }

    @Override
    public double getDailyOverdueFee() {
        return 0.25;
    }

    @Override
    protected int typeOrder() { return 1; }

    @Override
    protected String[] getSearchableFields() {
        return new String[] { getTitle(), author, genre, publisher };
    }
}
