package thesis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thesis.core.article.command.CommandCommonQuery;
import thesis.core.article.service.ArticleService;
import thesis.core.search_engine.command.CommandSearchArticle;
import thesis.core.search_engine.service.SearchEngineService;
import thesis.utils.dto.ResponseDTO;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private SearchEngineService searchEngineService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    public ResponseEntity<ResponseDTO<?>> get(@RequestBody CommandCommonQuery command) {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(articleService.get(command))
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search")
    public ResponseEntity<ResponseDTO<?>> searchArticle(@RequestBody CommandSearchArticle command) {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(searchEngineService.searchArticle(command))
                    .build(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search_by_topic")
    public ResponseEntity<ResponseDTO<?>> searchArticleByTopic(@RequestBody CommandSearchArticle command) {
        try {
            return new ResponseEntity<>(ResponseDTO.builder()
                    .statusCode(HttpStatus.OK.value())
                    .data(searchEngineService.searchArticleByTopic(command))
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
