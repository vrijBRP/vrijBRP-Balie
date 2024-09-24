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

package nl.procura.gba.web.modules.bs.naturalisatie.page10;

import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_BEVOEGD;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_OPTIE;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_TOELICHTING;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_TOELICHTING2;
import static nl.procura.gba.web.services.bs.naturalisatie.enums.BevoegdTotVerzoekType.JA;
import static nl.procura.gba.web.services.bs.naturalisatie.enums.BevoegdTotVerzoekType.NEE;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;

public class Page10NaturalisatieForm2 extends GbaForm<Page10NaturalisatieBean> {

  public Page10NaturalisatieForm2(DossierNaturalisatie zaakDossier) {
    setCaption("Controlevragen");
    setReadThrough(true);
    setColumnWidths("200px", "");
    setOrder(F_BEVOEGD, F_TOELICHTING, F_OPTIE, F_TOELICHTING2);

    Page10NaturalisatieBean bean = new Page10NaturalisatieBean();
    bean.setBevoegd(zaakDossier.getBevoegdTotVerzoekType());
    bean.setToelichting(zaakDossier.getBevoegdIndienenToel());
    bean.setOptie(zaakDossier.getOptie());
    bean.setToelichting2(zaakDossier.getOptieToel());
    setBean(bean);
  }

  @Override
  public Page10NaturalisatieBean getNewBean() {
    return new Page10NaturalisatieBean();
  }

  public void update(DossierPersoon persoon) {
    Page10NaturalisatieBean bean = getNewBean();
    bean.setBevoegd(persoon.getLeeftijd() >= 18 ? JA : NEE);
    setBean(bean);
  }
}
