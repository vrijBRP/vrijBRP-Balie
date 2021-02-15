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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public abstract class AbstractBronForm extends GbaForm<BronBean> {

  public void setBron(DossierOnderzoekBron bron) {
    BronBean bean = new BronBean();
    bean.setInstantie(bron.getInst());
    bean.setAfdeling(bron.getInstAfdeling());
    bean.setTavAanhef(new FieldValue(bron.getInstAanhef()));
    bean.setTavVoorl(bron.getInstVoorl());
    bean.setTavNaam(bron.getInstNaam());
    bean.setEmail(bron.getInstEmail());
    bean.setAdres(bron.getAdr());
    bean.setPc(new FieldValue(bron.getPc()));
    bean.setPlaats(bron.getPlaats());
    bean.setAdresType(bron.getAdresType());
    bean.setAanschrijving(bron.getInstAanschr());
    setBean(bean);
  }

  public void setPersoon(DossierPersoon persoon) {

    if (Geslacht.MAN == persoon.getGeslacht()) {
      getBean().setTavAanhef(new FieldValue("Dhr."));
    } else if (Geslacht.VROUW == persoon.getGeslacht()) {
      getBean().setTavAanhef(new FieldValue("Mw."));
    }

    getBean().setTavVoorl(persoon.getNaam().getInit());
    getBean().setTavNaam(persoon.getNaam().getGeslachtsnaam_naamgebruik());
    getBean().setAdres(persoon.getAdres().getAdres());
    getBean().setPc(new FieldValue(persoon.getAdres().getPostcode()));
    getBean().setPlaats(persoon.getAdres().getWoonplaats());

    setBean(getBean());
  }
}
