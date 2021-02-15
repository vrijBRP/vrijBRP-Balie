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

package nl.procura.tmv.actions;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.bprbzk.gba.terugmeldvoorziening.version1.InzageVerzoek;
import nl.bprbzk.gba.terugmeldvoorziening.version1.Statusoverzicht;
import nl.procura.tmv.TmvActionTemplate;

public class TmvInzageVerzoekAction extends TmvActionTemplate {

  public static String  SOORT_OVERZICHT_DOSSIER      = "D";
  public static String  SOORT_OVERZICHT_SAMENVATTING = "S";
  private static String BERICHTCODE                  = "IZVZ";
  private InzageVerzoek request                      = new InzageVerzoek();

  public void nieuw(String berichtnummer, String soortOverzicht) {

    request.setBerichtnummer(berichtnummer);
    request.setBerichtcode(BERICHTCODE);
    request.setSoortOverzicht(soortOverzicht);
  }

  public TmvInzageVerzoekAction setBehandelendeGemeente(Integer s) {

    if (pos(s)) {
      request.setBehandelendeGemeente(s);
    }

    return this;
  }

  public TmvInzageVerzoekAction setDatumVanaf(Integer s) {

    if (pos(s)) {
      request.setDatumVanaf(s);
    }

    return this;
  }

  public TmvInzageVerzoekAction setDatumTot(Integer s) {

    if (pos(s)) {
      request.setDatumTot(s);
    }

    return this;
  }

  public TmvInzageVerzoekAction setDossiernummer(long s) {

    if (pos(s)) {
      request.setDossiernummer(s);
    }

    return this;
  }

  public TmvInzageVerzoekAction setBerichtnummer(String s) {

    if (fil(s)) {
      request.setBerichtnummer(s);
    }

    return this;
  }

  public TmvInzageVerzoekAction setAnr(long s) {

    if (pos(s)) {
      request.setAnummer(s);
    }

    return this;
  }

  public TmvInzageVerzoekAction setBsn(long s) {

    if (pos(s)) {
      request.setBsn(s);
    }

    return this;
  }

  public TmvInzageVerzoekAction setStatusDossier(int s) {

    if (pos(s)) {
      request.setStatusDossier(s);
    }

    return this;
  }

  @Override
  public Statusoverzicht getResponseObject() {
    return (Statusoverzicht) super.getResponseObject();
  }

  @Override
  public void send() {

    setRequestObject(request);

    super.send();
  }
}
