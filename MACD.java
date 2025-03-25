package com.hy.stock.calculate;

import java.util.ArrayList;
import java.util.List;






/**
 * 技术指标：MACD的算法  
 */
public final class MACD
{


    /** MACD初始化值。当小于等于此值时，MACD指标无效 */
    public  static final double $MACD_InitValue = -999999;
    
    /** 日期 */
    private String date;
    
    /** 快速移动平均值：12日EMA */
    private double   ema12;
    
    /** 慢速移动平均值：26日EMA */
    private double   ema26;
    
    /** 快速线：差离值(DIF) */
    private double   dif;
    
    /** 慢速线：离差平均值：9日移动平均值：9日EMA */
    private double   dea;
    
    /** MACD柱 */
    private double   bar;
    

    
    
    
    public MACD()
    {
        this("",$MACD_InitValue ,$MACD_InitValue ,$MACD_InitValue ,$MACD_InitValue ,$MACD_InitValue);
    }
    
    
    
    public MACD(String i_date,double i_EMA12 ,double i_EMA26 ,double i_DIF ,double i_DEA ,double i_Bar)
    {
        this.date  = i_date;
        this.ema12 = i_EMA12;
        this.ema26 = i_EMA26;
        this.dif   = i_DIF;
        this.dea   = i_DEA;
        this.bar   = i_Bar;
    }
    
    
    
    /**
     * 计算MACD（整体返回）（返回值保留四位小数）
     * 
     * @param i_date
     * @param i_NewPrice         今日收盘价
     * @param i_YesterdayEMA12   前一日快速移动平均值EMA(12)
     * @param i_YesterdayEMA26   前一日慢速移动平均值EMA(26)
     * @param i_YesterdayDEA     前一日离差平均值DEA
     * @return
     */
    public static MACD calcMACD(String i_date,
                                double i_NewPrice 
                               ,double i_YesterdayEMA12 
                               ,double i_YesterdayEMA26
                               ,double i_YesterdayDEA)
    {
        double v_EMA12 = calcEMA12(i_NewPrice ,i_YesterdayEMA12);
        double v_EMA26 = calcEMA26(i_NewPrice ,i_YesterdayEMA26);
        double v_DIF   = calcDIF(  v_EMA12    ,v_EMA26);
        double v_DEA   = calcDEA(  v_DIF      ,i_YesterdayDEA);
        double v_Bar   = calcBar(  v_DIF ,v_DEA);
        
        return new MACD(i_date
                       ,round(v_EMA12 ,4) 
                       ,round(v_EMA26 ,4)
                       ,round(v_DIF   ,4)
                       ,round(v_DEA   ,4)
                       ,round(v_Bar   ,4));
    }
    
    
    
    public static void main(String [] i_Args)
    {   
        List<Pair<String,Double>> v_PriceList = new ArrayList<Pair<String,Double>>();
        v_PriceList.add(new Pair<String,Double>("1997-12-31",6.97));
        v_PriceList.add(new Pair<String,Double>("1998-12-31",9.66));
        v_PriceList.add(new Pair<String,Double>("1999-12-31",10.03));
        v_PriceList.add(new Pair<String,Double>("2000-12-31",17.75));
        v_PriceList.add(new Pair<String,Double>("2001-12-31",12.25));
        v_PriceList.add(new Pair<String,Double>("2002-12-31",13.35));
        v_PriceList.add(new Pair<String,Double>("2003-12-31",9.25));
        v_PriceList.add(new Pair<String,Double>("2004-12-31",6.2));
        List<MACD> v_MACDList = calcMACD();
        ;
        v_MACDList.forEach(System.out::println);
    }
    

    public static List<MACD> calcMACD(List<Pari<String,Double>> i_PriceList)
    {
        List<MACD> v_MACDList = new ArrayList<MACD>();
        MACD v_MACD = calcMACD(i_PriceList.get(0).getKey(), i_PriceList.get(0).getValue(), i_PriceList.get(0).getValue(), i_PriceList.get(0).getValue(), i_PriceList.get(0).getValue());
        for(int i = 1;i < i_PriceList.size();i++)
        {
            v_MACD = calcMACD(i_PriceList.get(i().getKey(), i_PriceList.get(i).getValue(), v_MACD.getEma12(), v_MACD.getEma26(), v_MACD.getDea()));
            v_MACDList.add(v_MACD)
        }
        return v_MACDList;
    }  
  
