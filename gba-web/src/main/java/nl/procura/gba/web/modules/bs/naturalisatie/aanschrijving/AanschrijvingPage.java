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
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.NATURALISATIE;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.OPTIE;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayoutConfig;
import nl.procura.gba.web.components.layouts.form.document.PrintShowRecordFilter;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.services.bs.naturalisatie.document.Ceremonie;
import nl.procura.gba.web.services.bs.naturalisatie.document.DossierNaturalisatieTemplateData;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.AllArgsConstructor;

public class AanschrijvingPage extends NormalPageTemplate {

  private AanschrijvingForm form;
  private final VLayout     soortLayout = new VLayout();
  private PrintLayout       printLayout;

  public final Button                buttonPreview = new Button("Voorbeeld / e-mailen");
  public final Button                buttonPrint   = new Button("Afdrukken (F3)");
  private final DossierNaturalisatie zaakDossier;

  public AanschrijvingPage(DossierNaturalisatie zaakDossier) {
    this.zaakDossier = zaakDossier;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPreview);
      addButton(buttonPrint, 1f);
      addButton(buttonClose);

      DocumentType documentType = zaakDossier.isOptie() ? OPTIE : NATURALISATIE;
      getServices().getNaturalisatieService().herlaadToestemminggevers(zaakDossier);
      form = new AanschrijvingForm(zaakDossier, getSoorten(documentType),
          documentPath -> setPrintLayout(documentType, documentPath));

      setInfo("Selecteer de persoon en het document. Druk het document af.");
      addComponent(form);
      addComponent(soortLayout);
    }

    super.event(event);
  }

  private List<DocumentSoort> getSoorten(DocumentType documentType) {
    Services services = getApplication().getServices();
    return services.getDocumentService().getDocumentSoorten(services.getGebruiker(), documentType);
  }

  private void setPrintLayout(DocumentType documentType, String documentPath) {
    soortLayout.removeAllComponents();
    soortLayout.addComponent(getLayout(documentPath, documentType));
    buttonPreview.setEnabled(printLayout != null);
    buttonPrint.setEnabled(printLayout != null);
  }

  private Fieldset getLayout(String documentPath, DocumentType documentType) {
    PrintMultiLayoutConfig config = PrintMultiLayoutConfig.builder()
        .model(zaakDossier)
        .zaak(zaakDossier.getDossier())
        .documentTypes(new DocumentType[]{ documentType })
        .formHidden(true)
        .showRecordFilter(new DocumentPathFilter(documentPath))
        .build();

    printLayout = new PrintLayout(config);
    VLayout layout = new VLayout();
    layout.addComponent(printLayout);
    return new Fieldset("Documenten - " + documentType.getOms(), layout);
  }

  @AllArgsConstructor
  private static class DocumentPathFilter implements PrintShowRecordFilter {

    private String documentPath;

    @Override
    public List<DocumentSoort> apply(List<DocumentSoort> soorten) {
      List<DocumentSoort> list = new ArrayList<>();
      for (DocumentSoort soort : soorten) {
        DocumentSoort newSoort = new DocumentSoort(soort.getType());
        for (DocumentRecord record : soort.getDocumenten()) {
          if (record.getPad().equals(documentPath)) {
            newSoort.getDocumenten().add(record);
          }
        }
        if (!newSoort.getDocumenten().isEmpty()) {
          list.add(newSoort);
        }
      }
      return list;
    }
  }

  public boolean checkPage() {
    form.commit();
    return false;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if ((button == buttonPrint) || (button == buttonPreview)) {
      if (printLayout == null) {
        throw new ProException(WARNING, "Selecteer een soort aanschrijving");
      }
    }

    if (button == buttonPrint || keyCode == ShortcutAction.KeyCode.F3) {
      setModel();
      printLayout.doPrint(false);

    } else if (button == buttonPreview) {
      setModel();
      printLayout.doPrint(true);
    }

    super.handleEvent(button, keyCode);
  }

  private void setModel() {
    checkPage();

    getServices().getDossierService().herlaadDossierPersoonPLen(zaakDossier.getDossier());
    getServices().getNaturalisatieService().herlaadToestemminggevers(zaakDossier);

    DossierPersoon aanschrijfpersoon = (DossierPersoon) form.getField(F_AANSCHRIJFPERSOON).getValue();
    Ceremonie ceremonie = (Ceremonie) form.getField(F_CEREMONIE).getValue();
    form.getField(F_AANSCHRIJFPERSOON).commit();
    printLayout.setModel(new DossierNaturalisatieTemplateData(zaakDossier, aanschrijfpersoon, ceremonie));
  }

  private static class PrintLayout extends PrintMultiLayout {

    public PrintLayout(PrintMultiLayoutConfig config) {
      super(config);
    }

    @Override
    public void doPrint(boolean isPreview) {
      super.doPrint(isPreview);
    }
  }
}
