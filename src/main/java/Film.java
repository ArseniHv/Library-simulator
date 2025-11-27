public class Film extends LibraryItem {
    private String genre;
    private String director;
    private int year;
    private int runtime;
    private String rating;

    public Film(int id, String title, String genre, String director, int year, int runtime, String rating) {
        super(id, title);
        this.genre = genre;
        this.director = director;
        this.year = year;
        this.runtime = runtime;
        this.rating = rating;
    }

    @Override
    public int getLoanPeriodDaysForBorrower() {
        return 7;
    }

    @Override
    public double getFinePerDay() {
        return 5.0;
    }
}
