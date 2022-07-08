package com.marcomarchionni.ibportfolio.update;

import com.marcomarchionni.ibportfolio.models.*;
import com.marcomarchionni.ibportfolio.models.dtos.FlexQueryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class ResponseParserImpl implements ResponseParser {

    static private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
    static private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd;hhmmss", Locale.ENGLISH);

    @Override
    public FlexStatement parseFlexStatement(FlexQueryResponseDto dto) {

        FlexQueryResponseDto.FlexStatement flexDto = dto.getFlexStatements().get(0).getFlexStatement().get(0);
        FlexStatement flexStatement = new FlexStatement();

        if (StringUtils.hasText(flexDto.getAccountId())) {
            flexStatement.setAccountId(flexDto.getAccountId());
        }
        if (StringUtils.hasText(flexDto.getFromDate())) {
            flexStatement.setFromDate(LocalDate.parse(flexDto.getFromDate(), dateFormatter));
        }
        if (StringUtils.hasText(flexDto.getToDate())) {
            flexStatement.setToDate(LocalDate.parse(flexDto.getToDate(), dateFormatter));
        }
        if (StringUtils.hasText(flexDto.getPeriod())) {
            flexStatement.setPeriod(flexDto.getPeriod());
        }
        if (StringUtils.hasText(flexDto.getWhenGenerated())) {
            flexStatement.setWhenGenerated(LocalDate.parse(flexDto.getWhenGenerated(), dateTimeFormatter));
        }
        log.info("Parsed 1 flex statement");

        return flexStatement;
    }

    /**
     * extract trades from dto
     * @param dto map of FlexQueryResponse
     * @return list of trades
     */

    //TODO update Dto and parse ORDER instead of TRADES and use ibOrder to generate Id
    @Override
    public List<Trade> parseTrades(FlexQueryResponseDto dto) {

        List<Trade> trades = new ArrayList<>();
        List<FlexQueryResponseDto.Trade> tradesDto =
                dto.getFlexStatements().get(0).getFlexStatement().get(0).getTrades().getTrade();

        for (FlexQueryResponseDto.Trade t : tradesDto) {
            Trade trade = new Trade();
            if (StringUtils.hasText(t.getTransactionID())) {
                trade.setId(Long.parseLong(t.getTransactionID()));
            }
            if (StringUtils.hasText(t.getTradeID())) {
                trade.setTradeId(Long.parseLong(t.getTradeID()));
            }
            if (StringUtils.hasText(t.getConid())) {
                trade.setConId(Long.parseLong(t.getConid()));
            }
            if (StringUtils.hasText(t.getTradeDate())) {
                trade.setTradeDate(LocalDate.parse(t.getTradeDate(), dateFormatter));
            }
            if (StringUtils.hasText(t.getDescription())) {
                trade.setDescription(t.getDescription());
            }
            if (StringUtils.hasText(t.getAssetCategory())) {
                trade.setAssetCategory(t.getAssetCategory());
            }
            if (StringUtils.hasText(t.getSymbol())) {
                trade.setSymbol(t.getSymbol());
            }
            if (StringUtils.hasText(t.getPutCall())) {
                trade.setPutCall(t.getPutCall());
            }
            if (StringUtils.hasText(t.getStrike())) {
                trade.setStrike(new BigDecimal(t.getStrike()));
            }
            if (StringUtils.hasText(t.getExpiry())) {
                trade.setExpiry(LocalDate.parse(t.getExpiry(), dateFormatter));
            }
            if (StringUtils.hasText(t.getMultiplier())) {
                trade.setMultiplier(Integer.parseInt(t.getMultiplier()));
            }
            if (StringUtils.hasText(t.getBuySell())) {
                trade.setBuySell(t.getBuySell());
            }
            if (StringUtils.hasText(t.getQuantity())) {
                trade.setQuantity(new BigDecimal(t.getQuantity()));
            }
            if (StringUtils.hasText(t.getTradePrice())) {
                trade.setTradePrice(new BigDecimal(t.getTradePrice()));
            }
            if (StringUtils.hasText(t.getTradeMoney())) {
                trade.setTradeMoney(new BigDecimal(t.getTradeMoney()));
            }
            if (StringUtils.hasText(t.getFifoPnlRealized())) {
                trade.setFifoPnlRealized(new BigDecimal(t.getFifoPnlRealized()));
            }
            if (StringUtils.hasText(t.getIbCommission())) {
                trade.setIbCommission(new BigDecimal(t.getIbCommission()));
            }
            trades.add(trade);
        }
        log.info("Parsed " + trades.size() + " trade(s)");

        return trades;
    }

    /**
     * extract positions from dto
     * @param dto map of FlexQueryResponse
     * @return list of positions
     */
    @Override
    public List<Position> parsePositions(FlexQueryResponseDto dto) {

        List<Position> positions = new ArrayList<>();
        List<FlexQueryResponseDto.OpenPosition> positionsDto =
                dto.getFlexStatements().get(0).getFlexStatement().get(0).getOpenPositions().getOpenPosition();

        for (FlexQueryResponseDto.OpenPosition p : positionsDto) {
            Position position = new Position();
            if (StringUtils.hasText(p.getConid())) {
                position.setId(Long.parseLong(p.getConid()));
            }
            if (StringUtils.hasText(p.getConid())) {
                position.setConId(Long.parseLong(p.getConid()));
            }
            if (StringUtils.hasText(p.getReportDate())) {
                position.setReportDate(LocalDate.parse(p.getReportDate(), dateFormatter));
            }
            if (StringUtils.hasText(p.getSymbol())) {
                position.setSymbol(p.getSymbol());
            }
            if (StringUtils.hasText(p.getDescription())) {
                position.setDescription(p.getDescription());
            }
            if (StringUtils.hasText(p.getAssetCategory())) {
                position.setAssetCategory(p.getAssetCategory());
            }
            if (StringUtils.hasText(p.getPutCall())) {
                position.setPutCall(p.getPutCall());
            }
            if(StringUtils.hasText(p.getStrike())) {
                position.setStrike(new BigDecimal(p.getStrike()));
            }
            if(StringUtils.hasText(p.getExpiry())) {
                position.setExpiry(LocalDate.parse(p.getExpiry(), dateFormatter));
            }
            if (StringUtils.hasText(p.getPosition())) {
                position.setQuantity(new BigDecimal(p.getPosition()));
            }
            if (StringUtils.hasText(p.getCostBasisPrice())) {
                position.setCostBasisPrice(new BigDecimal(p.getCostBasisPrice()));
            }
            if (StringUtils.hasText(p.getMarkPrice())) {
                position.setMarketPrice(new BigDecimal(p.getMarkPrice()));
            }
            if (StringUtils.hasText(p.getMultiplier())) {
                position.setMultiplier(Integer.parseInt(p.getMultiplier()));
            }
            positions.add(position);
        }
        log.info("Parsed " + positions.size() + " position(s)");

        return positions;
    }

    /**
     * extract dividends and open dividends from dto
     * @param dto map of FlexQueryResponse
     * @return list of dividends
     */
    @Override
    public List<Dividend> parseDividends(FlexQueryResponseDto dto) {

        List<Dividend> dividends = new ArrayList<>();
        List<FlexQueryResponseDto.ChangeInDividendAccrual> dividendsDto =
                dto.getFlexStatements().get(0).getFlexStatement().get(0).getChangeInDividendAccruals().getChangeInDividendAccrual();

        // TODO: change to levelOfDetail=SUMMARY and ignore code
        for (FlexQueryResponseDto.ChangeInDividendAccrual d : dividendsDto) {
            if (d.getCode().equalsIgnoreCase("Re") && d.getDate().equalsIgnoreCase(d.getPayDate())) {
                Dividend dividend = new Dividend();
                if (StringUtils.hasText(d.getConid())) {
                    dividend.setConId(Long.parseLong(d.getConid()));
                }
                if (StringUtils.hasText(d.getSymbol())) {
                    dividend.setSymbol(d.getSymbol());
                }
                if (StringUtils.hasText(d.getDescription())) {
                    dividend.setDescription(d.getDescription());
                }
                if (StringUtils.hasText(d.getExDate())) {
                    dividend.setExDate(LocalDate.parse(d.getExDate(), dateFormatter));
                }
                if (StringUtils.hasText(d.getPayDate())) {
                    dividend.setPayDate(LocalDate.parse(d.getPayDate(), dateFormatter));
                }
                if (StringUtils.hasText(d.getGrossRate())) {
                    dividend.setGrossRate(new BigDecimal(d.getGrossRate()));
                }
                if (StringUtils.hasText(d.getQuantity())) {
                    dividend.setQuantity(new BigDecimal(d.getQuantity()));
                }
                if (StringUtils.hasText(d.getGrossAmount())) {
                    dividend.setGrossAmount(new BigDecimal(d.getGrossAmount()).abs());
                }
                if (StringUtils.hasText(d.getTax())) {
                    dividend.setTax(new BigDecimal(d.getTax()).abs());
                }
                if (StringUtils.hasText(d.getNetAmount())) {
                    dividend.setNetAmount(new BigDecimal(d.getNetAmount()).abs());
                }
                dividend.setId(Long.parseLong(d.getConid() + d.getExDate()));
                dividend.setOpenClosed("CLOSED");
                dividends.add(dividend);
            }

        }
        log.info("Parsed " + dividends.size() + " dividend(s)");
        return dividends;
    }

    /**
     * extract open dividends from dto
     * @param dto map of FlexQueryResponse
     * @return list of open dividends
     */
    @Override
    public List<Dividend> parseOpenDividends(FlexQueryResponseDto dto) {

        List<Dividend> openDividends = new ArrayList<>();
        List<FlexQueryResponseDto.OpenDividendAccrual> openDividendsDto =
                dto.getFlexStatements().get(0).getFlexStatement().get(0).getOpenDividendAccruals().getOpenDividendAccrual();

        for (FlexQueryResponseDto.OpenDividendAccrual od : openDividendsDto) {
            Dividend openDividend = new Dividend();
            if (StringUtils.hasText(od.getConid())) {
                openDividend.setConId(Long.parseLong(od.getConid()));
            }
            if (StringUtils.hasText(od.getSymbol())) {
                openDividend.setSymbol(od.getSymbol());
            }
            if (StringUtils.hasText(od.getDescription())) {
                openDividend.setDescription(od.getDescription());
            }
            if (StringUtils.hasText(od.getExDate())) {
                openDividend.setExDate(LocalDate.parse(od.getExDate(), dateFormatter));
            }
            if (StringUtils.hasText(od.getPayDate())) {
                openDividend.setPayDate(LocalDate.parse(od.getPayDate(), dateFormatter));
            }
            if (StringUtils.hasText(od.getGrossRate())) {
                openDividend.setGrossRate(new BigDecimal(od.getGrossRate()));
            }
            if (StringUtils.hasText(od.getQuantity())) {
                openDividend.setQuantity(new BigDecimal(od.getQuantity()));
            }
            if (StringUtils.hasText(od.getGrossAmount())) {
                openDividend.setGrossAmount(new BigDecimal(od.getGrossAmount()));
            }
            if (StringUtils.hasText(od.getTax())) {
                openDividend.setTax(new BigDecimal(od.getTax()));
            }
            if (StringUtils.hasText(od.getNetAmount())) {
                openDividend.setNetAmount(new BigDecimal(od.getNetAmount()));
            }
            openDividend.setId(Long.parseLong(od.getConid() + od.getExDate()));
            openDividend.setOpenClosed("OPEN");
            openDividends.add(openDividend);
        }
        log.info("Parsed " + openDividendsDto.size() + " open dividend(s)");
        return openDividends;
    }
}
