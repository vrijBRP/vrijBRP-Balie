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

import static nl.procura.standard.Globalfunctions.pos;

import nl.bprbzk.gba.terugmeldvoorziening.version1.ArrayOfTerugmeldingDetail;
import nl.bprbzk.gba.terugmeldvoorziening.version1.Ontvangstbevestiging;
import nl.bprbzk.gba.terugmeldvoorziening.version1.Terugmelding;
import nl.bprbzk.gba.terugmeldvoorziening.version1.TerugmeldingDetail;
import nl.procura.tmv.TmvActionTemplate;

public class TmvRegistratieAction extends TmvActionTemplate {

  private static final String             BERICHTCODE = "TMLD";
  private final Terugmelding              request     = new Terugmelding();
  private final ArrayOfTerugmeldingDetail l           = new ArrayOfTerugmeldingDetail();

  public void nieuw(String berichtnummer, String melderId, long bsn, long anr) {

    request.setBerichtnummer(berichtnummer);
    request.setMelderId(melderId);
    request.setBerichtcode(BERICHTCODE);

    if (pos(bsn)) {
      request.setBsn(bsn);
    }

    if (pos(anr)) {
      request.setAnummer(anr);
    }

    request.setDetails(l);
  }

  @Override
  public Ontvangstbevestiging getResponseObject() {
    return (Ontvangstbevestiging) super.getResponseObject();
  }

  public TmvRegistratieAction addDetail(long cat, long elem, String orgValue, String newValue) {

    TerugmeldingDetail d = new TerugmeldingDetail();

    d.setRubrieknummer(Integer.valueOf(String.format("%d%04d", cat, elem)));
    d.setOrigineleWaardeGBAV(orgValue);
    d.setVoorgesteldeWaarde(newValue);
    l.getTerugmeldingDetail().add(d);

    return this;
  }

  public TmvRegistratieAction addDetail(Integer rubr, String orgValue, String newValue) {

    TerugmeldingDetail d = new TerugmeldingDetail();

    d.setRubrieknummer(rubr);
    d.setOrigineleWaardeGBAV(orgValue);
    d.setVoorgesteldeWaarde(newValue);
    l.getTerugmeldingDetail().add(d);

    return this;
  }

  public TmvRegistratieAction setToelichting(String toelichting) {

    request.setToelichting(toelichting);
    return this;
  }

  @Override
  public void send() {

    setRequestObject(request);

    super.send();
  }
}
