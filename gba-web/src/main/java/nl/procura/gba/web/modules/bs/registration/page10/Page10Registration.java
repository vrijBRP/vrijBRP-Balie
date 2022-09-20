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
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.ACHTERNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.EMAIL;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.GEBOORTELAND;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.GEBOORTEPLAATS;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.GESLACHT;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.HUISLETTER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.HUISNUMMER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.NATIONALITEIT;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.POSTCODE;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.STRAATNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.TOEVOEGING;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.VOORNAMEN;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.VOORVOEGSEL;
import static nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter.WOONPLAATS;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportProcess;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.beheer.fileimport.fileselection.FileImportHandler;
import nl.procura.gba.web.modules.beheer.fileimport.fileselection.FileImportWindow;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
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
import nl.procura.standard.ProcuraDate;
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
    optionLayout.getRight().addButton(new Button("Importeer uit bestand"),
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

      @Override
      public FileImportProcess getFileImportProcess() {
        return FIRST_REGISTRATION;
      }

      @Override
      public GbaApplication getApplication() {
        return Page10Registration.this.getApplication();
      }

      @Override
      public GbaTable getTable(FileImport fileImport) {
        return FileImportType.getById(fileImport.getTemplate()).map(type -> {
          FileImportTable table = type.getTable().apply(this::selectFileImportRecord);
          table.update(getApplication().getServices().getFileImportService().getFileImportRecords(fileImport));
          return table;
        }).orElseThrow(() -> new ProException("Dit bestand is verouderd"));
      }

      private void selectFileImportRecord(FileImportRecord record) {
        DossierRegistration zaakDossier = getZaakDossier();
        declarationForm.getBean().setStayDuration(StaydurationType.LONGER);
        declarationForm.setBean(declarationForm.getBean());
        zaakDossier.setAddressSource(AddressSourceType.BAG.getCode());
        setFileImportRegistrant(FileImportRegistrant.builder()
            .lastname(record.getValue(ACHTERNAAM))
            .prefix(record.getValue(VOORVOEGSEL))
            .firstname(record.getValue(VOORNAMEN))
            .gender(record.getValue(GESLACHT))
            .birthDate(new ProcuraDate(record.getValue(GEBOORTEDATUM)))
            .birthPlace(record.getValue(GEBOORTEPLAATS))
            .birthCountry(record.getValue(GEBOORTELAND))
            .street(record.getValue(STRAATNAAM))
            .postalcode(record.getValue(POSTCODE))
            .hnr(record.getValue(HUISNUMMER))
            .hnrL(record.getValue(HUISLETTER))
            .hnrT(record.getValue(TOEVOEGING))
            .place(record.getValue(WOONPLAATS))
            .nationality(record.getValue(NATIONALITEIT))
            .email(record.getValue(EMAIL))
            .build());
        Optional<Address> bagAddress = getBagAddress(record);

        try {
          if (bagAddress.isPresent()) {
            zaakDossier.setPostalCode(bagAddress.get().getPostalCode());
            zaakDossier.setHouseNumber(NumberUtils.toLong(bagAddress.get().getHnr()));
            zaakDossier.setHouseNumberL(bagAddress.get().getHnrL());
            zaakDossier.setHouseNumberT(bagAddress.get().getHnrT());
            addressLayout.updateAddress(new RegistrationAddress(zaakDossier));
          } else {
            throw new ProException(WARNING, "Adres niet gevonden in de BAG");
          }
        } finally {
          finishImport();
        }
      }

      private Optional<Address> getBagAddress(FileImportRecord record) {
        BagService geoService = getServices().getGeoService();
        if (geoService.isGeoServiceActive()) {
          AddressRequest request = new AddressRequest()
              .setStreet(record.getValue(STRAATNAAM))
              .setHnr(record.getValue(HUISNUMMER))
              .setHnrL(record.getValue(HUISLETTER))
              .setHnrT(record.getValue(TOEVOEGING))
              .setPc(Postcode.getCompact(record.getValue(POSTCODE)));
          return geoService.search(request).stream().findFirst();
        }
        return Optional.empty();
      }
    };
  }
}
