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

import static nl.procura.gba.web.modules.bs.erkenning.page25.Page25ErkenningBean2.*;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.data.Container;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;

public class Page25ErkenningForm2 extends GbaForm<Page25ErkenningBean2> {

  public Page25ErkenningForm2(DossierErkenning erkenning) {

    setColumnWidths("160px", "");

    setOrder(TOESTEMMINGGEVER_TYPE, RECHT_MOEDER, RECHT_KIND, RECHTBANK);

    Page25ErkenningBean2 bean = new Page25ErkenningBean2();

    bean.setRechtMoeder(erkenning.getLandToestemmingsRechtMoeder());
    bean.setRechtKind(erkenning.getLandToestemmingsRechtKind());

    if (erkenning.getToestemminggeverType() != ToestemminggeverType.ONBEKEND) {
      bean.setToestemminggeverType(erkenning.getToestemminggeverType());
    } else {
      bean.setToestemminggeverType(getToestemminggeverType(erkenning));
    }

    if (fil(erkenning.getRechtbank())) {
      bean.setRechtbank(RechtbankLocatie.get(erkenning.getRechtbank()));
    }

    setBean(bean);
  }

  @Override
  public void afterSetBean() {

    if (getField(RECHT_MOEDER) != null) {

      Container containerMoeder = getContainerMoeder();

      if (containerMoeder != null) {

        getField(RECHT_MOEDER, GbaNativeSelect.class).setDataSource(containerMoeder);
      }
    }

    if (getField(RECHT_KIND) != null) {

      Container containerKind = getContainerKind();

      if (containerKind != null) {

        getField(RECHT_KIND, GbaNativeSelect.class).setDataSource(containerKind);
      }
    }

    super.afterSetBean();
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    getField(TOESTEMMINGGEVER_TYPE).addListener(
        (ValueChangeListener) event -> onChangeToestemminggever((ToestemminggeverType) event.getProperty().getValue()));

    onChangeToestemminggever(getBean().getToestemminggeverType());
  }

  protected Container getContainerKind() {
    return null;
  }

  protected Container getContainerMoeder() {
    return null;
  }

  @SuppressWarnings("unused")
  protected void onChangeToestemminggever(ToestemminggeverType value) {
  }

  private ToestemminggeverType getToestemminggeverType(DossierErkenning erkenning) {

    switch (erkenning.getKindLeeftijdsType()) {

      case JONGER_DAN_7:
      case VAN_7_TM_11:
        return ToestemminggeverType.MOEDER;

      case VAN_12_TM_15:
        return ToestemminggeverType.MOEDER_EN_KIND;

      case OUDER_DAN_16:
        return ToestemminggeverType.KIND;

      default:
        return ToestemminggeverType.ONBEKEND;
    }
  }
}
