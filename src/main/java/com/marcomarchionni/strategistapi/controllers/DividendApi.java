package com.marcomarchionni.strategistapi.controllers;

import com.marcomarchionni.strategistapi.dtos.request.DividendFind;
import com.marcomarchionni.strategistapi.dtos.request.UpdateStrategyDto;
import com.marcomarchionni.strategistapi.dtos.response.DividendSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "7. Dividends", description = "List dividends or assign them to a strategy")
@RequestMapping("/dividends")
@SecurityRequirement(name = "bearerAuth")
public interface DividendApi {
    @GetMapping
    @Operation(summary = "Find dividends by filter")
    List<DividendSummary> findByFilter(@Valid DividendFind dividendFind);

    @PutMapping
    @Operation(summary = "Assign dividend to a strategy")
    DividendSummary updateStrategyId(@RequestBody @Valid UpdateStrategyDto dividendUpdate);
}
