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

package nl.procura.gba.web.modules.persoonslijst.verblijfstitel.page1;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;

public class Page1Verblijfstitel extends PlDataPage {

  public Page1Verblijfstitel(BasePLSet set) {
    super("Verblijfstitel", set);
  }

  @Override
  public PlForm getMainForm() {

    return new Page1VerblijfstitelForm() {

      @Override
      public void setBean(Object bean) {

        Page1VerblijfstitelBean b = new Page1VerblijfstitelBean();

        String cVbt = getElementW(GBAElem.AAND_VBT).getCode();
        String sVbt = getElementW(GBAElem.AAND_VBT).getDescr();

        b.setVerblijfstitel(cVbt + " (" + sVbt + ")");
        b.setDatumIngang(getElementW(GBAElem.INGANGSDATUM_VBT).getDescr());
        b.setDatumEinde(getElementW(GBAElem.DATUM_EINDE_VBT).getDescr());

        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{ GELDIGHEID, OPNAME, ONJUIST, INONDERZOEK, DATUMINGANG, DATUMEINDE };
      }
    };
  }
}
