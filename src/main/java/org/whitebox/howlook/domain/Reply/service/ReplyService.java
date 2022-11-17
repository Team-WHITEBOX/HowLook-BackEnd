package org.whitebox.howlook.domain.Reply.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.whitebox.howlook.domain.Reply.dto.ReplyDTO;
//import org.whitebox.howlook.dto.PageRequestDTO;
//import org.whitebox.howlook.dto.PageResponseDTO;

public interface ReplyService {
    int register(ReplyDTO replyDTO);

    ReplyDTO read(int CommendId);

    void modify(ReplyDTO replyDTO);

    void remove(int CommendId);

//    PageResponse<ReplyDTO> getListOfBoard(int bno, PageRequestDTO pageRequestDTO)
}
