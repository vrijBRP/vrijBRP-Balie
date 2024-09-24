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

package nl.procura.gba.web.modules.bs.common.pages;

import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.HUWELIJK;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.PARTNERSCHAP;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.bs.common.BsProces;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.modules.BsModule;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class BsPage<T extends ZaakDossier> extends NormalPageTemplate {

  public BsPage(String title) {
    super(title);
  }

  public boolean checkPage() {
    return true;
  }

  public BasePLExt getPersoonslijst(BsnFieldValue bsn) {
    return getServices().getPersonenWsService().getPersoonslijst(bsn.getStringValue());
  }

  @SuppressWarnings("unchecked")
  public T getZaakDossier() {
    return (T) getProcessen().getDossier().getZaakDossier();
  }

  @Override
  public InfoLayout setInfo(String header, String msg) {
    BsProces proces = getProcessen().getProces(getClass());
    if (proces != null) {
      String statusMessage = proces.getStatusMessage();
      if (fil(statusMessage)) {
        msg += "<hr/><b>" + statusMessage + "</b>";
      }
    }

    return super.setInfo(header, msg);
  }

  protected Dossier getDossier() {
    return getProcessen().getDossier();
  }

  protected BasePLExt getPartner(BasePLRec record) {

    if (record != null) {

      String anr = record.getElemVal(GBAElem.ANR).getVal();
      String bsn = record.getElemVal(GBAElem.BSN).getVal();

      boolean isAnummer = new Anr(anr).isCorrect();
      boolean isBsn = new Bsn(bsn).isCorrect();

      if (isAnummer || isBsn) {
        return getApplication().getServices().getPersonenWsService().getPersoonslijst(anr, bsn);
      }
    }

    return null;
  }

  protected BasePLRec getPartnerRecord(DossierPersoon dossierPersoon) {

    if (dossierPersoon.isIngeschreven()) {
      BasePLExt pl = getPersoonslijst(dossierPersoon.getBurgerServiceNummer());
      if (pl.getPersoon().getBurgerlijkeStand().getStatus().getType().is(HUWELIJK, PARTNERSCHAP)) {
        BasePLRec record = pl.getHuwelijk().getActueelOfMutatieRecord();
        if (record.hasElems()) {
          return record;
        }
      }
    }

    return null;
  }

  protected BsProcessen getProcessen() {
    return getBsModule().getProcessen();
  }

  protected BsModule getBsModule() {
    return (BsModule) getParentLayout();
  }

  /**
   * Ga naar de volgende pagina in het proces
   */
  protected void goToNextProces() {
    Class<? extends BsPage<? extends ZaakDossier>> nextPage = getProcessen().getNextProces().getPage();
    getNavigation().goToPage(nextPage);
    getDossier().setPagina(nextPage.getSimpleName());
  }

  /**
   * Ga naar de vorige pagina in het proces
   */
  protected void goToPreviousProces() {
    getNavigation().goBackToPage(getProcessen().getPreviousProces().getPage());
  }
}
