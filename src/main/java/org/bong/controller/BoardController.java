package org.bong.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bong.domain.BoardVO;
import org.bong.domain.Criteria;
import org.bong.domain.PageDTO;
import org.bong.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자
public class BoardController {
    private BoardService service;

//    @GetMapping("/list")
//    public void list(Model model){
//        log.info("list");
//        log.info("BoardController_list method calling!!!");
//        model.addAttribute("list", service.getList());
//    }


    @GetMapping("/list")
    public void list(Criteria cri, Model model){
        log.info("list : " + cri);

        List<BoardVO> list = service.getList(cri);

        model.addAttribute("list", list);





        int total = service.getTotal(cri);
        model.addAttribute("pageMarker", new PageDTO(cri,total));
    }

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(BoardVO board,RedirectAttributes rttr){
                                            // 글 작성이 끝난 후 다시 글 목록으로 돌아가고 새롭게 등록된 게시물의 번호를 같이 전달하기 위함 (addFlashAttribute())
//        log.info("register : "+board);
        service.register(board);

        rttr.addFlashAttribute("result", board.getBno());
        // addFlashAttribute()는 일회성으로 데이터를 전달한다. 이 메소드로 저장된 데이터는 단 한 번만 사용할 수 있게 보관된다.
        //  내부적으로는 HttpSession을 이용해서 처리

        return "redirect:/board/list";
                // = sendRedirect("/board/list");
    }

    @GetMapping({"/get", "/modify"})
    public void get(@RequestParam("bno")Long bno, Model model, @ModelAttribute("cri") Criteria cri){
                                                                // ModelAttribute Annotaion은 지정한 이름으로 Model에 데이터를 저장
        log.info("/get or modify");
        model.addAttribute("board", service.get(bno));
    }

    @PostMapping("/modify")
    public String modify(BoardVO board, RedirectAttributes rttr, @ModelAttribute("cri")Criteria cri){
        log.info("Modify :  " + board);

        if(service.modify(board)){
            rttr.addFlashAttribute("result","success");
        }

//        rttr.addAttribute("pageNum", cri.getPageNum());
//        rttr.addAttribute("amount", cri.getAmount());
//        rttr.addAttribute("keyword", cri.getKeyword());
//        rttr.addAttribute("type", cri.getType());

        return "redirect:/board/list" + cri.getListLink();
                                            // getListLink() method는 UriComponentsBuilder를 이용하여 파라미터가 포함된 uri를 생성한다.
                                            // ex_ &pageNum=2&amount=10 ...
    }

    @PostMapping("/remove")
    public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr, @ModelAttribute("cri") Criteria cri){
        log.info("remove...." +bno);
        if(service.delete(bno)){
            rttr.addFlashAttribute("result", "success");
        }
//        rttr.addAttribute("pageNum", cri.getPageNum());
//        rttr.addAttribute("amount", cri.getAmount());
//        rttr.addAttribute("keyword", cri.getKeyword());
//        rttr.addAttribute("type", cri.getType());

        return "redirect:/board/list" + cri.getListLink();
    }

}
