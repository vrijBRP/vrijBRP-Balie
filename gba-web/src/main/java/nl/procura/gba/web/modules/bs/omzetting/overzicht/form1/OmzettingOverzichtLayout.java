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

package nl.procura.gba.web.modules.bs.omzetting.overzicht.form1;

import static nl.procura.gba.web.modules.bs.omzetting.overzicht.form1.OmzettingOverzichtBean1.*;
import static nl.procura.gba.web.modules.bs.omzetting.overzicht.form1.OmzettingOverzichtBean2.NAAM;
import static nl.procura.gba.web.modules.bs.omzetting.overzicht.form1.OmzettingOverzichtBean2.NAAMGEBRUIK;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.vaadin.component.layout.Fieldset;

public class OmzettingOverzichtLayout extends VerticalLayout {

  private final DossierOmzetting omzetting;

  public OmzettingOverzichtLayout(final DossierOmzetting omzetting) {

    this.omzetting = omzetting;

    setSpacing(true);

    addComponent(new Fieldset("Overzicht", new Table1()));
    addComponent(new Table2());

    String tp1 = astr(omzetting.getTitelPartner1());
    String voorv1 = omzetting.getVoorvPartner1();
    String naam1 = omzetting.getNaamPartner1();
    String ng1 = omzetting.getNaamGebruikPartner1();

    String tp2 = astr(omzetting.getTitelPartner2());
    String voorv2 = omzetting.getVoorvPartner2();
    String naam2 = omzetting.getNaamPartner2();
    String ng2 = omzetting.getNaamGebruikPartner2();

    Form3 form3 = new Form3(tp1, voorv1, naam1, ng1);
    Form4 form4 = new Form4(tp2, voorv2, naam2, ng2);
    form3.setWidth("565px");

    HorizontalLayout naamgebruikLayout = new HorizontalLayout();
    naamgebruikLayout.setSizeFull();
    naamgebruikLayout.setSpacing(true);
    naamgebruikLayout.addComponent(form3);
    naamgebruikLayout.addComponent(form4);
    naamgebruikLayout.setExpandRatio(form4, 1f);

    addComponent(naamgebruikLayout);
    addComponent(new Form1());
    addComponent(new Form2());
    addComponent(new Fieldset("Vereisten", new OverzichtVereisteTable(omzetting.getDossier().getVereisten())));
  }

  public class Form1 extends OmzettingOverzichtForm1 {

    public Form1() {
      super(omzetting);
    }

    @Override
    public void init() {

      setCaption("Planning");
      setColumnWidths("150px", "400px", "150px", "");

      setOrder(LOCATIE, LOCATIEDATUMTIJD, LOCATIESTATUS, LOCATIEOPTIES, LOCATIETOELICHTING);
    }
  }

  public class Form2 extends OmzettingOverzichtForm1 {

    public Form2() {
      super(omzetting);
    }

    @Override
    public void init() {

      setCaption("Bijzonderheden");

      setOrder(GETUIGEN, GEMEENTEGETUIGEN);
    }
  }

  public class Form3 extends OmzettingOverzichtForm2 {

    public Form3(String tp, String voorv, String naam, String naamgebruik) {
      super(tp, voorv, naam, naamgebruik);
    }

    @Override
    public void init() {

      setCaption("Naam(gebruik) partner 1");

      setOrder(NAAM, NAAMGEBRUIK);
    }
  }

  public class Form4 extends OmzettingOverzichtForm2 {

    public Form4(String tp, String voorv, String naam, String naamgebruik) {
      super(tp, voorv, naam, naamgebruik);
    }

    @Override
    public void init() {

      setCaption("Naam(gebruik) partner 2");

      setOrder(NAAM, NAAMGEBRUIK);
    }
  }

  class Table1 extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Type", 100);
      addColumn("Inschreven", 232);
      addColumn("Naam");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      for (DossierAkte akte : omzetting.getDossier().getAktes()) {
        for (DossierPersoon person : BsPersoonUtils.sort(akte.getPersonen())) {

          Record r = addRecord(person);
          String naam = person.getNaam().getPred_adel_voorv_gesl_voorn();
          r.addValue(person.getDossierPersoonType());
          String adres = person.getWoongemeente().getDescription();

          if (person.isRNI()) {
            adres = "RNI";

          } else if (!Landelijk.isNederland(person.getLand())) {
            adres = person.getLand().getDescription();
          }

          r.addValue((fil(adres) ? adres : "-") + "\n");
          r.addValue(fil(naam) ? naam : "Nog niet aangegeven");
        }
      }

      super.setRecords();
    }
  }

  class Table2 extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Aktenummer", 100);
      addColumn("Type");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      for (DossierAkte akte : omzetting.getDossier().getAktes()) {

        Record r = addRecord(akte);
        r.addValue(akte.getDescription());
        r.addValue(omzetting.getDossier().getType());
      }

      super.setRecords();
    }
  }
}
