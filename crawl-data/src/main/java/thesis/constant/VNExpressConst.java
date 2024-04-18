package thesis.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VNExpressConst {
    public static final String TOPIC_URL_FORMATTER = "https://vnexpress.net/%s-p%s";
    public static final Integer PAGE_MAX = 20;

    @Getter
    @AllArgsConstructor
    public enum TOPIC {
        THOI_SU("thoi-su"),
        GOC_NHIN("goc-nhin"),
        THE_GIOI("the-gioi"),
        KINH_DOANH("kinh-doanh"),
        BAT_DONG_SAN("bat-dong-san"),
        KHOA_HOC("khoa-hoc"),
        GIAI_TRI("giai-tri"),
        THE_THAO("the-thao"),
        PHAP_LUAT("phap-luat"),
        GIAO_DUC("giao-duc"),
        SUC_KHOE("suc-khoe"),
        DOI_SONG("doi-song"),
        DU_LICH("du-lich"),
        SO_HOA("so-hoa"),
        XE("xe"),
        Y_KIEN("y-kien"),
        TAM_SU("tam-su");
        private final String value;

        public static List<String> getAllValues() {
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
        CONTENT_WITHOUT_AUTHOR("article > p.Normal:not([style])"),
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
