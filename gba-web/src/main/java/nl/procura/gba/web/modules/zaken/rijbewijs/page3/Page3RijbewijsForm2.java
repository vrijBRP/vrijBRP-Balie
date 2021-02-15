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

package nl.procura.gba.web.modules.zaken.rijbewijs.page3;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.zaken.rijbewijs.page3.Page3RijbewijsBean2.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;

class Page3RijbewijsForm2 extends RdwReadOnlyForm {

  Page3RijbewijsForm2(BasePLExt pl) {

    setCaption("Indicaties");
    setOrder(GBABESTENDIG, DAGEN185NL, NLNATIONALITEIT);
    setColumnWidths(WIDTH_130, "");

    boolean is185 = pl.getVerblijfplaats().is185DagenInNL();
    boolean isGBABes = pl.getNatio().isBestendig();
    boolean isNatNl = pl.getNatio().isNederlander();

    Page3RijbewijsBean2 b = new Page3RijbewijsBean2();
    b.setDagen185nl(setClass(is185, is185 ? "Ja" : "Nee"));
    b.setGbaBestendig(setClass(isGBABes, isGBABes ? "Ja" : "Nee"));
    b.setNlNationaliteit(setClass(isNatNl, isNatNl ? "Ja" : "Nee"));

    setBean(b);
  }
}
