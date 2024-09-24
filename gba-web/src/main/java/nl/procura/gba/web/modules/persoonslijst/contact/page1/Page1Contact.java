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

package nl.procura.gba.web.modules.persoonslijst.contact.page1;

import static nl.procura.gba.web.modules.persoonslijst.contact.page1.Page1ContactBean.EMAIL;
import static nl.procura.gba.web.modules.persoonslijst.contact.page1.Page1ContactBean.EMAIL_GELDIG_VANAF;
import static nl.procura.gba.web.modules.persoonslijst.contact.page1.Page1ContactBean.EMAIL_VER_IND;
import static nl.procura.gba.web.modules.persoonslijst.contact.page1.Page1ContactBean.TEL;
import static nl.procura.gba.web.modules.persoonslijst.contact.page1.Page1ContactBean.TEL_GELDIG_VANAF;
import static nl.procura.gba.web.modules.persoonslijst.contact.page1.Page1ContactBean.TEL_VER_IND;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;

public class Page1Contact extends PlDataPage {

  public Page1Contact(BasePLSet set) {
    super("Kiesrecht", set);
  }

  @Override
  public List<PlForm> getMainForms() {

    List<PlForm> forms = new ArrayList<>();
    forms.add(new Page1ContactForm("Telefoonnummer", TEL, TEL_VER_IND, TEL_GELDIG_VANAF) {

      @Override
      public void setBean(Object bean) {
        Page1ContactBean b = new Page1ContactBean();
        b.setTel(getElementW(GBAElem.TEL_NR).getDescr());
        b.setTelVerInd(getElementW(GBAElem.TEL_VERIFICATIE_IND).getDescr());
        b.setTelGeldigVanaf(getElementW(GBAElem.TEL_GELDIG_VANAF).getDescr());
        super.setBean(b);
      }
    });

    forms.add(new Page1ContactForm("E-mail", EMAIL, EMAIL_VER_IND, EMAIL_GELDIG_VANAF) {

      @Override
      public void setBean(Object bean) {
        Page1ContactBean b = new Page1ContactBean();
        b.setEmail(getElementW(GBAElem.EMAILADRES).getDescr());
        b.setEmailVerInd(getElementW(GBAElem.EMAIL_VERIFICATIE_IND).getDescr());
        b.setEmailGeldigVanaf(getElementW(GBAElem.EMAIL_GELDIG_VANAF).getDescr());
        super.setBean(b);
      }
    });
    return forms;
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{};
      }
    };
  }
}
