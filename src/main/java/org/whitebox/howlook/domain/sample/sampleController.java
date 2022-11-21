package org.whitebox.howlook.domain.sample;

import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/sample")
public class sampleController {
    @ApiOperation("Sample GET doA")
    @GetMapping("/doA")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public List<String> doA(){
        return Arrays.asList("AAA","BBB","CCC");
    }
    @GetMapping("/doB")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<String> doB(){
        return Arrays.asList("ADMINAAA","ADMINBBB","ADMINCCC");
    }
}
