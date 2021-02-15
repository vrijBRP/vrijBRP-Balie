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

package nl.procura.gba.web.modules.hoofdmenu.gv.overzicht;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;

public abstract class GvOverzichtLayoutForm extends ReadOnlyForm {

  public GvOverzichtLayoutForm(GvAanvraag aanvraag) {

    init(aanvraag);

    GvOverzichtLayoutBean b = new GvOverzichtLayoutBean();

    b.setDatumOntvangst(astr(aanvraag.getDatumIngang()));
    b.setAfnemer(astr(aanvraag.getAanvrager()));
    b.setGrondslag(astr(aanvraag.getGrondslagType()));

    StringBuilder toek = new StringBuilder();
    toek.append(astr(aanvraag.getToekenningType()));

    if (fil(aanvraag.getToekenningMotivering())) {
      toek.append(", motivering: ");
      toek.append(aanvraag.getToekenningMotivering());
    }

    b.setToekenning(toek.toString());
    b.setInformatievrager(aanvraag.getAanvrager());
    b.setTav(aanvraag.getTav());
    b.setAdresAanvrager(aanvraag.getAdres());
    b.setPc(astr(aanvraag.getPostcode() + " " + aanvraag.getPlaats()));
    b.setKenmerk(aanvraag.getKenmerk());
    b.setEmail(aanvraag.getEmail());

    setBean(b);
  }

  public abstract void init(GvAanvraag aanvraag);
}
