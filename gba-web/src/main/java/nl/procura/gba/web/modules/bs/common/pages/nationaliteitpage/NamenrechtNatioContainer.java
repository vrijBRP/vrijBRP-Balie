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

package nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.LinkedHashSet;
import java.util.Set;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class NamenrechtNatioContainer extends ArrayListContainer {

  public NamenrechtNatioContainer(DossierNamenrecht dossier) {

    Set<FieldValue> set = new LinkedHashSet<>();

    for (DossierNationaliteit nat : dossier.getMoeder().getNationaliteiten()) {
      set.add(Container.NATIO.get(nat.getNationaliteit().getStringValue()));
    }

    for (DossierNationaliteit nat : dossier.getVaderErkenner().getNationaliteiten()) {
      set.add(Container.NATIO.get(nat.getNationaliteit().getStringValue()));
    }

    set.add(Landelijk.getNederlandse());
    set.add(Container.NATIO.get(Landelijk.NATIONALITEIT_STAATLOOS));
    set.add(new FieldValue("0000", "Onbekend"));
    set.add(new FieldValue(astr(Landelijk.NATIONALITEIT_BEHANDELD_ALS_NEDERLANDER), "Behandeld als Nederlander"));

    for (FieldValue nat : set) {
      addItem(nat);
    }
  }
}
