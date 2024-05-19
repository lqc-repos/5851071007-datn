package thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thesis.news.article.service.ArticleService;
import thesis.news.search.command.CommandSearch;
import thesis.news.search.dto.SearchForm;
import thesis.news.search.service.SearchService;
import thesis.utils.aop.StaticConstant;

import java.util.ArrayList;

@Controller
public class NewsController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    @StaticConstant
    public String index(Model model) throws Exception {
        return "redirect:/search";
    }

    @GetMapping("/home")
    @StaticConstant
    public String home(Model model) throws Exception {
        return "redirect:/search";
    }

    @GetMapping("/search")
    @StaticConstant
    public String search(Model model, @RequestParam(value = "s", required = false) String search,
                         @RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "size", required = false) Integer size,
                         @RequestParam(value = "isCustomTag", required = false) Boolean isCustomTag) throws Exception {
        SearchForm searchForm = searchService.search(CommandSearch.builder()
                        .search(search)
                        .page(page)
                        .size(size)
                        .isCustomTag(isCustomTag)
                        .build())
                .orElse(SearchForm.builder()
                        .articles(new ArrayList<>())
                        .build());
        if (page != null && searchForm.getTotalPage() > 0 && page > searchForm.getTotalPage()) {
            return "404";
        }
        model.addAttribute("searchForm", searchForm);
        return "search";
    }

    @GetMapping("/post")
    @StaticConstant
    public String post(Model model, @RequestParam("id") String articleId) throws Exception {
        model.addAttribute("article", articleService.getArticleById(articleId));
        return "post";
    }

    @GetMapping("/topic")
    @StaticConstant
    public String topic(Model model, @RequestParam("v") String name,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "size", required = false) Integer size) throws Exception {
        model.addAttribute("searchForm", searchService.searchByTopic(name, page, size).orElseThrow());
        return "category";
    }

}
