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

package nl.procura.gba.web.modules.bs.lv.overzicht;

import static nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBean1.AKTE_JAAR;
import static nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBean1.AKTE_NUMMER;
import static nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBean1.AKTE_PLAATS;
import static nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBean1.DATUM_LV;
import static nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBean1.HUIDIG_BRP_AKTE_NUMMER;
import static nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBean1.NIEUW_BRP_AKTE_NUMMER;
import static nl.procura.gba.web.modules.bs.lv.overzicht.LvOverzichtBean1.SOORT;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.lv.afstamming.LvField;
import nl.procura.vaadin.component.layout.Fieldset;

public class LvOverzichtLayout extends GbaVerticalLayout {

  private final DossierLv zaakDossier;

  public LvOverzichtLayout(final DossierLv zaakDossier) {
    this.zaakDossier = zaakDossier;
    setSpacing(true);
  }

  @Override
  public void attach() {
    if (getComponentCount() == 0) {
      addComponent(new Fieldset("Overzicht", new Table1()));
      addComponent(new Form1());
      addComponent(new Form2());

      Form3 form3 = new Form3();
      Form4 form4 = new Form4();

      if (form3.getOrder().length > 0) {
        addComponent(form3);
      }
      if (form4.getOrder().length > 0) {
        addComponent(form4);
      }
    }
    super.attach();
  }

  public class Table1 extends GbaTable {

    @Override
    public void setColumns() {
      addColumn("Type", 100);
      addColumn("Naam");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      for (DossierPersoon p : zaakDossier.getBetrokkenen()) {
        Record r = addRecord(p);
        r.addValue(p.getDossierPersoonType());
        r.addValue(p.getNaam().getPred_adel_voorv_gesl_voorn());
      }

      super.setRecords();
    }
  }

  public class Form1 extends LvOverzichtForm2 {

    private Form1() {
      super("Soort");
      setOrder(SOORT, DATUM_LV);
      setBean(zaakDossier);
    }
  }

  public class Form2 extends LvOverzichtForm2 {

    private Form2() {
      super("Geboorteaktegegevens");
      setOrder(AKTE_NUMMER, HUIDIG_BRP_AKTE_NUMMER, NIEUW_BRP_AKTE_NUMMER, AKTE_PLAATS, AKTE_JAAR);
      setBean(zaakDossier);
    }
  }

  public class Form3 extends LvOverzichtForm2 {

    private Form3() {
      super("Gegevens ontvangen document(en)");
      setOrder(LvField.getForm1(zaakDossier.getSoort()));
      setBean(zaakDossier);
    }
  }

  public class Form4 extends LvOverzichtForm2 {

    private Form4() {
      super("Inhoud document(en)");
      setOrder(LvField.getForm2(zaakDossier.getSoort()));
      setBean(zaakDossier);
    }
  }
}
