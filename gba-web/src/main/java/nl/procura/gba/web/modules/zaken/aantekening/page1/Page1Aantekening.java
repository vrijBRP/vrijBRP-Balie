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

package nl.procura.gba.web.modules.zaken.aantekening.page1;

import static nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningStatus.AFGESLOTEN;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.aantekening.page2.Page2Aantekening;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.AantekeningService;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Aantekening
 */
public class Page1Aantekening extends NormalPageTemplate {

  private Page1AantekeningTable table = null;
  private CheckBox              checkbox;

  public Page1Aantekening() {
    setSpacing(true);
    setMargin(false);
  }

  private static void onChange() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new Page1AantekeningTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2Aantekening(record.getObject(PlAantekening.class), () -> {}));
        }
      };

      H2 h2 = new H2("Aantekeningen");

      addButton(buttonNew);
      addButton(buttonDel);
      addButton(buttonClose);

      CheckBox checkbox = getCheckbox();
      getButtonLayout().addComponent(checkbox, getButtonLayout().getComponentIndex(buttonNew));
      getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(checkbox));
      getButtonLayout().setExpandRatio(h2, 1f);
      getButtonLayout().setWidth("100%");
      getButtonLayout().setComponentAlignment(checkbox, Alignment.MIDDLE_RIGHT);

      setInfo("", "De aantekeningen van deze persoon");

      addExpandComponent(table);

    } else if (event.isEvent(AfterBackwardReturn.class)) {

      updateCheckbox();
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {

    for (PlAantekening aantekening : table.getSelectedValues(PlAantekening.class)) {
      if (aantekening.getLaatsteHistorie().getIndicatie().isKladblokAantekening()) {
        throw new ProException(ProExceptionSeverity.INFO,
            "Aantekeningen op de persoonslijst kunnen alleen via PROBEV worden verwijderd.");
      }
    }

    new DeleteProcedure<PlAantekening>(table) {

      @Override
      public void deleteValue(PlAantekening plAantekening) {
        getServices().getAantekeningService().delete(plAantekening);
      }

      @Override
      protected void afterDelete() {
        updateCheckbox();
      }
    };

  }

  @Override
  public void onNew() {

    PlAantekening aantekening = new PlAantekening();
    aantekening.setBurgerserviceNummer(new BsnFieldValue(getPl().getPersoon().getBsn().getVal()));
    getNavigation().goToPage(new Page2Aantekening(aantekening, Page1Aantekening::onChange));

    super.onNew();
  }

  private int getAantalAfgeslotenAantekeningen() {
    return getAantekeningenService().getPersoonAantekeningen(AFGESLOTEN).size();
  }

  private AantekeningService getAantekeningenService() {
    return getServices().getAantekeningService();
  }

  private CheckBox getCheckbox() {

    checkbox = new CheckBox("");
    checkbox.setImmediate(true);
    checkbox.addListener((ValueChangeListener) event -> {
      table.setToonAantekeningen((boolean) event.getProperty().getValue());
      table.init();
    });

    updateCheckbox();

    return checkbox;
  }

  private void updateCheckbox() {
    int aantal = getAantalAfgeslotenAantekeningen();
    String caption = "Toon gesloten aantekeningen (" + aantal + ")";
    checkbox.setCaption(caption);
  }
}
