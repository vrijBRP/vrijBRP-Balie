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

import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorteVerzoek;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.validation.Bsn;

import lombok.Data;

public class BirthFatherBsnCheckWindow extends DeclarationCheckWindow {

  private static final String BSN  = "bsn";
  private static final String NAAM = "naam";

  private final Services         services;
  private DossierGeboorteVerzoek dossier;
  private final Bsn              suppliedValue;
  private final Bsn              derivedValue;

  public BirthFatherBsnCheckWindow(
      Services services,
      DossierGeboorteVerzoek dossier,
      DossierPersoon dossierPersoon) {

    this.services = services;
    this.dossier = dossier;
    setWidth("800px");
    setCaption("Aangifte controle (Druk op Escape om te sluiten)");

    suppliedValue = new Bsn(dossier.getVerzoekBsnVaderDuoMoeder().longValue());
    derivedValue = new Bsn(dossierPersoon.getBurgerServiceNummer().getLongValue());

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
    return suppliedValue.isCorrect()
        && derivedValue.isCorrect()
        && suppliedValue.getLongBsn().equals(derivedValue.getLongBsn());
  }

  private String getName(Services services, Bsn bsn) {
    BasePLExt persoonslijst = services.getPersonenWsService().getPersoonslijst(bsn.getDefaultBsn());
    if (persoonslijst.getPersoon().getBsn().isNotBlank()) {
      return persoonslijst.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
    } else {
      return "Persoon niet gevonden";
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Bean implements Serializable {

    @Field(type = LABEL,
        caption = "Bsn vader / duo-moeder")
    private String bsn = "";

    public Bean(Bsn bsn) {
      if (bsn.isCorrect()) {
        this.bsn = bsn.getFormatBsn() + " (" + getName(services, bsn) + ")";
      }
    }
  }

  public class Form extends GbaForm<Bean> {

    public Form(String caption, Bsn bsn) {
      setCaption(caption);
      setReadonlyAsText(true);
      setColumnWidths("150px", "");
      setOrder(BSN, NAAM);
      setBean(new Bean(bsn));
    }
  }
}
