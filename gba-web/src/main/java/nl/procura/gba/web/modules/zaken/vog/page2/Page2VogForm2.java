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

package nl.procura.gba.web.modules.zaken.vog.page2;

import static nl.procura.gba.web.modules.zaken.vog.page2.Page2VogBean2.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraagBelanghebbende;

public class Page2VogForm2 extends Page2VogForm {

  public Page2VogForm2(VogAanvraag aanvraag) {

    setCaption("Belanghebbende");
    setOrder(NAAM, VERTEGENWOORDIGER, ADRES, PCPLAATS, LAND, TEL);
    setColumnWidths(WIDTH_130, "", "130px", "200px");

    Page2VogBean2 b = new Page2VogBean2();

    VogAanvraagBelanghebbende a = aanvraag.getBelanghebbende();

    b.setNaam(a.getNaam());
    b.setVertegenwoordiger(a.getVertegenwoordiger());

    Adresformats af = new Adresformats();
    af.setValues(a.getStraat(), astr(a.getHnr()), a.getHnrL(), a.getHnrT(), "", "", "", "", "", "", "", "", "", "",
        "", "");

    b.setAdres(af.getAdres());
    b.setPcPlaats(a.getPc() + " " + a.getPlaats());
    b.setLand(a.getLand().getDescription());
    b.setTel(a.getTel());

    setBean(b);
  }
}
