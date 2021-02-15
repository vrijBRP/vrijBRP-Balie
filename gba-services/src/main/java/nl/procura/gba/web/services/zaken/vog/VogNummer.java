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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;

public class VogNummer {

  private long codeGemeente  = 0;
  private long codeLocatie   = 0;
  private long volgnummer    = 0;
  private long datumAanvraag = 0;

  public VogNummer() {
  }

  public long getCodeGemeente() {
    return codeGemeente;
  }

  public void setCodeGemeente(long codeGemeente) {
    this.codeGemeente = along(defaultNul(astr(codeGemeente)));
  }

  public long getCodeLocatie() {
    return codeLocatie;
  }

  public void setCodeLocatie(long codeLocatie) {
    this.codeLocatie = along(defaultNul(astr(codeLocatie)));
  }

  public String getCOVOGNummer() {

    return pos(getVolgnummer()) ? String.format("%04d%02d%08d%04d", getCodeGemeente(), getCodeLocatie(),
        getDatumAanvraag(), getVolgnummer()) : "";
  }

  public String getCOVOGNummerFormatted() {

    String s = getCOVOGNummer();

    return fil(s) ? MessageFormat.format("{0}-{1}-{2}-{3}", s.substring(0, 4), s.substring(4, 6),
        s.substring(6, 14), s.substring(14, 18)) : "";
  }

  public long getDatumAanvraag() {
    return datumAanvraag;
  }

  public void setDatumAanvraag(long datumAanvraag) {
    this.datumAanvraag = along(defaultNul(astr(datumAanvraag)));
  }

  public String getDatumAanvraagString() {
    return date2str(astr(getDatumAanvraag()));
  }

  public long getVolgnummer() {
    return volgnummer;
  }

  public void setVolgnummer(long volgnummer) {
    this.volgnummer = along(defaultNul(astr(volgnummer)));
  }
}
