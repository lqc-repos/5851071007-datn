package thesis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.algorithm.service.AlgorithmService;
import thesis.utils.dto.ResponseDTO;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {
    @Autowired
    private AlgorithmService algorithmService;

    @RequestMapping(method = RequestMethod.GET, value = "/migrate_article")
    public ResponseEntity<ResponseDTO<?>> annotate() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(algorithmService.migrateArticle())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/process_tf")
    public ResponseEntity<ResponseDTO<?>> processTf() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(algorithmService.saveFrequency())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

}
