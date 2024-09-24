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

package nl.procura.gba.web.modules.bs.registration.page10;

import static nl.procura.gba.web.modules.beheer.fileimport.FileImportProcess.FIRST_REGISTRATION;
import static nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableListener.existingRegistration;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISLETTER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISNUMMER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.POSTCODE;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.TOEVOEGING;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportProcess;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.beheer.fileimport.fileselection.FileImportHandler;
import nl.procura.gba.web.modules.beheer.fileimport.fileselection.FileImportWindow;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableFilter;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.AddressLayout;
import nl.procura.gba.web.modules.bs.registration.AbstractRegistrationPage;
import nl.procura.gba.web.modules.bs.registration.fileimport.FileImportRegistrant;
import nl.procura.gba.web.services.beheer.bag.BagService;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.StaydurationType;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressRequest;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.validation.Postcode;

/**
 * First registration - declaration
 */
public class Page10Registration extends AbstractRegistrationPage {

  private Page10DeclarationForm declarationForm;
  private InterpreterForm       interpreterForm;
  private AddressLayout         addressLayout;

  public Page10Registration() {
    super("Eerste inschrijving - aangifte");
  }

  @Override
  public boolean checkPage() {
    if (super.checkPage()) {
      declarationForm.save();
      interpreterForm.save();

      if (addressLayout.isSaved()) {
        getServices().getRegistrationService().saveRegistration(getZaakDossier());
        return true;
      }
    }

    return false;
  }

  @Override
  public void initPage() {
    super.initPage();

    buttonPrev.setEnabled(false);
    addButton(buttonPrev);
    addButton(buttonNext);

    declarationForm = new Page10DeclarationForm(getDossier());
    interpreterForm = new InterpreterForm(getZaakDossier());
    addressLayout = new AddressLayout(new RegistrationAddress(getZaakDossier()), getServices());

    OptieLayout optionLayout = new OptieLayout();
    optionLayout.getRight().setWidth("200px");
    optionLayout.getRight().addButton(new Button("Selecteer uit bestand"),
        event -> getApplication().getParentWindow()
            .addWindow(new FileImportWindow(getFileImportHandler())));
    optionLayout.getLeft().addComponent(declarationForm);

    addComponent(registrantLayout);
    addComponent(new Fieldset("Aanvang inschrijving"));
    addComponent(optionLayout);
    addComponent(interpreterForm);
    addComponent(addressLayout);
  }

  public FileImportHandler getFileImportHandler() {

    return new FileImportHandler() {

      private FileImportTable       table;
      private FileImportTableFilter filter;

      @Override
      public FileImportProcess getFileImportProcess() {
        return FIRST_REGISTRATION;
      }

      @Override
      public GbaApplication getApplication() {
        return Page10Registration.this.getApplication();
      }

      @Override
      public FileImportTable getTable(FileImport fileImport) {
        return FileImportType.getById(fileImport.getTemplate())
            .map(type -> {
              table = type.getImporter().createTable(existingRegistration(record -> selectRecord(type, record)));
              table.update(getApplication().getServices().getFileImportService().getFileImportRecords(fileImport));
              filter = type.getImporter().createFilter(fileImport, table);
              return table;
            }).orElseThrow(() -> new ProException("Dit bestand is verouderd"));
      }

      @Override
      public FileImportTableFilter getTableFilter() {
        return filter;
      }

      private void selectRecord(FileImportType type, FileImportRecord record) {
        loadImportRegistrant(FileImportRegistrant.of(type, record));
        finishImport();
      }
    };
  }

  @Override
  protected void loadImportRegistrant(FileImportRegistrant fileImportRegistrant) {
    DossierRegistration zaakDossier = getZaakDossier();
    declarationForm.getBean().setStayDuration(StaydurationType.LONGER);
    declarationForm.getBean().setCountry(GbaTables.LAND.getByDescr(fileImportRegistrant.getPreviousCountry()));
    declarationForm.setBean(declarationForm.getBean());
    zaakDossier.setAddressSource(AddressSourceType.BAG.getCode());
    zaakDossier.setCFileRecord(fileImportRegistrant.getRecord().getId());
    setImportRegistrant(fileImportRegistrant);

    addDocuments(fileImportRegistrant);
    addBagAddress(fileImportRegistrant.getRecord(), zaakDossier);
  }

  private void addDocuments(FileImportRegistrant fileImportRegistrant) {
    String uuid = fileImportRegistrant.getRecord().getUuid();
    DMSService dmsService = getServices().getDmsService();
    if (isNotBlank(uuid) && dmsService.getDocumentsByZaak(getDossier()).getDocuments().isEmpty()) {
      List<DMSDocument> documents = dmsService.getDocumentsById(uuid).getDocuments();
      if (!documents.isEmpty()) {
        documents.forEach(document -> dmsService.save(getDossier(), document));
        successMessage(String.format("Er zijn %d documenten toegevoegd aan de zaak", documents.size()));
        getBsModule().checkButtons();
      }
    }
  }

  private void addBagAddress(FileImportRecord record, DossierRegistration zaakDossier) {
    if (isNotBlank(record.getValue(POSTCODE))) {
      Optional<Address> bagAddress = getBagAddress(record);
      if (bagAddress.isPresent()) {
        zaakDossier.setPostalCode(bagAddress.get().getPostalCode());
        zaakDossier.setHouseNumber(NumberUtils.toLong(bagAddress.get().getHnr()));
        zaakDossier.setHouseNumberL(bagAddress.get().getHnrL());
        zaakDossier.setHouseNumberT(bagAddress.get().getHnrT());
        addressLayout.updateAddress(new RegistrationAddress(zaakDossier));
      } else {
        warningMessage("Adres is niet gevonden in de BAG. Controleer de gegevens");
      }
    }
  }

  private Optional<Address> getBagAddress(FileImportRecord record) {
    BagService geoService = getServices().getGeoService();
    if (geoService.isGeoServiceActive()) {
      AddressRequest request = new AddressRequest()
          .setHnr(record.getValue(HUISNUMMER))
          .setHnrL(record.getValue(HUISLETTER))
          .setHnrT(record.getValue(TOEVOEGING))
          .setPc(Postcode.getCompact(record.getValue(POSTCODE)));
      return geoService.search(request).stream().findFirst();
    }
    return Optional.empty();
  }
}
