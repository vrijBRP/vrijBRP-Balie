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

package nl.procura.gba.web.modules.zaken.vog.page11;

import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.zaken.vog.VogAanvraagPage;
import nl.procura.gba.web.modules.zaken.vog.VogHeaderForm;
import nl.procura.gba.web.modules.zaken.vog.page12.Page12Vog;
import nl.procura.gba.web.modules.zaken.vog.page20.Page20Vog;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraagBelanghebbende;
import nl.procura.gba.web.services.zaken.vog.VogBelanghebbende;
import nl.procura.gba.web.services.zaken.vog.VogsService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page11Vog extends VogAanvraagPage {

  private final Button   buttonBelang = new Button("Belanghebbenden (F3)");
  private Page11VogForm1 form1        = null;

  public Page11Vog(VogAanvraag aanvraag) {

    super("Verklaring omtrent gedrag", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    addComponent(new VogHeaderForm(aanvraag));
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      form1 = new Page11VogForm1(getAanvraag());

      getSnelKeuze().addListener((ValueChangeListener) event1 -> {
        if (event1.getProperty().getValue() != null) {
          setBelangHebbende((VogBelanghebbende) event1.getProperty().getValue());
        }
      });

      OptieLayout ol = new OptieLayout();
      ol.getLeft().addComponent(form1);

      ol.getRight().setWidth("200px");
      ol.getRight().setCaption("Opties");

      ol.getRight().addButton(buttonBelang, this);

      addComponent(ol);
    } else if (event instanceof LoadPage) {

      getSnelKeuze().setContainerDataSource(new BelanghebbendeContainer());
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonBelang) || (keyCode == KeyCode.F3)) {
      getNavigation().goToPage(new Page20Vog());
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    Page11VogBean1 b = form1.getBean();

    VogAanvraagBelanghebbende a = getAanvraag().getBelanghebbende();

    a.setNaam(b.getNaam());
    a.setVertegenwoordiger(b.getVertegenwoordiger());
    a.setNaam(b.getNaam());
    a.setStraat(b.getStraat());
    a.setHnr(along(defaultNul(astr(b.getHnr()))));
    a.setHnrL(b.getHnrL());
    a.setHnrT(b.getHnrT());
    a.setPc(b.getPostcode());
    a.setPlaats(b.getPlaats());
    a.setLand(b.getLand());
    a.setTel(b.getTelefoon());

    VogsService d = getApplication().getServices().getVogService();
    d.save(getAanvraag());

    getNavigation().goToPage(new Page12Vog(getAanvraag()));

    super.onNextPage();
  }

  private GbaComboBox getSnelKeuze() {
    return form1.getField(Page11VogBean1.SNELKEUZE, GbaComboBox.class);
  }

  private void setBelangHebbende(VogBelanghebbende b) {
    form1.setBelangHebbende(b);
  }

  class BelanghebbendeContainer extends IndexedContainer {

    public BelanghebbendeContainer() {

      for (VogBelanghebbende b : getApplication().getServices().getVogService().getBelangHebbenden()) {

        addItem(b);
      }
    }
  }
}
