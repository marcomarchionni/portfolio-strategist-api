package com.marcomarchionni.ibportfolio.model.mapping;

import com.marcomarchionni.ibportfolio.model.domain.Trade;
import com.marcomarchionni.ibportfolio.model.dtos.response.TradeListDto;

public interface TradeMapper {

    TradeListDto toTradeListDto(Trade trade);
}
