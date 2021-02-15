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

package nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage;

import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.utils.BsOuderUtils;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsNamenPage extends ButtonPageTemplate {

  private final DossierNamenrecht dossier;
  private final BsNamenWindow     namenWindow;
  private Table1                  table1 = new Table1();

  public BsNamenPage(BsNamenWindow namenWindow, DossierNamenrecht dossier) {

    H2 h2 = new H2("Beschikbare namen");

    addButton(buttonClose);
    getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().setExpandRatio(h2, 1f);
    getButtonLayout().setWidth("100%");

    this.namenWindow = namenWindow;
    this.dossier = dossier;

  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setSpacing(true);
      setInfo("De geslachtsnaam is nog niet bepaald. Selecteer de geslachtsnaam.");
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

    @Override
    public void onClick(Record record) {
      selectRecord(record);
      super.onClick(record);
    }

    @Override
    public void setColumns() {

      addColumn("Relatie");
      addColumn("Geslachtsnaam");
      addColumn("Opmerking");

      setSelectable(true);
      addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      String opmerkingPersoon1 = "";
      String opmerkingPersoon2 = "";

      DossierPersoon moeder = dossier.getMoeder();
      DossierPersoon partner = dossier.getVaderErkenner();
      String moederTypeDescr = moeder.getDossierPersoonType().getDescrExtra();
      String partnerTypeDescr = partner.getDossierPersoonType().getDescrExtra();

      int eerdereKinderenPersoon1 = BsOuderUtils.getEerdereKinderen(getServices(), dossier, false, true);
      int eerdereKinderenPersoon2 = BsOuderUtils.getEerdereKinderen(getServices(), dossier, true, true);

      String format1 = "Heeft 1 eerder kind met de {0}. Dit kind heeft de naam van {1}";
      String format2 = "Heeft {0} eerdere kinderen met {1}. Deze kinderen hebben de naam van {2}";

      switch (eerdereKinderenPersoon1) {
        case 0:
          break;

        case 1:
          opmerkingPersoon1 = MessageFormat.format(format1, partnerTypeDescr, moederTypeDescr);
          break;

        default:
          opmerkingPersoon1 = MessageFormat.format(format2, eerdereKinderenPersoon1, partnerTypeDescr, moederTypeDescr);
          break;
      }

      switch (eerdereKinderenPersoon2) {
        case 0:
          break;

        case 1:
          opmerkingPersoon2 = MessageFormat.format(format1, moederTypeDescr, partnerTypeDescr);
          break;

        default:
          opmerkingPersoon2 = MessageFormat.format(format2, eerdereKinderenPersoon2, moederTypeDescr, partnerTypeDescr);
          break;
      }

      add(moeder.getDossierPersoonType().getDescr(), moeder, opmerkingPersoon1);
      add(partner.getDossierPersoonType().getDescr(), partner, opmerkingPersoon2);
      add("Geen", null, null);

      super.setRecords();
    }

    private void add(String relatie, DossierPersoon persoon, String opmerking) {

      if (persoon != null && !persoon.isVolledig()) {
        return;
      }

      String naam = "";
      if (persoon != null) {
        // Als er sprake is van een namenreek (alleen geslachtsnaam) 
        // dan streepje bij geslachtsnaam
        naam = persoon.getNaam().getVoorv_gesl();
        String voorn = persoon.getNaam().getVoornamen();

        if (fil(naam) && emp(voorn)) {
          opmerking = naam + " (is namenreeks)";
          naam = "-";
        }
      }

      Record r = addRecord(persoon);
      r.addValue(relatie);
      r.addValue(persoon == null ? "zelf invullen" : naam);
      r.addValue(astr(opmerking));
    }

    private void selectRecord(Record record) {
      String naam = null;
      FieldValue voorvoegsel = null;
      FieldValue titel = null;
      DossierPersoonType type = DossierPersoonType.ONBEKEND;

      if (record.getObject() != null) {
        DossierPersoon persoon = (DossierPersoon) record.getObject();
        String pNaam = persoon.getNaam().getGeslachtsnaam();
        String pVoorn = persoon.getNaam().getVoornamen();
        String pVoorv = persoon.getNaam().getVoorvoegsel();
        String pTp = persoon.getNaam().getTitel();
        type = persoon.getDossierPersoonType();

        if (fil(pNaam)) {
          naam = pNaam;
        }

        if (fil(pVoorv)) {
          voorvoegsel = new FieldValue(pVoorv);
        }

        if (fil(pTp)) {
          titel = persoon.getTitel();
        }

        if (fil(pNaam) && emp(pVoorn)) {
          naam = "-";
        }
      }

      namenWindow.setNaam(naam, voorvoegsel, titel, type);
      ((ModalWindow) getWindow()).closeWindow();
    }
  }
}
