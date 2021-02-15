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

package nl.procura.gba.web.modules.persoonslijst.persoon.page1;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;

public class Page1Persoon extends PlDataPage {

  public Page1Persoon(BasePLSet set) {
    super("Persoon", set);
  }

  @Override
  public PlForm getMainForm() {

    return new Page1PersoonForm() {

      @Override
      public void setBean(Object bean) {

        Page1PersoonBean b = new Page1PersoonBean();

        b.setBsn(getElementW(GBAElem.BSN).getDescr());
        b.setAnr(getElementW(GBAElem.ANR).getDescr());
        b.setGeslachtsnaam(getElementW(GBAElem.GESLACHTSNAAM).getDescr());
        b.setVoorvoegsel(getElementW(GBAElem.VOORV_GESLACHTSNAAM).getDescr());
        b.setTitel(getElementW(GBAElem.TITEL_PREDIKAAT).getDescr());
        b.setVoornaam(getElementW(GBAElem.VOORNAMEN).getDescr());
        b.setGeboren(new Geboorte(getGbaRecord()).getDatumLeeftijdPlaatsLand());

        BasePLElem nge = getElement(GBAElem.AANDUIDING_NAAMGEBRUIK);
        BasePLValue ng = nge.getValue();

        if (nge.isAllowed()) {
          b.setNaamgebruik(ng.getCode() + ": " + ng.getDescr());
        }

        b.setGeslacht(getElementW(GBAElem.GESLACHTSAAND).getDescr());

        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{ GELDIGHEID, OPNAME, VORIGANR, VOLGENDANR, ONJUIST, INONDERZOEK, DATUMINGANG, DATUMEINDE,
            AKTE, ONTLENING, DOCUMENT };
      }
    };
  }
}
