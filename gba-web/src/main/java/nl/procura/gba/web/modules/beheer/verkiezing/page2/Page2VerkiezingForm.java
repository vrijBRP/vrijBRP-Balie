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

package nl.procura.gba.web.modules.beheer.verkiezing.page2;

import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_AANTAL_STEMPASSEN;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_AANTAL_VOlMACHTEN;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_AFK_VERK;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_BRIEFSTEMBEWIJS;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_DATUM_KAND;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_DATUM_VERK;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_GEMACHTIGDE_KIESREGISTER;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_GEMEENTE;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_KIEZERSPAS;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2VerkiezingBean.F_VERK;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page2VerkiezingForm extends GbaForm<Page2VerkiezingBean> {

  public Page2VerkiezingForm(KiesrVerk verkiezing) {
    setCaption("Verkiezing");
    setOrder(F_GEMEENTE, F_AFK_VERK, F_VERK, F_DATUM_KAND, F_DATUM_VERK, F_KIEZERSPAS, F_BRIEFSTEMBEWIJS,
        F_AANTAL_VOlMACHTEN, F_GEMACHTIGDE_KIESREGISTER, F_AANTAL_STEMPASSEN);
    setColumnWidths("180px", "");

    Page2VerkiezingBean bean = new Page2VerkiezingBean();
    bean.setGemeente(GbaTables.PLAATS.get(verkiezing.getCodeGemeente()));
    bean.setAfkVerkiezing(verkiezing.getAfkVerkiezing());
    bean.setVerkiezing(verkiezing.getVerkiezing());
    bean.setDatumKandidaatstelling(verkiezing.getdKand());
    bean.setDatumVerkiezing(verkiezing.getdVerk());
    bean.setKiezerspas(verkiezing.isIndKiezerspas());
    bean.setBriefstembewijs(verkiezing.isIndBriefstembewijs());
    bean.setGemachtigdeKiesregister(verkiezing.isIndGemachtKiesr());
    bean.setAantalVolmachten(verkiezing.getAantalVolm());
    setBean(bean);
  }

  public void setAantalStemgerechtigden(long aantalStemgerechtigden) {
    getBean().setAantalStempassen(aantalStemgerechtigden);
    setBean(getBean());
  }
}
