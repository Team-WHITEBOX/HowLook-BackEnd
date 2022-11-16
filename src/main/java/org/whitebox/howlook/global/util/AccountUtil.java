package org.whitebox.howlook.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.exception.MemberDoesNotExistException;
import org.whitebox.howlook.domain.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
@Log4j2
public class AccountUtil {
    private final MemberRepository memberRepository;

    public String getLoginMemberId() {
        try {
            final String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return memberId;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Member getLoginMember() {
        log.info("AccountUtil");
        try {
            final String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("AccountUtil get Login Member : "+memberId);
            return memberRepository.findById(memberId).orElseThrow(MemberDoesNotExistException::new);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
