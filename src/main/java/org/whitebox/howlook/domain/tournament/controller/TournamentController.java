package org.whitebox.howlook.domain.tournament.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.member.dto.UpdatePasswordRequest;
import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;
import org.whitebox.howlook.domain.tournament.service.TournamentService;
import org.whitebox.howlook.domain.tournament.task.TournamentTask;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;


@RestController
@RequestMapping("/tournament")
@Log4j2
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentTask tournamentTask;
    @ApiOperation(value = "날짜로 토너먼트 게시글 가져오기")
    @GetMapping("/post/{date}")
    public ResponseEntity<ResultResponse> getPosts(@PathVariable("date") String date){
        List<TournamentPostDTO> result = tournamentService.getPosts(LocalDate.parse(date));
        return ResponseEntity.ok(ResultResponse.of(GET_TOURNAMENT_POST_SUCCESS,result));
    }
    @ApiOperation(value = "토너먼트 결과 반영")
    @PutMapping("/result")
    public ResponseEntity<ResultResponse> resultTournament(@RequestBody List<TournamentPostDTO> result){
        tournamentService.UpdatePosts(result);
        return ResponseEntity.ok(ResultResponse.of(UPDATE_TOURNAMENT_SCORE_SUCCESS));
    }
    @ApiOperation(value = "날짜로 토너먼트 기록 조회")
    @GetMapping("/history/{date}")
    public ResponseEntity<ResultResponse> getTHistory(@PathVariable("date") String date) {
        final THistoryResponse tHistoryResponse = tournamentService.getTHistory(LocalDate.parse(date));
        List<TournamentPostDTO> postDTOs = new ArrayList<>();

        return ResponseEntity.ok(ResultResponse.of(GET_TOURNAMENT_HISTORY_SUCCESS,tHistoryResponse));
    }
    @ApiOperation(value = "날짜로 이벤트 토너먼트 기록 조회")
    @GetMapping("/event/{date}")
    public ResponseEntity<ResultResponse> getEHistory(@PathVariable("date") String date) {
        final EHistoryResponse eHistoryResponse = tournamentService.getEHistory(LocalDate.parse(date));

        return ResponseEntity.ok(ResultResponse.of(GET_TOURNAMENT_EVENT_HISTORY_SUCCESS,eHistoryResponse));
    }

    @ApiOperation(value = "id로 토너먼트 게시글 조회")
    @GetMapping("/getbyid")
    public ResponseEntity<ResultResponse> getPostById(Long postId) {
        final TournamentPostDTO tournamentPostDTO = tournamentService.getPostById(postId);

        return ResponseEntity.ok(ResultResponse.of(GET_TOURNAMENT_POST_SUCCESS,tournamentPostDTO));
    }
    @ApiOperation(value = "post Task 수동으로 수행")
    @GetMapping("/feedtopost")
    public ResponseEntity<ResultResponse> feedToPost() {
        tournamentTask.feedToTPost();
        return ResponseEntity.ok(ResultResponse.of(GET_TOURNAMENT_POST_SUCCESS));
    }
    @ApiOperation(value = "result Task 수동으로 수행")
    @GetMapping("/resulttournament")
    public ResponseEntity<ResultResponse> resultTournament() {
        tournamentTask.resultTournament();
        return ResponseEntity.ok(ResultResponse.of(GET_TOURNAMENT_POST_SUCCESS));
    }
}
