package thesis.core.label_handler.model.total_label_frequency.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandQueryTotalLabelFrequency {
    private String label;
    private TotalLabelProjection totalLabelProjection;
    private Boolean hasProjection;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TotalLabelProjection {
        private Boolean isId;
        private Boolean isLabel;
        private Boolean isCount;
    }
}
