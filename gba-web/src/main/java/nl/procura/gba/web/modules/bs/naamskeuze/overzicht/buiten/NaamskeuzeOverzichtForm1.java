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

package nl.procura.gba.web.modules.bs.naamskeuze.overzicht.buiten;

import static nl.procura.gba.web.modules.bs.naamskeuze.overzicht.buiten.NaamskeuzeOverzichtBean1.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb.NaamskeuzeBuitenProweb;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class NaamskeuzeOverzichtForm1 extends ReadOnlyForm<NaamskeuzeOverzichtBean1> {

  public NaamskeuzeOverzichtForm1(NaamskeuzeBuitenProweb naamskeuzeBuitenProweb) {

    setColumnWidths("140px", "");
    setOrder(LAND, PLAATS, DATUM, AKTENR, NAAMSAANDUIDING_TYPE);

    NaamskeuzeOverzichtBean1 bean = new NaamskeuzeOverzichtBean1();
    FieldValue landNaamskeuze = naamskeuzeBuitenProweb.getLand();

    String plaats = astr(naamskeuzeBuitenProweb.getGemeente());
    if (!Landelijk.getNederland().equals(landNaamskeuze)) {
      plaats = naamskeuzeBuitenProweb.getBuitenlandsePlaats() + ", " + naamskeuzeBuitenProweb.getLand();
    }

    bean.setPlaats(plaats);
    bean.setAktenr(naamskeuzeBuitenProweb.getAktenummer());
    bean.setDatum(astr(naamskeuzeBuitenProweb.getDatum()));
    bean.setNaamsAanduidingType(astr(naamskeuzeBuitenProweb.getNaamskeuzePersoon()));

    setBean(bean);
  }
}
