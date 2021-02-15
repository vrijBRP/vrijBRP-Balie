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

package nl.procura.gba.web.modules.hoofdmenu.kassa.page1;

import static nl.procura.standard.Globalfunctions.aval;

import java.util.Set;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.kassa.page1.Page1KassaForm;
import nl.procura.gba.web.modules.zaken.kassa.page1.Page1KassaTable;
import nl.procura.gba.web.modules.zaken.kassa.page3.Page3Kassa;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;
import nl.procura.gba.web.services.beheer.kassa.KassaService;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Kassa
 */
public class Page1Kassa extends NormalPageTemplate {

  private final Button buttonAfrekenen = new Button("Afrekenen (F2)");
  private final Button buttonBundels   = new Button("Bundels");

  private final InfoLayout bundelLayout = new InfoLayout("Bundels",
      "2 of meer producten zijn te combineren tot een bundel.");
  private Page1KassaForm   form1        = null;
  private Page1KassaTable  table1       = null;

  public Page1Kassa() {

    super("Kassa");

    addButton(buttonBundels);
    addButton(buttonAfrekenen);
    addButton(buttonDel);

    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(buttonAfrekenen, 1f);

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page1KassaForm();
      table1 = new Page1KassaTable() {

        @Override
        public void init() {

          Set<KassaProduct> zoekBundels = getServices().getKassaService().findBundels();
          if (zoekBundels.isEmpty()) {
            removeComponent(bundelLayout);
            getButtonLayout().removeComponent(buttonBundels);
          } else {
            removeComponent(bundelLayout);
            getButtonLayout().addComponent(buttonBundels, 0);
            getButtonLayout().setComponentAlignment(buttonBundels, Alignment.MIDDLE_LEFT);
            addComponent(bundelLayout, 1);
          }

          super.init();
        }
      };

      addComponent(form1);
      addExpandComponent(table1);
    } else if (event.isEvent(AfterReturn.class)) {
      table1.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonAfrekenen) || (keyCode == KeyCode.F2)) {
      afrekenen();
    }

    if ((button == buttonBundels)) {
      bundels();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<KassaProductAanvraag>(table1) {

      @Override
      public void afterDelete() {
        form1.init();
      }

      @Override
      public void deleteValue(KassaProductAanvraag aanvraag) {
        getServices().getKassaService().deleteProductUitWinkelwagen(aanvraag);
      }
    };
  }

  /**
   * Stuur kassa object naar kassa (filesystem)
   */
  private void afrekenen() {

    final KassaService db = getServices().getKassaService();

    if (db.verstuur()) {

      int type_opschonen = aval(getApplication().getParmValue(ParameterConstant.KASSA_CLEAR_LIST));

      switch (type_opschonen) {

        case -1: // Nooit
        case 0: // Nooit

          successMessage("De gegevens zijn verstuurd naar de kassa.");

          break;

        case 1: // Eerst vragen

          getWindow().addWindow(new ConfirmDialog(
              "De gegevens zijn verstuurd naar de kassa. <hr/> " + "Wilt u deze lijst nu leegmaken?") {

            @Override
            public void buttonYes() {

              db.leegWinkelwagen();

              closeKassa();

              close();
            }
          });

          break;

        case 2: // Altijd

          db.leegWinkelwagen();

          successMessage("De gegevens zijn verstuurd naar de kassa.");

          closeKassa();

          break;

        default:
          break;
      }
    }
  }

  private void bundels() {
    getNavigation().goToPage(new Page3Kassa("Kassa - producten bundelen"));
  }

  /**
   * Sluit kassascherm
   */
  private void closeKassa() {

    table1.init();
    form1.init();
  }
}
