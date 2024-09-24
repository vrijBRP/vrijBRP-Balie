/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;

import nl.procura.bvbsn.actions.ActionVerificatieIdentiteitsDocument;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab4.search.window.BvBsnWindow;
import nl.procura.gba.web.modules.zaken.identificatie.page1.IdVraagGenerator.IDVraag;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringWindow;
import nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RijbewijsErrorWindow;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.gba.verificatievraag.VerificatievraagService;
import nl.procura.gba.web.services.gba.verificatievraag.VerificatievraagService.VerificatieActie;
import nl.procura.gba.web.services.zaken.identiteit.IdVerplichtMate;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieStatusListener;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.rdw.functions.RdwMessage;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Identificatie vaststelling
 */
public class Page1Identificatie extends NormalPageTemplate {

  private static final int                     MIN_AANTAL_VRAGEN    = 4;
  private final        Button                  buttonAkkoord        = new Button("Akkoord (F2)");
  private final        Button                  buttonVerifieer      = new Button("Verifieer (F4)");
  private final        Button                  buttonVragen         = new Button("Nieuwe vragen (F5)");
  private final        Button                  buttonSkip           = new Button("Overslaan (F1)");
  private final        Button                  buttonRijbewijs      = new Button("Toon rijbewijs");
  private final        Button                  buttonReisdocumenten = new Button("Toon reisdocumenten BRP");
  private final        Button                  buttonBasisregister  = new Button("Basisregister");
  private final        Button                  buttonRpsInfo        = new Button("Toon RPS info");
  private final IdentificatieStatusListener succesListener;
  private              Page1IdentificatieTable table                = null;
  private              Page1IdentificatieForm1 form1                = null;
  private              Page1IdentificatieForm2 form2                = null;

