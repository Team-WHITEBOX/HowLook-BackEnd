package org.whitebox.howlook.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // Member
    LOGIN_SUCCESS(200, "M001", "로그인에 성공하였습니다."),
    REGISTER_SUCCESS(201, "M002", "회원가입에 성공하였습니다."),
    GET_USERPROFILE_SUCCESS(200, "M003", "회원 프로필을 조회하였습니다."),
    GET_USER_BY_TOKEN_SUCCESS(200, "M004", "토큰으로 회원정보를 조회하였습니다."),
    GET_EDIT_PROFILE_SUCCESS(200, "M005", "회원 프로필 수정정보를 조회하였습니다."),
    EDIT_PROFILE_SUCCESS(200, "M006", "회원 프로필을 수정하였습니다."),
    UPDATE_PASSWORD_SUCCESS(200, "M007", "회원 비밀번호를 변경하였습니다."),
    CHECK_MEMBERID_GOOD(200, "M008", "사용가능한 memberId 입니다."),
    CHECK_MEMBERID_BAD(200, "M009", "사용불가능한 memberId 입니다."),
    CHECK_NICKNAME_GOOD(200, "M010", "사용가능한 닉네임 입니다."),
    CHECK_NICKNAME_BAD(200, "M011", "사용불가능한 닉네임 입니다."),
    LOGOUT_SUCCESS(200, "M012", "로그아웃에 성공하였습니다."),
    REFRESH_TOKEN_SUCCESS(200,"M013","리프레쉬 토큰 발급에 성공하였습니다."),



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
    IS_BOOKMARK_SUCCESS(200, "P018", "게시물 북마크 조회에 성공하였습니다."),

    // Weather
    FIND_RECENT10POSTS_BY_WEATHER_SUCCESS(200, "W001", "날씨로 최근 포스트 10개 조회에 성공했습니다."),
    FIND_WEATHER_BY_GPS_SUCCESS(200, "W002", "위치로 현재 날씨 조회에 성공했습니다."),

    // Hashtag
    GET_HASHTAG_POST_SUCCESS(200, "H001", "해시태그로 게시글 조회에 성공하였습니다."),

    // Tournament
    GET_TOURNAMENT_POST_SUCCESS(200, "T001", "토너먼트 게시글 조회에 성공하였습니다."),
    UPDATE_TOURNAMENT_SCORE_SUCCESS(200, "T002", "토너먼트 결과 업데이트에 성공하였습니다."),
    GET_TOURNAMENT_HISTORY_SUCCESS(200, "T003", "토너먼트 기록 조회에 성공하였습니다."),
    GET_TOURNAMENT_EVENT_HISTORY_SUCCESS(200, "T004", "이벤트 토너먼트 기록 조회에 성공하였습니다."),

    // Eval
    EVAL_REGISTER_SUCCESS(200, "E001", "평가 게시글 등록에 성공하였습니다."),
    EVAL_SEARCH_SUCCESS(200, "E002", "평가 게시글 조회에 성공하였습니다."),
    EVAL_SEARCH_FAIL(200, "E003", "평가 게시글 조회에 실패하였습니다."),

    // chat
    GET_CHATROOMLIST_SUCCESS(200,"C001","채팅방 목록 조회에 성공하였습니다."),
    CREATE_CHATROOM_SUCCESS(200,"C002","채팅방 생성에 성공하였습니다."),
    GET_CHAT_USERLIST_SUCCESS(200,"C003","채팅방 유저 목록 조회에 성공하였습니다."),
    GET_CHATLIST_SUCCESS(200,"C004","채팅방 채팅 내용 조회에 성공하였습니다."),
    LEAVE_ROOM_SUCCESS(200,"C005","채팅방 나가기에 성공하였습니다."),

    // Report
    REPORT_POST_SUCCESS(200, "R001", "게시글 신고에 성공했습니다."),

    // Creator
    CREATOR_EVAL_POST_SUCCESS(200,"CR001","크리에이터 평가 게시글 등록에 성공하였습니다."),
    GET_CREATOR_EVAL(200, "CR002", "크리에이터 평가글 읽어오기에 성공하였습니다."),
    DELETE_CREATOR_EVAl_SUCCESS(200, "CR003", "크리에이터 평가글 삭제에 성공하였습니다."),
    MODIFY_CREATOR_EVAL_SUCCESS(200, "CR004", "크리에이터 평가글 수정에 성공하였습니다."),
    GET_CREATOR_EVAL_LIST_SUCCESS(200,"CR005","크리에이터 평가글 목록 조회에 성공하였습니다."),
    CREATOR_EVAL_REPLY_SUCCESS(200,"CR006","크리에이터 평가 리뷰 등록에 성공하였습니다."),
    GET_EVAL_REPLY_SUCCESS(200,"CR007","크리에이터 리뷰 조회에 성공하였습니다."),

    // Payment
    PAYMENT_SUCCESS(200, "PA001","결제성공");

    private final int status;
    private final String code;
    private final String message;
}
