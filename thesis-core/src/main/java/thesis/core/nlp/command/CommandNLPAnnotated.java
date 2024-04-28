package thesis.core.nlp.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommandNLPAnnotated {
    private String sentence;
    private List<String> vnDicts;
}
