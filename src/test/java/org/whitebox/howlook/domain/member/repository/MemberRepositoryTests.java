package org.whitebox.howlook.domain.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.entity.MemberRole;
import org.whitebox.howlook.global.config.QuerydslConfig;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원가입")
    @Test
    void joinMemberTest() {
        // given
        Member member = member();

        // when
        Member save = memberRepository.save(member);

        // then
        assertThat(save.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(save.getMemberPassword()).isEqualTo(member.getMemberPassword());
    }

    @DisplayName("회원 조회")
    @Test
    void MemberListTest() {
        // given
        Member member = memberRepository.save(member());
        // when
        Member find = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();

        // then
        assertThat(find.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(find.getRoleSet().contains(MemberRole.USER)).isTrue();
    }

    private Member member(){
        return Member.builder()
                .memberId("testcode999").memberPassword("a1234567").gender('M').nickName("테스트999")
                .birthDay(LocalDate.now()).del(false).height(180L).weight(70L).name("테스트")
                .phone("01012345678").profilePhoto(null).social(false).roleSet(Collections.singleton(MemberRole.USER))
                .build();
    }
}