package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;

public interface EvalSearchQuerydsl {
    public Page<EvalReaderDTO> findEvalReaderDTOPage(Pageable pageable);
}
