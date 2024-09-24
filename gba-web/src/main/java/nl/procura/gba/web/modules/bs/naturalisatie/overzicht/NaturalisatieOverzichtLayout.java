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

package nl.procura.gba.web.modules.bs.naturalisatie.overzicht;

import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_AANGEVER;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_ADVIES;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_ANDERE_OUDER_AKKOORD;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BASIS_VERZOEK;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BEREIDAFLEGGENVERKLARING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BEREIDAFSTANDNATIONALITEIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BERICHT_OMTRENT_TOELATING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BESLISSING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BETROKKENEBEKENDMETBETALING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BEVOEGD;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BEWIJSNOODAANGETOOND;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BEWIJSVANIDENTITEIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_BEWIJSVANNATIONALITEIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_CEREMONIE_1;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_CEREMONIE_2;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_CEREMONIE_3;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_DATUM_AANVRAAG;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_DATUM_BEVESTIGING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_DATUM_KONINKLIJK_BESLUIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_DATUM_UITREIKING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_DATUM_VERVAL;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_EINDE_TERMIJN;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_GELDIGEVERBLIJFSVERGUNNING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_GESLACHTSNAAMGEWIJZIGD;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_GESLACHTSNAAMVASTGESTELD;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_INFORMATIE_JUSTIS;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_KINDEREN_MEENATURALISEREN;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_MINDERJARIGE_KINDEREN_12;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_MINDERJARIGE_KINDEREN_16;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_NAAMSVASTSTELLINGOFWIJZIGING;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_NAAM_ANDERE_OUDER_WETTELIJK;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_NUMMER_KONINKLIJK_BESLUIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_OPTIE_MOGELIJK;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_VERKLARINGVERBLIJF;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_VERTEGENWOORDIGER;
import static nl.procura.gba.web.modules.bs.naturalisatie.overzicht.NaturalisatieOverzichtBean.F_VOORNAMENVASTGESTELD;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.VerticalLayout;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.modules.bs.naturalisatie.BetrokkenenTable;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.vaadin.component.layout.HLayout;

public class NaturalisatieOverzichtLayout extends VerticalLayout {

  private final DossierNaturalisatie naturalisatie;

  public NaturalisatieOverzichtLayout(final DossierNaturalisatie naturalisatie) {

    this.naturalisatie = naturalisatie;

    setSpacing(true);

    Table1 table1 = new Table1();

    NaturalisatieOverzichtForm form1 = new NaturalisatieOverzichtForm(naturalisatie) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Procedurekeuze");
        setColumnWidths("300px", "");
        setOrder(F_AANGEVER, F_BEVOEGD, F_OPTIE_MOGELIJK);
      }
    };

    NaturalisatieOverzichtForm form2 = new NaturalisatieOverzichtForm(naturalisatie) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Optie / naturalisatie");
        setColumnWidths("300px", "");
        setOrder(F_BASIS_VERZOEK, F_KINDEREN_MEENATURALISEREN, F_VERTEGENWOORDIGER);
      }
    };

    NaturalisatieOverzichtForm form3 = new NaturalisatieOverzichtForm(naturalisatie) {

      @Override
      public void setCaptionAndOrder() {
        setWidth("600px");
        setCaption("Toetsing");
        setColumnWidths("300px", "");
        setOrder(F_VERKLARINGVERBLIJF, F_BEREIDAFLEGGENVERKLARING, F_BETROKKENEBEKENDMETBETALING,
            F_BEREIDAFSTANDNATIONALITEIT, F_BEWIJSVANIDENTITEIT, F_BEWIJSVANNATIONALITEIT,
            F_BEWIJSNOODAANGETOOND, F_GELDIGEVERBLIJFSVERGUNNING);
      }
    };

    NaturalisatieOverzichtForm form4 = new NaturalisatieOverzichtForm(naturalisatie) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Naamvaststelling");
        setColumnWidths("200px", "");
        setOrder(F_NAAMSVASTSTELLINGOFWIJZIGING, F_GESLACHTSNAAMVASTGESTELD,
            F_VOORNAMENVASTGESTELD, F_GESLACHTSNAAMGEWIJZIGD);

      }
    };

    NaturalisatieOverzichtForm form5 = new NaturalisatieOverzichtForm(naturalisatie) {

      @Override
      public void setCaptionAndOrder() {
        setWidth("600px");
        setCaption("Behandeling - aanvullingen n.a.v. aanvraag");
        setColumnWidths("300px", "");
        setOrder(F_BERICHT_OMTRENT_TOELATING, F_MINDERJARIGE_KINDEREN_12, F_MINDERJARIGE_KINDEREN_16,
            F_ANDERE_OUDER_AKKOORD, F_NAAM_ANDERE_OUDER_WETTELIJK, F_INFORMATIE_JUSTIS, F_DATUM_AANVRAAG);

      }
    };

    NaturalisatieOverzichtForm form6 = new NaturalisatieOverzichtForm(naturalisatie) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Behandeling - termijn / beslissing / advies");
        setColumnWidths("200px", "");
        setOrder(F_EINDE_TERMIJN, F_BESLISSING, F_ADVIES, F_DATUM_BEVESTIGING,
            F_DATUM_KONINKLIJK_BESLUIT, F_NUMMER_KONINKLIJK_BESLUIT);
      }
    };

    NaturalisatieOverzichtForm form7 = new NaturalisatieOverzichtForm(naturalisatie) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Ceremonie");
        setColumnWidths("300px", "");
        setOrder(F_CEREMONIE_1, F_CEREMONIE_2, F_CEREMONIE_3, F_DATUM_UITREIKING, F_DATUM_VERVAL);
      }
    };

    addComponent(form1);
    addComponent(form2);
    addComponent(new HLayout(form3, form4).widthFull().margin(false).addExpandComponent(form4, 1F));
    addComponent(new HLayout(form5, form6).widthFull().margin(false).addExpandComponent(form6, 1F));
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

      super.setColumns();
    }

    @Override
    public void setRecords() {
      int nr = 0;
      for (DossierPersoon d : naturalisatie.getDossier().getPersonen(DossierPersoonType.BETROKKENE)) {
        addPersoon(++nr, d);
      }
      super.setRecords();
    }

    @Override
    public void onDoubleClick(Record record) {
      DossierPersoon persoon = record.getObject(DossierPersoon.class);
      getApplication().goToPl(getWindow(), "zaken.naturalisatie", PLEDatasource.STANDAARD,
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
      }
    }
  }
}
