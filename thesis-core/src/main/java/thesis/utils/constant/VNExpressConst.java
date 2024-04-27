package thesis.utils.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VNExpressConst {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy, HH:mm");
    public static final String TOPIC_URL_FORMATTER = "https://vnexpress.net/%s-p%s";
    public static final String CATEGORY_URL_FORMATTER = "https://vnexpress.net/category/day/cateid/%s/fromdate/%s/todate/%s/allcate/0/page/%d";
    public static final Integer PAGE_MAX = 20;

    @Getter
    @AllArgsConstructor
    public enum TOPIC {
        THOI_SU("1001005", "thoi-su"),
        GOC_NHIN("1003450", "goc-nhin"),
        THE_GIOI("1001002", "the-gioi"),
        KINH_DOANH("1003159", "kinh-doanh"),
        BAT_DONG_SAN("1005628", "bat-dong-san"),
        KHOA_HOC("1001009", "khoa-hoc"),
        GIAI_TRI("1002691", "giai-tri"),
        THE_THAO("1002565", "the-thao"),
        PHAP_LUAT("1001007", "phap-luat"),
        GIAO_DUC("1003497", "giao-duc"),
        SUC_KHOE("1003750", "suc-khoe"),
        DOI_SONG("1002966", "doi-song"),
        DU_LICH("1003231", "du-lich"),
        SO_HOA("1002592", "so-hoa"),
        XE("1001006", "xe"),
        Y_KIEN("1001012", "y-kien"),
        TAM_SU("1001014", "tam-su");
        private final String categoryId;
        private final String value;

        public static List<String> getCategoryIds() {
            return Arrays.stream(values()).map(TOPIC::getCategoryId).collect(Collectors.toList());
        }

        public static List<String> getValues() {
            return Arrays.stream(values()).map(TOPIC::getValue).collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor
    public enum CSS_QUERY {
        TITLE("h1.title-detail"),
        LOCATION("p.description > span.location-stamp"),
        DESCRIPTION("p.description"),
        CONTENT("p.Normal"),
        CONTENT_WITHOUT_AUTHOR("article > p.Normal:not([style]):not([align])"),
        PUBLICATION_DATE("span.date"),
        AUTHOR("article > p.Normal[style*=text-align:right;] > strong," +
                "article > * > p.Normal[style*=text-align:right;] > strong," +
                "article > p.Normal[align=right] > strong," +
                "article > p.author_mail > strong"),
        TOPIC("ul.breadcrumb > li > a"),
        LABEL("ul.breadcrumb > li > a"),
        HOT_LABEL(".li-title-topic a"),
        TAG("meta[name=keywords]");
        private final String value;
    }
}
