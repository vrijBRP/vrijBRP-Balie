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

package nl.procura.gba.web.modules.bs.huwelijk.overzicht.form1;

import static nl.procura.gba.web.modules.bs.huwelijk.overzicht.form1.HuwelijkOverzichtBean1.*;
import static nl.procura.gba.web.modules.bs.huwelijk.overzicht.form1.HuwelijkOverzichtBean2.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.vaadin.component.layout.Fieldset;

public class HuwelijkOverzichtLayout extends VerticalLayout {

  private final DossierHuwelijk huwelijk;

  public HuwelijkOverzichtLayout(final DossierHuwelijk huwelijk) {
    this.huwelijk = huwelijk;
    setSpacing(true);
  }

  @Override
  public void attach() {

    updateComponents();

    super.attach();
  }

  /**
   * Na het updaten van de persoonsgevens via de controle moeten de
   * layouts wel opnieuw worden toegevoegd
   */
  private void updateComponents() {

    removeAllComponents();

    addComponent(new Fieldset("Overzicht", new Table1()));
    addComponent(new Table2());

    String tp1 = astr(huwelijk.getTitelPartner1());
    String voorv1 = huwelijk.getVoorvPartner1();
    String naam1 = huwelijk.getNaamPartner1();
    String ng1 = huwelijk.getNaamGebruikPartner1();

    String tp2 = astr(huwelijk.getTitelPartner2());
    String voorv2 = huwelijk.getVoorvPartner2();
    String naam2 = huwelijk.getNaamPartner2();
    String ng2 = huwelijk.getNaamGebruikPartner2();

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
    addComponent(new Fieldset("Vereisten", new OverzichtVereisteTable(huwelijk.getDossier().getVereisten())));
  }

  public class Form1 extends HuwelijkOverzichtForm1 {

    public Form1() {
      super(huwelijk);
    }

    @Override
    public void init() {

      setCaption("Planning");
      setColumnWidths("150px", "400px", "150px", "");

      setOrder(SOORT, LOCATIE, LOCATIEDATUMTIJD, LOCATIESTATUS, LOCATIEOPTIES, LOCATIETOELICHTING);
    }
  }

  public class Form2 extends HuwelijkOverzichtForm1 {

    public Form2() {
      super(huwelijk);
    }

    @Override
    public void init() {

      setCaption("Bijzonderheden");

      setOrder(GETUIGEN, GEMEENTEGETUIGEN);
    }
  }

  public class Form3 extends HuwelijkOverzichtForm2 {

    public Form3(String tp, String voorv, String naam, String naamgebruik) {
      super(tp, voorv, naam, naamgebruik);
    }

    @Override
    public void init() {

      setCaption("Naam(gebruik) partner 1");

      setOrder(TITEL, NAAM, NAAMGEBRUIK);
    }
  }

  public class Form4 extends HuwelijkOverzichtForm2 {

    public Form4(String tp, String voorv, String naam, String naamgebruik) {
      super(tp, voorv, naam, naamgebruik);
    }

    @Override
    public void init() {

      setCaption("Naam(gebruik) partner 2");

      setOrder(TITEL, NAAM, NAAMGEBRUIK);
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

      for (DossierAkte akte : huwelijk.getDossier().getAktes()) {

        for (DossierPersoon p : BsPersoonUtils.sort(akte.getPersonen())) {

          Record r = addRecord(p);

          String naam = p.getNaam().getPred_adel_voorv_gesl_voorn();
          r.addValue(p.getDossierPersoonType());

          String adres = p.getWoongemeente().getDescription();

          if (p.isRNI()) {
            adres = "RNI";
          } else if (!Landelijk.isNederland(p.getLand())) {
            adres = p.getLand().getDescription();
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

      for (DossierAkte akte : huwelijk.getDossier().getAktes()) {

        Record r = addRecord(akte);
        r.addValue(akte.getDescription());
        r.addValue(huwelijk.getDossier().getType());
      }

      super.setRecords();
    }
  }
}
