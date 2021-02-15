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

package nl.procura.gba.web.modules.beheer.profielen.page1.kopie;

import static nl.procura.gba.web.modules.beheer.profielen.page1.kopie.KopieProfielBean.PROFIELNAAR;
import static nl.procura.gba.web.modules.beheer.profielen.page1.kopie.KopieProfielBean.PROFIELVAN;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.containers.ProfielContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.profiel.Profiel;

public class KopieProfielForm extends GbaForm<KopieProfielBean> {

  public KopieProfielForm() {

    setOrder(PROFIELVAN, PROFIELNAAR);

    setColumnWidths("80px", "");

    setBean(new KopieProfielBean());
  }

  @Override
  public void attach() {

    List<Profiel> profielen = getApplication().getServices().getProfielService().getProfielen();

    setContainer(getField(PROFIELVAN), profielen);
    setContainer(getField(PROFIELNAAR), profielen);

    super.attach();
  }

  private void setContainer(Field field, List<Profiel> profielen) {

    GbaNativeSelect selectField = (GbaNativeSelect) field;

    selectField.setContainerDataSource(new ProfielContainer(profielen));
  }
}
