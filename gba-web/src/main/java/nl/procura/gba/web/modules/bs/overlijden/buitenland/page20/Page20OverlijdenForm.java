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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.page20;

import static nl.procura.gba.web.modules.bs.overlijden.buitenland.page20.Page20OverlijdenBean.*;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.enums.TypeAfgever;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page20OverlijdenForm extends GbaForm<Page20OverlijdenBean> {

  public Page20OverlijdenForm(DossierOverlijdenBuitenland dossier, List<DossierPersoon> relaties) {

    setColumnWidths("170px", "");

    init();

    Page20OverlijdenBean bean = new Page20OverlijdenBean();
    bean.setDatumOntvangst(new DateFieldValue(dossier.getDatumOntvangst().getLongDate()));
    bean.setOntvangstType(dossier.getAfgeverType());
    bean.setDatumOverlijden(new DateFieldValue(dossier.getDatumOverlijden().getLongDate()));
    bean.setPlaatsOverlijden(dossier.getPlaatsOverlijden());
    bean.setLandOverlijden(dossier.getLandOverlijden());
    bean.setBronDocument(dossier.getTypeBronDocument());
    bean.setLandAfgifte(dossier.getLandAfgifte());
    bean.setVoldoetAanEisen(dossier.isVoldoetAanEisen());

    setBean(bean);

    wijzingOntvangstType(bean.getOntvangstType());
    setVoldoetAanEisen(bean.getLandAfgifte());

    if (relaties != null && relaties.size() > 0) {
      ((GbaNativeSelect) getField(GERELATEERDEN)).setContainerDataSource(new NabestaandeContainer(relaties));
    }
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(VOLDOETAANEISEN)) {

      column.addComponent(new KennisbankButton(KennisBankBron.LAND, KennisBankDoel.NAMENRECHT, 0) {

        @Override
        public long getCode() {
          return getFieldValueCode(LANDAFGIFTE);
        }
      });
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    if (getField(LANDAFGIFTE) != null) {
      getField(LANDAFGIFTE).addListener((ValueChangeListener) event -> {

        FieldValue fieldValue = (FieldValue) event.getProperty().getValue();

        Field eisenVeld = getField(VOLDOETAANEISEN);

        if (eisenVeld != null) {

          boolean isVisible = eisenVeld.isVisible();

          setVoldoetAanEisen(fieldValue);

          if (!isVisible) {

            eisenVeld.setValue(false);
          }
        }

        repaint();
      });
    }

    if (getField(ONTVANGSTTYPE) != null) {
      getField(ONTVANGSTTYPE).addListener(
          (ValueChangeListener) event -> wijzingOntvangstType((TypeAfgever) event.getProperty().getValue()));
    }

    if (getField(GERELATEERDEN) != null) {
      getField(GERELATEERDEN)
          .addListener((ValueChangeListener) event -> wijzingGerelateerde((FieldValue) event.getProperty().getValue()));
    }
  }

  @Override
  public Page20OverlijdenBean getNewBean() {
    return new Page20OverlijdenBean();
  }

  protected void init() {
  }

  @SuppressWarnings("unused")
  protected void wijzingGerelateerde(FieldValue value) {
  } // Override

  @SuppressWarnings("unused")
  protected void wijzingOntvangstType(TypeAfgever typeAfgever) {
  } // Override

  private void setVoldoetAanEisen(FieldValue land) {

    if (getField(VOLDOETAANEISEN) != null) {
      getField(VOLDOETAANEISEN).setVisible(!Landelijk.getNederland().equals(land));
      repaint();
    }
  }

  public class NabestaandeContainer extends ArrayListContainer {

    private NabestaandeContainer(List<DossierPersoon> relaties) {

      removeAllItems();

      for (DossierPersoon relatie : relaties) {

        switch (relatie.getDossierPersoonType()) {
          case PARTNER:
          case KIND:
          case MOEDER:
          case VADER_DUO_MOEDER:
            String type = relatie.getDossierPersoonType().getDescr();
            String naam = relatie.getNaam().getNaam_naamgebruik();
            addItem(new FieldValue(relatie, type + " - " + naam));
            break;

          default:
            break;

        }
      }
    }
  }
}
