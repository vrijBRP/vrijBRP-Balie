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

package nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1;

import static nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1.OntbindingOverzichtBean1.*;
import static nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1.OntbindingOverzichtBean2.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis.HUWELIJK;
import static nl.procura.gba.web.services.bs.ontbinding.WijzeBeeindigingVerbintenis.RECHTERLIJKE_UITSPRAAK;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;

public class OntbindingOverzichtLayout extends VerticalLayout {

  private final DossierOntbinding ontbinding;

  public OntbindingOverzichtLayout(final DossierOntbinding ontbinding) {

    this.ontbinding = ontbinding;

    setSpacing(true);

    addComponent(new Fieldset("Overzicht", new Table1()));

    String tp1 = astr(ontbinding.getTitelPartner1());
    String voorv1 = ontbinding.getVoorvPartner1();
    String naam1 = ontbinding.getNaamPartner1();
    String ng1 = ontbinding.getNaamGebruikPartner1();

    String tp2 = astr(ontbinding.getTitelPartner2());
    String voorv2 = ontbinding.getVoorvPartner2();
    String naam2 = ontbinding.getNaamPartner2();
    String ng2 = ontbinding.getNaamGebruikPartner2();

    Form3 form3 = new Form3(tp1, voorv1, naam1, ng1);
    Form4 form4 = new Form4(tp2, voorv2, naam2, ng2);
    form3.setWidth("565px");

    addComponent(new Form1());

    if (SoortVerbintenis.GPS.equals(ontbinding.getSoortVerbintenis())) {
      addComponent(new Form5());
    }

    addComponent(new Form2());
    addComponent(new HLayout().sizeFull().add(form3).addExpandComponent(form4));
  }

  public class Form1 extends OntbindingOverzichtForm1 {

    public Form1() {
      super(ontbinding);
    }

    @Override
    public void init() {

      setCaption("Huidige sluitinggegevens");
      setColumnWidths("220px", "");
      setOrder(SLUITING, AKTE);
    }
  }

  public class Form2 extends OntbindingOverzichtForm1 {

    public Form2() {
      super(ontbinding);
    }

    @Override
    public void init() {

      setCaption("Brondocument - gegevens ontvangen document(en)");

      boolean isHuwelijk = (HUWELIJK == getDossierOntbinding().getSoortVerbintenis());
      boolean isRechterlijkeUitspraak = (RECHTERLIJKE_UITSPRAAK == getDossierOntbinding()
          .getWijzeBeeindigingVerbintenis());

      if (isHuwelijk || isRechterlijkeUitspraak) {
        setOrder(UITSPRAAK, DATUM_GEWIJSDE, VERZOEK_INSCHRIJVING_DOOR, DATUM_VERZOEK, BINNEN_TERMIJN);
      } else {
        setOrder(DATUM_VERKLARING, ONDERTEKEND_DOOR, DATUM_ONDERTEKENING);
      }
    }
  }

  public class Form3 extends OntbindingOverzichtForm2 {

    public Form3(String tp, String voorv, String naam, String naamgebruik) {
      super(tp, voorv, naam, naamgebruik);
    }

    @Override
    public void init() {

      setCaption("Naam(gebruik) partner 1");
      setOrder(TITEL, NAAM, NAAMGEBRUIK);
    }
  }

  public class Form4 extends OntbindingOverzichtForm2 {

    public Form4(String tp, String voorv, String naam, String naamgebruik) {
      super(tp, voorv, naam, naamgebruik);
    }

    @Override
    public void init() {

      setCaption("Naam(gebruik) partner 2");
      setOrder(TITEL, NAAM, NAAMGEBRUIK);
      setColumnWidths("220px", "");
    }
  }

  public class Form5 extends OntbindingOverzichtForm1 {

    public Form5() {
      super(ontbinding);
    }

    @Override
    public void init() {

      setCaption("Brondocument - wijze van beëindiging");
      setOrder(DOOR);
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

      for (DossierPersoon p : ontbinding.getPartners()) {

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

      super.setRecords();
    }
  }
}
