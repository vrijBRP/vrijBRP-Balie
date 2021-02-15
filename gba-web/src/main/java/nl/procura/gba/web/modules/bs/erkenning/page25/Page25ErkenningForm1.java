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

package nl.procura.gba.web.modules.bs.erkenning.page25;

import static nl.procura.gba.web.modules.bs.erkenning.page25.Page25ErkenningBean1.*;

import java.util.List;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page25ErkenningForm1 extends GbaForm<Page25ErkenningBean1> {

  private final DossierErkenning zaakDossier;

  public Page25ErkenningForm1(DossierErkenning zaakDossier) {

    this.zaakDossier = zaakDossier;

    setCaption("Benodigde informatie");

    setColumnWidths("160px", "", "100px", "300px");

    setOrder(NAT_MOEDER, VBT_MOEDER, VB_MOEDER, NAT_KIND, VBT_KIND, VB_KIND);

    setBean(new Page25ErkenningBean1(zaakDossier));
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(VB_MOEDER, VB_KIND)) {
      column.setColspan(3);
    }

    if (property.is(NAT_MOEDER)) {
      List<DossierNationaliteit> nationaliteiten = zaakDossier.getMoeder().getNationaliteiten();
      column.addComponent(
          new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.AFSTAMMING, nationaliteiten));
    }

    if (property.is(VB_MOEDER)) {
      FieldValue land = zaakDossier.getMoeder().getLand();
      column.addComponent(new KennisbankButton(KennisBankBron.LAND, KennisBankDoel.AFSTAMMING, land));
    }

    if (property.is(NAT_KIND)) {
      if (zaakDossier.getKinderen().size() > 0) {
        List<DossierNationaliteit> nationaliteiten = zaakDossier.getKinderen().get(0).getNationaliteiten();
        column.addComponent(
            new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.AFSTAMMING, nationaliteiten));
      }
    }

    if (property.is(VB_KIND)) {
      if (zaakDossier.getKinderen().size() > 0) {
        FieldValue land = zaakDossier.getKinderen().get(0).getLand();
        column.addComponent(new KennisbankButton(KennisBankBron.LAND, KennisBankDoel.AFSTAMMING, land));
      }
    }

    super.afterSetColumn(column, field, property);
  }

}
