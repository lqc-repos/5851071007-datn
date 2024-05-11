package thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thesis.news.search.command.CommandSearch;
import thesis.news.search.dto.SearchForm;
import thesis.news.search.service.SearchService;
import thesis.utils.aop.StaticConstant;

@Controller
public class NewsController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/")
    @StaticConstant
    public String index(Model model) throws Exception {
        return "index";
    }

    @GetMapping("/home")
    @StaticConstant
    public String home(Model model) throws Exception {
        return "home";
    }

    @GetMapping("/search")
    @StaticConstant
    public String search(Model model, @RequestParam("s") String search) throws Exception {
        SearchForm searchForm = searchService.search(CommandSearch.builder()
                .search(search)
                .build()).orElse(null);
        model.addAttribute("searchForm", searchForm);
        return "search";
    }
}
