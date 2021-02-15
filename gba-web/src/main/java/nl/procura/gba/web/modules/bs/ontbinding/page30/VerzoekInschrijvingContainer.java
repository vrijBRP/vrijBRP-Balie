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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VerzoekInschrijvingContainer extends GbaContainer {

  public VerzoekInschrijvingContainer(DossierPersoon p1, DossierPersoon p2) {

    String naam1 = p1.getNaam().getPred_adel_voorv_gesl_voorn();
    String naam2 = p2.getNaam().getPred_adel_voorv_gesl_voorn();

    addItem(new FieldValue("p1", naam1));
    addItem(new FieldValue("p2", naam2));
    addItem(new FieldValue("echtgenoten", "Beide partners"));
  }
}
