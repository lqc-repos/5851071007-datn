package thesis.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    public ResponseEntity<String> annotate() throws Exception {
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
