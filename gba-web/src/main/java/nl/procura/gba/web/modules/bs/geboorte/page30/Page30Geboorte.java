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

package nl.procura.gba.web.modules.bs.geboorte.page30;

import static nl.procura.gba.web.services.bs.geboorte.RedenVerplicht.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonPage;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Moeder
 */

public class Page30Geboorte<T extends DossierGeboorte> extends BsPersoonPage<T> {

  public Page30Geboorte() {
    this("Geboorte - moeder uit wie het kind is geboren");
  }

  public Page30Geboorte(String title) {
    super(title);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {
      getApplication().getServices().getGeboorteService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        DossierPersoon aangever = getZaakDossier().getAangever();
        DossierPersoon moeder = getZaakDossier().getMoeder();

        try {

          if (getZaakDossier().getRedenVerplichtBevoegd().is(VADER, DUO_MOEDER)) {

            // Als geen naam is en aangever is vader of duo-moeder is dan moeder zoeken aan de hand van de aangever

            if (!moeder.isVolledig()) {

              BasePLRec partnerRecord = getPartnerRecord(aangever);

              BasePLExt partner = getPartner(partnerRecord);

              if (partnerRecord != null) {

                if (partner != null) {

                  BsPersoonUtils.kopieDossierPersoon(partner, moeder);
                } else {

                  BsPersoonUtils.kopieDossierPersoon(partnerRecord, moeder);
                }
              }
            }
          } else if (getZaakDossier().getRedenVerplichtBevoegd().is(MOEDER)) {

            // Als de aangever ook de moeder is

            if (!moeder.isVolledig()) {

              BasePLExt pl = getPersoonslijst(aangever.getBurgerServiceNummer());

              BsPersoonUtils.kopieDossierPersoon(pl, moeder);
            }
          }
        } catch (Exception e) {
          throw new ProException(SELECT, WARNING, "De persoonslijst kan niet worden geladen", e);
        } finally {

          setDossierPersoon(moeder);
          getDossierPersoon().setDossierPersoonType(DossierPersoonType.MOEDER);
        }
      }
    } finally {
      super.event(event);
    }
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
