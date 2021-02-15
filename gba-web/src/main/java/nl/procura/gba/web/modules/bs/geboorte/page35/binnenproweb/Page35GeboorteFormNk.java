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

package nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb;

import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanNk.*;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page35GeboorteFormNk extends ReadOnlyForm<Page35GeboorteBeanNk> {

  public Page35GeboorteFormNk(DossierGeboorte geboorte) {

    setColumnWidths("140px", "");
    setOrder(GEMEENTE, DATUM, AKTENR, GESLACHTSNAAM, VOORVOEGSEL, TITEL, NAAMSKEUZE_PERSOON);
    setGeboorte(geboorte);
  }

  @Override
  public Page35GeboorteBeanNk getNewBean() {
    return new Page35GeboorteBeanNk();
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanNk bean = new Page35GeboorteBeanNk();
    if (geboorte != null && geboorte.getVragen().heeftNaamskeuzeVoorGeboorte()) {
      DossierNaamskeuze naamskeuze = geboorte.getNaamskeuzeVoorGeboorte();
      List<DossierAkte> aktes = naamskeuze.getDossier().getAktes();

      if (aktes.size() > 0) {
        bean.setAktenr(astr(aktes.get(0).getAkte()));
      }

      bean.setGemeente(naamskeuze.getGemeente().getDescription());
      bean.setDatum(astr(naamskeuze.getDossier().getDatumTijdInvoer().getFormatDate()));
      bean.setGeslachtsnaam(naamskeuze.getKeuzeGeslachtsnaam());
      bean.setVoorv(new FieldValue(naamskeuze.getKeuzeVoorvoegsel()));
      bean.setTitel(naamskeuze.getKeuzeTitel());
      bean.setNaamskeuzePersoon(naamskeuze.getNaamskeuzePersoon());
    }

    setBean(bean);
  }
}
