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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import static nl.procura.gba.web.modules.bs.ontbinding.page30.Page30OntbindingBean2.DOOR;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.ontbinding.WijzeBeeindigingVerbintenis;

public abstract class Page30OntbindingForm2 extends GbaForm<Page30OntbindingBean2> {

  public Page30OntbindingForm2() {
    setCaption("Wijze van beëindiging");
    setColumnWidths("200px", "");
    setOrder(DOOR);
  }

  @Override
  public void afterSetBean() {

    getField(Page30OntbindingBean2.DOOR, GbaNativeSelect.class).addListener(
        new FieldChangeListener<WijzeBeeindigingVerbintenis>() {

          @Override
          public void onChange(WijzeBeeindigingVerbintenis value) {
            onChangeWijze(value);
          }
        });
  }

  public void setBean(DossierOntbinding zaakDossier) {

    Page30OntbindingBean2 bean = new Page30OntbindingBean2();
    if (WijzeBeeindigingVerbintenis.ONBEKEND != zaakDossier.getWijzeBeeindigingVerbintenis()) {
      bean.setDoor(zaakDossier.getWijzeBeeindigingVerbintenis());
    }

    setBean(bean);
  }

  @Override
  public Page30OntbindingBean2 getNewBean() {
    return new Page30OntbindingBean2();
  }

  protected abstract void onChangeWijze(WijzeBeeindigingVerbintenis wijze);
}
