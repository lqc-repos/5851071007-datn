package thesis.news.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OkHttpResponse {
    private Integer statusCode;
    private String message;
    private Object data;
}
