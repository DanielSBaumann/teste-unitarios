package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;

import java.util.Calendar;

public class MatchersProprios {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNumaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DataDiferencaDiasMatcher hojeComDiferencaDias(Integer qtdDias) {
        return new DataDiferencaDiasMatcher(qtdDias);
    }

    public static DataDiferencaDiasMatcher hoje() {
        return new DataDiferencaDiasMatcher(0);
    }


}
