package org.whitebox.howlook.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.post.dto.ReplyModifyDTO;
import org.whitebox.howlook.domain.post.dto.ReplyReadDTO;
import org.whitebox.howlook.domain.post.dto.ReplyRegisterDTO;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.entity.Reply;
import org.whitebox.howlook.domain.post.entity.ReplyLike;
import org.whitebox.howlook.domain.post.repository.PostRepository;
import org.whitebox.howlook.domain.post.repository.ReplyLikeRepository;
import org.whitebox.howlook.domain.post.repository.ReplyRepository;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.EntityAlreadyExistException;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final AccountUtil accountUtil;
    private final ModelMapper modelMapper;
    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    @Override
    public long register_reply(ReplyRegisterDTO replyRegisterDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Reply reply =  modelMapper.map(replyRegisterDTO,Reply.class);
        Member member = accountUtil.getLoginMember();

        Post post = postRepository.findById(replyRegisterDTO.getPostId()).orElseThrow(()
                -> new EntityNotFoundException(ErrorCode.POST_CANT_FOUND));

        Long registerDTO_ParentId = replyRegisterDTO.getParentId();

        if(!replyRepository.findById(registerDTO_ParentId).isPresent() && registerDTO_ParentId != 0) { // 대댓글 예외처리
            throw new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }

        post.UpCommentCount();

        reply.setMember(member);
        reply.setPost(post);
        reply.setParentId(replyRegisterDTO.getParentId());
        reply.setLikeCount(0L);
        long ReplyId = replyRepository.save(reply).getReplyId();
        postRepository.save(post);
        return ReplyId;
    }

    @Override
    public ReplyReadDTO read(Long ReplyId) {
        Optional<Reply> replyOptional = replyRepository.findById(ReplyId);
        Reply reply = replyOptional.orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
        Member member = accountUtil.getLoginMember();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ReplyReadDTO dto = modelMapper.map(reply, ReplyReadDTO.class);

        if(replyLikeRepository.findByMemberAndReply(member,reply).isPresent()) {
            dto.setLikeCheck(true);
        }

        else {
            dto.setLikeCheck(false);
        }

        dto.setPostId(reply.getPost().getPostId());
        dto.setNickName(reply.getMember().getNickName());
        dto.setProfilePhoto(reply.getMember().getProfilePhoto());
        dto.setLikeCount(reply.getLikeCount());

        return dto;
    }

    @Override
    @Transactional
    public void remove(Long ReplyId) {
        Reply reply = replyRepository.findById(ReplyId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
        Member member = accountUtil.getLoginMember();

        if(reply.getMember().getMemberId() != member.getMemberId()) {
            throw new EntityNotFoundException(ErrorCode.COMMENT_CANT_DELETE);
        }

        Post post = postRepository.findById(reply.getPost().getPostId()).orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
        post.DownCommentCount();


        if(reply.getLikeCount() != 0) { // 댓글에 있는 모든 좋아요 삭제
            List<ReplyLike> replyLikes = replyLikeRepository.findByReplyId(ReplyId);
            log.info(replyLikes);
            for(ReplyLike replyLike : replyLikes) {
                log.info(replyLike);
                replyLikeRepository.delete(replyLike);
            }
        }

        postRepository.save(post);
        replyRepository.delete(reply);
    }

    @Transactional
    @Override
    public void modify(ReplyModifyDTO replyModifyDTO, Long replyId) {

        Optional<Reply> replyOptional = replyRepository.findById(replyId);

        Reply reply = replyOptional.orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        if(accountUtil.getLoginMember() == reply.getMember()) {
            reply.changeText(replyModifyDTO.getContent());
            replyRepository.save(reply);
        }

        else {
            throw new EntityAlreadyExistException(ErrorCode.COMMENT_CANT_MODIFY);
        }
    }

    @Override // 게시글에 해당하는 댓글 읽어오기.
    public List<ReplyReadDTO> getListOfPost(Long postId) {
        List<Reply> replies = replyRepository.listOfpost(postId);
        Member member = accountUtil.getLoginMember();
        List<ReplyReadDTO> result = replies.stream().map(reply -> new ReplyReadDTO(reply)).collect(Collectors.toList());


       for(ReplyReadDTO replyReadDTO: result) {
            if(replyLikeRepository.findByMemberIdAndReplyId(member.getMemberId(), replyReadDTO.getReplyId()).isPresent()) {
                replyReadDTO.setLikeCheck(true);
            }
       }
        return result;
    }

    @Override // 댓글 페이지 처리
    public Page<ReplyReadDTO> getReplyPage(Long postId, int page, int size) {
        final Pageable pageable = PageRequest.of(page,size);
        Member member = accountUtil.getLoginMember();

        Post post = postRepository.findByPostId(postId);
        Page<ReplyReadDTO> DTOPages = replyRepository.findAllByPost(pageable,post);

        DTOPages.forEach(replyReadDTO -> {
            if(replyLikeRepository.findByMemberIdAndReplyId(member.getMemberId(), replyReadDTO.getReplyId()).isPresent()) {
                replyReadDTO.setLikeCheck(true);
            }
        });

        return DTOPages;
    }

    @Transactional
    @Override
    public void likeReply(Long ReplyId) { // 댓글 좋아요
        Optional<Reply> replyOptional = replyRepository.findById(ReplyId);

        Reply reply = replyOptional.orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        Member member = accountUtil.getLoginMember();

        if(replyLikeRepository.findByMemberAndReply(member,reply).isPresent())
            throw new EntityAlreadyExistException(ErrorCode.COMMENT_LIKE_ALREADY_EXIST);

        else {
            reply.increaseLikeCount();
            replyLikeRepository.save(new ReplyLike(member, reply));
        }
    }

    @Transactional
    @Override
    public void unlikeReply(Long ReplyId) { // 좋아요 취소
        Optional<Reply> replyOptional = replyRepository.findById(ReplyId);

        Reply reply = replyOptional.orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        Member member = accountUtil.getLoginMember();

        Optional<ReplyLike> ReplyLikeOptional = replyLikeRepository.findByMemberAndReply(member,reply);

        ReplyLike replyLike = ReplyLikeOptional.orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.COMMENT_LIKE_NOT_FOUND));

        reply.decreaseLikeCount();

        replyLikeRepository.delete(replyLike);
    }
}