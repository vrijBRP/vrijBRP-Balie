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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean3.ADRES1;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean3.ADRES2;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean3.ADRES3;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean3.LAND;
import static nl.procura.standard.Globalfunctions.pos;

public class AdresForm3 extends AdresForm<AdresBean3> {

  public AdresForm3(OnderzoekAdres adres) {

    super();
    setOrder(ADRES1, ADRES2, ADRES3, LAND);

    AdresBean3 bean = new AdresBean3();
    bean.setAdres1(adres.getBuitenl1());
    bean.setAdres2(adres.getBuitenl2());
    bean.setAdres3(adres.getBuitenl3());

    if (pos(adres.getLand().getStringValue())) {
      bean.setLand(adres.getLand());
    }

    setBean(bean);
  }

}
