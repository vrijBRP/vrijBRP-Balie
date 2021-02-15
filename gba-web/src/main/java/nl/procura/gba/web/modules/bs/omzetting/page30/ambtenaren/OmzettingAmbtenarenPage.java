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

package nl.procura.gba.web.modules.bs.omzetting.page30.ambtenaren;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.components.fields.GeldigheidField;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.omzetting.page30.AmbtenarenForm;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class OmzettingAmbtenarenPage extends ButtonPageTemplate {

  private final AmbtenarenForm  form1;
  private final GeldigheidField geldigheidField;
  private Table1                table1 = new Table1();
  private final DossierPersoon  dossierPersoon;

  public OmzettingAmbtenarenPage(AmbtenarenForm form1, DossierPersoon dossierPersoon) {

    H2 h2 = new H2("Beschikbare Ambtenaren");
    geldigheidField = new GeldigheidField() {

      @Override
      public void onChangeValue(GeldigheidStatus value) {
        table1.init();
      }
    };

    addButton(buttonClose);
    getButtonLayout().add(h2, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().add(geldigheidField, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().expand(h2, 1f).widthFull();

    this.form1 = form1;
    this.dossierPersoon = dossierPersoon;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSpacing(true);

      setInfo("Selecteer een huwelijksAmbtenaar");

      setTable1(new Table1());

      addComponent(getTable1());
    }

    super.event(event);
  }

  public Table1 getTable1() {
    return table1;
  }

  public void setTable1(Table1 table1) {
    this.table1 = table1;
  }

  @Override
  public void onClose() {

    getWindow().closeWindow();
  }

  class Table1 extends GbaTable {

    public Table1() {
      setHeight("300px");
    }

    @Override
    public void onClick(Record record) {

      selectRecord(record);

      super.onClick(record);
    }

    @Override
    public void setColumns() {

      addColumn("Ambtenaar").setUseHTML(true);
      addColumn("E-mail", 150);
      addColumn("Toelichting", 300);

      setSelectable(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      HuwelijksAmbtenaar lege = new HuwelijksAmbtenaar();

      lege.setHuwelijksAmbtenaar("(Nog geen Ambtenaar)");

      add(lege);

      List<HuwelijksAmbtenaar> huwelijksAmbtenaren = getServices().getOmzettingService().getHuwelijksAmbtenaren(
          geldigheidField.getValue());
      for (HuwelijksAmbtenaar l : huwelijksAmbtenaren) {
        add(l);
      }

      super.setRecords();
    }

    private void add(HuwelijksAmbtenaar l) {

      Record r = addRecord(l);

      r.addValue(GeldigheidStatus.getHtml(l.getHuwelijksAmbtenaar(), l));
      r.addValue(l.getEmail());
      r.addValue(l.getToelichting());
    }

    private void selectRecord(Record record) {

      HuwelijksAmbtenaar ambtenaar = (HuwelijksAmbtenaar) record.getObject();

      PLEArgs args = new PLEArgs();

      long bsn = along(ambtenaar.getBurgerServiceNummer().getValue());

      if (bsn > 0) {

        args.addNummer(astr(bsn));

        PLResultComposite result = getApplication().getServices().getPersonenWsService().getPersoonslijsten(
            args, true);

        if (result.getBasisPLWrappers().size() > 0) {

          BsPersoonUtils.kopieDossierPersoon(result.getBasisPLWrappers().get(0), dossierPersoon);

          dossierPersoon.setAktenaam(ambtenaar.getHuwelijksAmbtenaar());
        } else {
          throw new ProException(ProExceptionSeverity.WARNING, "Persoon kan niet worden gevonden.");
        }
      } else {

        BsPersoonUtils.reset(dossierPersoon);
      }

      form1.update();

      ((ModalWindow) getWindow()).closeWindow();
    }
  }
}
