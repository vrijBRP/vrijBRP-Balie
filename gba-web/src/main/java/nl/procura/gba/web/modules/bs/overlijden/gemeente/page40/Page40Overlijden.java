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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.page40;

import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.gba.web.modules.bs.overlijden.PageBsOverlijdenGerelateerde;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page40Overlijden extends PageBsOverlijdenGerelateerde<DossierOverlijdenGemeente> {

  public Page40Overlijden() {
    super("Overlijden - gerelateerden");
  }

  @Override
  public boolean checkPage() {

    if (isPartnerNodig()) {
      throw new ProException(WARNING, "Geef de partner van de overledene op.");
    }

    if (isExPartnerNodig()) {
      throw new ProException(WARNING, "Geef de ex-partner van de overledene op.");
    }

    for (DossierPersoon persoon : getPersonen()) {
      if (isMeerderjarig(persoon)) {
        if (persoon.getDossierPersoonType().is(KIND)) {
          throw new ProException(WARNING, "Eén of meerdere kinderen is niet minderjarig");
        }
      }
    }

    super.checkPage();

    getServices().getOverlijdenGemeenteService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      init(getZaakDossier().getOverledene());
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

  private boolean isExPartnerNodig() {

    BurgerlijkeStaatType bsType = getZaakDossier().getOverledene().getBurgerlijkeStaat();
    boolean isExPartnerNodig = (bsType != null && bsType.is(ACHTERGEBLEVEN, GESCHEIDEN, ONTBONDEN, WEDUWE));

    for (DossierPersoon persoon : getPersonen()) {
      if (isExPartnerNodig && persoon.getDossierPersoonType() == EXPARTNER) {
        return false;
      }
    }

    return isExPartnerNodig;
  }

  private boolean isPartnerNodig() {

    BurgerlijkeStaatType bsType = getZaakDossier().getOverledene().getBurgerlijkeStaat();
    boolean isPartnerNodig = (bsType != null && bsType.is(HUWELIJK, BurgerlijkeStaatType.PARTNERSCHAP));

    for (DossierPersoon persoon : getPersonen()) {
      if (isPartnerNodig && persoon.getDossierPersoonType().is(PARTNER)) {
        return false;
      }
    }

    return isPartnerNodig;
  }
}
