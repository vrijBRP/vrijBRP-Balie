/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.fileimport.fileselection;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.vaadin.functies.VaadinUtils;

public class FileImportSearchFilter extends GbaIndexedTableFilterLayout {

  public FileImportSearchFilter(GbaTable table) {
    super(table);
  }

  @Override
  public void attach() {
    super.attach();
    setSizeFull();
    VaadinUtils.getChild(this, Label.class).setWidth("80px");
    VaadinUtils.getChild(this, TextField.class).setWidth("200px");
    Button button = VaadinUtils.getChild(this, Button.class);
    ((AbstractOrderedLayout) button.getParent()).setExpandRatio(button, 1F);
    AbstractOrderedLayout parent = (AbstractOrderedLayout) this.getParent();
    parent.setComponentAlignment(this, Alignment.MIDDLE_LEFT);
  }
}
