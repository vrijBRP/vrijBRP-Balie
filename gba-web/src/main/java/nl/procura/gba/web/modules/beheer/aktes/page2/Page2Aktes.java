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

package nl.procura.gba.web.modules.beheer.aktes.page2;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.aktes.page3.Page3Aktes;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteCategorie;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Aktes extends NormalPageTemplate {

  protected final Button buttonNewDeel = new Button("Nieuw");
  protected final Button buttonDelDeel = new Button("Verwijderen");

  private DossierAkteCategorie categorie;
  private Page2AktesForm1      form1  = null;
  private Table1               table1 = null;

  public Page2Aktes(DossierAkteCategorie categorie) {
    super("Toevoegen / muteren akte-categorie");
    this.categorie = categorie;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setForm(new Page2AktesForm1(categorie));

      addComponent(form1);

      table1 = new Table1();

      addComponent(new Fieldset("De registerdelen"));

      OptieLayout optieLayout = new OptieLayout();
      optieLayout.getLeft().addExpandComponent(table1);
      optieLayout.getRight().setWidth("200px");

      optieLayout.getRight().addButton(buttonNewDeel, this);
      optieLayout.getRight().addButton(buttonDelDeel, this);

      addExpandComponent(optieLayout);
    } else if (event.isEvent(AfterReturn.class)) {

      table1.init();
    }

    super.event(event);
  }

  public Page2AktesForm1 getForm() {
    return form1;
  }

  public void setForm(Page2AktesForm1 form) {
    this.form1 = form;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonNewDeel) {

      getNavigation().goToPage(new Page3Aktes(new DossierAkteDeel(categorie)));
    }

    if (button == buttonDelDeel) {

      new DeleteProcedure<DossierAkteDeel>(table1) {

        @Override
        protected void deleteValue(DossierAkteDeel deel) {

          getServices().getAkteService().deleteRegisterDeel(deel);
        }
      };

    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {

    getForm().reset();

    categorie = new DossierAkteCategorie();

    table1.init();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    getForm().commit();

    Page2AktesBean1 b = getForm().getBean();

    categorie.setCategorie(b.getCategorie());

    getServices().getAkteService().saveRegisterCategorie(categorie);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }

  class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {

      getNavigation().goToPage(new Page3Aktes((DossierAkteDeel) record.getObject()));

      super.onClick(record);
    }

    @Override
    public void setColumns() {

      setSizeFull();
      setMultiSelect(true);
      setSelectable(true);

      addColumn("Registersoort", 300);
      addColumn("Code", 40);
      addColumn("Omschrijving");
      addColumn("Min.", 40);
      addColumn("Max.", 40);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      List<DossierAkteDeel> delen = getServices().getAkteService().getAkteRegisterDelen(categorie);

      for (DossierAkteDeel deel : delen) {

        Record r = addRecord(deel);

        r.addValue(deel.getRegisterSoort());
        r.addValue(deel.getRegisterdeel());
        r.addValue(deel.getOmschrijving());
        r.addValue(deel.getMin());
        r.addValue(deel.getMax());
      }

      super.setRecords();
    }
  }
}
