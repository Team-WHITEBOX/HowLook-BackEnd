package org.whitebox.howlook.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // Member
    REGISTER_SUCCESS(200, "M001", "회원가입에 성공하였습니다."),
    GET_USERPROFILE_SUCCESS(200, "M002", "회원 프로필을 조회하였습니다."),
    GET_USER_BY_TOKEN_SUCCESS(200, "M003", "토큰으로 회원정보를 조회하였습니다."),
    GET_EDIT_PROFILE_SUCCESS(200, "M004", "회원 프로필 수정정보를 조회하였습니다."),
    EDIT_PROFILE_SUCCESS(200, "M005", "회원 프로필을 수정하였습니다."),
    UPDATE_PASSWORD_SUCCESS(200, "M006", "회원 비밀번호를 변경하였습니다."),
    CHECK_MEMBERID_GOOD(200, "M007", "사용가능한 memberId 입니다."),
    CHECK_MEMBERID_BAD(200, "M008", "사용불가능한 memberId 입니다."),
    CHECK_NICKNAME_GOOD(200, "M009", "사용가능한 닉네임 입니다."),
    CHECK_NICKNAME_BAD(200, "M010", "사용불가능한 닉네임 입니다."),



    // MemberPost
    GET_MEMBER_SAVED_POSTS_SUCCESS(200, "MP001", "회원의 저장한 게시물 조회에 성공하였습니다."),

    // post
    CREATE_POST_SUCCESS(200, "P001", "게시물 업로드에 성공하였습니다."),
    DELETE_POST_SUCCESS(200, "P002", "게시물 삭제에 성공하였습니다."),
    FIND_POST_SUCCESS(200, "P003", "게시물 조회에 성공하였습니다."),
    FIND_RECENT10POSTS_SUCCESS(200, "F004", "최근 게시물 10개 조회에 성공하였습니다."),
    LIKE_POST_SUCCESS(200, "P005", "게시물 좋아요에 성공하였습니다."),
    UN_LIKE_POST_SUCCESS(200, "P006", "게시물 좋아요 해제에 성공하였습니다."),
    BOOKMARK_POST_SUCCESS(200, "P007", "게시물 북마크에 성공하였습니다."),
    UN_BOOKMARK_POST_SUCCESS(200, "P008", "게시물 북마크 해제에 성공하였습니다."),
    CREATE_COMMENT_SUCCESS(200, "P009", "댓글 업로드에 성공하였습니다."),
    DELETE_COMMENT_SUCCESS(200, "P010", "댓글 삭제에 성공하였습니다."),
    GET_REPLY_SUCCESS(200,"P011", "댓글 조회에 성공하였습니다."),
    LIKE_COMMENT_SUCCESS(200, "P012", "댓글 좋아요에 성공하였습니다."),
    UNLIKE_COMMENT_SUCCESS(200, "P013", "댓글 좋아요 해제에 성공하였습니다."),
    MODIFY_REPLY_SUCCESS(200,"P014","댓글 수정에 성공하였습니다."),
    GET_REPLY_IN_POST_SUCCESS(200,"P015","게시글 댓글 조회에 성공하였습니다."),
    FIND_POST_BY_ID_SUCCESS(200, "P016", "아이디로 게시글 조회에 성공했습니다."),
    FIND_POST_BY_MEMBER_ID_SUCCESS(200, "P017", "멤버아디로 게시글 조회에 성공했습니다."),

    // Tournament
    GET_TOURNAMENT_POST_SUCCESS(200, "T001", "토너먼트 게시글 조회에 성공하였습니다."),
    UPDATE_TOURNAMENT_SCORE_SUCCESS(200, "T002", "토너먼트 결과 업데이트에 성공하였습니다."),
    GET_TOURNAMENT_HISTORY_SUCCESS(200, "T003", "토너먼트 기록 조회에 성공하였습니다."),
    GET_TOURNAMENT_EVENT_HISTORY_SUCCESS(200, "T004", "이벤트 토너먼트 기록 조회에 성공하였습니다."),

    // Eval
    EVAL_REGISTER_SUCCESS(200, "E001", "평가 게시글 등록에 성공하였습니다."),
    EVAL_SEARCH_SUCCESS(200, "E002", "평가 게시글 조회에 성공하였습니다."),
    EVAL_REGISTER_FAIL(200, "E003", "평가 게시글 등록에 실패하였습니다."),
    EVAL_SEARCH_FAIL(200, "E004", "평가 게시글 조회에 실패하였습니다.");

    private final int status;
    private final String code;
    private final String message;
}
