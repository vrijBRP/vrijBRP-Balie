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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Field;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.modules.beheer.parameters.form.DatabaseParameterForm;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ZaakStatusParameterType;
import nl.procura.gba.web.services.zaken.algemeen.ZaakTypeStatussen;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class ZakenStatusParametersLayout extends DatabaseParameterLayout {

  public ZakenStatusParametersLayout(GbaApplication application, String naam, String category) {
    super(application, naam, category);
  }

  public ZaakType getZaakByFieldname(Object object) {
    for (ParameterBean.ParameterBeanField p : ParameterBean.getFields((b) -> b.isFieldName(object))) {
      List<ZaakStatusParameterType> types = ZaakStatusParameterType.getByParameterType(p.getType());
      if (!types.isEmpty()) {
        return types.get(0).getZaakType();
      }
    }
    return ZaakType.ONBEKEND;
  }

  @Override
  public void setForm(String category) {

    super.setForm(new DatabaseParameterForm(category) {

      @Override
      public Field newField(Field field, Property property) {

        Object value = field.getValue();
        ZaakType zaakType = getZaakByFieldname(property.getId());

        if (isParameter(property, ParameterConstant.ZAKEN_MAX_STATUS_ZAAK_WIJZIGEN)) {
          GbaNativeSelect select = (GbaNativeSelect) field;
          select.setContainerDataSource(new ZaakStatusParameterContainer(ZaakTypeStatussen.getAlle()));
          select.setItemCaptionPropertyId(ZaakStatusParameterContainer.OMSCHRIJVING);
          field.setValue(value);

        } else if (zaakType != ZaakType.ONBEKEND) {
          GbaNativeSelect select = (GbaNativeSelect) field;
          select.setContainerDataSource(new ZaakStatusParameterContainer(ZaakTypeStatussen.getAlle(zaakType)));
          select.setItemCaptionPropertyId(ZaakStatusParameterContainer.OMSCHRIJVING);
          field.setValue(value);
        }

        return super.newField(field, property);
      }
    });
  }

  public class ZaakStatusParameterContainer extends IndexedContainer implements ProcuraContainer {

    public ZaakStatusParameterContainer(List<ZaakStatusType> statussen) {
      addContainerProperty(OMSCHRIJVING, String.class, "");
      removeAllItems();

      for (ZaakStatusType status : statussen) {
        Item item = addItem(astr(status.getCode()));
        item.getItemProperty(OMSCHRIJVING).setValue(status.getOms());
      }
    }
  }
}
