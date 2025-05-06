package com.casestudy.BetCaseStudy.application.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessageConstant {
    public static final String LEAGUE_REQUIRED = "Lig adı boş olamaz.";
    public static final String HOME_TEAM_REQUIRED = "Ev sahibi takım adı boş olamaz.";
    public static final String AWAY_TEAM_REQUIRED = "Deplasman takım adı boş olamaz.";
    public static final String RATES_REQUIRED = "Bahis oranları boş olamaz.";
    public static final String RATES_MUST_BE_POSITIVE = "Bahis oranları sıfırdan büyük olmalıdır.";
    public static final String START_TIME_INVALID = "Maç başlama zamanı geçerli ve ileri bir tarih olmalıdır.";
    public static final String EVENT_ALREADY_EXISTS = "Maç için daha öncesinde kayıt işlemi yapılmıştır";
}
