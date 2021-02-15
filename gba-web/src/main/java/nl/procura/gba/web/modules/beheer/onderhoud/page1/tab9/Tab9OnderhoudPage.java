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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab9;

import static java.lang.String.format;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Objects;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.PasswordField;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.onderhoud.OnderhoudTabPage;
import nl.procura.gba.web.services.applicatie.onderhoud.LicenseConf;
import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService;
import nl.procura.proweb.admin.license.License;
import nl.procura.proweb.admin.license.LicenseModule;
import nl.procura.proweb.admin.license.LicenseUtils;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

public class Tab9OnderhoudPage extends OnderhoudTabPage {

  private static final String F_PW          = "pw";
  private static final String F_LICENSE     = "license";
  private static final String F_CUSTOMER    = "customer";
  private static final String F_ENVIRONMENT = "environment";
  private final Table         table         = new Table();
  private License             l;
  private Form1               form1         = null;
  private Form2               form2         = null;

  public Tab9OnderhoudPage() {
    super("Licentie");
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      LicenseConf licenseConf = getServices().getOnderhoudService().getLicense();
      form1 = new Form1(licenseConf);
      form2 = new Form2();

      addButton(buttonSave);
      addComponent(form1);
      addComponent(form2);
      addExpandComponent(table);

      parseLicense(licenseConf);
      updateLicense();
    }

    super.event(event);
  }

  @Override
  public void onSave() {
    form1.commit();
    String pw = form1.getBean().getPw();
    String license = form1.getBean().getLicense();
    getServices().getOnderhoudService().saveLicense(pw, license);
    successMessage("De licentie is opgeslagen");
  }

  private void parseLicense(LicenseConf license) {
    l = null;
    try {
      l = LicenseUtils.decryptLicense(license.getPw(), license.getLicense());
    } catch (Exception ignore) {
      l = null;
    } finally {
      updateLicense();
      form1.getField(F_PW).setValue(license.getPw());
      form1.getField(F_LICENSE).setValue(license.getLicense());
      form1.commit();
    }
  }

  private void updateLicense() {
    Bean bean = new Bean();
    if (l != null) {
      bean.setCustomer(format("%s (%s) (%s)", l.getCustomerName(), l.getCustomerId(), getMatch(isMatchCustomer())));
      bean.setEnvironment(format("%s (%s)", l.getEnvironmentName(), getMatch(isMatchEnvironment())));
    }
    form2.setBean(bean);
    table.init();
  }

  private String getMatch(boolean match) {
    return match ? MiscUtils.setClass(true, "komt overeen") : MiscUtils.setClass(false, "komt niet overeen");
  }

  private boolean isMatchCustomer() {
    return Objects.equals(l.getCustomerId(), getServices().getOnderhoudService().getGemeenteCodes());
  }

  private boolean isMatchEnvironment() {
    if (getServices().getOnderhoudService().isTestEnvironment()) {
      return Objects.equals(l.getEnvironmentId(), OnderhoudService.LICENSE_ENV_TEST);
    } else if (!getServices().getOnderhoudService().isTestEnvironment()) {
      return Objects.equals(l.getEnvironmentId(), OnderhoudService.LICENSE_ENV_PROD);
    }
    return false;
  }

  private String getExpired(LicenseModule module) {
    if (new ProcuraDate(module.getEndDate()).isExpired()) {
      return MiscUtils.setClass(false, " (is verlopen)");
    }
    return "";
  }

  public static class LicenseValidator extends AbstractStringValidator {

    private PasswordField pw;

    public LicenseValidator(PasswordField pw) {
      super("Licentie kan niet ontsleuteld worden.");
      this.pw = pw;
    }

    @Override
    protected boolean isValidString(String value) {
      try {
        LicenseUtils.decryptLicense((String) pw.getValue(), value);
        return true;
      } catch (Exception e) {
        return false;
      }
    }

  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD, defaultWidth = "150px")
  public static class Bean implements Serializable {

    @Field(type = Field.FieldType.PASSWORD_FIELD, caption = "Wachtwoord")
    private String pw = "";

    @Field(customTypeClass = ProTextArea.class,
        caption = "Licentie",
        width = "600px")
    @TextArea(rows = 6)
    @InputPrompt(text = "Plak hier de licentie in")
    @Immediate
    private String license = "";

    @Field(type = Field.FieldType.LABEL, caption = "Klant")
    private String customer = "";

    @Field(type = Field.FieldType.LABEL, caption = "Omgeving")
    private String environment = "";

  }

  private class Table extends GbaTable {

    public Table() {
      setSelectable(true);
      setMultiSelect(true);
    }

    @Override
    public void setColumns() {
      addColumn("Module");
      addColumn("Geldig tot", 400).setUseHTML(true);
    }

    @Override
    public void setRecords() {
      if (l != null) {
        for (LicenseModule module : l.getModules()) {
          Record r = addRecord(module);
          r.addValue(module.getModuleName());
          r.addValue(new ProcuraDate(module.getEndDate()).getFormatDate() + getExpired(module));
        }
      }
      super.setRecords();
    }
  }

  public class Form1 extends GbaForm<Bean> {

    public Form1(LicenseConf license) {
      setColumnWidths("100px", "");
      setOrder(F_PW, F_LICENSE);

      Bean bean = new Bean();
      bean.setPw(license.getPw());
      bean.setLicense(license.getLicense());
      setBean(bean);
    }

    @Override
    public void afterSetBean() {
      super.afterSetBean();
      ProTextArea textArea = getField(F_LICENSE, ProTextArea.class);
      PasswordField password = getField(F_PW, PasswordField.class);
      password.addListener((FieldEvents.TextChangeListener) event -> {
        parseLicense(new LicenseConf(event.getText(), (String) textArea.getValue()));
      });
      textArea.addValidator(new LicenseValidator(password));
      textArea.addListener((FieldEvents.TextChangeListener) event -> {
        parseLicense(new LicenseConf((String) password.getValue(), event.getText()));
      });
    }
  }

  public class Form2 extends GbaForm<Bean> {

    public Form2() {
      setCaption("Ingevoerde licentie");
      setColumnWidths("100px", "");
      setOrder(F_CUSTOMER, F_ENVIRONMENT);
      setBean(new Bean());
    }

  }
}
