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

package nl.procura.gba.web.modules.zaken.kassa.page1;

import static nl.procura.gba.web.modules.zaken.kassa.page1.Page1KassaBean.*;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;

public class Page1KassaForm extends ReadOnlyForm {

  public Page1KassaForm() {

    setCaption("Overzicht kassa");

    setColumnWidths("100px", "");
    setOrder(NAAM, ADRES, PC);

    setBean(new Page1KassaBean());
  }

  @Override
  public void attach() {
    init();
    super.attach();
  }

  @Override
  public Page1KassaBean getBean() {
    return (Page1KassaBean) super.getBean();
  }

  public void init() {

    if (getApplication() != null) {

      List<KassaProductAanvraag> aanvragen = getApplication().getServices().getKassaService()
          .getProductenInWinkelwagen();

      Page1KassaBean bean = new Page1KassaBean();

      if (aanvragen.size() > 0) {

        KassaProductAanvraag kpa = aanvragen.get(0);
        BasePLExt pl = kpa.getPl();

        bean.setNaam(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
        bean.setAdres(pl.getVerblijfplaats().getAdres().getAdres());
        bean.setPc(pl.getVerblijfplaats().getAdres().getPcWpl());
      }

      setBean(bean);
    }
  }
}
