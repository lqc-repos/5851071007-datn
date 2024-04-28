package thesis.api;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.nlp.command.CommandNLPAnnotated;
import thesis.core.nlp.service.NLPService;
import thesis.utils.dto.ResponseDTO;

@RestController
@RequestMapping("/nlp")
public class NLPController {
    @Autowired
    private NLPService nlpService;

    @RequestMapping(method = RequestMethod.POST, value = "/annotate")
    public ResponseEntity<ResponseDTO<?>> annotate(@RequestBody CommandNLPAnnotated command) {
        try {
            if (StringUtils.isBlank(command.getSentence()))
                throw new Exception("Sentence is empty");
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(nlpService.annotate(command.getSentence()))
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add_dict")
    public ResponseEntity<ResponseDTO<?>> addDict(@RequestBody CommandNLPAnnotated command) {
        try {
            if (CollectionUtils.isEmpty(command.getVnDicts()))
                throw new Exception("VnDicts is empty");
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(nlpService.addDict(command.getVnDicts()))
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }
}
