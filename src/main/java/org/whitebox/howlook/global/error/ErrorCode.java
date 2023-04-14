package org.whitebox.howlook.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Global
    INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
    INPUT_VALUE_INVALID(400, "G003", "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "G004", "입력 타입이 유효하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "G005", "request message body가 없거나, 값 타입이 올바르지 않습니다."),
    HTTP_HEADER_INVALID(400, "G006", "request header가 유효하지 않습니다."),
    ENTITY_NOT_FOUNT(500, "G007", "존재하지 않는 Entity입니다."),

    // Member
    MEMBER_NOT_FOUND(400, "M001", "존재 하지 않는 유저입니다."),
    NICKNAME_ALREADY_EXIST(400, "M002", "이미 존재하는 닉네임입니다."),
    MEMBERID_ALREADY_EXIST(400, "M003", "이미 존재하는 아이디입니다."),
    AUTHORITY_INVALID(403, "M004", "권한이 없습니다."),
    ACCOUNT_MISMATCH(401, "M005", "계정 정보가 일치하지 않습니다."),
    PASSWORD_EQUAL_WITH_OLD(400, "M006", "기존 비밀번호와 동일하게 변경할 수 없습니다."),
    LOGOUT_BY_ANOTHER(401, "M007", "다른 기기에 의해 로그아웃되었습니다."),

    // Jwt
    JWT_INVALID(401, "J001", "유효하지 않은 토큰입니다."),
    REFRESH_EXPIRED(401, "J002", "만료된 REFRESH 토큰입니다."),
    REFRESH_INVALID(401, "J003", "유효하지 않은 REFRESH 토큰입니다."),
    JWT_UNACCEPT(401,"J004","토큰이 없거나 짧습니다."),
    JWT_BADTYPE(401, "J005","Bearer 타입 토큰이 아닙니다."),
    JWT_EXPIRED(403, "J006", "만료된 토큰입니다."),
    JWT_MALFORM(403, "J007","토큰값이 올바르지 않습니다."),

    // post
    POST_NOT_FOUND(400, "F001", "존재하지 않는 게시물입니다."),
    POST_CANT_DELETE(400, "F002", "게시물 게시자만 삭제할 수 있습니다."),
    POST_LIKE_ALREADY_EXIST(400, "F003", "해당 게시물에 이미 좋아요를 누른 회원입니다."),
    SCRAP_ALREADY_EXIST(400, "F004", "이미 해당 게시물을 저장하였습니다."),
    SCRAP_NOT_FOUND(400, "F005", "아직 해당 게시물을 저장하지 않았습니다."),
    COMMENT_NOT_FOUND(400, "F006", "존재하지 않는 댓글입니다."),
    COMMENT_CANT_DELETE(400, "F007", "타인이 작성한 댓글은 삭제할 수 없습니다."),
    COMMENT_LIKE_ALREADY_EXIST(400, "F008", "해당 댓글에 이미 좋아요를 누른 회원입니다."),
    COMMENT_LIKE_NOT_FOUND(400, "F009", "해당 댓글에 좋아요를 누르지 않은 회원입니다."),
    POST_CANT_FOUND(400, "F010", "해당 정보에 부합하는 게시글을 찾을 수 없습니다."),
    POST_CANT_PROFILE(400, "F011", "타인의 사진은 프로필사진으로 등록할 수 없습니다."),
    COMMENT_CANT_MODIFY(400,"F012", "타인의 댓글을 수정할 수 없습니다."),
    POST_NOT_LIKED(400, "F013", "좋아요가 눌리지 않은 게시글입니다."),

    // HashTag
    HASHTAG_NOT_FOUND(400, "H001", "존재하지 않는 해시태그 입니다."),

    // Eval
    EVAL_ALREADY_EXSIST(400, "E001", "평가 게시글이 이미 존재합니다."),
    EVAL_REGISTER_FAIL(400, "E002", "평가 게시글 등록에 실패하였습니다."),
    EVAL_NOT_EXIST(400, "E003", "존재하지 않는 게시글입니다."),

    // Payment
    AMOUNT_NOT_EQUAL(400, "P001", "실제 결제금액과 서버에서 결제금액이 다릅니다.");

    private final int status;
    private final String code;
    private final String message;

}