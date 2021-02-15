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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.overzicht;

import static nl.procura.gba.common.MiscUtils.setClass;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;

public class OverlijdenBuitenlandOverzichtForm1 extends ReadOnlyForm {

  public OverlijdenBuitenlandOverzichtForm1(DossierOverlijdenBuitenland dossier) {

    setReadonlyAsText(true);

    setColumnWidths("170px", "");

    init();

    OverlijdenBuitenlandOverzichtBean1 bean = (OverlijdenBuitenlandOverzichtBean1) getNewBean();

    String datum = dossier.getDatumOverlijden().getFormatDate();
    String plaats = dossier.getPlaatsOverlijden();
    String land = dossier.getLandOverlijden().getDescription();

    bean.setDatum(datum + " te " + plaats + ", " + land);
    bean.setOntvangenOp(dossier.getDatumOntvangst().getFormatDate());
    bean.setVan(dossier.getAfgeverType().getOms());
    bean.setType(dossier.getTypeBronDocument().getOms());
    bean.setLandAfgifte(dossier.getLandAfgifte().getDescription());
    bean.setVoldoetAanEisen(setClass(dossier.isVoldoetAanEisen(), dossier.isVoldoetAanEisen() ? "Ja" : "Nee"));

    setBean(bean);
  }

  @Override
  public OverlijdenBuitenlandOverzichtBean1 getBean() {
    return (OverlijdenBuitenlandOverzichtBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new OverlijdenBuitenlandOverzichtBean1();
  }

  protected void init() {
  }
}
