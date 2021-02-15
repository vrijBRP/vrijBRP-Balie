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

package nl.procura.gba.web.modules.zaken.vog.page12;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.TextArea;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.vog.VogAanvraagPage;
import nl.procura.gba.web.modules.zaken.vog.VogHeaderForm;
import nl.procura.gba.web.modules.zaken.vog.page13.Page13Vog;
import nl.procura.gba.web.modules.zaken.vog.page14.Page14Vog;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraagDoel;
import nl.procura.gba.web.services.zaken.vog.VogDoel;
import nl.procura.gba.web.services.zaken.vog.VogsService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page12Vog extends VogAanvraagPage {

  private Page12VogForm1 form1 = null;

  public Page12Vog(VogAanvraag aanvraag) {

    super("Verklaring omtrent gedrag", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    addComponent(new VogHeaderForm(aanvraag));
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      form1 = new Page12VogForm1(getAanvraag());

      getDoel().setContainerDataSource(new DoelContainer());
      getDoel().setValue(form1.getBean().getDoel());

      getDoel().addListener((ValueChangeListener) event1 -> checkFields());

      checkFields();

      addComponent(form1);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    Page12VogBean1 b = form1.getBean();

    VogAanvraagDoel a = getAanvraag().getDoel();

    a.setDoel(b.getDoel());
    a.setFunctie(getFunctie().isVisible() ? b.getFunctie() : "");
    a.setDoelTekst(getOms().isVisible() ? b.getOmschrijving() : "");

    VogsService d = getApplication().getServices().getVogService();

    // Als er sprake is van een integriteitsverklaring dan profiel 24 gebruiken

    if (b.getDoel().isIntegriteitsVerklaring()) {
      getAanvraag().getScreening().setProfiel(d.getProfiel(VogsService.INTEGRITEITSVERKLARING_BEROEPSVERVOER));
    }

    d.save(getAanvraag());

    if (b.getDoel().isIntegriteitsVerklaring()) {
      getNavigation().goToPage(new Page14Vog(getAanvraag()));
    } else {
      getNavigation().goToPage(new Page13Vog(getAanvraag()));
    }

    super.onNextPage();
  }

  private void checkFields() {

    VogDoel value = (VogDoel) getDoel().getValue();
    getFunctie().setVisible((value != null) && "wre".equalsIgnoreCase(value.getCVogDoelTab()));
    getOms().setVisible((value != null) && "ovg".equalsIgnoreCase(value.getCVogDoelTab()));

    form1.repaint();
  }

  private GbaNativeSelect getDoel() {
    return (GbaNativeSelect) form1.getField(Page12VogBean1.DOEL);
  }

  private TextArea getFunctie() {
    return (TextArea) form1.getField(Page12VogBean1.FUNCTIE);
  }

  private TextArea getOms() {
    return (TextArea) form1.getField(Page12VogBean1.OMSCHRIJVING);
  }

  class DoelContainer extends IndexedContainer {

    public DoelContainer() {

      for (VogDoel b : getApplication().getServices().getVogService().getDoelen()) {
        addItem(b);
      }
    }
  }
}
