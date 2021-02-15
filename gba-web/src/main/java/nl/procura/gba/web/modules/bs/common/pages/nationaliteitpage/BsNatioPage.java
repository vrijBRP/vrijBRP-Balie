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

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nationaliteiten
 */

public class BsNatioPage extends ButtonPageTemplate {

  private final BsNatioType            type;
  private BsNatioTable                 table = null;
  private BsNatioForm                  form  = null;
  private final DossierNationaliteiten dossier;
  private final String                 message;

  public BsNatioPage(BsNatioType type, DossierNationaliteiten dossier, String message) {

    H2 h2 = new H2("Nationaliteiten");

    buttonSave.setCaption("Toevoegen (F9)");

    addButton(buttonSave);
    addButton(buttonDel);
    addButton(buttonClose);

    getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(buttonSave));
    getButtonLayout().setExpandRatio(h2, 1f);
    getButtonLayout().setWidth("100%");

    this.dossier = dossier;
    this.message = message;
    this.type = type;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSpacing(true);

      setInfo("", message);

      form = new BsNatioForm(type, dossier);
      table = new NatioTable(dossier);

      table.setSelectable(true);
      table.setMultiSelect(true);

      addComponent(form);
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public BsNatioWindow getWindow() {
    return (BsNatioWindow) super.getWindow();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {

    for (DossierNationaliteit natio : table.getSelectedValues(DossierNationaliteit.class)) {

      getWindow().delete(natio);
    }

    table.init();

    super.onSave();
  }

  @Override
  public void onSave() {

    getWindow().add(form.getDossierNationaliteit());

    table.init();

    super.onSave();
  }

  class NatioTable extends BsNatioTable {

    public NatioTable(DossierNationaliteiten dossier) {
      super(dossier);
    }

    @Override
    public void setColumns() {

      addColumn("Nationaliteit");
      addColumn("Sinds");
      addColumn("Reden");
    }
  }
}
