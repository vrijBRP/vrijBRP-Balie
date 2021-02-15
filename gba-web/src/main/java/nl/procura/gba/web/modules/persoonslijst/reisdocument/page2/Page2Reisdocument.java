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

package nl.procura.gba.web.modules.persoonslijst.reisdocument.page2;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentUtils;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2Reisdocument extends PlDataPage {

  public Page2Reisdocument(BasePLSet set) {
    super("Reisdocument", set);
  }

  @Override
  public PlForm getMainForm() {

    return new Page2ReisdocumentForm() {

      @Override
      public void setBean(Object bean) {

        Page2ReisdocumentBean b = new Page2ReisdocumentBean();
        b.setReisdocument(getElementW(GBAElem.SOORT_NL_REISDOC).getDescr());
        b.setNummer(getElementW(GBAElem.NR_NL_REISDOC).getDescr());
        b.setAutoriteit(
            getElementW(GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC).getDescr());
        b.setDatumUitgifte(getElementW(GBAElem.DATUM_UITGIFTE_NL_REISDOC).getDescr());
        b.setDatumEinde(
            getElementW(GBAElem.DATUM_EINDE_GELDIG_NL_REISDOC).getDescr());

        String sAanduiding = ReisdocumentUtils.getInhoudingOmschrijving(getGbaRecord());

        if (!getElement(
            GBAElem.AAND_INH_VERMIS_NL_REISDOC).isAllowed()) {
          sAanduiding = getElementW(
              GBAElem.AAND_INH_VERMIS_NL_REISDOC).getDescr();
        }

        b.setInhoudingVermissing(sAanduiding);
        b.setSignalering(get(GBAElem.SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC));
        b.setBuitenlandsDocument(get(GBAElem.AAND_BEZIT_BUITENL_REISDOC));
        b.setLengte(getElementW(GBAElem.LENGTE_HOUDER).getDescr());

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

  private String get(GBAElem elementType) {
    BasePLElem element = getElement(elementType);
    String code = element.getValue().getCode();
    String oms = pos(code) ? "Ja" : "Nee";
    return element.isAllowed() ? oms : element.getValue().getDescr();
  }
}
