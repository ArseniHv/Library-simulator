public final class Film extends LibraryItem implements Loanable {
    private final String genre;
    private final String director;
    private final int filmYear;
    private final int runtime;
    private final String rating;

    public Film(int id, String title, String genre, String director, int year, int runtime, String rating) {
        super(id, title, year);
        this.genre = genre;
        this.director = director;
        this.filmYear = year;
        this.runtime = runtime;
        this.rating = rating;
    }

    @Override
    public int getLoanPeriod(User user) {
        return 7;
    }

    @Override
    public double getDailyOverdueFee() {
        return 5.0;
    }

    @Override
    protected int typeOrder() { return 3; }

    @Override
    protected String[] getSearchableFields() {
        return new String[] { getTitle(), genre, director, rating };
    }
}
