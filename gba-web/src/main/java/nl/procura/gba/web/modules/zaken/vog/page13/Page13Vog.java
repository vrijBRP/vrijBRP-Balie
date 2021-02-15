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

package nl.procura.gba.web.modules.zaken.vog.page13;

import com.vaadin.data.Property.ValueChangeListener;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.vog.VogAanvraagPage;
import nl.procura.gba.web.modules.zaken.vog.VogHeaderForm;
import nl.procura.gba.web.modules.zaken.vog.page14.Page14Vog;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogProfiel;
import nl.procura.gba.web.services.zaken.vog.VogsService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page13Vog extends VogAanvraagPage {

  private Page13VogForm1  form1  = null;
  private Page13VogTable1 table1 = null;
  private Page13VogTable2 table2 = null;

  public Page13Vog(VogAanvraag aanvraag) {

    super("Verklaring omtrent gedrag", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    setInfo(
        "Er kan maximaal één screeningsprofiel worden gekozen. Er kunnen wel meerdere functiegebieden worden gekozen.");
    addComponent(new VogHeaderForm(aanvraag));
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      form1 = new Page13VogForm1(getAanvraag());
      table1 = new Page13VogTable1(getAanvraag());
      table2 = new Page13VogTable2(getAanvraag());

      getBasis().addListener((ValueChangeListener) event1 -> checkFields());

      addComponent(form1);

      checkFields();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();

    Page13VogBean1 b = form1.getBean();

    getAanvraag().getScreening().getFunctiegebieden().clear();

    if (((b.getBasis() == ScreeningsType.SCREENINGSPROFIEL)
        && ((table1.getSelectedVogProfiel() == null) || (table1.getSelectedVogProfiel().getCVogProfTab() <= 0)))) {
      infoMessage("Selecteer eerst een screeningsprofiel.");
      return;
    }

    if ((b.getBasis() == ScreeningsType.FUNCTIEGEBIED) && (table2.getSelectedVogFuncties().isEmpty())) {
      infoMessage("Selecteer minimaal één functiegebied.");
      return;
    }

    getAanvraag().getScreening().setProfiel(new VogProfiel());
    getAanvraag().getScreening().getFunctiegebieden().clear();

    if (b.getBasis() == ScreeningsType.SCREENINGSPROFIEL) {
      getAanvraag().getScreening().setProfiel(table1.getSelectedVogProfiel());
    } else if (b.getBasis() == ScreeningsType.FUNCTIEGEBIED) {
      getAanvraag().getScreening().getFunctiegebieden().addAll(table2.getSelectedVogFuncties());
    }

    VogsService d = getApplication().getServices().getVogService();
    d.save(getAanvraag());

    getNavigation().goToPage(new Page14Vog(getAanvraag()));

    super.onNextPage();
  }

  private void checkFields() {

    if (getBasis().getValue() == ScreeningsType.SCREENINGSPROFIEL) {
      addExpandComponent(table1);
    } else {
      removeComponent(table1);
    }

    if (getBasis().getValue() == ScreeningsType.FUNCTIEGEBIED) {
      addExpandComponent(table2);
    } else {
      removeComponent(table2);
    }
  }

  private GbaNativeSelect getBasis() {
    return (GbaNativeSelect) form1.getField(Page13VogBean1.BASIS);
  }
}
