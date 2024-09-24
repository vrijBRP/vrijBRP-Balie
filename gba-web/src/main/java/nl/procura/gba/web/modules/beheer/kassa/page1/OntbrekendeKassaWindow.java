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

package nl.procura.gba.web.modules.beheer.kassa.page1;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.gba.web.services.beheer.kassa.KassaService;
import nl.procura.gba.web.services.beheer.kassa.KassaUtils;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class OntbrekendeKassaWindow extends GbaModalWindow {

  private List<KassaProduct> kassaProducten;

  public OntbrekendeKassaWindow(List<KassaProduct> kassaProducten) {
    super("Ontbrekende producten (Escape om te sluiten)", "800px");
    this.kassaProducten = kassaProducten;
    setHeight("415px");
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  public List<KassaProduct> getKassaProducten() {
    return kassaProducten;
  }

  public void setKassaProducten(List<KassaProduct> kassaProducten) {
    this.kassaProducten = kassaProducten;
  }

  public class Page extends NormalPageTemplate {

    private GbaTable table = null;

    public Page() {

      super("Ontbrekende producten in de kassa");
      buttonSave.setCaption("Toevoegen (F9)");
      addButton(buttonSave, 1f);
      addButton(buttonClose);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        table = new GbaTable() {

          @Override
          public void setColumns() {

            setSelectable(true);
            setMultiSelect(true);

            addColumn("ID", 50);
            addColumn("Type", 200);
            addColumn("Omschrijving");

            super.setColumns();
          }

          @Override
          public void setRecords() {

            for (KassaProduct kassa : getKassaProducten()) {

              Record r = addRecord(kassa);
              r.addValue(kassa.getKassa());
              r.addValue(kassa.getKassaType());
              r.addValue(kassa.getDescr());
            }
          }
        };

        addExpandComponent(table);
      }

      super.event(event);
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
      super.onClose();
    }

    @Override
    public void onSave() {

      if (!table.isSelectedRecords()) {
        throw new ProException(ProExceptionSeverity.INFO, "Geen records geselecteerd");
      }

      List<KassaProduct> list = table.getSelectedValues(KassaProduct.class);
      KassaService kassaService = getServices().getKassaService();

      for (KassaProduct kassaProduct : list) {
        kassaService.save(kassaProduct);
      }

      successMessage(list.size() + " product(en) toegevoegd");
      setKassaProducten(KassaUtils.getOntbrekendeProducten(kassaService));

      table.init();

      super.onSave();
    }
  }
}
