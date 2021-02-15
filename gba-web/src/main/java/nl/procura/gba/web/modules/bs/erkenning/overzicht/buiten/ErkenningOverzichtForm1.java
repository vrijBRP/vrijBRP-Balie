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

package nl.procura.gba.web.modules.bs.erkenning.overzicht.buiten;

import static nl.procura.gba.web.modules.bs.erkenning.overzicht.buiten.ErkenningOverzichtBean1.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ErkenningBuitenProweb;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ErkenningOverzichtForm1 extends ReadOnlyForm<ErkenningOverzichtBean1> {

  public ErkenningOverzichtForm1(ErkenningBuitenProweb erkenningBuitenProweb) {

    setColumnWidths("140px", "");

    ErkenningOverzichtBean1 bean = new ErkenningOverzichtBean1();

    FieldValue landErkenning = erkenningBuitenProweb.getLandErkenning();

    setOrder(LAND, PLAATS, DATUM, AKTENR, TOESTEMMINGGEVER_TYPE, NAAMSKEUZE, NAAMSAANDUIDING_TYPE,
        AFSTAMMINGSRECHT);

    String plaats = astr(erkenningBuitenProweb.getGemeente());

    if (!Landelijk.getNederland().equals(landErkenning)) {
      plaats = erkenningBuitenProweb.getBuitenlandsePlaats() + ", " + erkenningBuitenProweb.getLandErkenning();
    }

    bean.setPlaats(plaats);
    bean.setAktenr(erkenningBuitenProweb.getAktenummer());
    bean.setDatum(astr(erkenningBuitenProweb.getDatumErkenning()));

    ToestemminggeverType toestType = erkenningBuitenProweb.getToestemminggeverType();
    String toestemming = astr(toestType);

    if (toestType == ToestemminggeverType.RECHTBANK) {
      toestemming += " " + erkenningBuitenProweb.getRechtbank();
    }

    bean.setToestemminggeverType(toestemming);
    bean.setNaamskeuze(astr(erkenningBuitenProweb.getNaamskeuzeType()));
    bean.setNaamsAanduidingType(astr(erkenningBuitenProweb.getNaamskeuzePersoon()));
    bean.setAfstammingsrecht(astr(erkenningBuitenProweb.getLandAfstamming()));

    setBean(bean);
  }
}
