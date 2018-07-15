package com.example.kafka.kafkaexample;

import lombok.Data;

@Data
public class StockQuotationInfo {

    /*股票代码*/
    private String stockCode;
    /*股票名称*/
    private String stockName;
    /*交易时间*/
    private long tradeTime;
    /*昨日收盘价*/
    private float preClosePrice;
    /*开盘价*/
    private float openPrice;
    /*当前价，收盘时即为当日收盘价*/
    private float currentPrice;
    /*今日最高价*/
    private float highPrice;
    /*今日最低价*/
    private float lowPrice;
}
