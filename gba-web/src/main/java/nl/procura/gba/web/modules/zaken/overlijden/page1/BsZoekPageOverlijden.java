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

package nl.procura.gba.web.modules.zaken.overlijden.page1;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.function.Consumer;

import com.vaadin.ui.Alignment;

import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.components.fields.GeldigheidField;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.page.buttons.ModalCloseButton;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.RedenVerplicht;
import nl.procura.gba.web.services.gba.basistabellen.overlijdenaangever.OverlijdenAangever;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

/**
 * Zoekpagina voor aangevers van overlijden
 */
public class BsZoekPageOverlijden extends ButtonPageTemplate {

  private final ModalCloseButton   buttonCloseModal = new ModalCloseButton();
  private DossierPersoon           dossierPersoon   = null;
  private Table                    table            = null;
  private GeldigheidField          geldigheidField;
  private Consumer<RedenVerplicht> redenVerplichtConsumer;

  public BsZoekPageOverlijden(DossierPersoon dossierPersoon) {
    this(dossierPersoon, null);
  }

  public BsZoekPageOverlijden(DossierPersoon dossierPersoon, Consumer<RedenVerplicht> redenVerplichtConsumer) {
    this.redenVerplichtConsumer = redenVerplichtConsumer;
    setDossierPersoon(dossierPersoon);
    addButton(buttonCloseModal);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setSpacing(true);
      setInfo("Selecteer een aangever door te dubbelklikken op een regel.");

      table = new Table();
      geldigheidField = new GeldigheidField() {

        @Override
        public void onChangeValue(GeldigheidStatus value) {
          table.init();
        }
      };

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
      getButtonLayout().addComponent(geldigheidField);
      getButtonLayout().align(geldigheidField, Alignment.MIDDLE_RIGHT);

      addComponent(table);
    }

    super.event(event);
  }

  public DossierPersoon getDossierPersoon() {
    return dossierPersoon;
  }

  public void setDossierPersoon(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;
  }

  public class Table extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      kopieerDossierPersoon((OverlijdenAangever) record.getObject());
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {
      setSelectable(true);
      setMultiSelect(true);

      addColumn("Code", 30);
      addColumn("Geboortedatum", 100);
      addColumn("Naam").setUseHTML(true);
    }

    @Override
    public void setRecords() {
      int nr = 0;
      for (OverlijdenAangever a : getServices().getOverlijdenGemeenteService()
          .getOverlijdenAangevers(geldigheidField.getValue())) {

        nr++;
        Record r = addRecord(a);
        r.addValue(nr);
        r.addValue(a.getGeboorte().getDatum_leeftijd());
        r.addValue(GeldigheidStatus.getHtml(a.getNaam().getPred_adel_voorv_gesl_voorn(), a));
      }

      super.setRecords();
    }

    private void kopieerDossierPersoon(OverlijdenAangever aangever) {

      BsPersoonUtils.reset(dossierPersoon);

      // Zoek op BSN indien aanwezig

      if (aangever.getBurgerServiceNummer().isCorrect()) {

        PLEArgs args = new PLEArgs();
        args.addNummer(astr(aangever.getBurgerServiceNummer().getValue()));
        PLResultComposite result = getApplication().getServices()
            .getPersonenWsService().getPersoonslijsten(args, true);

        if (result.getBasisPLWrappers().size() > 0) {
          BsPersoonUtils.kopieDossierPersoon(result.getBasisPLWrappers().get(0), dossierPersoon);
          dossierPersoon.setIdentificatieNodig(false);
          updateRedenVerplicht();
          buttonCloseModal.closeWindow();

        } else {
          new Message(getWindow(), "Geen zoekresultaten met BSN: " + aangever.getBurgerServiceNummer(),
              Message.TYPE_WARNING_MESSAGE);
        }
      } else {
        // Vul de rest van de gegevens in
        dossierPersoon.setVoornaam(aangever.getVoornamen());
        dossierPersoon.setNaamgebruik("");
        dossierPersoon.setVoorvoegsel(aangever.getVoorvoegsel());
        dossierPersoon.setGeslachtsnaam(aangever.getGeslachtsnaam());
        dossierPersoon.setTitel(aangever.getTitel());
        dossierPersoon.setGeslacht(aangever.getGeslacht());
        dossierPersoon.setGeboorteplaats(new FieldValue(aangever.getGeboorteplaats()));
        dossierPersoon.setBurgerServiceNummer(aangever.getBurgerServiceNummer());
        dossierPersoon.setDatumGeboorte(aangever.getDatumGeboorte());
        dossierPersoon.setGeboorteplaats(aangever.getGeboorteplaats());
        dossierPersoon.setGeboorteland(aangever.getGeboorteland());
        dossierPersoon.setIdentificatieNodig(false);
        updateRedenVerplicht();
        buttonCloseModal.closeWindow();
      }
    }
  }

  private void updateRedenVerplicht() {
    if (redenVerplichtConsumer != null) {
      redenVerplichtConsumer.accept(RedenVerplicht.BEGRAFENIS_ONDERNEMER);
    }
  }
}
