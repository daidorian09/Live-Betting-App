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

    public static final String TOTAL_INVESTMENT_LIMIT_EXCEEDED_MESSAGE = "Toplam yatırım, izin verilen maksimum limiti (%.2f TL) aşıyor.";
    public static final String MULTIPLIER_LIMIT_EXCEEDED_MESSAGE = "Çarpan, izin verilen maksimum değeri (%d) aşıyor.";
    public static final String EVENT_NOT_FOUND_MESSAGE = "Etkinlik bulunamadı (ID: %d).";
    public static final String BET_RATE_CHANGED_MESSAGE = "Bahis oranı değişti. Güncel oran: %.2f";
    public static final String BETSLIP_TIMEOUT_MESSAGE = "Bahis kuponu oluşturma süresi %d ms’yi aştı.";

    public static final String UNAUTHORIZED_CUSTOMER_MESSAGE = "Müşteri yetkilendirilmemiş.";
    public static final String BET_RATE_ON_CHANGE_ERROR_MESSAGE = "Oran güncellenirken beklenmeyen bir hata oluştu.";
}
