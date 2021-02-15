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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.windows;

import static nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages.Tab1RaasPage2Bean.*;
import static nl.procura.raas.rest.domain.aanvraag.AanduidingVermissingType.NIET_VAN_TOEPASSING;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Optional;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.dto.raas.aanvraag.DocHistorieDto;
import nl.procura.dto.raas.aanvraag.VermissingDto;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.raas.AfsluitRequest;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentStatus;
import nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType;
import nl.procura.raas.rest.domain.aanvraag.AanduidingVermissingType;
import nl.procura.raas.rest.domain.aanvraag.AfsluitingStatusType;
import nl.procura.raas.rest.domain.aanvraag.LeveringStatusType;
import nl.procura.raas.rest.domain.aanvraag.UpdateAanvraagRequest;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class RaasUitreikWindow extends GbaModalWindow {

  protected RaasUitreikWindow(DocAanvraagDto aanvraag) {
    setWidth("600px");
    setCaption("Uitreiking reisdocument");
    addComponent(new ModuleTemplate() {

      @Override
      public void event(PageEvent event) {
        super.event(event);
        if (event.isEvent(InitPage.class)) {
          getPages().getNavigation().goToPage(new Page(aanvraag));
        }
      }
    });
  }

  public class Page extends NormalPageTemplate {

    private final Form           form;
    private final DocAanvraagDto aanvraag;

    public Page(DocAanvraagDto aanvraag) {
      this.aanvraag = aanvraag;
      buttonClose.setCaption("Annuleren (Esc)");
      addButton(buttonSave, 1f);
      addButton(buttonClose);

      boolean isDocumentGoed = LeveringStatusType.DOCUMENT_GOED.equals(
          aanvraag.getLevering().getStatus().getValue());

      boolean isNogNietAfgesloten = AfsluitingStatusType.NIET_AFGESLOTEN.equals(
          aanvraag.getAfsluiting().getStatus().getValue());

      form = new Form(aanvraag);
      boolean isOk = isDocumentGoed && isNogNietAfgesloten;

      if (!isOk) {

        StringBuilder message = new StringBuilder();

        if (!isDocumentGoed) {
          message.append("Status levering moet 2 (document goed) zijn.").append("</br> ");
        }

        if (!isNogNietAfgesloten) {
          message.append("Status afsluiting moet 0 (aanvraag niet afgesloten) zijn.");
        }

        addComponent(new InfoLayout("Proces niet mogelijk", ProcuraTheme.ICOON_24.WARNING,
            MiscUtils.setClass(false, message.toString())));
        buttonSave.setEnabled(false);
      }

      addComponent(form);

      if (!isOk) {
        form.setReadOnly(true);
      }
    }

    @Override
    public void onSave() {

      form.commit();

      // Maak levering of afsluiting request
      AfsluitRequest afsluitRequest = new AfsluitRequest();
      afsluitRequest.setUpdateProweb(true); // Update Proweb want uitgereikt vanuit RAAS service
      afsluitRequest.setAanvraagNummer(aanvraag.getAanvraagNr().getValue());

      DocumentRecord nrNLDocIn = form.getBean().getNrNLDocIn();
      afsluitRequest.setNrNLDocIn(nrNLDocIn != null ? nrNLDocIn.getNummer() : "");
      afsluitRequest.setStatusLevering(form.getBean().getStatusLev());
      afsluitRequest.setStatusAfsluiting(form.getBean().getStatusAfsl());

      UpdateAanvraagRequest request = afsluitRequest.createRequest();
      DocAanvraagDto dto = request.getAanvraag();

      // Vermissing
      DocumentRecord nrVerm = form.getBean().getNrVerm();
      AanduidingVermissingType indVerm = form.getBean().getIndVerm();
      dto.getVermissing().withIndicatie(indVerm != null ? indVerm : NIET_VAN_TOEPASSING);
      dto.getVermissing().withAutoriteit(form.getBean().getAutoritVerm());
      dto.getVermissing().withDatum(Integer.valueOf(form.getBean().getDatumVerm().getStringValue()));
      dto.getVermissing().withDocNr(nrVerm != null ? nrVerm.getNummer() : "");
      dto.getVermissing().withProcesverbaal(form.getBean().getPvVerm());
      dto.getVermissing().withVerzoek(form.getBean().getVerzoekVerm());

      getServices().getRaasService().updateAanvraag(request);
      getServices().getRaasService().reload(aanvraag);

      ZaakArgumenten z = new ZaakArgumenten(aanvraag.getAanvraagNr().getValue().toString());
      Optional<Zaak> zaak = getServices().getZakenService().getStandaardZaken(z).stream().findFirst();

      if (zaak.isPresent()) {
        ReisdocumentAanvraag raanvraag = (ReisdocumentAanvraag) zaak.get();
        ReisdocumentStatus status = raanvraag.getReisdocumentStatus();
        status.setStatusLevering(LeveringType.get(form.getBean().getStatusLev().getCode()));
        status.setStatusAfsluiting(SluitingType.get(form.getBean().getStatusAfsl().getCode()));
        status.setDatumTijdAfsluiting(new DateTime());
        raanvraag.setGebruikerUitgifte(new UsrFieldValue(getServices().getGebruiker()));
        getServices().getReisdocumentService().save(raanvraag);
      }

      if (afsluitRequest.isAanpassingLevering()) {
        new Message(getParentWindow(), "Aanvraag is afgesloten. Het bericht zal worden verstuurd naar het RAAS",
            Message.TYPE_SUCCESS);

      } else {
        new Message(getParentWindow(),
            "Status levering is gewijzigd. Het bericht zal worden verstuurd naar het RAAS",
            Message.TYPE_SUCCESS);
      }

      close();
    }

    @Override
    public void onClose() {
      close();
    }
  }

  public class Form extends GbaForm<Bean> {

    private final DocAanvraagDto aanvraag;

    public Form(DocAanvraagDto aanvraag) {
      this.aanvraag = aanvraag;
      setOrder(STATUS_LEV, STATUS_AFSL, NR_NL_DOC_IN, NR_NL_DOC, IND_VERM, D_VERM, NR_VERM, AUTORIT_VERM, PV_VERM,
          VERZOEK_VERM);
      setColumnWidths("160px", "");
      setReadonlyAsText(true);
      setCaption("Levering / uitreiking");

      Bean bean = new Bean();
      bean.setStatusLev(aanvraag.getLevering().getStatus().getValue());
      bean.setStatusAfsl(aanvraag.getAfsluiting().getStatus().getValue());
      bean.setNrNLDocIn(new DocumentRecord(aanvraag.getNrNederlandsDocIn().getValue()));
      bean.setNrNLDoc(aanvraag.getNrNederlandsDoc().getValue());

      // Vermissing
      VermissingDto vermissing = aanvraag.getVermissing();
      bean.setIndVerm(vermissing.getIndicatie().getValue());
      bean.setDatumVerm(new DateFieldValue(vermissing.getDatum().getValue()));
      bean.setNrVerm(new DocumentRecord(vermissing.getDocNr().getValue()));
      bean.setAutoritVerm(vermissing.getAutoriteit().getValue());
      bean.setPvVerm(vermissing.getProcesverbaal().getValue());
      bean.setVerzoekVerm(vermissing.getVerzoek().getValue());

      setBean(bean);
    }

    @Override
    public void setColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {

      if (property.is(IND_VERM)) {
        getLayout().addFieldset("Inhouding / vermissing (optioneel)");
      }

      super.setColumn(column, field, property);
    }

    @Override
    public void afterSetBean() {

      ProNativeSelect leveringField = getField(STATUS_LEV, ProNativeSelect.class);
      setSluitingField(getBean().getStatusLev());

      leveringField.addListener((ValueChangeListener) event -> {
        enableVermissing((LeveringStatusType) leveringField.getValue());
        setSluitingField((LeveringStatusType) leveringField.getValue());
      });

      ProNativeSelect nrNlDocIn = getField(NR_NL_DOC_IN, ProNativeSelect.class);
      nrNlDocIn.setContainerDataSource(new DocumentContainer(aanvraag));
      nrNlDocIn.setValue(new DocumentRecord(aanvraag.getNrNederlandsDocIn().getValue()));

      com.vaadin.ui.Field indVermField = getField(IND_VERM);
      indVermField.addListener((ValueChangeListener) event -> {
        updateVermissing((AanduidingVermissingType) event.getProperty().getValue());
      });

      ProNativeSelect nrVerm = getField(NR_VERM, ProNativeSelect.class);
      nrVerm.setContainerDataSource(new DocumentContainer(aanvraag));
      nrVerm.setValue(new DocumentRecord(aanvraag.getVermissing().getDocNr().getValue()));

      nrVerm.addListener((ValueChangeListener) event -> {
        DocumentRecord dr = (DocumentRecord) event.getProperty().getValue();
        if (dr != null) {
          getField(AUTORIT_VERM).setValue(dr.getAutoriteit());
        }
      });

      enableVermissing((LeveringStatusType) leveringField.getValue());
      updateVermissing((AanduidingVermissingType) indVermField.getValue());

      super.afterSetBean();
    }

    private void enableVermissing(final LeveringStatusType value) {
      getFields(IND_VERM, D_VERM, NR_VERM, AUTORIT_VERM, PV_VERM, VERZOEK_VERM)
          .forEach(f -> f.setVisible(LeveringStatusType.DOCUMENT_GOED.equals(value)));
    }

    private void setSluitingField(LeveringStatusType leveringType) {
      ProNativeSelect sluitingField = getField(STATUS_AFSL, ProNativeSelect.class);
      com.vaadin.ui.Field nrNlDocInField = getField(NR_NL_DOC_IN);
      com.vaadin.ui.Field nrNlDocField = getField(NR_NL_DOC);

      if (LeveringStatusType.DOCUMENT_GOED.equals(leveringType)) {
        sluitingField.setContainerDataSource(StatusAfsluitingContainer.getAll());
        sluitingField.setValue(AfsluitingStatusType.DOC_UITGEREIKT);
        nrNlDocInField.setVisible(true);
        nrNlDocField.setVisible(true);
      } else {
        sluitingField.setContainerDataSource(StatusAfsluitingContainer.getError());
        sluitingField.setValue(AfsluitingStatusType.DOC_ONJUIST);
        nrNlDocInField.setVisible(false);
        nrNlDocField.setVisible(false);
      }
      repaint();
    }

    /**
     * Als indicatie voorkomt dan zijn datum, nummer en autoreit verplicht
     * Als indicatie V dan is pv ook verplicht
     */
    private void updateVermissing(AanduidingVermissingType aand) {

      getField(D_VERM).setRequired(false);
      getField(NR_VERM).setRequired(false);
      getField(AUTORIT_VERM).setRequired(false);
      getField(PV_VERM).setRequired(false);

      if (aand != null && fil(aand.getCode())) {
        getField(D_VERM).setRequired(true);
        getField(D_VERM).setValue(new ProcuraDate().getSystemDate());
        getField(NR_VERM).setRequired(true);
        getField(AUTORIT_VERM).setRequired(true);

        if (AanduidingVermissingType.VERMISSING.getCode().equals(aand.getCode())) {
          getField(PV_VERM).setRequired(true);
        }
      } else {

        getField(D_VERM).setValue("");
        getField(NR_VERM).setValue(null);
        getField(AUTORIT_VERM).setValue("");
        getField(PV_VERM).setValue("");
        getField(VERZOEK_VERM).setValue("");
      }

      repaint();
    }
  }

  public static class AanduidingVermissing extends ArrayListContainer {

    public AanduidingVermissing() {
      addItem(AanduidingVermissingType.INGENOMEN);
      addItem(AanduidingVermissingType.VERMISSING);
    }
  }

  @Data
  @EqualsAndHashCode(of = "nummer")
  public class DocumentRecord {

    private DocHistorieDto document;
    private String         nummer;
    private String         autoriteit;

    public DocumentRecord(DocHistorieDto document) {
      this.document = document;
      this.nummer = document.getDocNr().getValue();
      this.autoriteit = document.getAutoriteit().getValue();
    }

    public DocumentRecord(String nummer) {
      this.nummer = nummer;
    }

    public String toString() {
      if (document != null) {
        return document.getDocSoort().getValue() + ": " + document.getDocNr().getValue();
      }
      return nummer;
    }
  }

  public class DocumentContainer extends ArrayListContainer {

    DocumentContainer(DocAanvraagDto aanvraag) {
      for (DocHistorieDto document : aanvraag.getHistorie().getAll()) {
        if (document.getDocSoort().getValue().getCode().equals(aanvraag.getDocSoort().getValue().getCode())) {
          addItem(new DocumentRecord(document));
        }
      }
    }
  }

  public static class StatusLeveringContainer extends ArrayListContainer {

    public StatusLeveringContainer() {
      addItem(LeveringStatusType.DOCUMENT_GOED);
      addItem(LeveringStatusType.DOCUMENT_NIET_GOED);
      addItem(LeveringStatusType.DOCUMENT_VERDWENEN);
    }
  }

  public static class StatusAfsluitingContainer extends ArrayListContainer {

    public static StatusAfsluitingContainer getAll() {
      StatusAfsluitingContainer sl = new StatusAfsluitingContainer();
      sl.addItem(AfsluitingStatusType.DOC_UITGEREIKT);
      sl.addItem(AfsluitingStatusType.DOC_ONJUIST);
      sl.addItem(AfsluitingStatusType.DOC_ANDERE_INSTANTIE);
      sl.addItem(AfsluitingStatusType.DOC_NIET_UITGEREIKT);
      return sl;
    }

    public static StatusAfsluitingContainer getError() {
      StatusAfsluitingContainer sl = new StatusAfsluitingContainer();
      sl.addItem(AfsluitingStatusType.DOC_ONJUIST);
      sl.addItem(AfsluitingStatusType.DOC_NIET_UITGEREIKT);
      return sl;
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Bean implements Serializable {

    @Field(customTypeClass = ProNativeSelect.class,
        caption = "Status levering",
        required = true,
        width = "300px")
    @Select(containerDataSource = StatusLeveringContainer.class,
        nullSelectionAllowed = false)
    @Immediate
    private LeveringStatusType statusLev = null;

    @Field(customTypeClass = ProNativeSelect.class,
        caption = "Status afsluiting",
        required = true,
        width = "300px")
    @Select(nullSelectionAllowed = false)
    @Immediate
    private AfsluitingStatusType statusAfsl = null;

    // Optioneel
    @Field(customTypeClass = ProNativeSelect.class, caption = "Huidig document ingeleverd", width = "300px")
    private DocumentRecord nrNLDocIn = null;

    @Field(type = FieldType.LABEL,
        caption = "Nieuw document")
    @TextField(maxLength = 9,
        nullRepresentation = "")
    private String nrNLDoc = "";

    // Inhouding / vermissing
    // Als gevuld dan datum,nummer,autoriteit verplicht
    // Als waarde == Vermissing dan ook nog pv nummer verplicht

    @Field(type = FieldType.NATIVE_SELECT,
        caption = "Aanduiding",
        width = "90px")
    @Select(containerDataSource = AanduidingVermissing.class)
    @Immediate
    private AanduidingVermissingType indVerm = null;

    @Field(customTypeClass = DatumVeld.class,
        caption = "Datum vermissing",
        width = "90px")
    private DateFieldValue datumVerm = null;

    @Field(customTypeClass = ProNativeSelect.class,
        caption = "Document",
        width = "300px")
    @Immediate
    private DocumentRecord nrVerm = null;

    @Field(type = FieldType.TEXT_FIELD,
        caption = "Autoriteit",
        width = "300px")
    @TextField(maxLength = 80,
        nullRepresentation = "")
    private String autoritVerm = "";

    @Field(type = FieldType.TEXT_FIELD,
        caption = "Proces-verbaal",
        width = "300px")
    @TextField(maxLength = 25,
        nullRepresentation = "")
    private String pvVerm = "";

    @Field(type = FieldType.TEXT_FIELD,
        caption = "Verzoek aanvraag",
        width = "300px")
    @TextField(maxLength = 80,
        nullRepresentation = "")
    private String verzoekVerm = "";
  }

}
