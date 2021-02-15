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

package nl.procura.gba.web.services.bs.algemeen.persoon;

import java.util.List;

import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;

public interface DossierPersonen {

  List<DossierPersoon> getAllePersonen();

  List<DossierPersoon> getExPartners();

  List<DossierPersoon> getKinderen();

  String getKinderenSamenvatting();

  List<DossierPersoon> getPersonen();

  List<DossierPersoon> getPersonen(DossierPersoonType... types);

  String getPersonenSamenvatting(DossierPersoonType... types);

  DossierPersoon getPersoon(DossierPersoon persoon);

  DossierPersoon getPersoon(DossierPersoonFilter filter);

  boolean heeftPersoon(DossierPersoon persoon);

  boolean isPersoon(DossierPersoon persoon);

  List<DossierPersoon> toevoegenPersonen(List<DossierPersoon> personen);

  DossierPersoon toevoegenPersoon(DossierPersoon persoon);

  DossierPersoon toevoegenPersoon(DossierPersoonType type);

  void verwijderPersoon(DossierPersoon persoon);
}