  public Page1Identificatie(IdentificatieStatusListener succesListener) {

    setSpacing(true);

    this.succesListener = succesListener;

    buttonVerifieer.setEnabled(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new Page1IdentificatieTable() {

        @Override
        public void onDoubleClick(Record record) {
          selectRecord(record);
        }

        @Override
        public void setRecords() {

          int i = 0;
          IdVraagGenerator generator = new IdVraagGenerator();
          for (IDVraag v : generator.getVragen(getPl())) {
            i++;
            Record r = addRecord(v);
            r.addValue(i);
            r.addValue(v.getVraag());
            r.addValue(v.getAntwoord());
          }
        }
      };

      form1 = new Page1IdentificatieForm1();
      form1.getField(Page1IdentificatieBean.SOORT).addListener((ValueChangeListener) event1 -> {
        Object value = event1.getProperty().getValue();
        buttonVerifieer.setEnabled(value != null);
      });

      buttonVerifieer.setDescription("Verifieer het nummer met de verificatievraag");
      buttonRijbewijs.setDescription("Toon de rijbewijsgegevens");

      OptieLayout ol1 = new OptieLayout();
      ol1.getLeft().addComponent(form1);
      ol1.getRight().setWidth("200px");
      ol1.getRight().setCaption("Opties");
      ol1.getRight().addButton(buttonVerifieer, this);
      ol1.getRight().addButton(buttonRijbewijs, this);
      ol1.getRight().addButton(buttonReisdocumenten, this);
      ol1.getRight().addButton(buttonBasisregister, this);

      form2 = new Page1IdentificatieForm2();
      OptieLayout ol2 = new OptieLayout();
      ol2.getLeft().addComponent(form2);
      ol2.getRight().setWidth("200px");
      ol2.getRight().setCaption("Opties");
      ol2.getRight().addButton(buttonRpsInfo, this);

      VrsService vrsService = getServices().getReisdocumentService().getVrsService();
      buttonRpsInfo.setEnabled(vrsService.isEnabled());
      buttonBasisregister.setEnabled(vrsService.isBasisregisterEnabled());

      OptieLayout ol3 = new OptieLayout();
      ol3.getLeft().addComponent(new Fieldset("Identificatie aan de hand van vragen.", table));
      ol3.getRight().setWidth("200px");
      ol3.getRight().setCaption("Opties");
      ol3.getRight().addButton(buttonVragen, this);

      addButton(buttonSkip);
      addButton(buttonAkkoord);
      addButton(buttonClose);

      H2 h2 = new H2("Identificatie");
      getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(buttonSkip));
      getButtonLayout().setExpandRatio(h2, 1f);
      getButtonLayout().setWidth("100%");

      setInfo("Identificatie van " + getPl().getPersoon().getNaam().getPredAdelVoorvGeslVoorn(),
          "Om verder te kunnen zal de identiteit van de aanvrager moeten worden vastgesteld. "
              + "Geef een soort document en een documentnummer in of stel de vragen aan de aanvrager. "
              + "In het geval van vragen zal de aanvrager minimaal 4 vragen correct moeten beantwoorden.");

      addComponent(ol1);
      addComponent(ol2);
      addComponent(ol3);

      IdVerplichtMate verplichtMate = IdVerplichtMate.get(
          along(getApplication().getParmValue(ParameterConstant.ID_VERPLICHT)));
      buttonSkip.setVisible(verplichtMate == IdVerplichtMate.NIET_VERPLICHT_WEL_TONEN);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    Identificatie id = new Identificatie(getPl());

    if (isKeyCode(button, keyCode, KeyCode.F2, buttonAkkoord)) {
      form2.commit();

      if (form2.getBean().isReisdocumentAdministratie()) {
        id.addIdentificatieType(IdentificatieType.REISDOCUMENTEN_ADMINISTRATIE);
      }

      if (form2.getBean().isRijbewijsAdministratie()) {
        id.addIdentificatieType(IdentificatieType.RIJBEWIJZEN_ADMINISTRATIE);
      }

      if (form2.getBean().isExterneApp()) {
        id.addIdentificatieType(IdentificatieType.EXTERNE_APPLICATIE);
      }

      if (form2.getBean().isVragenNietMogelijk()) {
        id.addIdentificatieType(IdentificatieType.PERSOON_GEZIEN_VNM);
      }

      if (form2.getBean().isRps()) {
        id.addIdentificatieType(IdentificatieType.RPS);
      }

      if (buttonVerifieer.isEnabled()) {
        form1.commit();
        id.addIdentificatieType(form1.getBean().getSoort());
        id.setDocumentnr(form1.getBean().getNummer());
      }

      if (!table.getSelectedRecords().isEmpty()) {
        if (table.getSelectedRecords().size() >= MIN_AANTAL_VRAGEN) {
          id.addIdentificatieType(IdentificatieType.VRAGEN);

        } else {
          throw new ProException(INFO, "Selecteer minimaal 4 vragen");
        }
      }

      if (!id.getIdentificatieTypes().isEmpty()) {
        setIdentificatie(id);

      } else {
        throw new ProException(INFO, "Geen identificatie ingevoerd");
      }
    } else if (isKeyCode(button, keyCode, KeyCode.F4, buttonVerifieer)) {
      form1.commit();
      verifierNummer();

    } else if (isKeyCode(button, keyCode, KeyCode.F5, buttonVragen)) {
      table.init();

    } else if (isKeyCode(button, keyCode, KeyCode.F1, buttonSkip)) {
      setStatus(false);

    } else if (button == buttonRpsInfo) {
      ReisdocumentService reisdocumentService = getServices().getReisdocumentService();
      reisdocumentService.getVrsService().checkIdentiteit(getServices().getPersonenWsService().getHuidige())
          .ifPresent(sig -> {
            form2.getField(Page1IdentificatieBean.RPS, CheckBox.class).setValue(Boolean.TRUE);
            getApplication().getParentWindow().addWindow(new SignaleringWindow(sig));
          });

    } else if (button == buttonRijbewijs) {
      toonRijbewijs();

    } else if (button == buttonReisdocumenten) {
      toonReisdocumenten();

    } else if (button == buttonBasisregister) {
      toonBasisregister();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onEnter() {

    if (table.getRecord() != null) {
      selectRecord(table.getRecord());
    }
  }

  public void setDocumentGegevens(IdentificatieType type, String nummer) {

    form1.getField(Page1IdentificatieBean.SOORT).setValue(type);
    form1.getField(Page1IdentificatieBean.NUMMER).setValue(nummer);
  }

  private void selectRecord(Record record) {

    IDVraag v = (IDVraag) record.getObject();

    v.setCorrect(!v.isCorrect());

    table.refreshRecords();
  }

  private boolean sendRijbewijsMessage(RdwMessage message) {

    if (NrdServices.sendMessage(getServices().getRijbewijsService(), message)) {
      return true;
    }
    getParentWindow().getParent().addWindow(new RijbewijsErrorWindow(message.getResponse().getMelding()));
    return false;
  }

  private void setIdentificatie(Identificatie id) {

    getServices().getIdentificatieService().addIdentificatie(id);

    setStatus(true);
  }

  private void setStatus(boolean saved) {

    if (succesListener != null) {
      succesListener.onStatus(saved, true);
    }

    onClose();
  }

  private void toonReisdocumenten() {
    getWindow().getParent().addWindow(new Page1IdentificatieReisdocumentWindow(this, getPl()));
  }

  private void toonBasisregister() {
    getWindow().getParent().addWindow(new Page1IdentificatieBasisregisterWindow(this, getPl()));
  }

  private void toonRijbewijs() {

    P0252 p0252 = new P0252();
    p0252.newF1(getPl().getPersoon().getBsn().getCode());

    if (sendRijbewijsMessage(p0252)) {
      NATPRYBMAATR maatregelen = (NATPRYBMAATR) p0252.getResponse().getObject();
      getWindow().getParent().addWindow(new Page1IdentificatieRijbewijsWindow(this, maatregelen));
    }
  }

  private void verifierNummer() {

    try {
      VerificatievraagService verificatie = getApplication().getServices().getVerificatievraagService();
      Page1IdentificatieBean bean = form1.getBean();

      String type = "";
      String waarde = bean.getNummer();

      switch (bean.getSoort()) {

        case IDENTITEITSKAART:
        case PASPOORT:
          type = ActionVerificatieIdentiteitsDocument.TYPE_REISDOCUMENT;
          break;

        case RIJBEWIJS:
          type = ActionVerificatieIdentiteitsDocument.TYPE_RIJBEWIJS;
          break;

        case VERBLIJFSDOCUMENT:
          type = ActionVerificatieIdentiteitsDocument.TYPE_VREEMDELINGENDOCUMENT;
          break;

        default:
          break;

      }

      ActionVerificatieIdentiteitsDocument actie = verificatie.getActionVerificatieIdentiteitsDocument(type,
          waarde);
      VerificatieActie vactie = verificatie.find(actie);

      if (vactie != null) {
        getWindow().getParent().addWindow(new BvBsnWindow(vactie.getTitle(), actie));
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
