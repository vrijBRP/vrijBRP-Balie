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

package nl.procura.gba.web.modules.bs.geboorte.checks;

import static nl.procura.gba.web.common.tables.GbaTables.TITEL;
import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import org.apache.commons.lang3.builder.EqualsBuilder;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrechtVerzoek;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.VLayout;

import lombok.Data;

public class NameSelectionCheckWindow extends DeclarationCheckWindow {

  private static final String GESLACHTSNAAM = "geslachtsnaam";
  private static final String VOORV         = "voorvoegsel";
  private static final String TP            = "titel";

  private DossierNamenrechtVerzoek dossier;
  private Bean                     suppliedValue;
  private Bean                     derivedValue;

  public NameSelectionCheckWindow(
      DossierNamenrechtVerzoek dossier,
      String geslachtsnaam,
      String voorv,
      FieldValue titel) {
    this.dossier = dossier;

    setWidth("800px");
    setCaption("Aangifte controle (Druk op Escape om te sluiten)");

    suppliedValue = new Bean(dossier.getVerzoekKeuzeNaamGesl(),
        dossier.getVerzoekKeuzeNaamVoorv(),
        TITEL.get(dossier.getVerzoekKeuzeNaamTitel()));
    derivedValue = new Bean(geslachtsnaam, voorv, titel);

    Form suppliedForm = new Form("Gegevens die in de e-aangifte zijn aangeleverd", suppliedValue);
    Form derivedForm = new Form("Gegevens die door de applicatie zijn afgeleid", derivedValue);

    VLayout vLayout = new VLayout().margin(true);
    vLayout.addComponent(getInfoLayout());
    vLayout.addComponent(suppliedForm);
    vLayout.addComponent(derivedForm);
    setContent(vLayout);
  }

  @Override
  public boolean isShowIcon() {
    return dossier.isVerzoekInd();
  }

  @Override
  public boolean isMatchValues() {
    return suppliedValue.equals(derivedValue);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Bean implements Serializable {

    @Field(type = LABEL,
        caption = "Geslachtsnaam")
    private String geslachtsnaam = "";

    @Field(type = LABEL,
        caption = "Voorvoegsel")
    private String voorvoegsel = "";

    @Field(type = LABEL,
        caption = "Titel/predikaat")
    private FieldValue titel;

    public Bean(String geslachtsnaam, String voorv, FieldValue titel) {
      this.geslachtsnaam = geslachtsnaam;
      this.voorvoegsel = voorv;
      this.titel = titel;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      Bean bean = (Bean) o;
      return new EqualsBuilder()
          .append(geslachtsnaam, bean.getGeslachtsnaam())
          .append(voorvoegsel, bean.getVoorvoegsel())
          .append(titel, bean.getTitel())
          .build();
    }

    @Override
    public int hashCode() {
      int result = geslachtsnaam != null ? geslachtsnaam.hashCode() : 0;
      result = 31 * result + (voorvoegsel != null ? voorvoegsel.hashCode() : 0);
      result = 31 * result + (titel != null ? titel.hashCode() : 0);
      return result;
    }
  }

  public class Form extends GbaForm<Bean> {

    public Form(String caption, Bean bean) {
      setCaption(caption);
      setReadonlyAsText(true);
      setColumnWidths("150px", "");
      setOrder(GESLACHTSNAAM, VOORV, TP);
      setBean(bean);
    }
  }

}
