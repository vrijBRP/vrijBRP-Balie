/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.diensten.gba.ple.extensions.formats;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.standard.ProcuraDate;

public class Geboorte {

  private final BasePLElem geboortedatum;
  private final BasePLElem geboorteplaats;
  private final BasePLElem geboorteland;
  private final BasePLElem overlijdensdatum;

  public Geboorte(BasePLRec r) {
    this(r, null);
  }

  public Geboorte(BasePLRec r, BasePLRec or) {
    this(r.getElem(GEBOORTEDATUM),
        r.getElem(GEBOORTEPLAATS),
        r.getElem(GEBOORTELAND),
        (or != null ? or.getElem(DATUM_OVERL) : null));
  }

  public Geboorte(BasePLElem dGeb, BasePLElem pGeb, BasePLElem lGeb) {
    this(dGeb, pGeb, lGeb, null);
  }

  public Geboorte(BasePLElem dGeb, BasePLElem pGeb, BasePLElem lGeb, BasePLElem dOverl) {
    this.geboortedatum = dGeb;
    this.geboorteplaats = pGeb;
    this.geboorteland = lGeb;
    this.overlijdensdatum = dOverl;
  }

  private static int getLeeftijd(String dGeb, String dOverl) {
    if (!pos(dGeb.replaceAll("\\D", ""))) {
      return 0;
    }

    int d_in = aval(new ProcuraDate(dGeb).setAllowedFormatExceptions(true)
        .setForceFormatType(dGeb.contains("-") ? ProcuraDate.FORMATDATE_ONLY : ProcuraDate.SYSTEMDATE_ONLY)
        .getSystemDate());

    int d_sys = aval(pos(dOverl) ? new ProcuraDate(dOverl).getSystemDate() : new ProcuraDate().getSystemDate());
    return (d_sys - d_in) / 10000;
  }

  public String getDatum() {
    return trim(getOms(geboortedatum));
  }

  public String getDatumPlaats() {
    return trim(getOms(geboortedatum) + " " + getOms(geboorteplaats));
  }

  public String getDatumTePlaats() {
    return trim(getOms(geboortedatum) + " te " + getOms(geboorteplaats));
  }

  public String getDatumPlaatsLand() {
    return trimM("{0} {1} {2}", getOms(geboortedatum), getOms(geboorteplaats), getGeboorteLand());
  }

  public String getDatumLeeftijdPlaatsLand() {
    return trimM("{0} {1} {2}", getDatumLeeftijd(), getOms(geboorteplaats), getGeboorteLand());
  }

  public String getDatumTePlaatsLand() {
    return trimM("{0} te {1} {2}", getOms(geboortedatum), getOms(geboorteplaats), getGeboorteLand());
  }

  public String getDatumLeeftijd() {
    if (geboortedatum.isAllowed() && overlijdensdatum != null) {
      return getDatum() + " (" + getLeeftijd() + ")";
    }

    return getDatum();
  }

  public int getLeeftijd() {
    return aval(getLeeftijd(getCode(geboortedatum), overlijdensdatum != null ? getCode(overlijdensdatum) : null));
  }

  public BasePLValue getGeboortedatum() {
    return geboortedatum.getValue();
  }

  public BasePLValue getGeboorteland() {
    return geboorteland.getValue();
  }

  public BasePLValue getGeboorteplaats() {
    return geboorteplaats.getValue();
  }

  private String getOms(BasePLElem element) {
    return element.getValue().getDescr();
  }

  private String getCode(BasePLElem element) {
    return element.getValue().getCode();
  }

  private String trimM(String pattern, Object... arguments) {
    return trim(MessageFormat.format(pattern, arguments));
  }

  private String getGeboorteLand() {
    return (fil(getOms(geboorteland)) && !eq(getOms(geboorteland).toLowerCase(), "nederland")) ? "(" + getOms(
        geboorteland) + ")" : "";
  }
}
