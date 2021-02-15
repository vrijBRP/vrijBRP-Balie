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

package nl.procura.gba.web.modules.beheer.overig;

import java.util.List;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.vaadin.functies.VaadinUtils;

@SuppressWarnings("unused")
public class KoppelTabel<K extends KoppelbaarAanProfiel> extends GbaTable {

  public KoppelTabel() {

    setSelectable(true);
    setMultiSelect(true);
  }

  @Override
  public void attach() {

    ModuleTemplate mod = VaadinUtils.getParent(this, ModuleTemplate.class);

    if (mod != null) {

      ModuleAnnotation annotation = mod.getClass().getAnnotation(ModuleAnnotation.class);

      if (annotation != null) {

        ProfielActie profielActie = annotation.profielActie();

        setSelectable(getApplication().isProfielActie(ProfielActieType.UPDATE, profielActie));
      }
    }

    super.attach();
  }

  public void setTableStatus(KoppelActie koppelActie, int indexStatus, List<Record> selectedRecords) {

    for (Record r : selectedRecords) {

      setRecordValue(r, indexStatus, koppelActie.getStatus());
    }
  }
}
