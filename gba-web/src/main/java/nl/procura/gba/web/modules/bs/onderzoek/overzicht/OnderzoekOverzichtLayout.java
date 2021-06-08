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

package nl.procura.gba.web.modules.bs.onderzoek.overzicht;

import static nl.procura.gba.web.modules.bs.onderzoek.overzicht.OnderzoekOverzichtBean.*;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.VerticalLayout;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.modules.bs.onderzoek.BetrokkenenTable;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.vaadin.component.label.Ruler;

public class OnderzoekOverzichtLayout extends VerticalLayout {

  private final DossierOnderzoek onderzoek;

  public OnderzoekOverzichtLayout(final DossierOnderzoek onderzoek) {

    this.onderzoek = onderzoek;

    setSpacing(true);

    Table1 table1 = new Table1();

    OnderzoekOverzichtForm form1 = new OnderzoekOverzichtForm(onderzoek) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Aanleiding");
        setOrder(BRON);
      }
    };

    OnderzoekOverzichtForm form2 = new OnderzoekOverzichtForm(onderzoek) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Melder");
        setColumnWidths("200px", "300px", "200px", "");
        switch (onderzoek.getOnderzoekBron()) {
          case BURGER:
            setColumnWidths("200px", "");
            setOrder(NAAM);
            break;
          case TMV:
            setOrder(DOSSIER_NR_TMV, KENMERK);
            break;
          case INSTANTIE:
            setOrder(INSTANTIE, TAV, ADRES, PC, PLAATS, KENMERK);
            break;
          case AMBTSHALVE:
            setOrder(AFDELING, KENMERK);
            break;
        }
      }
    };

    OnderzoekOverzichtForm form3 = new OnderzoekOverzichtForm(onderzoek) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Melding");
        setColumnWidths("200px", "");
        setOrder(DATUM_ONTVANGST, AARD, VERMOED_ADRES);
      }
    };

    OnderzoekOverzichtForm form4 = new OnderzoekOverzichtForm(onderzoek) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Beoordeling");
        setColumnWidths("200px", "300px", "200px", "");
        setOrder(BINNEN_5_DAGEN, DATUM_AANVANG_ONDERZOEK, AAND_GEG_ONDERZOEK, ONDERZOEK_DOOR_ANDERGEDAAN,
            VOLDOENDE_REDEN, TOELICHTING1);
      }
    };

    OnderzoekOverzichtForm form5 = new OnderzoekOverzichtForm(onderzoek) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Procesverloop");
        setColumnWidths("200px", "300px", "200px", "");
        setOrder(START_FASE1_OP, START_FASE1_TM, REACTIE_ONTVANGEN, VERVOLGACTIES, TOELICHTING2, DOORLOOPTIJD);
      }
    };

    OnderzoekOverzichtForm form6 = new OnderzoekOverzichtForm(onderzoek) {

      @Override
      public void setCaptionAndOrder() {
        setColumnWidths("200px", "300px", "200px", "");
        setOrder(START_FASE2_OP, START_FASE2_TM, EXTERNE_BRON1, EXTERNE_BRON2, ONDERZOEK_TER_PLAATSE,
            TOELICHTING3);
      }
    };

    OnderzoekOverzichtForm form7 = new OnderzoekOverzichtForm(onderzoek) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Resultaat");
        setColumnWidths("200px", "300px", "200px", "");
        switch (onderzoek.getResultaatOnderzoekBetrokkene()) {
          case ZELFDE:
            setOrder(BETROKKENEN, DATUM_EINDE_ONDERZOEK);
            break;
          case IMMIGRATIE:
          case BINNEN:
            setOrder(BETROKKENEN, RESULTAAT_NOGMAALS, RESULTAAT_ADRES, RESULTAAT_PC);
            break;
          case NAAR_ANDERE:
            setOrder(BETROKKENEN, RESULTAAT_NOGMAALS, RESULTAAT_ADRES, RESULTAAT_PCGEM);
            break;
          case EMIGRATIE:
            setOrder(BETROKKENEN, RESULTAAT_NOGMAALS, RESULTAAT_BUITENL1, RESULTAAT_BUITENL2,
                RESULTAAT_BUITENL3, RESULTAAT_LAND);
            break;
          case NAAR_ONBEKEND:
          case ONBEKEND:
            setOrder(BETROKKENEN, RESULTAAT_TOEL);
            break;
        }
      }
    };

    addComponent(table1);
    addComponent(form1);
    addComponent(form2);
    addComponent(form3);
    addComponent(form4);
    addComponent(form5);

    if (onderzoek.getFase1Vervolg() != null && onderzoek.getFase1Vervolg()) {
      addComponent(new Ruler());
      addComponent(form6);
    }

    addComponent(form7);
    table1.focus();
  }

  class Table1 extends BetrokkenenTable {

    @Override
    public void setColumns() {

      setClickable(true);

      addColumn("Nr.", 50);
      addColumn("Type persoon", 140);
      addColumn("Naam");
      addColumn("Geboorte");
      addColumn("Adres");
      addColumn("Aanduiding op persoonslijst", 450).setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      int nr = 0;
      for (DossierPersoon d : onderzoek.getDossier().getPersonen(DossierPersoonType.BETROKKENE)) {
        addPersoon(++nr, d);
      }
      super.setRecords();
    }

    @Override
    public void onDoubleClick(Record record) {
      DossierPersoon persoon = record.getObject(DossierPersoon.class);
      getApplication().goToPl(getWindow(), "zaken.onderzoek", PLEDatasource.STANDAARD,
          persoon.getBurgerServiceNummer().getStringValue());
      super.onDoubleClick(record);
    }

    private void addPersoon(int nr, DossierPersoon d) {
      if (d.isVolledig()) {
        Record r = addRecord(d);
        r.addValue(nr);
        r.addValue(d.getDossierPersoonType());
        String adres = d.getAdres().getAdres_pc_wpl();
        r.addValue(BsPersoonUtils.getNaam(d));
        r.addValue(d.getGeboorte().getDatum_leeftijd());
        r.addValue(fil(adres) ? adres : "Geen adres ingevoerd");
        r.addValue(getAanduidingStatus(d, getAanduidingen(onderzoek)));
      }
    }
  }
}
