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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.joinWith;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.utils.BsOuderUtils;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

public class BsNamenPage extends ButtonPageTemplate {

  private final DossierNamenrecht dossier;
  private final BsNamenWindow     namenWindow;
  private Table1                  table1 = new Table1();

  public BsNamenPage(BsNamenWindow namenWindow, DossierNamenrecht dossier) {
    H2 h2 = new H2("Naamselectie");

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
      setInfo("De geslachtsnaam kan geselecteerd worden uit de lijst met suggesties die hieronder is aangegeven. "
          + "Let erop dat het voorkomen in deze lijst niet per definitie betekent dat deze naam in dit "
          + "geval mogelijk is voor deze persoon. Dat is afhankelijk van de van toepassing zijnde wetgeving. "
          + "De lijst is bedoeld als hulpmiddel.");
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

      addColumn("Relatie", 200);
      addColumn("Voorvoegsel", 100);
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

      String moederDescr = moeder.getDossierPersoonType().getDescr();
      String partnerDescr = partner.getDossierPersoonType().getDescr();
      add(moederDescr, singletonList(moeder), opmerkingPersoon1);
      add(partnerDescr, singletonList(partner), opmerkingPersoon2);
      add(partnerDescr + " + " + moederDescr, asList(partner, moeder), opmerkingPersoon2);
      add(moederDescr + " + " + partnerDescr, asList(moeder, partner), opmerkingPersoon2);
      add("Geen", new ArrayList<>(), null);

      super.setRecords();
    }

    private void add(String relatie, List<DossierPersoon> personen, String opmerking) {
      for (DossierPersoon persoon : personen) {
        if (persoon != null && !persoon.isVolledig()) {
          return;
        }
      }

      NaamsKeuze naamsKeuze = toNaamsKeuze(personen);
      Record r = addRecord(naamsKeuze);
      r.addValue(relatie);
      r.addValue(naamsKeuze.getVoorvoegsel());
      r.addValue(personen.isEmpty() ? "zelf invullen" : naamsKeuze.geslachtsnaam);
      r.addValue(trim(joinWith(". ", opmerking, naamsKeuze.getOpmerking())));
    }

    private void selectRecord(Record record) {
      NaamsKeuze naamsKeuze = new NaamsKeuze();
      if (record.getObject() != null) {
        naamsKeuze = (NaamsKeuze) record.getObject();
      }
      namenWindow.setNaam(naamsKeuze);
      ((ModalWindow) getWindow()).closeWindow();
    }
  }

  private NaamsKeuze toNaamsKeuze(List<DossierPersoon> personen) {
    NaamsKeuze naamsKeuze = new NaamsKeuze();
    String naam = "";
    FieldValue voorv = null;
    FieldValue titel = null;
    NaamsPersoonType type = NaamsPersoonType.ONBEKEND;

    int personNr = 0;
    for (DossierPersoon persoon : personen) {
      if (persoon != null && persoon.isVolledig()) {
        personNr++;
        DossierPersoonType persoonType = persoon.getDossierPersoonType();
        String pNaam = persoon.getNaam().getGeslachtsnaam();
        String pVoorn = persoon.getNaam().getVoornamen();
        String pVoorv = persoon.getNaam().getVoorvoegsel();
        String pTp = persoon.getNaam().getTitel();
        boolean namenreeks = fil(pNaam) && emp(pVoorn);

        if (namenreeks) {
          naamsKeuze.setOpmerking(persoonType + " heeft namenreeks");
        }

        if (fil(pNaam) && !namenreeks) {
          if (personNr == 2) {
            naam += (" " + pVoorv + " " + pNaam);
          } else {
            naam = pNaam;
          }
        }
        if (personNr == 1 && fil(pVoorv)) {
          voorv = new FieldValue(pVoorv);
        }
        if (fil(pTp)) {
          titel = persoon.getTitel();
        }
        if (dossier instanceof DossierNaamskeuze) {
          titel = titelOpschonen(titel, persoonType);
        }
        type = toType(persoon);
      }

      if (personen.size() == 2) {
        NaamsPersoonType type1 = toType(personen.get(0));
        NaamsPersoonType type2 = toType(personen.get(1));
        type = NaamsPersoonType.getType(type1, type2);
      }

      naamsKeuze.setVoorvoegsel(voorv);
      naamsKeuze.setTitel(titel);
      naamsKeuze.setGeslachtsnaam(normalizeSpace(defaultIfBlank(naam, "-")));
      naamsKeuze.setType(type);
    }
    return naamsKeuze;
  }

  private NaamsPersoonType toType(DossierPersoon persoon) {
    switch (persoon.getDossierPersoonType()) {
      case MOEDER:
        return NaamsPersoonType.MOEDER;
      case VADER_DUO_MOEDER:
        return NaamsPersoonType.VADER_DUO_MOEDER;
      case PARTNER:
      case PARTNER_ANDERE_OUDER:
        return NaamsPersoonType.PARTNER;
      case ERKENNER:
        return NaamsPersoonType.ERKENNER;
      default:
        return NaamsPersoonType.ONBEKEND;
    }
  }

  /*
  Een adellijke titel of een adellijk predikaat gaat alleen op de kinderen
  over als zij de geslachtsnaam van hun adellijke vader verkrijgen.
  */
  private FieldValue titelOpschonen(FieldValue titel, DossierPersoonType type) {
    return dossier.getDossier()
        .getPersonen(type)
        .stream()
        .anyMatch(p -> Geslacht.MAN == p.getGeslacht())
        ? titel
        : null;
  }

  @Data
  public static class NaamsKeuze {

    private String           geslachtsnaam;
    private FieldValue       voorvoegsel;
    private FieldValue       titel;
    private NaamsPersoonType type = NaamsPersoonType.ONBEKEND;
    private String           opmerking;
  }
}
