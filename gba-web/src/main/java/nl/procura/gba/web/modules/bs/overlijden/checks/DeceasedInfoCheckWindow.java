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

package nl.procura.gba.web.modules.bs.overlijden.checks;

import static nl.procura.gba.web.common.tables.GbaTables.*;
import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.geboorte.checks.DeclarationCheckWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijdenVerzoek;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.Data;

public class DeceasedInfoCheckWindow extends DeclarationCheckWindow {

  private static final String VOORN           = "voorn";
  private static final String GESLACHTSNAAM   = "geslachtsnaam";
  private static final String VOORV           = "voorv";
  private static final String TP              = "titel";
  private static final String GEBOORTE_DATUM  = "geboortedatum";
  private static final String GEBOORTE_PLAATS = "geboorteplaats";
  private static final String GEBOORTE_LAND   = "geboorteland";

  private final DossierOverlijdenVerzoek verzoek;
  private final Bean                     suppliedValue;
  private final Bean                     derivedValue;

  public DeceasedInfoCheckWindow(DossierOverlijdenVerzoek verzoek, DossierPersoon persoon) {
    this.verzoek = verzoek;

    setWidth("800px");
    setCaption("Aangifte controle (Druk op Escape om te sluiten)");

    String pGeb = verzoek.getGeboorteplaats();
    FieldValue geboorteplaats = isDigits(pGeb)
        ? PLAATS.get(pGeb)
        : new FieldValue(pGeb);

    suppliedValue = new Bean(
        verzoek.getVoorn(),
        verzoek.getGeslNaam(),
        verzoek.getVoorv(),
        TITEL.get(verzoek.getTitel()),
        verzoek.getGeboortedatum(),
        geboorteplaats,
        LAND.get(verzoek.getGeboorteland()));

    derivedValue = new Bean(
        persoon.getVoornaam(),
        persoon.getGeslachtsnaam(),
        persoon.getVoorvoegsel(),
        persoon.getTitel(),
        persoon.getDatumGeboorte().toInt(),
        persoon.getGeboorteplaats(),
        persoon.getGeboorteland());

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
    return verzoek.isVerzoekInd();
  }

  @Override
  public boolean isMatchValues() {
    return suppliedValue.equals(derivedValue);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(type = LABEL,
        caption = "Voornamen")
    private String voorn = "";

    @Field(type = LABEL,
        caption = "Geslachtsnaam")
    private String geslachtsnaam = "";

    @Field(type = LABEL,
        caption = "Voorvoegsel")
    private String voorv = "";

    @Field(type = LABEL,
        caption = "Titel/predikaat")
    private String titel;

    @Field(type = LABEL,
        caption = "Geboortedatum")
    private String geboortedatum;

    @Field(type = LABEL,
        caption = "Geboorteplaats")
    private String geboorteplaats;

    @Field(type = LABEL,
        caption = "Geboorteland")
    private String geboorteland;

    public Bean(String voornamen,
        String geslachtsnaam,
        String voorv,
        FieldValue titel,
        Integer geboortedatum,
        FieldValue geboorteplaats,
        FieldValue geboorteland) {
      this.voorn = voornamen;
      this.geslachtsnaam = geslachtsnaam;
      this.voorv = voorv;
      this.titel = titel.toString();
      this.geboortedatum = new ProcuraDate(geboortedatum).getFormatDate();
      this.geboorteplaats = geboorteplaats.toString();
      this.geboorteland = geboorteland.toString();
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
          .append(voorn, bean.getVoorn())
          .append(voorv, bean.getVoorv())
          .append(titel, bean.getTitel())
          .append(geboortedatum, bean.getGeboortedatum())
          .append(geboorteplaats, bean.getGeboorteplaats())
          .append(geboorteland, bean.getGeboorteland())
          .build();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder()
          .append(geslachtsnaam)
          .append(voorn)
          .append(voorv)
          .append(titel)
          .append(geboortedatum)
          .append(geboorteplaats)
          .append(geboorteland)
          .build();
    }
  }

  public static class Form extends GbaForm<Bean> {

    public Form(String caption, Bean bean) {
      setCaption(caption);
      setReadonlyAsText(true);
      setColumnWidths("150px", "200px", "150px", "");
      setOrder(VOORN, GEBOORTE_DATUM, GESLACHTSNAAM, GEBOORTE_PLAATS, VOORV, GEBOORTE_LAND, TP);
      setBean(bean);
    }

    @Override
    public void setColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
      if (property.is(TP)) {
        column.setColspan(3);
      }
      super.setColumn(column, field, property);
    }
  }

}
