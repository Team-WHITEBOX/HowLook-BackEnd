package org.whitebox.howlook.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.report.dto.ReportDTO;
import org.whitebox.howlook.domain.report.entity.Report;
import org.whitebox.howlook.domain.report.repository.ReportRepository;
import org.whitebox.howlook.global.util.AccountUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportPostServiceImpl implements ReportPostService{
    private final ModelMapper modelMapper;
    private final ReportRepository reportRepository;
    private final AccountUtil accountUtil;

    public void report(ReportDTO reportDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Report report =  modelMapper.map(reportDTO, Report.class);

        report.setMemberId(accountUtil.getLoginMemberId());

        reportRepository.save(report);
    }
}
