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

package nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;

public class Page1Kiesrecht extends PlDataPage {

  public Page1Kiesrecht(BasePLSet set) {
    super("Kiesrecht", set);
  }

  @Override
  public PlForm getMainForm() {

    return new Page1KiesrechtForm() {

      @Override
      public void setBean(Object bean) {

        Page1KiesrechtBean b = new Page1KiesrechtBean();

        BasePLValue aand = getElementW(GBAElem.AAND_UITGESLOTEN_KIESR);
        BasePLValue aandE = getElementW(GBAElem.AANDUIDING_EURO_KIESR);

        if (!aand.getCode().isEmpty()) {
          b.setAanduidingUitgeslotenKiesrecht(aand.getCode() + " (" + aand.getDescr() + ")");
        }

        b.setEinddatumUitsluiting(getElementW(GBAElem.EINDDATUM_UITSLUIT_KIESR).getDescr());

        if (!aandE.getCode().isEmpty()) {
          b.setAanduidingEuropeesKiesrecht(aandE.getCode() + " (" + aandE.getDescr() + ")");
        }

        b.setDatumVerzoekMededeling(
            getElementW(GBAElem.DATUM_VERZ_OF_MED_EURO_KIESR).getDescr());
        b.setEinddatumUitsluitingEuropeesKiesrecht(
            getElementW(GBAElem.EINDDATUM_UITSL_EURO_KIESR).getDescr());

        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{ GELDIGHEID, ONTLENING, DOCUMENT };
      }
    };
  }
}
