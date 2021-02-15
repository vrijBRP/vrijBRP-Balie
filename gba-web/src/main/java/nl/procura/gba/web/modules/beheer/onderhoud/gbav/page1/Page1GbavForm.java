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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.beheer.onderhoud.gbav.page1.Page1GbavBean.*;
import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;

public class Page1GbavForm extends GbaForm<Page1GbavBean> {

  public Page1GbavForm(GbaWsRestGbavAccount account) {

    setColumnWidths("160px", "");
    setOrder(ID, NAAM, TYPE, VERLOOPDATUM, GEBLOKKEERD);

    update(account);
  }

  public void update(GbaWsRestGbavAccount account) {

    Page1GbavBean bean = new Page1GbavBean();

    int dagen = account.getDagenGeldig();
    String dVerloopDatum = account.getDatumVerloop();
    String sVerloopDatum = pos(dagen) ? setClass("green", "(Nog " + dagen + " dagen over)")
        : setClass("red",
            "(Al " + (dagen - (dagen * 2)) + " dagen verlopen)");

    bean.setId(account.getGebruikersnaam());
    bean.setNaam(account.getNaam() + " (" + account.getOmschrijving() + ")");
    bean.setType(account.getType().getOms());

    bean.setGeblokkeerd(setClass(!account.isGeblokkeerd(), account.isGeblokkeerd() ? "Ja" : "Nee"));

    if (fil(dVerloopDatum)) {
      bean.setVerloopdatumWachtwoord(
          trim(MessageFormat.format("{0} {1}", date2str(dVerloopDatum), sVerloopDatum)));
    } else {
      bean.setVerloopdatumWachtwoord("Niet van toepassing");
    }

    setBean(bean);
  }
}
