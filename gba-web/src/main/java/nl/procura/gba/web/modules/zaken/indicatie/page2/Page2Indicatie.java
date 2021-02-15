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

package nl.procura.gba.web.modules.zaken.indicatie.page2;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.tables.GBATableValues.Value;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.indicatie.page1.Page1Indicatie;
import nl.procura.gba.web.services.zaken.indicaties.IndicatieAanvraag;
import nl.procura.gba.web.services.zaken.indicaties.IndicatiesService;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nieuwe aanvraag
 */

public class Page2Indicatie extends ZakenPage {

  private Page2IndicatieForm form = null;

  public Page2Indicatie() {

    super("Indicatie: nieuw");

    addButton(buttonPrev);
    addButton(buttonSave);

    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event instanceof InitPage) {

      Page2IndicatieBean bean = new Page2IndicatieBean();

      form = new Page2IndicatieForm(bean);

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onSave() {

    form.commit();

    Page2IndicatieBean bean = form.getBean();

    IndicatiesService d = getServices().getIndicatiesService();

    IndicatieAanvraag aanvraag = (IndicatieAanvraag) d.getNewZaak();
    AnrFieldValue anr = new AnrFieldValue(getPl().getPersoon().getAnr().getVal());
    BsnFieldValue bsn = new BsnFieldValue(getPl().getPersoon().getBsn().getVal());

    // Algemeen

    aanvraag.setAnummer(anr);
    aanvraag.setBurgerServiceNummer(bsn);

    // Specifiek
    aanvraag.setActie(bean.getActie());
    aanvraag.setIndicatie(bean.getIndicatie());
    aanvraag.setInhoud(bean.getToelichting());

    d.save(aanvraag);

    successMessage("De gegevens zijn opgeslagen.");

    getApplication().getProcess().endProcess();

    getNavigation().goToPage(new Page1Indicatie());

    getNavigation().removeOtherPages();
  }

  public class Page2IndicatieContainer extends IndexedContainer {

    private Page2IndicatieContainer(FieldValue fieldValue) {
      for (Value w : GBATable.AAND_NAAMGEBRUIK.getValues().getValues()) {
        if (fieldValue.getValue().equals(w.getKey())) {
          continue;
        }

        addItem(new FieldValue(w.getKey(), w.getKey() + ": " + w.getValue()));
      }
    }
  }
}
