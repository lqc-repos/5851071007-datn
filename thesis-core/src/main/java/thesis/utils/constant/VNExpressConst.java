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
        THOI_SU("1001005", "thoi-su", "thời_sự"),
        GOC_NHIN("1003450", "goc-nhin", "góc_nhìn"),
        THE_GIOI("1001002", "the-gioi", "thế_giới"),
        KINH_DOANH("1003159", "kinh-doanh", "kinh_doanh"),
        BAT_DONG_SAN("1005628", "bat-dong-san", "bất_động_sản"),
        KHOA_HOC("1001009", "khoa-hoc", "khoa_học"),
        GIAI_TRI("1002691", "giai-tri", "giải_trí"),
        THE_THAO("1002565", "the-thao", "thể_thao"),
        PHAP_LUAT("1001007", "phap-luat", "pháp_luật"),
        GIAO_DUC("1003497", "giao-duc", "giáo_dục"),
        SUC_KHOE("1003750", "suc-khoe", "sức_khỏe"),
        DOI_SONG("1002966", "doi-song", "đời_sống"),
        DU_LICH("1003231", "du-lich", "du_lịch"),
        SO_HOA("1002592", "so-hoa", "số_hóa"),
        XE("1001006", "xe", "xe"),
        Y_KIEN("1001012", "y-kien", "ý_kiến"),
        TAM_SU("1001014", "tam-su", "tâm_sự");
        private final String categoryId;
        private final String value;
        private final String vnValue;

        public static List<String> getCategoryIds() {
            return Arrays.stream(values()).map(TOPIC::getCategoryId).collect(Collectors.toList());
        }

        public static List<String> getValues() {
            return Arrays.stream(values()).map(TOPIC::getValue).collect(Collectors.toList());
        }

        public static List<String> getVnValues() {
            return Arrays.stream(values()).map(TOPIC::getVnValue).collect(Collectors.toList());
        }

        public static TOPIC fromVnValue(String vnValue) {
            return Arrays.stream(values()).filter(f -> f.vnValue.equals(vnValue)).findFirst().orElseThrow();
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
        TAG("meta[name=keywords]"),
        POSTER_URL(".fig-picture > picture"),
        IMAGE_DESCRIPTION("p.Image");
        private final String value;
    }
}
