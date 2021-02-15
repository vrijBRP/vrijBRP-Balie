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

package nl.procura.gba.web.modules.bs.huwelijk.page90;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.bs.huwelijk.page90.Page90HuwelijkBean1.*;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.trim;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.huwelijk.page30.AmbtenarenForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page90HuwelijkForm1 extends GbaForm implements AmbtenarenForm {

  private final DossierHuwelijk zaakDossier;

  public Page90HuwelijkForm1(DossierHuwelijk zaakDossier) {

    setReadonlyAsText(true);

    setOrder(AMBTENAAR1, AMBTENAAR2, AMBTENAAR3);

    setCaption("Definitieve ambtenaar");

    setColumnWidths("160px", "");

    this.zaakDossier = zaakDossier;
  }

  @Override
  public Page90HuwelijkBean1 getBean() {
    return (Page90HuwelijkBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new Page90HuwelijkBean1();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, final Property property) {

    if (property.is(AMBTENAAR3)) {

      column.addComponent(new Button("Kies", (ClickListener) event -> onClickKies(property)));

      column.addComponent(new Button("Anders", (ClickListener) event -> onClickAnders(property)));

    }

    super.setColumn(column, field, property);
  }

  @Override
  public void update() {

    Page90HuwelijkBean1 bean = new Page90HuwelijkBean1();

    DossierPersoon ambtenaar1 = zaakDossier.getAmbtenaar1();
    DossierPersoon ambtenaar2 = zaakDossier.getAmbtenaar2();
    DossierPersoon ambtenaar3 = zaakDossier.getAmbtenaar3();

    String keuzes = "";

    if (ambtenaar1.isVolledig()) {
      bean.setAmbtenaar1(ambtenaar1.getAktenaam());
      keuzes = "1e keus is " + bean.getAmbtenaar1();
    } else {
      bean.setAmbtenaar1(setClass("grey", "(Geen persoon geselecteerd)"));
      keuzes += "Geen 1e keus";
    }

    if (ambtenaar2.isVolledig()) {
      bean.setAmbtenaar2(ambtenaar2.getAktenaam());
      keuzes += (", 2e keus is " + bean.getAmbtenaar2());
    } else {
      bean.setAmbtenaar2(setClass("grey", "(Geen persoon geselecteerd)"));
      keuzes += ", geen 2e keus";
    }

    if (ambtenaar3.isVolledig()) {
      bean.setAmbtenaar3(ambtenaar3.getAktenaam());
    } else {
      bean.setAmbtenaar3(setClass("grey", "(Geen persoon geselecteerd)"));
    }

    if (emp(keuzes)) {
      keuzes = setClass("grey", "(Geen personen geselecteerd)");
    }

    bean.setKeuzes(trim(keuzes));

    setBean(bean);
  }

  @SuppressWarnings("unused")
  protected void onClickAnders(Property propertyId) {
  } // Override

  @SuppressWarnings("unused")
  protected void onClickKies(Property propertyId) {
  } // Override
}
