public class Book extends LibraryItem {
    private String author;
    private String genre;
    private String publisher;

    public Book(int id, String title, String author, String genre, String publisher) {
        super(id, title);
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
    }

    @Override
    public int getLoanPeriodDaysForBorrower() {
        return 21;
    }

    @Override
    public double getFinePerDay() {
        return 0.25;
    }
}
