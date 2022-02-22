package org.bong.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bong.domain.BoardVO;
import org.bong.domain.Criteria;
import org.bong.mapper.BoardMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{

    private BoardMapper mapper;



    @Override
    public void register(BoardVO board) {
        log.info("register........" +board);
        mapper.insertSelectKey(board);
    }

    @Override
    public BoardVO get(Long bno) {
        log.info("get..........."+bno);
        return mapper.read(bno);
    }

    @Override
    public boolean modify(BoardVO board) {
        log.info("modify......." +board);

        return mapper.update(board)==1;
    }

    @Override
    public boolean delete(Long bno) {
       log.info("Delete ........... " +bno);
        return mapper.delete(bno)==1;
    }

//    @Override
//    public List<BoardVO> getList() {
//        log.info("get List .......");
//
//        return mapper.getList();
//    }

    @Override
    public List<BoardVO> getList(Criteria cri){
        log.info("get List with Paging...");
        List<BoardVO> list = mapper.getListWithPaging(cri);

        for(int i = 0 ; i<list.size() ; i++){
            System.err.println("List : "+ list.get(i).getBno() + " / " +list.get(i).getReplyCnt());
        }

        return list;
    }

    @Override
    public int getTotal(Criteria cri){
        return mapper.getTotalCount(cri);
    }


}
