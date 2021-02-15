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

package nl.procura.gba.web.modules.zaken.verhuizing.page2;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;

public class Page2VerhuizingTable1 extends GbaTable {

  private VerhuisAanvraag aanvraag = null;

  public Page2VerhuizingTable1(VerhuisAanvraag aanvraag) {
    setCaption("Verhuizende personen");
    setAanvraag(aanvraag);
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Verwerken", 80).setUseHTML(true);
    addColumn("Persoon");
    addColumn("Aangifte", 170);
    addColumn("Geslacht", 80);
    addColumn("Geboren", 130);

    super.setColumns();
  }

  @Override
  public void setPageLength(int pageLength) {
    super.setPageLength(getRecords().size() + 1);
  }

  @Override
  public void setRecords() {

    for (VerhuisPersoon p : getAanvraag().getPersonen()) {
      Record r = addRecord(p);
      String naam = p.getPersoon().getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
      String leeftijd = p.getPersoon().getPersoon().getFormats().getGeboorte().getDatum_leeftijd();

      r.addValue(activate(p.isGeenVerwerking() ? "Nee" : "Ja", p.isGeenVerwerking()));
      r.addValue(naam);
      r.addValue(p.getAangifte());
      r.addValue(p.getPersoon().getPersoon().getGeslacht());
      r.addValue(leeftijd);
    }
  }

  private Object activate(Object waarde, boolean geenVerwerking) {
    return MiscUtils.setClass(!geenVerwerking, astr(waarde));
  }
}