    public static double round(double i_Value,int i_Digits){
        return (double)Math.round(i_Value * Math.pow(10,i_Digits)) / Math.pow(10,i_Digits);
    }
 
    
    
    
    /**
     * 快速移动平均值：12日EMA的计算
     * 
     * EMA(12) = 前一日EMA(12) * 11 / 13 + 今日收盘价 * 2 / 13
     * 
     *
     * @param i_NewPrice        今日收盘价
     * @param i_YesterdayEMA12  前一日EMA(12)
     * @return
     */
    public static double calcEMA12(double i_NewPrice ,double i_YesterdayEMA12)
    {
        return (i_YesterdayEMA12 * 11 + i_NewPrice * 2) / 13;
    }
    
    
    
    /**
     * 慢速移动平均值：26日EMA的计算
     * 
     * EMA(26) = 前一日EMA(26) * 25 / 27 + 今日收盘价 * 2 / 27
     * 
     * @param i_NewPrice        今日收盘价
     * @param i_YesterdayEMA26  前一日EMA(26)
     * @return
     */
    public static double calcEMA26(double i_NewPrice ,double i_YesterdayEMA26)
    {
        return (i_YesterdayEMA26 * 25 + i_NewPrice * 2) / 27;
    }
    
    
    
    /**
     * 快速线：差离值(DIF)的计算
     * 
     *
     * @param i_EMA12   12日EMA
     * @param i_EMA26   26日EMA
     * @return
     */
    public static double calcDIF(double i_EMA12 ,double i_EMA26)
    {
        return i_EMA12 - i_EMA26;
    }
    
    
    
    /**
     * 慢速线：9日移动平均值：9日EMA的计算
     * 
     * 据差离值计算其9日的EMA，即离差平均值。为了不与指标原名相混淆，此值又名DEA或DEM。
     * 
     * 今日DEA = 前一日DEA * 8 / 10 + 今日DIF * 2 / 10
     * 
     * @param i_DIF            今日DIF
     * @param i_YesterdayDEA   前一日DEA
     * @return
     */
    public static double calcDEA(double i_DIF ,double i_YesterdayDEA)
    {
        return i_YesterdayDEA * 8 / 10 + i_DIF * 2 / 10;
    }
    
    
    
    /**
     * 计算MACD柱状图(线)
     * 
     * (DIF - DEA) * 2
     * 
     *
     * @param i_DIF   差离值
     * @param i_DEA   离差平均值
     * @return
     */
    public static double calcBar(double i_DIF ,double i_DEA)
    {
        return (i_DIF - i_DEA) * 2;
    }


    
    /**
     * 获取：快速移动平均值：12日EMA
     */
    public double getEma12()
    {
        return ema12;
    }


    
    /**
     * 设置：快速移动平均值：12日EMA
     * 
     * @param ema12 
     */
    public void setEma12(double ema12)
    {
        this.ema12 = ema12;
    }


    
    /**
     * 获取：慢速移动平均值：26日EMA
     */
    public double getEma26()
    {
        return ema26;
    }


    
    /**
     * 设置：慢速移动平均值：26日EMA
     * 
     * @param ema26 
     */
    public void setEma26(double ema26)
    {
        this.ema26 = ema26;
    }


    
    /**
     * 获取：快速线：差离值(DIF)
     */
    public double getDif()
    {
        return dif;
    }



    /**
     * 设置：差离值(DIF)
     * 
     * @param dif 
     */
    public void setDif(double dif)
    {
        this.dif = dif;
    }


    
    /**
     * 获取：慢速线：离差平均值：9日移动平均值：9日EMA
     */
    public double getDea()
    {
        return dea;
    }


    
    /**
     * 设置：离差平均值：9日移动平均值：9日EMA
     * 
     * @param dea 
     */
    public void setDea(double dea)
    {
        this.dea = dea;
    }


    
    /**
     * 获取：MACD柱
     */
    public double getBar()
    {
        return bar;
    }


    
    /**
     * 设置：MACD柱
     * 
     * @param bar 
     */
    public void setBar(double bar)
    {
        this.bar = bar;
    }


    
}
