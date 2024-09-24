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

package nl.procura.gba.web.modules.zaken.common;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.lang.annotation.ElementType;
import java.util.Optional;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;

import nl.procura.burgerzaken.keesy.api.model.MaakZaakRequest;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.inwonerapp.InwonerAppService;
import nl.procura.gba.web.services.beheer.inwonerapp.OpenZaakRequest;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.window.Message;

import lombok.Data;
import lombok.Setter;

@Setter
public class ZaakInwonerAppButton extends Button {

  public static final String ZAAK_ID = "zaakId";
  public static final String AANTAL  = "aantal";
  public static final String NAAM    = "naam";
  public static final String TEL     = "tel";
  public static final String EMAIL   = "email";

  private Zaak     zaak;
  private boolean  found;
  private Runnable closeListener;

  public ZaakInwonerAppButton() {
    setCaption("Signalen Inwoner.app");
  }

  @Override
  public void attach() {
    if (!found && zaak != null) {
      getZaakId().ifPresent(zaakId -> {
        Optional<Integer> aantalSignalen = getAantalSignalen(zaakId);
        setCaption(aantalSignalen
            .map(count -> format("Signalen Inwoner.app (%d)", count))
            .orElse("Signalen Inwoner.app"));
        found = true;
      });
    }
    super.attach();
  }

  private Optional<Integer> getAantalSignalen(String zaakId) {
    try {
      GbaApplication app = getGbaApplication();
      InwonerAppService signalenService = app.getServices().getInwonerAppService();
      return signalenService.getAantalSignalen(zaakId);
    } catch (Exception e) {
      new Message(getWindow(), e.getMessage(), Message.TYPE_ERROR_MESSAGE);
      return Optional.of(0);
    }
  }

  private Optional<String> getZaakId() {
    return ofNullable(defaultIfBlank(zaak.getZaakHistorie()
        .getIdentificaties()
        .getNummer(ZaakIdType.INWONER_APP.getCode()),
        null));
  }

  public void onClick() {
    if (zaak != null) {
      getGbaApplication().getParentWindow().addWindow(new SignalenWindow());
    }
  }

  public void addCloseListener(Runnable closeListener) {
    this.closeListener = closeListener;
  }

  public class SignalenWindow extends GbaModalWindow {

    public SignalenWindow() {
      super("Signalen Inwoner.app", "400px");
      VLayout vLayout = new VLayout();
      vLayout.setMargin(true);

      Button openButton = new Button("Toon zaak in Inwoner.app");
      openButton.setWidth("100%");

      Optional<String> zaakId = getZaakId();
      if (zaakId.isPresent() && getAantalSignalen(zaakId.get()).isPresent()) {
        openButton.addListener((ClickListener) event -> {
          OpenZaakRequest request = new OpenZaakRequest(zaakId.get());
          getInwonerSignalenService().getExternalURL(request).ifPresent(ZaakInwonerAppButton.this::openExternalURL);
          closeWindow();
        });
      } else {
        openButton.setCaption("Maak zaak aan in Inwoner.app");
        openButton.addListener((ClickListener) event -> {
          if (getInwonerSignalenService().maakZaak(zaak)) {
            new Message(getGbaApplication().getParentWindow(),
                "De zaak is aangemaakt in Inwoner.app", Message.TYPE_SUCCESS);

            getZaakId().ifPresent(id -> {
              OpenZaakRequest request = new OpenZaakRequest(id);
              getInwonerSignalenService()
                  .getExternalURL(request)
                  .ifPresent(ZaakInwonerAppButton.this::openExternalURL);
            });
          }
          closeWindow();
        });
      }

      SignaalForm form = new SignaalForm();
      vLayout.addComponent(form);
      vLayout.addComponent(openButton);
      addComponent(vLayout);
    }

    private InwonerAppService getInwonerSignalenService() {
      return getGbaApplication().getServices().getInwonerAppService();
    }

    @Override
    public void closeWindow() {
      ofNullable(closeListener).ifPresent(Runnable::run);
      super.closeWindow();
    }
  }

  public void openExternalURL(String url) {
    getGbaApplication().getParentWindow().open(new ExternalResource(url), "Inwoner.app");
  }

  public GbaApplication getGbaApplication() {
    return (GbaApplication) getApplication();
  }

  public class SignaalForm extends GbaForm<SignaalBean> {

    public SignaalForm() {
      setOrder(ZAAK_ID, AANTAL, NAAM, TEL, EMAIL);
      setColumnWidths("100px", "");

      SignaalBean bean = new SignaalBean();
      bean.setZaakId("N.v.t.");
      bean.setAantal("N.v.t.");
      Services services = getGbaApplication().getServices();

      InwonerAppService signalenService = services.getInwonerAppService();
      MaakZaakRequest request = signalenService.createMaakZaakRequest(zaak);

      bean.setNaam(request.persoon().naam());
      bean.setTel(request.persoon().telnr());
      bean.setEmail(request.persoon().email());
      setBean(bean);

      getZaakId().ifPresent(zaakId -> {
        bean.setZaakId(zaakId);
        bean.setAantal(getAantalSignalen(zaakId)
            .map(Object::toString)
            .orElse("N.v.t."));
      });

      setBean(bean);
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class SignaalBean {

    @Field(type = FieldType.LABEL,
        caption = "Signalen-id",
        width = "300px")
    private String zaakId = "";

    @Field(type = FieldType.LABEL,
        caption = "Aantal signalen",
        width = "300px")
    private String aantal = "";

    @Field(type = FieldType.LABEL,
        caption = "Naam",
        width = "300px")
    private String naam = "";

    @Field(type = FieldType.LABEL,
        caption = "Telefoon",
        width = "300px")
    private String tel = "";

    @Field(type = FieldType.LABEL,
        caption = "E-mail",
        width = "300px")
    private String email = "";
  }
}
