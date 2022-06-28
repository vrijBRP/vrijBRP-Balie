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

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtBean3.*;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentStatus;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;

public class ReisdocumentOverzichtLayoutForm3 extends ReadOnlyForm<ReisdocumentOverzichtBean3> {

  private final ReisdocumentAanvraag aanvraag;
  private SignaleringResult          signalering;

  public ReisdocumentOverzichtLayoutForm3(ReisdocumentAanvraag aanvraag) {

    this.aanvraag = aanvraag;
    setCaption("Huidige status reisdocument en aanvraag");
    setOrder(DOCUMENT, AFLEVERING, AFSLUITING, CODE_RAAS);
    setColumnWidths(WIDTH_130, "");

    updateBean();
  }

  public ReisdocumentOverzichtLayoutForm3(ReisdocumentAanvraag aanvraag, SignaleringResult signalering) {
    this.aanvraag = aanvraag;
    this.signalering = signalering;

    setCaption("Huidige status reisdocument, aanvraag en signalering");
    setOrder(AFLEVERING, DOCUMENT, AFSLUITING, SIGNALERING);
    setColumnWidths(WIDTH_130, "450px", WIDTH_130, "");

    updateBean();
  }

  public void updateBean() {

    ReisdocumentOverzichtBean3 b = new ReisdocumentOverzichtBean3();

    ReisdocumentStatus status = aanvraag.getReisdocumentStatus();

    String aflevering = status.getStatusLevering().toString();
    String afsluiting = status.getStatusAfsluiting().toString();

    b.setDocument(fil(status.getNrNederlandsDocument()) ? status.getNrNederlandsDocument() : "Onbekend");

    if (pos(status.getDatumTijdLevering().getLongDate())) {
      aflevering += ", gemeld op " + status.getDatumTijdLevering();
    }

    if (pos(status.getDatumTijdAfsluiting().getLongDate())) {
      afsluiting += ", afgesloten op " + status.getDatumTijdAfsluiting();
    }

    b.setCodeRaas(astr(aanvraag.getCodeRaas()));
    b.setAflevering(aflevering);
    b.setAfsluiting(afsluiting);
    b.setSignalering(isSprakeVanSignalering()
        ? setClass("red", "Ja")
        : setClass("green", "Nee"));

    setBean(b);
  }

  public boolean isSprakeVanSignalering() {
    return signalering != null;
  }
}
