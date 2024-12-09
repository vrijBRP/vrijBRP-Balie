package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3;

import static java.util.Optional.ofNullable;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType.REGV;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType.REHG;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType.REIT;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType.RENV;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType.REVF;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType.REVG;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType.REVS;
import static nl.procura.burgerzaken.vrsclient.api.VrsMeldingType.RDO;

import java.util.Arrays;
import lombok.Getter;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;

@Getter
public enum VrsRedenDatumToelichting {
  REVG_TOEL(REVG, "Datum constatering van vermissing door de houder"),
  RENV_TOEL(RENV, "Datum verlies Nederlanderschap"),
  REGV_TOEL(REGV, "Datum van constatering dat gebruik is gemaakt van onjuiste gegevens"),
  REVS_TOEL(REVS, "Datum einde verblijfstitel, datum van ingang NL-schap of datum verkrijging buitenlands reisdocument"),
  REHG_TOEL(REHG, "Datum ingang wijziging persoonsgegevens"),
  REVF_TOEL(REVF, "Datum dat houder vermoeden van fraude heeft geconstateerd"),
  REIT_TOEL(REIT, "Datum dat van rechtswege vervallen zijn definitief is.</br>Volg eerst de <a href='#' target='_blank'>"
      + "Werkinstructie melden van vermoeden van mogelijke fraude</a>"),
  REDO_TOEL(RDO, "Datum van feitelijke onttrekking aan het verkeer"),
  ONBEKEND("");

  private VrsMeldingType      meldingType;
  private VrsMeldingRedenType redenType;
  private final String              toelichting;

  VrsRedenDatumToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  VrsRedenDatumToelichting(VrsMeldingRedenType redenType, String toelichting) {
    this.redenType = redenType;
    this.toelichting = toelichting;
  }

  VrsRedenDatumToelichting(VrsMeldingType meldingType, String toelichting) {
    this.meldingType = meldingType;
    this.toelichting = toelichting;
  }

  public static VrsRedenDatumToelichting get(VrsMeldingType meldingType, VrsMeldingRedenType redenType) {
    return Arrays.stream(values())
        .filter(value -> ofNullable(value.getMeldingType())
            .map(mType -> mType == meldingType)
            .orElse(ofNullable(value.getRedenType())
                .map(rType -> rType == redenType)
                .orElse(false)))
        .findFirst()
        .orElse(ONBEKEND);
  }
}
