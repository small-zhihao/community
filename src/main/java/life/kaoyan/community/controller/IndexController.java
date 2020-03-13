package life.kaoyan.community.controller;

import life.kaoyan.community.dto.PaginationDTO;
import life.kaoyan.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public  String index(Model model,
                         @RequestParam(name = "page",defaultValue = "1") Integer page,
                         @RequestParam(name = "size",defaultValue = "5") Integer size,
                         @RequestParam(name = "search", required = false)String search){
        log.info("========开始===========");
        System.out.println("进入");
        PaginationDTO pagination=questionService.list(search,page,size);
        System.out.println("最后");
        model.addAttribute("pagination",pagination);
        log.info("========结束===========");
        model.addAttribute("search",search);
        return "index";
    }
}
