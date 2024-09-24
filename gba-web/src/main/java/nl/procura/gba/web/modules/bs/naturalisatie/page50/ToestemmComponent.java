/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page50;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.naturalisatie.page50.ToestemmComponent.ToestemmComponentValue;
import nl.procura.gba.web.modules.bs.naturalisatie.valuechoice.ValueChoiceComponent;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonConfig;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonConfig.QuickSearchPersonConfigBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.SelectListener;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatieVerzoeker;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.validation.Bsn;

import lombok.Data;

public class ToestemmComponent implements ValueChoiceComponent<ToestemmComponentValue> {

  private final HLayout                hLayout = new ToestemmComponentLayout().spacing(true);
  private final Label                  label   = new Label("[ Selecteer de persoon ]");
  private final Map<String, Component> fields  = new LinkedHashMap<>();

  private final DossierNaturalisatie          dossier;
  private final DossierNaturalisatieVerzoeker verzoeker;

  private QuickSearchPersonWindow snelZoekWindow;

  private BsnFieldValue       bsn = new BsnFieldValue();
  private Supplier<BasePLExt> plSupplier;

  public ToestemmComponent(DossierNaturalisatie dossier, DossierNaturalisatieVerzoeker verzoeker) {
    this.dossier = dossier;
    this.verzoeker = verzoeker;

    Button selectButton = new Button("Zoek");
    selectButton.addListener((Button.ClickListener) event -> onSelectToestemminggever());

    Button resetButton = new Button("Reset");
    resetButton.addListener((Button.ClickListener) event -> onResetToestemminggever());

    hLayout.add(selectButton, resetButton)
        .addExpandComponent(label)
        .width("400px");

    fields.put("Andere ouder/wettelijk vertegenwoordiger", hLayout);
  }

  @Override
  public Map<String, Component> getFields() {
    return fields;
  }

  @Override
  public void setValue(ToestemmComponentValue value) {
    this.bsn = value.getBsn();
    this.plSupplier = value.getPlSupplier();
  }

  @Override
  public ToestemmComponentValue getValue() {
    return new ToestemmComponentValue(bsn);
  }

  private void onSelectToestemminggever() {
    if (snelZoekWindow == null) {
      SelectListener selectListener = this::setLabel;
      QuickSearchPersonConfigBuilder configBuilder = QuickSearchPersonConfig.builder()
          .selectListener(selectListener)
          .bsn(new Bsn(verzoeker.getBsn().longValue()));

      if (dossier.getVertegenwoordiger().isVolledig()) {
        configBuilder.page("Wettelijke vertegenwoordiger",
            new VertegenwoordigerQuickSearch(dossier.getVertegenwoordiger()
                .getBurgerServiceNummer(),
                selectListener));
      }

      snelZoekWindow = new QuickSearchPersonWindow(configBuilder.build());
    }
    snelZoekWindow.setParent(null);
    ((GbaApplication) label.getApplication()).getParentWindow().addWindow(snelZoekWindow);
  }

  private void setLabel(BasePLExt persoon) {
    if (persoon != null) {
      label.setValue(getNaam(persoon));
      bsn = new BsnFieldValue(persoon.getPersoon().getBsn().getVal());

    } else if (this.bsn.isCorrect()) {
      label.setValue(getNaam(plSupplier.get()));

    } else {
      label.setValue("[ Selecteer de persoon ]");
      bsn = new BsnFieldValue();
    }
  }

  private static String getNaam(BasePLExt persoon) {
    return persoon != null ? persoon.getPersoon()
        .getNaam().getNaamNaamgebruikEersteVoornaam()
        : "[ Selecteer de persoon ]";
  }

  private void onResetToestemminggever() {
    this.bsn = new BsnFieldValue();
    setLabel(null);
  }

  public class ToestemmComponentLayout extends HLayout {

    private boolean labelAttached = false;

    @Override
    public void attach() {
      if (!labelAttached) {
        setLabel(null);
        labelAttached = true;
      }
      super.attach();
    }
  }

  @Data
  public static class ToestemmComponentValue {

    private final BsnFieldValue bsn;
    private Supplier<BasePLExt> plSupplier;

    public ToestemmComponentValue(BsnFieldValue bsn, Supplier<BasePLExt> plSupplier) {
      this.bsn = bsn;
      this.plSupplier = plSupplier;
    }

    public ToestemmComponentValue(BsnFieldValue bsn) {
      this.bsn = bsn;
    }

    public String toString() {
      return bsn.isCorrect() ? bsn.toString() : "";
    }
  }
}
