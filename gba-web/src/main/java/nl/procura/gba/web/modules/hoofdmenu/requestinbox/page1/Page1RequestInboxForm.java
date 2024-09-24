/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1;

import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1.Page1RequestInboxBean.BSN;
import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1.Page1RequestInboxBean.STATUS;
import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1.Page1RequestInboxBean.TYPE;
import static nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1.Page1RequestInboxBean.USER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Container;

import nl.procura.burgerzaken.requestinbox.api.model.InboxEnum;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.ModuleRequestInboxParams;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.validation.Bsn;

public class Page1RequestInboxForm extends GbaForm<Page1RequestInboxBean> {

  private final Runnable  changeListener;
  private GbaNativeSelect userField;

  public Page1RequestInboxForm(ModuleRequestInboxParams params, Runnable changeListener) {
    this.changeListener = changeListener;

    setReadonlyAsText(false);
    setColumnWidths("80px", "");
    setOrder(STATUS, TYPE, BSN, USER);

    Page1RequestInboxBean bean = new Page1RequestInboxBean();
    Bsn bsn = params.getBsn();
    InboxItemStatus status = params.getStatus();

    if (bsn.isCorrect()) {
      bean.setBsn(new BsnFieldValue(bsn.getDefaultBsn()));
    }
    if (status != null) {
      bean.setStatus(status);
    }
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    ValueChangeListener valueChangeListener = event -> {
      commit();
      changeListener.run();
    };
    GbaNativeSelect statusField = getField(STATUS, GbaNativeSelect.class);
    GbaNativeSelect typeField = getField(TYPE, GbaNativeSelect.class);
    GbaBsnField bsnField = getField(BSN, GbaBsnField.class);
    userField = getField(USER, GbaNativeSelect.class);

    statusField.setDataSource(getStatusses());
    typeField.setDataSource(getTypes());

    statusField.addListener(valueChangeListener);
    typeField.addListener(valueChangeListener);
    bsnField.addListener(valueChangeListener);
    userField.addListener(valueChangeListener);

    super.afterSetBean();
  }

  @Override
  public void attach() {
    userField.setDataSource(getUsers());
    super.attach();
  }

  public interface ChangeListener {

    void onChange(Page1RequestInboxBean bean);
  }

  private Container getStatusses() {
    return new ArrayListContainer(filter(InboxItemStatus.values()));
  }

  private Container getTypes() {
    return new ArrayListContainer(filter(InboxItemTypeName.values()));
  }

  private Container getUsers() {
    return new ArrayListContainer(getApplication().getServices().getGebruikerService().getGebruikers(false));
  }

  private <T extends InboxEnum<String>> List<T> filter(T[] filter) {
    return Arrays.stream(filter)
        .filter(v -> isNotBlank(v.getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Page1RequestInboxBean getBean() {
    return super.getBean();
  }
}
