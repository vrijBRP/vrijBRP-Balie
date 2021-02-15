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

package nl.procura.gba.web.modules.bs.huwelijk.page30;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.bs.huwelijk.page30.Page30HuwelijkBean1.*;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.huwelijk.page30.ambtenaren.HuwelijkAmbtenarenWindow;
import nl.procura.gba.web.modules.bs.huwelijk.page35.Page35Huwelijk;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.vaadin.component.layout.page.PageNavigation;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page30HuwelijkForm1 extends GbaForm implements AmbtenarenForm {

  private final DossierHuwelijk zaakDossier;
  private final PageNavigation  navigation;

  public Page30HuwelijkForm1(DossierHuwelijk zaakDossier, PageNavigation navigation) {
    this.zaakDossier = zaakDossier;
    this.navigation = navigation;

    setReadonlyAsText(true);

    setOrder(AMBTENAAR1, AMBTENAAR2, AMBTENAAR3);

    setCaption("Ambtenaren");

    setColumnWidths("200px", "");

  }

  @Override
  public Page30HuwelijkBean1 getBean() {
    return (Page30HuwelijkBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new Page30HuwelijkBean1();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, final Property property) {

    if (property.is(AMBTENAAR1, AMBTENAAR2, AMBTENAAR3)) {

      column.addComponent(new Button("Kies", event -> onClickSelect(property)));

      column.addComponent(new Button("Anders", event -> onClickAnders(property)));

      column.addComponent(new Button("Verwijder", event -> onClickDelete(property)));
    }

    super.setColumn(column, field, property);
  }

  @Override
  public void update() {

    Page30HuwelijkBean1 bean = new Page30HuwelijkBean1();

    DossierPersoon ambtenaar1 = zaakDossier.getAmbtenaar1();
    DossierPersoon ambtenaar2 = zaakDossier.getAmbtenaar2();
    DossierPersoon ambtenaar3 = zaakDossier.getAmbtenaar3();

    bean.setAmbtenaar1(getNaam(ambtenaar1));
    bean.setAmbtenaar2(getNaam(ambtenaar2));
    bean.setAmbtenaar3(getNaam(ambtenaar3));

    setBean(bean);
  }

  private void onClickAnders(Property property) {
    navigation.goToPage(new Page35Huwelijk(toPerson(property)));
  }

  private void onClickDelete(Property property) {
    BsPersoonUtils.reset(toPerson(property));
    update();
  }

  private void onClickSelect(Property property) {
    getWindow().addWindow(new HuwelijkAmbtenarenWindow(this, toPerson(property)));
  }

  private DossierPersoon toPerson(Property property) {
    if (property.is(AMBTENAAR1)) {
      return zaakDossier.getAmbtenaar1();
    } else if (property.is(AMBTENAAR2)) {
      return zaakDossier.getAmbtenaar2();
    } else if (property.is(AMBTENAAR3)) {
      return zaakDossier.getAmbtenaar3();
    }
    throw new IllegalArgumentException("Illegal property " + property);
  }

  private String getNaam(DossierPersoon ambtenaar) {

    if (ambtenaar.isVolledig()) {
      return BsPersoonUtils.getNaam(ambtenaar);
    }

    return setClass("grey", "(Geen persoon geselecteerd)");
  }
}
