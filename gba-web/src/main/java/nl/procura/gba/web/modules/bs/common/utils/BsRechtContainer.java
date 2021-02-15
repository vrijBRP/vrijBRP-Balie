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

package nl.procura.gba.web.modules.bs.common.utils;

import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class BsRechtContainer extends ArrayListContainer {

  private final Services services;

  public BsRechtContainer(Services services) {
    this.services = services;
  }

  protected void add(FieldValue land) {
    if (land != null && pos(land.getValue())) {
      addItem(land);
    }
  }

  protected void addAll(List<? extends FieldValue> values) {
    for (FieldValue land : values) {
      add(land);
    }
  }

  protected void addDossierNationaliteiten(DossierNamenrecht dossier) {

    boolean isOnbekend = false;
    for (DossierNationaliteit natio : dossier.getDossier().getNationaliteiten()) {
      add(getLand(natio.getNationaliteit()));
      if (Landelijk.isOnbekend(natio.getNationaliteit())) {
        isOnbekend = true;
      }
    }

    // Als de persoon een onbekende nationaliteit heeft
    // dan alle landen toevoegen
    if (isOnbekend) {
      addAll(Container.LAND.get());
    }
  }

  protected void addPersoonLand(DossierPersoon persoon) {
    add(persoon.getLand());
  }

  protected void addPersoonNationaliteiten(DossierPersoon persoon) {

    boolean isOnbekend = false;
    if (persoon != null && persoon.isVolledig()) {
      for (DossierNationaliteit natio : persoon.getNationaliteiten()) {
        add(getLand(natio.getNationaliteit()));
        if (Landelijk.isOnbekend(natio.getNationaliteit())) {
          isOnbekend = true;
        }
      }
    }

    // Als de persoon een onbekende nationaliteit heeft
    // dan alle landen toevoegen
    if (isOnbekend) {
      addAll(Container.LAND.get());
    }
  }

  protected FieldValue getLand(FieldValue natio) {
    return services.getKennisbankService().getLand(natio);
  }

  protected Services getServices() {
    return services;
  }
}
