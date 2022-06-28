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

import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

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
import nl.procura.vaadin.component.layout.VLayout;

import lombok.Data;

public class DeceasedInfoCheckWindow extends DeclarationCheckWindow {

  private static final String VOORN          = "voorn";
  private static final String GESLACHTSNAAM  = "geslachtsnaam";
  private static final String VOORV          = "voorv";
  private static final String GEBOORTE_DATUM = "geboortedatum";

  private final DossierOverlijdenVerzoek verzoek;
  private final Bean                     suppliedValue;
  private final Bean                     derivedValue;

  public DeceasedInfoCheckWindow(DossierOverlijdenVerzoek verzoek, DossierPersoon persoon) {
    this.verzoek = verzoek;
    setWidth("900px");
    setCaption("Aangifte controle (Druk op Escape om te sluiten)");

    suppliedValue = new Bean(
        verzoek.getVoorn(),
        verzoek.getGeslNaam(),
        verzoek.getVoorv(),
        verzoek.getGeboortedatum());

    derivedValue = new Bean(
        persoon.getVoornaam(),
        persoon.getGeslachtsnaam(),
        persoon.getVoorvoegsel(),
        persoon.getDatumGeboorte().toInt());

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
    private String voorn;

    @Field(type = LABEL,
        caption = "Geslachtsnaam")
    private String geslachtsnaam;

    @Field(type = LABEL,
        caption = "Voorvoegsel")
    private String voorv;

    @Field(type = LABEL,
        caption = "Geboortedatum")
    private String geboortedatum;

    public Bean(String voornamen,
        String geslachtsnaam,
        String voorv,
        Integer geboortedatum) {
      this.voorn = voornamen;
      this.geslachtsnaam = geslachtsnaam;
      this.voorv = voorv;
      this.geboortedatum = new ProcuraDate(geboortedatum).getFormatDate();
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
          .append(geboortedatum, bean.getGeboortedatum())
          .build();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder()
          .append(geslachtsnaam)
          .append(voorn)
          .append(voorv)
          .append(geboortedatum)
          .build();
    }
  }

  public static class Form extends GbaForm<Bean> {

    public Form(String caption, Bean bean) {
      setCaption(caption);
      setReadonlyAsText(true);
      setColumnWidths("130px", "300px", "130px", "");
      setOrder(VOORN, GEBOORTE_DATUM, GESLACHTSNAAM, VOORV);
      setBean(bean);
    }
  }
}
