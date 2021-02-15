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

package nl.procura.gba.web.modules.bs.omzetting.page30;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.bs.omzetting.page30.Page30OmzettingBean1.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page30OmzettingForm1 extends GbaForm implements AmbtenarenForm {

  private final DossierOmzetting zaakDossier;

  public Page30OmzettingForm1(DossierOmzetting zaakDossier) {

    setReadonlyAsText(true);

    setOrder(AMBTENAAR1, AMBTENAAR2, AMBTENAAR3);

    setCaption("Ambtenaren");

    setColumnWidths("200px", "");

    this.zaakDossier = zaakDossier;
  }

  @Override
  public Page30OmzettingBean1 getBean() {
    return (Page30OmzettingBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new Page30OmzettingBean1();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, final Property property) {

    if (property.is(AMBTENAAR1, AMBTENAAR2, AMBTENAAR3)) {

      column.addComponent(new Button("Kies", (ClickListener) event -> onClickSelect(property)));

      column.addComponent(new Button("Anders", (ClickListener) event -> onClickAnders(property)));

      column.addComponent(new Button("Verwijder", (ClickListener) event -> onClickDelete(property)));
    }

    super.setColumn(column, field, property);
  }

  @Override
  public void update() {

    Page30OmzettingBean1 bean = new Page30OmzettingBean1();

    DossierPersoon ambtenaar1 = zaakDossier.getAmbtenaar1();
    DossierPersoon ambtenaar2 = zaakDossier.getAmbtenaar2();
    DossierPersoon ambtenaar3 = zaakDossier.getAmbtenaar3();

    bean.setAmbtenaar1(getNaam(ambtenaar1));
    bean.setAmbtenaar2(getNaam(ambtenaar2));
    bean.setAmbtenaar3(getNaam(ambtenaar3));

    setBean(bean);
  }

  @SuppressWarnings("unused")
  protected void onClickAnders(Property propertyId) {
  } // Override

  @SuppressWarnings("unused")
  protected void onClickDelete(Property propertyId) {
  } // Override

  @SuppressWarnings("unused")
  protected void onClickSelect(Property propertyId) {
  } // Override

  private String getNaam(DossierPersoon ambtenaar) {

    if (ambtenaar.isVolledig()) {
      return ambtenaar.getAktenaam();
    }

    return setClass("grey", "(Geen persoon geselecteerd)");
  }
}
