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

package nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving;

import static nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving.AanschrijvingBean.F_AANSCHRIJFPERSOON;
import static nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving.AanschrijvingBean.F_CEREMONIE;
import static nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving.AanschrijvingBean.F_SOORT;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.vaadin.component.field.ProNativeSelect;

public class AanschrijvingForm extends GbaForm<AanschrijvingBean> {

  private final DossierNaturalisatie zaakDossier;
  private final List<DocumentSoort>  soorten;
  private final Consumer<String>     documentPath;

  public AanschrijvingForm(DossierNaturalisatie zaakDossier,
      List<DocumentSoort> soorten,
      Consumer<String> documentPath) {

    this.zaakDossier = zaakDossier;
    this.soorten = soorten;
    this.documentPath = documentPath;

    setCaption("Soort aanschrijving");
    setColumnWidths(WIDTH_130, "");

    setOrder(F_SOORT, F_AANSCHRIJFPERSOON, F_CEREMONIE);
    setBean(new AanschrijvingBean());
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    Field optieField = getField(F_SOORT);
    if (optieField != null) {
      getField(F_SOORT, ProNativeSelect.class).setContainerDataSource(new DocumentSoortContainer(soorten));
      optieField.addListener((ValueChangeListener) event -> {
        String path = (String) event.getProperty().getValue();
        documentPath.accept(path);
        getWindow().center();
      });
      documentPath.accept((String) optieField.getValue());
    }

    getField(F_AANSCHRIJFPERSOON, ProNativeSelect.class).setContainerDataSource(getPersoonContainer());
    getField(F_AANSCHRIJFPERSOON, ProNativeSelect.class).addListener((ValueChangeListener) valueChangeEvent -> {
      setCeremonieContainer((DossierPersoon) valueChangeEvent.getProperty().getValue());
    });
    setCeremonieContainer(zaakDossier.getAangever());
  }

  private void setCeremonieContainer(DossierPersoon persoon) {
    if (persoon != null) {
      DossierNaturalisatieVerzoeker gegevens = zaakDossier.getVerzoekerGegevens(persoon.getBurgerServiceNummer());
      if (gegevens != null) {
        getField(F_CEREMONIE, ProNativeSelect.class).setContainerDataSource(new CeremonieContainer(gegevens));
        repaint();
      }
    }
  }

  @Override
  public AanschrijvingBean getNewBean() {
    return new AanschrijvingBean();
  }

  private AanschrijfpersoonContainer getPersoonContainer() {
    List<DossierPersoon> personen = new ArrayList<>(zaakDossier.getDossier().getPersonen());
    personen.addAll(zaakDossier.getToestemminggevers());
    return new AanschrijfpersoonContainer(personen);
  }
}
