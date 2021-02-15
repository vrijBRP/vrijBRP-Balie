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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;

public abstract class Page3KlapperForm<T> extends GbaForm<T> {

  private final DossierAkte dossierAkte;
  private final boolean     muteerbaar;

  public Page3KlapperForm(DossierAkte dossierAkte, boolean muteerbaar) {
    this.dossierAkte = dossierAkte;
    this.muteerbaar = muteerbaar;
  }

  public DossierAkte getDossierAkte() {
    return dossierAkte;
  }

  public boolean isMuteerbaar() {
    return muteerbaar;
  }

  @Override
  public Field newField(Field field, Property property) {
    field.setReadOnly(!isMuteerbaar());
    return super.newField(field, property);
  }
}
