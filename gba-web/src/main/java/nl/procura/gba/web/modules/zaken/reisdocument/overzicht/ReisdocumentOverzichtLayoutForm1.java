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

package nl.procura.gba.web.modules.zaken.reisdocument.overzicht;

import static nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtBean1.*;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;

public class ReisdocumentOverzichtLayoutForm1 extends ReadOnlyForm {

  public ReisdocumentOverzichtLayoutForm1(ReisdocumentAanvraag a) {

    setCaption("Reisdocument");

    setOrder(REISDOCUMENT, AANVRAAGNUMMER, SIGNALERING, LENGTE, TOESTEMMING, SPOED, JEUGDTARIEF, DOCUMENTOUDERVOOGD,
        REDENNIETAANWEZIG, GELDIGHEID);

    setColumnWidths(WIDTH_130, "300px", "130px", "300px", "130px", "");

    ReisdocumentOverzichtBean1 b = new ReisdocumentOverzichtBean1();

    b.setReisdocument(a.getReisdocumentType().getOms());
    b.setAanvraagnummer(a.getAanvraagnummer().getFormatNummer());
    b.setLengte(a.getLengte().intValue() > 0 ? (a.getLengte() + " cm") : "Niet ingevuld");
    b.setToestemming(a.getToestemmingen().getOmschrijving());
    b.setSpoed(a.getSpoed().getOms());
    b.setJeugdtarief(a.isGratis() ? "Ja" : "Nee");
    b.setRedenNietAanwezig(a.getRedenAfwezig());
    b.setGeldigheid(a.getGeldigheid());
    b.setSignalering(a.getSignalering().getOms());

    setBean(b);
  }
}
