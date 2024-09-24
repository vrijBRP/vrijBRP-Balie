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

package nl.procura.gba.web.modules.bs.ontbinding.page10;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsContactpersoonPage;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Partner 1
 */
public class Page10Ontbinding extends BsContactpersoonPage<DossierOntbinding> {

  public Page10Ontbinding() {
    super("Ontbinding/einde huwelijk/GPS in gemeente - partner 2");
  }

  @Override
  public void addButtons() {

    super.addButtons();

    getOptieLayout().getRight().addComponent(new Ruler());
    getOptieLayout().getRight().addButton(buttonIden, this);
    getOptieLayout().getRight().addButton(buttonContact, this);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {
      getServices().getOntbindingService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      DossierPersoon partner1 = getZaakDossier().getPartner1();
      DossierPersoon partner2 = getZaakDossier().getPartner2();

      try {

        if (partner1.isVolledig() && !partner2.isVolledig()) {

          BasePLRec partnerRecord = getPartnerRecord(partner1);

          BasePLExt partner = getPartner(partnerRecord);

          if (partnerRecord != null) {

            if (partner != null) {

              BsPersoonUtils.kopieDossierPersoon(partner, partner2);
            } else {

              BsPersoonUtils.kopieDossierPersoon(partnerRecord, partner2);
            }
          }
        }
      } catch (Exception e) {
        throw new ProException(SELECT, WARNING, "De persoonslijst kan niet worden geladen", e);
      } finally {

        setDossierPersoon(partner2);
        getDossierPersoon().setDossierPersoonType(DossierPersoonType.PARTNER2);
      }
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
