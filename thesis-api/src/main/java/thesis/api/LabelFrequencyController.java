package thesis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.label_handler.service.LabelHandlerService;
import thesis.utils.dto.ResponseDTO;

@RestController
@RequestMapping("/algorithm")
public class LabelFrequencyController {
    @Autowired
    private LabelHandlerService labelHandlerService;

    @RequestMapping(method = RequestMethod.GET, value = "/migrate_article")
    public ResponseEntity<ResponseDTO<?>> annotate() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(labelHandlerService.migrateArticle())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/migrate_images")
    public ResponseEntity<ResponseDTO<?>> migrateImages() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(labelHandlerService.migrateImages())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/storage_frequency")
    public ResponseEntity<ResponseDTO<?>> storageFrequency() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(labelHandlerService.storeFrequency())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/divide_component")
    public ResponseEntity<ResponseDTO<?>> divideComponent() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(labelHandlerService.divideArticleComponent())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/calculate_tf")
    public ResponseEntity<ResponseDTO<?>> calculateTfIdf() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(labelHandlerService.calculateTf())
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/handle_nlp_label")
    public ResponseEntity<ResponseDTO<?>> handleNLPLabel() {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(labelHandlerService.handleNLPLabel())
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
