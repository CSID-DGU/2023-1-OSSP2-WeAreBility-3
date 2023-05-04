package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.domain.Walkway;
import com.dongguk.cse.naemansan.service.WalkwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WalkwayController {
    private final WalkwayService walkwayService;

    @Autowired
    public WalkwayController(WalkwayService walkwayService) {
        this.walkwayService = walkwayService;
    }

    //산책로 맵핑
    @GetMapping("/walkways/new")
    public String createForm(){
        return "walkways/creatWalkwayForm";
    }
    //산책로 생성
    @PostMapping("/walkways/new")
    public String create(WalkwayForm form){
        Walkway walkway = new Walkway();
        walkway.setId(form.getId());
        walkway.setUser_id(form.getUser_id());
        walkway.setTitle(form.getTitle());
        walkway.setCreated_date(form.getCreated_date());
        walkway.setIntroduction(form.getIntroduction());
        walkway.setStart_location(form.getStart_location());
        walkway.setLocations(form.getLocations());
        walkway.setDistance(form.getDistance());
        walkway.setStatus(form.getStatus());
        walkwayService.join(walkway);

        return "redirect:/";
    }

    @GetMapping("/walkways")
    public String list(Model model){
        List<Walkway> walkways = walkwayService.findWalkways();
        model.addAttribute("walkways",walkways);
        return "walkways/walkwayList";
    }

}
