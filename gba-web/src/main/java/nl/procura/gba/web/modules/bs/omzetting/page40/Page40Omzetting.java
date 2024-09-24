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

package nl.procura.gba.web.modules.bs.omzetting.page40;

import static nl.procura.gba.web.modules.bs.omzetting.page40.Page40OmzettingBean.GEMEENTEGETUIGEN;
import static nl.procura.gba.web.modules.bs.omzetting.page40.Page40OmzettingBean.GERELATEERDEN;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.omzetting.page45.Page45Omzetting;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page40Omzetting extends BsPage<DossierOmzetting> {

  private Form  form  = null;
  private Table table = null;

  public Page40Omzetting() {

    super("Omzetting GPS in huwelijk - getuigen");

    addButton(buttonPrev);
    addButton(buttonNext);
    addButton(buttonDel);
  }

  @Override
  public boolean checkPage() {

    getZaakDossier().getGetuigen().clear();

    for (DossierPersoon dossierPersoon : table.getAllValues(DossierPersoon.class)) {

      if (dossierPersoon.isVolledig()) {

        getZaakDossier().getDossier().toevoegenPersoon(dossierPersoon);
      }
    }

    form.commit();

    getZaakDossier().setGemeenteGetuigen(aval(form.getBean().getGemeenteGetuigen().getValue()));

    getProcessen().updateStatus();

    getApplication().getServices().getOmzettingService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        addComponent(new BsStatusForm(getDossier()));

        setInfo("Leg vast wie de getuigen zijn voor deze verbintenis. Druk op Volgende (F2) om verder te gaan.");

        Page40OmzettingBean bean = new Page40OmzettingBean();

        bean.setGemeenteGetuigen(new FieldValue(astr(getZaakDossier().getGemeenteGetuigen())));

        table = new Table();
        addComponent(table);

        form = new Form();
        form.setBean(bean);
        addComponent(form);
      } else if (event.isEvent(AfterReturn.class)) {

        table.init();
        form.reset();
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onDelete() {

    if (table.isSelectedRecords()) {

      for (DossierPersoon dossierPersoon : table.getSelectedValues(DossierPersoon.class)) {

        getServices().getOmzettingService().deleteGetuige(getZaakDossier(), dossierPersoon);

        BsPersoonUtils.reset(dossierPersoon);
      }

      table.init();
    } else {
      throw new ProException(INFO, "Selecteer eerst één of meerdere personen");
    }
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  public class Form extends Page40OmzettingForm {

    public Form() {
      super(table, getZaakDossier());
    }

    @Override
    public void init() {

      setCaption(null);
      setOrder(GEMEENTEGETUIGEN, GERELATEERDEN);
    }
  }

  public class Table extends Page40OmzettingTable {

    public Table() {

      super(getZaakDossier());
    }

    @Override
    public void onDoubleClick(Record record) {

      getNavigation().goToPage(new Page45Omzetting((DossierPersoon) record.getObject()));

      super.onDoubleClick(record);
    }
  }
}
