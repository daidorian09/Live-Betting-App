package com.casestudy.BetCaseStudy.application.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessageConstant {
    public static final String LEAGUE_REQUIRED = "Lig adı boş olamaz.";
    public static final String HOME_TEAM_REQUIRED = "Ev sahibi takım adı boş olamaz.";
    public static final String AWAY_TEAM_REQUIRED = "Deplasman takım adı boş olamaz.";
    public static final String RATES_REQUIRED = "Bahis oranları boş olamaz.";
    public static final String RATES_MUST_BE_POSITIVE = "Bahis oranları sıfırdan büyük olmalıdır.";
    public static final String START_TIME_INVALID = "Maç başlama zamanı geçerli ve ileri bir tarih olmalıdır.";
    public static final String EVENT_ALREADY_EXISTS = "Maç için daha öncesinde kayıt işlemi yapılmıştır";

    public static final String TOTAL_INVESTMENT_LIMIT_EXCEEDED_MESSAGE = "Total investment exceeds maximum allowed limit of %.2f TL";
    public static final String MULTIPLIER_LIMIT_EXCEEDED_MESSAGE = "Multiplier exceeds allowed limit of %d";
    public static final String EVENT_NOT_FOUND_MESSAGE = "Event not found for id %d";
    public static final String BET_RATE_CHANGED_MESSAGE = "Bet rate has changed. Current: %.2f";
    public static final String BETSLIP_TIMEOUT_MESSAGE = "BetSlip creation exceeded timeout of %dms";

    public static final String UNAUTHORIZED_CUSTOMER_MESSAGE = "Customer is unauthorized";
}
