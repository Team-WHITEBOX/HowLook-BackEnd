package org.whitebox.howlook.domain.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.whitebox.howlook.domain.member.dto.UserProfileResponse;
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
        Member member = member("testcode999");

        // when
        Member save = memberRepository.save(member);

        // then
        assertThat(save.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(save.getMemberPassword()).isEqualTo(member.getMemberPassword());
    }

    @DisplayName("회원 조회")
    @Test
    void findByMemberIdTest() {
        // given
        Member member = memberRepository.save(member("testcode999"));
        // when
        Member find = memberRepository.findByMemberId(member.getMemberId()).orElseThrow();

        // then
        assertThat(find.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(find.getRoleSet().contains(MemberRole.USER)).isTrue();
    }

    @DisplayName("닉네임 중복 조회")
    @Test
    void existNickNameTest() {
        // given
        Member member = memberRepository.save(member("testcode999"));

        // when
        Boolean exist = memberRepository.existsByNickName(member.getNickName());

        // then
        assertThat(exist).isTrue();
    }

    @DisplayName("유저프로필 조회 테스트")
    @Test
    void findUserProfileTest() {
        // given
        Member member = memberRepository.save(member("testcode999"));
        UserProfileResponse userProfileResponse = userProfileResponse();

        // when
        UserProfileResponse myProfile = memberRepository.findUserProfileByMemberIdAndTargetMemberId("testcode999","testcode999").orElseThrow();
        UserProfileResponse profile = memberRepository.findUserProfileByMemberIdAndTargetMemberId("testcode111","testcode999").orElseThrow();

        // then
        assertThat(myProfile).usingRecursiveComparison().isEqualTo(userProfileResponse); //본인 프로필 확인
        assertThat(profile).usingRecursiveComparison().isNotEqualTo(userProfileResponse); //타인 프로필 확인
    }

    private Member member(String name){
        return Member.builder()
                .memberId(name).memberPassword("a1234567").gender('M').nickName(name)
                .birthDay(LocalDate.now()).del(false).height(180L).weight(70L).name("테스트")
                .phone("01012345678").profilePhoto(null).social(false).roleSet(Collections.singleton(MemberRole.USER))
                .build();
    }
    private UserProfileResponse userProfileResponse(){
        return UserProfileResponse.builder()
                .memberId("testcode999").memberNickName("testcode999").memberHeight(180L).memberWeight(70L)
                .profilePhoto(null).memberPosts(null).memberPostCount(null).isMe(true).build();
    }
}