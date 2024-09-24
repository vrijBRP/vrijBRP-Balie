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

package nl.procura.gba.web.modules.bs.naturalisatie.page20;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.Collection;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.naturalisatie.BsPageNaturalisatie;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page20Naturalisatie extends BsPageNaturalisatie {

  private final Button buttonGerelateerden = new Button("Gerelateerden");

  private Page20NaturalisatieForm form;
  private NaturalisatieTable      table;

  public Page20Naturalisatie() {
    super("Nationaliteit - optie en naturalisatie");
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      addButton(buttonNext);

      if (getZaakDossier().isOptie()) {
        setInfo("Geef aan op basis van welk onderdeel van artikel 6, lid 1 "
            + "RWN (of ander artikel of wettelijke bepaling) "
            + "betrokkene een optieverzoek doet. Druk op Volgende (F2) om verder te gaan.");
      } else {
        setInfo("Geef aan op basis van welk artikel uit de RWN betrokkene een naturalisatieverzoek doet. "
            + "Druk op Volgende (F2) om verder te gaan.");
      }

      form = new Page20NaturalisatieForm(getZaakDossier(), getApplication());
      addComponent(form);

      if (getZaakDossier().isMeerderjarig()) {
        table = new NaturalisatieTable(getZaakDossier());

        OptieLayout optieLayout = new OptieLayout();
        optieLayout.getRight().setWidth("150px");
        optieLayout.getLeft().addComponent(table);

        optieLayout.getRight().addButton(buttonGerelateerden, this);
        optieLayout.getRight().addButton(buttonDel, this);

        addComponent(new Fieldset("Gerelateerden mee-verzoeken"));
        addComponent(new InfoLayout("", "Druk op de button om te zien welk(e) gerelateerden van de "
            + "aangever als medeverzoekers in aanmerking komen en selecteer deze."));
        addComponent(optieLayout);
      }
    }

    super.event(event);
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      form.commit();

      String originalDossier = getZaakDossier().getDossiernr();
      Page20NaturalisatieBean bean = form.getBean();
      getZaakDossier().setBasisVerzoekType(bean.getBasisVerzoek());
      getZaakDossier().setDossiernr(getZaakDossier().isOptie() ? "" : bean.getDossiernr());

      if (getZaakDossier().isMeerderjarig()) {
        BsPersoonUtils.reset(getZaakDossier().getVertegenwoordiger());
      } else {
        verwijderPersonen(getZaakDossier().getMedeVerzoekers());
      }

      try {
        getServices().getNaturalisatieService().saveVerzoekerGegevens(getZaakDossier());
        getServices().getNaturalisatieService().save(getDossier());

      } catch (Exception ex) {
        getZaakDossier().setDossiernr(originalDossier);
        if (MiscUtils.isStackTrace(ex, "dossiernr", "already exists")) {
          throw new ProException(WARNING, "Dit dossiernummer komt al voor bij een ander dossier");
        }
        throw ex;
      }
      return true;
    }

    return false;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonGerelateerden) {
      getParentWindow().addWindow(new Page20NaturalisatieWindow(this, getZaakDossier().getAangever()));
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onDelete() {
    if (!table.isSelectedRecords()) {
      infoMessage("Selecteer eerst één of meerdere records.");
    }

    verwijderPersonen(table.getSelectedValues(DossierPersoon.class));
    table.init();
  }

  public void toevoegenMedeVerzoeker(DossierPersoon persoon) {
    if (persoon.isVolledig()) {
      if (getDossier().isPersoon(persoon)) {
        throw new ProException(INFO, persoon.getNaam().getPred_adel_voorv_gesl_voorn() + " is al toegevoegd.");
      }

      getServices().getDossierService().savePersoon(persoon);
      getDossier().toevoegenPersoon(persoon);
      getServices().getDossierService().saveDossier(getDossier());

      table.init();
    }
  }

  private void verwijderPersonen(Collection<DossierPersoon> personen) {
    getServices().getDossierService().deletePersonen(getDossier(), personen);
  }

  public class NaturalisatieTable extends Page20NaturalisatieTable {

    public NaturalisatieTable(DossierNaturalisatie naturalisatie) {
      super(naturalisatie);
      setSelectable(true);
      setClickable(false);
    }

    @Override
    public void onDoubleClick(Record record) {
      BsnFieldValue bsn = record.getObject(DossierPersoon.class).getBurgerServiceNummer();
      getApplication().goToPl(getWindow(), "pl.persoon", PLEDatasource.STANDAARD, bsn.getStringValue());
      super.onDoubleClick(record);
    }

    @Override
    public void setRecords() {
      int aantalGerelateerden = getZaakDossier().getMedeVerzoekers().size();
      if (aantalGerelateerden > 0) {
        addDossierPersonen(getZaakDossier().getMedeVerzoekers());
      }
      super.setRecords();
    }
  }
}
