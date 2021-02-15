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

package nl.procura.gba.web.modules.persoonslijst.kind.page2;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2Kind extends PlDataPage {

  public Page2Kind(BasePLSet set) {

    super("Kind", set);

    getZoekButton().setVisible(true);
  }

  @Override
  public PlForm getMainForm() {

    return new Page2KindForm() {

      @Override
      public void setBean(Object bean) {

        Page2KindBean b = new Page2KindBean();

        BasePLValue bsn = getElementW(GBAElem.BSN);
        BasePLValue anr = getElementW(GBAElem.ANR);

        b.setBsn(new BsnFieldValue(astr(bsn.getVal()), bsn.getDescr()));
        b.setAnr(new AnrFieldValue(astr(anr.getVal()), anr.getDescr()));
        b.setGeslachtsnaam(getElementW(GBAElem.GESLACHTSNAAM).getDescr());
        b.setVoorvoegsel(getElementW(GBAElem.VOORV_GESLACHTSNAAM).getDescr());
        b.setTitel(getElementW(GBAElem.TITEL_PREDIKAAT).getDescr());
        b.setVoornaam(getElementW(GBAElem.VOORNAMEN).getDescr());
        b.setGeboren(new Geboorte(getGbaRecord()).getDatumLeeftijdPlaatsLand());
        b.setRegBetrek(getElementW(GBAElem.REG_BETREKK).getDescr());

        if (fil(bsn.getVal())) {
          BasePLExt kind = getPl(bsn.getVal());
          if (kind != null) {
            if (kind.isToonBeperking()) {
              b.setStatus("Verstrekkingsbeperking");
            } else {
              b.setGeslacht(kind.getPersoon().getGeslacht().getDescr());
              b.setAdres(kind.getVerblijfplaats().getAdres().getAdresPcWplGem());
              b.setStatus(kind.getPersoon().getStatus().getOpsomming());
            }
          }
        }
        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{ GELDIGHEID, OPNAME, ONJUIST, INONDERZOEK, DATUMINGANG, DATUMEINDE, AKTE, ONTLENING,
            DOCUMENT };
      }

      @Override
      public void setColumn(Column column, Field field, Property property) {

        if (property.is(ONJUIST, AKTE)) {
          getLayout().addBreak();
        }
      }
    };
  }
}
