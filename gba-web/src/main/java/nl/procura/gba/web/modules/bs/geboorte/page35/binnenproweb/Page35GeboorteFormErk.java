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

import static nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb.Page35GeboorteBeanErk.*;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;

public class Page35GeboorteFormErk extends ReadOnlyForm<Page35GeboorteBeanErk> {

  public Page35GeboorteFormErk(DossierGeboorte geboorte) {

    setColumnWidths("140px", "");
    setOrder(GEMEENTE, DATUM, AKTENR, TOESTEMMINGGEVER, NAAMSKEUZE, NAAMSAANDUIDING_TYPE, RECHT);
    setGeboorte(geboorte);
  }

  @Override
  public Page35GeboorteBeanErk getNewBean() {
    return new Page35GeboorteBeanErk();
  }

  public void setGeboorte(DossierGeboorte geboorte) {
    Page35GeboorteBeanErk bean = new Page35GeboorteBeanErk();
    if (geboorte != null && geboorte.getVragen().heeftErkenningVoorGeboorte()) {
      DossierErkenning erkenning = geboorte.getErkenningVoorGeboorte();
      List<DossierAkte> aktes = erkenning.getDossier().getAktes();
      if (aktes.size() > 0) {
        bean.setAktenr(astr(aktes.get(0).getAkte()));
      }

      bean.setGemeente(erkenning.getGemeente().getDescription());
      bean.setDatum(astr(erkenning.getDossier().getDatumTijdInvoer().getFormatDate()));
      bean.setToestemminggever(erkenning.getToestemminggeverType().getOms() + " " + erkenning.getRechtbank());
      bean.setNaamskeuze(erkenning.getNaamskeuzeType().getType());
      bean.setNaamsAanduidingType(erkenning.getKeuzeVoorvoegsel() + " " + erkenning.getKeuzeGeslachtsnaam());
      bean.setRecht(astr(erkenning.getLandAfstammingsRecht()));
    }

    setBean(bean);
  }
}
