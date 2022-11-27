package org.whitebox.howlook.domain.tournament.controller;

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
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;


@RestController
@RequestMapping("/tournament")
@Log4j2
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;

    @GetMapping("/post/{date}")
    public ResponseEntity<ResultResponse> getPosts(@PathVariable("date") String date){
        List<TournamentPostDTO> result = tournamentService.getPosts(LocalDate.parse(date));
        return ResponseEntity.ok(ResultResponse.of(UPDATE_PASSWORD_SUCCESS,result));
    }

    @GetMapping("/history/{date}")
    public ResponseEntity<ResultResponse> getTHistory(@PathVariable("date") String date) {
        final THistoryResponse tHistoryResponse = tournamentService.getTHistory(LocalDate.parse(date));

        return ResponseEntity.ok(ResultResponse.of(UPDATE_PASSWORD_SUCCESS,tHistoryResponse));
    }

    @GetMapping("/event/{date}")
    public ResponseEntity<ResultResponse> getEHistory(@PathVariable("date") String date) {
        final EHistoryResponse eHistoryResponse = tournamentService.getEHistory(LocalDate.parse(date));

        return ResponseEntity.ok(ResultResponse.of(UPDATE_PASSWORD_SUCCESS,eHistoryResponse));
    }
}
