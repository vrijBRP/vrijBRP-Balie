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

package nl.procura.gba.web.modules.persoonslijst.inschrijving.page1;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.GELDIGHEID;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;

public class Page1Inschrijving extends PlDataPage {

  public Page1Inschrijving(BasePLSet set) {
    super("Inschrijving", set);
  }

  @Override
  public PlForm getMainForm() {

    return new Page1InschrijvingForm() {

      @Override
      public void setBean(Object bean) {

        Page1InschrijvingBean b = new Page1InschrijvingBean();

        String cGeheim = getElementW(GBAElem.IND_GEHEIM).getCode();
        String sGeheim = getElementW(GBAElem.IND_GEHEIM).getDescr();

        String cReden = getElementW(GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD).getCode();
        String sReden = getElementW(GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD).getDescr();

        b.setIndicatieGeheim("".equals(sGeheim) ? cGeheim : cGeheim + " (" + sGeheim + ")");
        b.setBlokkering(getElementW(GBAElem.DATUM_INGANG_BLOK_PL).getDescr());
        b.setOpschorting(getElementW(GBAElem.DATUM_OPSCH_BIJHOUD).getDescr());
        b.setReden("".equals(sReden) ? cReden : cReden + " (" + sReden + ")");
        b.setInschrijvingGBA(getElementW(GBAElem.DATUM_EERSTE_INSCHR_GBA).getDescr());
        b.setPkGemeente(getElementW(GBAElem.GEMEENTE_VAN_PK).getDescr());
        b.setPkConversie(getElementW(GBAElem.PK_GEGEVENS_VOL_CONVERT).getDescr());
        b.setVersienummer(getElementW(GBAElem.VERSIENR).getDescr());
        b.setDatumtijdStempel(getElementW(GBAElem.DATUMTIJDSTEMPEL).getDescr());
        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{ GELDIGHEID };
      }
    };
  }
}
