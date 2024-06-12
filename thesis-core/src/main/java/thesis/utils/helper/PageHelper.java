package thesis.utils.helper;

public class PageHelper {
    public static int getSkip(long page, int size) {
        return (int) ((page - 1) * size);
    }

    public static int getTotalPage(long total, int size) {
        return (int) ((total + size - 1) / size);
    }
}
