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

package nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage;

import static java.util.Arrays.asList;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getTypePersonen;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.EXPARTNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.OUDER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;

import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class PageBsGerelateerdeLayout extends PageBsGerelateerdeTableLayout {

  private final GbaApplication application;
  private final DossierPersoon dossierPersoon;

  public PageBsGerelateerdeLayout(GbaApplication application, Dossier dossier, DossierPersoon dossierPersoon,
      DossierPersoonType... types) {
    super(application, dossier);
    this.application = application;
    this.dossierPersoon = dossierPersoon;

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (isGeenPersonen(PARTNER)) {
      laadPersonen(dossierPersoon.getPersonen(PARTNER), PARTNER);
      afterLaadPersonen();
    }

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (isGeenPersonen(EXPARTNER)) {
      laadPersonen(dossierPersoon.getPersonen(EXPARTNER), EXPARTNER);
      afterLaadPersonen();
    }

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (isGeenPersonen(OUDER)) {
      laadPersonen(dossierPersoon.getPersonen(OUDER), OUDER);
      afterLaadPersonen();
    }

    // Laad alleen de personen als deze nog niet zijn gevuld
    if (isGeenPersonen(KIND)) {
      laadPersonen(dossierPersoon.getPersonen(KIND), KIND);
      afterLaadPersonen();
    }

    RelatiesTableLayout layout1 = addLayout(new RelatiesTableLayout("Partner(s)", PARTNER, EXPARTNER));
    RelatiesTableLayout layout2 = addLayout(new RelatiesTableLayout("Ouder(s)", OUDER));
    RelatiesTableLayout layout3 = addLayout(new RelatiesTableLayout("Minderjarige kind(eren)", KIND));

    if (isType(asList(types), PARTNER, EXPARTNER)) {
      addExpandComponent(layout1, 0.30f);
    }

    if (isType(asList(types), OUDER)) {
      addExpandComponent(layout2, 0.30f);
    }

    if (isType(asList(types), KIND)) {
      addExpandComponent(layout3, 0.40f);
    }
  }

  public boolean isGeenPersonen(DossierPersoonType... types) {
    return dossierPersoon.getPersonen(types).isEmpty();
  }

  @Override
  public List<DossierPersoon> getPersonen(DossierPersoonType... types) {
    return dossierPersoon.getPersonen(types);
  }

  @Override
  public void laadRelaties(DossierPersoonType... types) {
    Services services = application.getServices();
    if (isType(asList(types), PARTNER, EXPARTNER)) {
      toevoegenPersonen(getTypePersonen(services, dossierPersoon, HUW_GPS));
    }

    if (isType(asList(types), OUDER)) {
      getTypePersonen(services, dossierPersoon, OUDER_1, OUDER_2).forEach(dossierPersoon::toevoegenPersoon);
    }

    if (isType(asList(types), DossierPersoonType.KIND)) {
      toevoegenPersonen(getTypePersonen(services, dossierPersoon, GBACat.KINDEREN));
    }
  }

  @Override
  public void verwijderPersoon(DossierPersoon persoon) {
    dossierPersoon.verwijderPersoon(persoon);
  }

  @Override
  public void toevoegenPersonen(List<DossierPersoon> personen) {
    dossierPersoon.toevoegenPersonen(personen);
  }

  // Override please
  @Override
  @SuppressWarnings("unused")
  public void onDossierPersoon(DossierPersoon dossierPersoon) {
  }

  @Override
  public void onHerladen(DossierPersoonType[] types) {
    for (DossierPersoonType type : types) {
      laadPersonen(dossierPersoon.getPersonen(type), type);
      afterLaadPersonen();
    }
  }
}
