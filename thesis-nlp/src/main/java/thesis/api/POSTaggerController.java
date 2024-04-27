package thesis.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.nlp.pos_tagger.command.POSCommand;
import thesis.nlp.pos_tagger.service.POSTaggerService;

@RestController
@RequestMapping("/pos")
public class POSTaggerController {
    @Autowired
    private POSTaggerService posTaggerService;

    @RequestMapping(method = RequestMethod.POST, value = "/annotate")
    public ResponseEntity<String> tag(@RequestBody POSCommand command) throws Exception {
        if (StringUtils.isBlank(command.getTextToTag()))
            throw new Exception("Text to annotate is empty");
        return new ResponseEntity<>(posTaggerService.annotate(command.getTextToTag()).orElse("Something is error"), HttpStatus.OK);
    }


}
