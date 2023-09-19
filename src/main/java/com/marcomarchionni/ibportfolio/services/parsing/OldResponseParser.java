package com.marcomarchionni.ibportfolio.services.parsing;

import com.marcomarchionni.ibportfolio.model.domain.Dividend;
import com.marcomarchionni.ibportfolio.model.domain.FlexStatement;
import com.marcomarchionni.ibportfolio.model.domain.Position;
import com.marcomarchionni.ibportfolio.model.domain.Trade;
import com.marcomarchionni.ibportfolio.model.dtos.flex.FlexQueryResponseDto;

import java.util.List;


public interface OldResponseParser {

    FlexStatement parseFlexStatement(FlexQueryResponseDto dto);

    List<Trade> parseTrades(FlexQueryResponseDto dto);

    List<Position> parsePositions(FlexQueryResponseDto dto);

    List<Dividend> parseClosedDividends(FlexQueryResponseDto dto);

    List<Dividend> parseOpenDividends(FlexQueryResponseDto dto);
}