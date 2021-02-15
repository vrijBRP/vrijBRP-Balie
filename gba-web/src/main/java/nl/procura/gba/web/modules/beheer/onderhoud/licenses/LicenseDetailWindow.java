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

package nl.procura.gba.web.modules.beheer.onderhoud.licenses;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Arrays;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Link;

import nl.procura.burgerzaken.dependencies.models.Dependency;
import nl.procura.burgerzaken.dependencies.models.License;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

import lombok.Data;

public class LicenseDetailWindow extends GbaModalWindow {

  private static final String NAME     = "name";
  private static final String ARTIFACT = "artifact";
  private static final String URL      = "url";

  public LicenseDetailWindow(Dependency dependency) {
    super("Open source licenties (Escape om te sluiten)", "750px");

    VLayout layout = new VLayout()
        .margin(true)
        .spacing(true)
        .add(new DependencyForm(dependency));

    if (dependency.getLicenses().isEmpty()) {
      layout.addComponent(new InfoLayout("", ProcuraTheme.ICOON_24.WARNING, "Geen licenties"));
    } else {
      layout.addComponent(new Fieldset("Licentie(s)"));
      for (License license : dependency.getLicenses()) {
        layout.addComponent(new LicenseForm(license));
      }
    }

    setContent(layout);
  }

  public static class DependencyForm extends ReadOnlyForm<DependencyBean> {

    private Dependency dependency;

    public DependencyForm(Dependency dependency) {
      this.dependency = dependency;
      setCaption("Afhankelijk software component");
      setOrder(NAME, ARTIFACT, URL);
      setColumnWidths("100px", "");
      setBean(new DependencyBean(dependency));
    }

    @Override
    public void setColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
      if (property.is(URL)) {
        column.addComponent(new URLLayout(dependency.getUrl()));
      }
      super.setColumn(column, field, property);
    }
  }

  public static class LicenseForm extends ReadOnlyForm<LicenseBean> {

    private License license;

    public LicenseForm(License license) {
      this.license = license;
      setOrder(NAME, URL);
      setColumnWidths("100px", "");
      setBean(new LicenseBean(license));
    }

    @Override
    public void setColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
      if (property.is(URL)) {
        column.addComponent(new URLLayout(license.getUrl(), license.getOfficialUrl()));
      }
      super.setColumn(column, field, property);
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class DependencyBean implements Serializable {

    @Field(caption = "Naam")
    private final String name;

    @Field(caption = "Maven artifact")
    private final String artifact;

    @Field(caption = "Website")
    private final String url;

    public DependencyBean(Dependency dependency) {
      this.name = dependency.getName();
      this.artifact = dependency.getGroupId() + " - " + dependency.getArtifactId();
      this.url = "";
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class LicenseBean implements Serializable {

    @Field(caption = "Namen")
    private final String name;

    @Field(caption = "Bronnen")
    private final String url;

    public LicenseBean(License license) {
      this.name = merge(license.getOfficialName(), license.getName());
      this.url = "";
    }

  }

  private static String merge(String name, String officialName) {
    if (name.equals(officialName)) {
      return name;
    }
    return name + ", <br>" + officialName;
  }

  private static class URLLayout extends CssLayout {

    public URLLayout(String... urls) {
      Arrays.asList(urls).forEach(url -> {
        addComponent(new Link(url, new ExternalResource(url), "blank", 900, 700, 0));
      });
    }
  }
}
