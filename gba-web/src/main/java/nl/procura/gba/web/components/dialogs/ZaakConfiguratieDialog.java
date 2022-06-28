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

package nl.procura.gba.web.components.dialogs;

import static nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.BsmZknDmsRestElementTypes.*;
import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Button;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.GeefZaakDetailsAntwoordRestElement;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.Data;

public class ZaakConfiguratieDialog {

  public static void of(GbaApplication application, Zaak zaak, Services services) {
    of(application, zaak, services, () -> {});
  }

  public static void of(GbaApplication application, Zaak zaak, Services services, Runnable onContinue) {
    Set<ZaakConfiguratie> configuraties = services
        .getZaakConfiguratieService()
        .getZaakConfiguraties(zaak.getType());
    if (configuraties.isEmpty()) {
      onContinue.run();
    } else {
      application.getParentWindow().addWindow(new MainDialog(application, zaak, services, configuraties, onContinue));
    }
  }

  public static class MainDialog extends ModalWindow {

    private final Zaak     zaak;
    private final Services services;
    private final Runnable onContinue;

    public MainDialog(GbaApplication application,
        Zaak zaak,
        Services services,
        Set<ZaakConfiguratie> configuraties,
        Runnable onContinue) {
      this.zaak = zaak;
      this.services = services;
      this.onContinue = onContinue;

      setCaption("Wat voor zaak betreft het?");
      setWidth("400px");
      setClosable(false);

      VLayout layout = new VLayout()
          .spacing(true)
          .margin(true)
          .add(button("Nieuwe zaak", () -> {
            zaak.setBron(ZaakUtils.PROWEB_PERSONEN);
            zaak.setLeverancier(ZaakUtils.PROCURA);
            closeMainDialog();
            onContinue.run();
          }));

      configuraties
          .stream()
          .map(configuratie -> button(configuratie.getZaakConf(), () -> {
            if (configuratie.isOptions()) {
              application.getParentWindow().addWindow(new OptionsDialog(configuratie));
            } else {
              closeMainDialog();
              onContinue.run();
            }
          })).forEach(layout::add);

      setContent(layout);
    }

    private void doZaakIdCheck(String zaakId) {
      GeefZaakDetailsAntwoordRestElement zaakDetails = services.getZaakDmsService().getZaakDetails(zaakId);
      BsmRestElement zaakElement = zaakDetails.getZaak();
      if (zaakElement.isAdded(ZAAKTYPE)) {
        BsmRestElement zaakType = zaakElement.get(ZAAKTYPE);
        String zaakDescr = zaakType.getElementWaarde(OMSCHRIJVING) + " (" + zaakType.getElementWaarde(CODE) + ")";
        String zaakDate = "registratiedatum: " + date2str(zaakElement.getElementWaarde(REGISTRATIEDATUM));
        throw new ProException(INFO, "Zaak gevonden: {0}", zaakDescr + ", " + zaakDate);
      } else {
        throw new ProException(WARNING, "Geen zaak gevonden.");
      }
    }

    public class OptionsDialog extends ModalWindow {

      public static final String ZAAK_ID = "zaakID";

      public OptionsDialog(ZaakConfiguratie configuratie) {
        setCaption("Opties");
        setWidth("450px");

        VLayout layout = new VLayout()
            .spacing(true)
            .margin(true);

        OptionsForm form = new OptionsForm();

        List<String> fields = new ArrayList<>();
        if (configuratie.getIndZaaksysteemId()) {
          fields.add(ZAAK_ID);
        }
        form.setOrder(fields.toArray(new String[0]));
        Button button = button("Opslaan", () -> {
          form.commit();
          addZaaksysteemID(zaak, configuratie, services, form.getBean().getZaakID());
          closeOptionsDialog();
          closeMainDialog();
          onContinue.run();
        });
        layout.add(form);
        layout.add(button);
        setContent(layout);
      }

      @Data
      @FormFieldFactoryBean(accessType = ElementType.FIELD)
      public class OptionsBean {

        @Field(customTypeClass = ProTextField.class,
            caption = "Zaaksysteem ID",
            required = true,
            width = "200px")
        @TextField(maxLength = 250)
        private String zaakID = "";
      }

      public class OptionsForm extends GbaForm<OptionsBean> {

        public OptionsForm() {
          setOrder(ZAAK_ID);
          setColumnWidths("100px", "");
          setBean(new OptionsBean());
        }

        @Override
        public void afterSetColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
          if (property.is(ZAAK_ID)) {
            Button button = button("Check", () -> {
              commit();
              doZaakIdCheck((String) getField(ZAAK_ID).getValue());
            });
            button.setWidth(null);
            column.addComponent(button);
          }
          super.afterSetColumn(column, field, property);
        }
      }

      private void closeOptionsDialog() {
        OptionsDialog.this.close();
      }
    }

    private void addZaaksysteemID(Zaak zaak, ZaakConfiguratie configuratie, Services services, String zaaksysteemId) {
      if (isNotBlank(configuratie.getBron())) {
        zaak.setBron(configuratie.getBron());
      }
      if (isNotBlank(configuratie.getLeverancier())) {
        zaak.setLeverancier(configuratie.getLeverancier());
      }
      services.getZaakDmsService().setZaaksysteemId(zaak, zaaksysteemId);
    }

    private static Button button(String caption, Runnable onClick) {
      Button button = new Button(caption);
      button.setWidth("100%");
      button.addListener((Button.ClickListener) clickEvent -> onClick.run());
      return button;
    }

    private void closeMainDialog() {
      MainDialog.this.close();
    }
  }
}
