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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.gba.web.common.misc.GbaDatumUtils.isNieuwer;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.dto.raas.aanvraag.*;
import nl.procura.gba.common.DateTime;

public class RdmRaasComparison {

  private ReisdocumentAanvraag zaak;
  private DocAanvraagDto       raasAanvraag;
  private String               updateRemarks = "";

  private RdmRaasComparison(ReisdocumentAanvraag zaak) {
    this.zaak = zaak;
  }

  RdmRaasComparison(ReisdocumentAanvraag zaak, DocAanvraagDto raasAanvraag) {
    this(zaak);
    this.raasAanvraag = raasAanvraag;
  }

  public LeveringType getRdmDeliveryStatus() {
    return getRdmStatus().getStatusLevering();
  }

  public SluitingType getRdmClosingStatus() {
    return getRdmStatus().getStatusAfsluiting();
  }

  public DateTime getRdmDeliveryDate() {
    return getRdmStatus().getDatumTijdLevering();
  }

  public DateTime getRdmClosingDate() {
    return getRdmStatus().getDatumTijdAfsluiting();
  }

  public String getRdwDocumentNr() {
    return astr(getRdmStatus().getNrNederlandsDocument());
  }

  public boolean shouldCaseBeClosed() {
    updateRemarks = "";
    switch (getRdmStatus().getStatusLevering()) {
      case DOCUMENT_NIET_GELEVERD:
      case DOCUMENT_NIET_GOED:
      case DOCUMENT_VERDWENEN:
        updateRemarks = getRdmStatus().getStatusLevering().getOms();
        return true;
    }

    switch (getRdmStatus().getStatusAfsluiting()) {
      case DOCUMENT_UITGEREIKT:
      case DOCUMENT_NIET_UITGEREIKT_ONJUIST:
      case DOCUMENT_UITGEREIKT_DOOR_ANDERE_INSTANTIE:
      case DOCUMENT_NIET_UITGEREIKT_OVERIGE_REDEN:
      case DOCUMENT_NIET_OPGEHAALD:
        updateRemarks = getRdmStatus().getStatusAfsluiting().getOms();
        return true;
    }
    return false;
  }

  public ReisdocumentStatus getUpdatedStatus() {
    getRdmStatus().setNrNederlandsDocument(getRaasDocumentNr());
    getRdmStatus().setStatusLevering(getRaasDeliveryStatus());
    getRdmStatus().setDatumTijdLevering(getRaasDeliveryDate());
    getRdmStatus().setStatusAfsluiting(getRaasClosingStatus());
    getRdmStatus().setDatumTijdAfsluiting(getRaasClosingDate());
    return getRdmStatus();
  }

  public boolean isDifferent() {
    boolean isNewDeliveryStatus = getRdmDeliveryStatus().getCode() < getRaasDeliveryStatus().getCode();
    boolean isNewDeliveryDate = isNieuwer(getRdmDeliveryDate(), getRaasDeliveryDate());
    boolean isNewClosingStatus = getRdmClosingStatus().getCode() < getRaasClosingStatus().getCode();
    boolean isNewClosingDate = isNieuwer(getRdmClosingDate(), getRaasClosingDate());

    boolean different = !getRdwDocumentNr().equals(getRaasDocumentNr());
    if (isNewDeliveryStatus || isNewDeliveryDate || isNewClosingStatus || isNewClosingDate) {
      different = true;
    }

    return different;
  }

  public DocAanvraagDto getRaasCase() {
    return raasAanvraag;
  }

  public ReisdocumentAanvraag getCase() {
    return zaak;
  }

  public boolean isRaasDeliveryStatus(LeveringType leveringType) {
    return getRaasDeliveryStatus() == leveringType;
  }

  public boolean isRaasClosingStatus(SluitingType sluitingType) {
    return getRaasClosingStatus() == sluitingType;
  }

  private ReisdocumentStatus getRdmStatus() {
    return zaak.getReisdocumentStatus();
  }

  private LeveringType getRaasDeliveryStatus() {
    return LeveringType.get(raasAanvraag.getLevering().getStatus().getValue().getCode());
  }

  private DateTime getRaasDeliveryDate() {
    DatumLeveringDto datumLevering = raasAanvraag.getLevering().getDatum();
    TijdLeveringDto tijdLevering = raasAanvraag.getLevering().getTijd();
    return new DateTime(datumLevering.getValue(), tijdLevering.getValue());
  }

  private SluitingType getRaasClosingStatus() {
    return SluitingType.get(raasAanvraag.getAfsluiting().getStatus().getValue().getCode());
  }

  private DateTime getRaasClosingDate() {
    DatumAfsluitingDto datumAfsluiting = raasAanvraag.getAfsluiting().getDatum();
    TijdAfsluitingDto tijdAfsluiting = raasAanvraag.getAfsluiting().getTijd();
    return new DateTime(datumAfsluiting.getValue(), tijdAfsluiting.getValue());
  }

  private String getRaasDocumentNr() {
    return raasAanvraag.getNrNederlandsDoc().getValue();
  }

  String getUpdateRemarks() {
    return updateRemarks;
  }
}
