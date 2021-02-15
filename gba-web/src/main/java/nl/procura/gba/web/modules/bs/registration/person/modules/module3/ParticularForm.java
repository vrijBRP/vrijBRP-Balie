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

package nl.procura.gba.web.modules.bs.registration.person.modules.module3;

import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.ONBEKEND;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.ONGEHUWD;
import static nl.procura.gba.web.modules.bs.registration.person.modules.module3.ParticularBean.*;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.rijbewijs.NaamgebruikType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class ParticularForm extends GbaForm<ParticularBean> {

  private final DossierPersoon person;

  ParticularForm(DossierPersoon person) {
    this.person = person;
    setCaption("Overige");
    setColumnWidths("200px", "");
    setOrder(F_MARITAL_STATUS, F_USE_OF_NAME, F_CONFIDENTIALITY, F_BEFORE_IN_NL, F_BEFORE_LIVE);

    final ParticularBean particularBean = new ParticularBean();
    particularBean.setMaritalStatus(person.getBurgerlijkeStaat());
    particularBean.setConfidentiality(person.isVerstrekkingsbeperking());
    particularBean.setBeforeInNl(getBeforeInNlFromDB(person.getPrevType()));
    particularBean.setBeforeLive(person.getPrevDescription());
    setBean(particularBean);
    getField(F_BEFORE_IN_NL).addListener((ValueChangeListener) e -> onBeforeInNlChange(""));
    onBeforeInNlChange(person.getPrevDescription());
  }

  // suppress as we intentionally return null
  @SuppressWarnings("squid:S2447")
  private static Boolean getBeforeInNlFromDB(String prevType) {
    if (prevType == null || "".equals(prevType)) {
      return null;
    }
    return "Y".equals(prevType);
  }

  private static String getDBPrevType(Boolean prevType) {
    if (prevType == null) {
      return "";
    }
    return prevType ? "Y" : "N";
  }

  @Override
  public void afterSetBean() {
    final GbaNativeSelect maritialStatus = (GbaNativeSelect) getField(F_MARITAL_STATUS);
    maritialStatus.addListener((ValueChangeListener) valueChangeEvent -> {
      setUseOfName();
    });
    setUseOfName();
    super.afterSetBean();
  }

  private void setUseOfName() {
    final GbaNativeSelect maritialStatus = (GbaNativeSelect) getField(F_MARITAL_STATUS);
    final GbaNativeSelect useOfName = (GbaNativeSelect) getField(F_USE_OF_NAME);
    UseOfNameContainer container = new UseOfNameContainer((BurgerlijkeStaatType) maritialStatus.getValue());
    useOfName.setContainerDataSource(container);
    useOfName.setValue(NaamgebruikType.getByAfk(person.getNaamgebruik()));
  }

  void save() {
    commit();
    final ParticularBean enteredOtherInformation = getBean();
    person.setBurgerlijkeStaat(enteredOtherInformation.getMaritalStatus());
    person.setVerstrekkingsbeperking(enteredOtherInformation.getConfidentiality());
    person.setNaamgebruik(enteredOtherInformation.getUseOfName().getAfk());
    person.setPrevType(getDBPrevType(enteredOtherInformation.getBeforeInNl()));
    person.setPrevDescription(enteredOtherInformation.getBeforeLive());
  }

  private void onBeforeInNlChange(String prevDescription) {
    final Field source = getField(F_BEFORE_IN_NL);

    if (source != null) {
      final Boolean value = (Boolean) source.getValue();
      if (Boolean.TRUE.equals(value)) {
        getField(F_BEFORE_LIVE).setValue(prevDescription);
        getField(F_BEFORE_LIVE).setVisible(true);
        getField(F_BEFORE_LIVE).setRequired(true);
      } else {
        getField(F_BEFORE_LIVE).setValue("");
        getField(F_BEFORE_LIVE).setVisible(false);
        getField(F_BEFORE_LIVE).setRequired(false);
      }
    }
    repaint();
  }

  class UseOfNameContainer extends ArrayListContainer {

    public UseOfNameContainer(BurgerlijkeStaatType value) {
      if (value != null) {
        addItem(NaamgebruikType.EIGEN_NAAM);
        if (!value.is(ONBEKEND, ONGEHUWD)) {
          addItem(NaamgebruikType.NAAM_PARTNER_EIGEN_NAAM);
          addItem(NaamgebruikType.EIGEN_NAAM_NAAM_PARTNER);
          addItem(NaamgebruikType.NAAM_PARTNER);
        }
      }
    }
  }
}
