package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;

public interface MemberService {
    static class MidExistException extends Exception{

    }
    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
}
