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

package nl.procura.gba.web.services.zaken.algemeen.dms;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid.ONBEKEND;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.DOCUMENTS;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.matcher.HasArgumentWithValue;

/**
 * De locale opslag van afgedrukte documenten. We noemen het maar DMS, maar in feite is het gewoon het filesystem.
 */
@SuppressWarnings("deprecation")
public class LokaleDmsService extends AbstractService implements DmsOptiesService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LokaleDmsService.class.getName());

  public LokaleDmsService() {
    super("Lokale DMS");
  }

  @Override
  public DmsStream getBestand(DmsDocument record) {
    try {
      return new DmsStream("document." + record.getExtensie(), new FileInputStream(record.getPad()));
    } catch (FileNotFoundException e) {
      throw new ProException("Kan bestand: " + record.getBestandsnaam() + " niet vinden", e);
    }
  }

  @Override
  @ThrowException(type = DOCUMENTS,
      value = "Fout bij ophalen opgeslagen bestanden")
  public DmsResultaat getDocumenten(BasePLExt pl) {

    if (pl == null) {
      return new DmsResultaat();
    }

    List<String> mappen = getBestandSubMappen(pl);
    return toDmsResult(getDocumenten(mappen, null));
  }

  @Override
  public int getAantalDocumenten(BasePLExt pl) {

    if (pl == null) {
      return 0;
    }

    List<String> mappen = getBestandSubMappen(pl);
    return getAantalDocumenten(mappen, null);
  }

  @Override
  public int getAantalDocumenten(Zaak zaak) {

    if (zaak == null) {
      return 0;
    }

    BsnFieldValue bsn = zaak.getBurgerServiceNummer();
    AnrFieldValue anr = zaak.getAnummer();

    List<String> subMappen = new ArrayList<>();
    subMappen.add(zaak.getZaakId()); // Zoek op zaak-id

    if (bsn.isCorrect()) {
      subMappen.add(bsn.getStringValue()); // Zoek op BSN
    }

    if (anr.isCorrect()) {
      subMappen.add(anr.getStringValue()); // Zoek op A-nummer
    }

    return getAantalDocumenten(subMappen, zaak.getZaakId());
  }

  /**
   * Geeft alle bestanden terug van deze zaak
   */
  @Override
  public DmsResultaat getDocumenten(Zaak zaak) {

    if (zaak == null) {
      return new DmsResultaat();
    }

    BsnFieldValue bsn = zaak.getBurgerServiceNummer();
    AnrFieldValue anr = zaak.getAnummer();

    List<String> subMappen = new ArrayList<>();
    subMappen.add(zaak.getZaakId()); // Zoek op zaak-id

    if (bsn.isCorrect()) {
      subMappen.add(bsn.getStringValue()); // Zoek op BSN
    }

    if (anr.isCorrect()) {
      subMappen.add(anr.getStringValue()); // Zoek op A-nummer
    }

    DmsResultaat resultaat = toDmsResult(getDocumenten(subMappen, zaak.getZaakId()));

    // Als alleen de zaak is dan filteren op zaak-id
    DmsResultaat filteredResult = new DmsResultaat();
    HasArgumentWithValue<Object, String> matcher = having(on(DmsDocument.class).getZaakId(),
        equalTo(zaak.getZaakId()));
    filteredResult.setDocumenten(Lambda.filter(matcher, resultaat.getDocumenten()));
    return filteredResult;
  }

  public File getDocumentenFolder() {

    String standaardPad = GbaConfig.getPath().getApplicationDir() + "/documenten/output/";
    String customPad = getServices().getParameterService().getParm(ParameterConstant.DOC_OUTPUT_PAD);

    return maakMap(new File(fil(customPad) ? customPad : standaardPad));
  }

  @Override
  public void save(byte[] bytes, String titel, String extensie, String gebruiker, String datatype, String id,
      String zaakId, String dmsNaam, String vertrouwelijkheid) {
    File dir = LokaleDmsUtils.getGenormaliseerdeFolder(getDocumentenFolder(), id);
    LokaleDmsUtils.opslaan(bytes, dir, extensie, titel, gebruiker, datatype, zaakId, dmsNaam, vertrouwelijkheid);
  }

  /**
   * Opslaan onder vermelding van de BSN
   */
  @Override
  public void save(File bestand, String titel, String bestandsnaam, String gebruiker, BasePLExt pl,
      String vertrouwelijkheid) {

    String subMap = getBestandSubMappen(pl).get(0);
    save(bestand, subMap, fil(titel) ? titel : bestandsnaam, gebruiker, DocumentType.PL_UITTREKSEL.getType(),
        subMap, vertrouwelijkheid);
  }

  /**
   * Opslaan met het zaakId
   */
  @Override
  public DmsDocument save(File bestand, String titel, String bestandsnaam, String gebruiker, Zaak zaak,
      String vertrouwelijkheid) {

    List<String> ids = getBestandSubMappen(zaak.getBasisPersoon());
    String zaakId = asList(zaak.getZaakId()).get(0);
    String subMap = ids.size() > 0 ? ids.get(0) : zaakId;

    return save(bestand, subMap, fil(titel) ? titel : bestandsnaam, gebruiker, DocumentType.PL_UITTREKSEL.getType(),
        zaakId, vertrouwelijkheid);
  }

  @Override
  public DmsDocument save(Zaak zaak, File bestand, DmsDocument dmsDocument) {
    List<String> ids = getBestandSubMappen(zaak.getBasisPersoon());
    String zaakId = zaak.getZaakId();
    String subMap = ids.isEmpty() ? zaakId : ids.get(0);

    return save(bestand, subMap, dmsDocument.getTitel(), dmsDocument.getAangemaaktDoor(), dmsDocument.getDatatype(),
        zaakId, dmsDocument.getVertrouwelijkheid());
  }

  @Override
  @ThrowException("Fout bij opslaan document")
  public void save(PrintActie printActie, byte[] documentBytes) {

    if (printActie.getZaak() != null) {
      // Opslaan onder BSN
      String id = astr(printActie.getZaak().getBurgerServiceNummer().getLongValue());

      if (!pos(id)) {
        // Opslaan onder ZaakID
        id = printActie.getZaak().getZaakId();
      }

      File dir = LokaleDmsUtils.getGenormaliseerdeFolder(getDocumentenFolder(), id);
      DocumentRecord document = printActie.getDocument();
      DocumentService documentService = getServices().getDocumentService();
      LokaleDmsUtils.opslaan(documentBytes, dir, printActie.getPrintOptie().getUitvoerformaatType().getType(),
          document.getDocument(), getServices().getGebruiker().getNaam(), document.getType(),
          printActie.getZaak().getZaakId(), document.getDmsNaam(),
          documentService.getStandaardVertrouwelijkheid(document.getVertrouwelijkheid(), ONBEKEND)
              .getNaam());
    }
  }

  @Override
  public void delete(DmsDocument record) {

    File deleteFile = new File(record.getPad());
    if (deleteFile.exists()) {
      deleteFile.delete();
    }
  }

  private DmsResultaat toDmsResult(List<LokaleDmsDocument> localDmsFiles) {

    DmsResultaat dmsResultaat = new DmsResultaat();

    for (LokaleDmsDocument l : localDmsFiles) {

      DmsDocument dmsDocument = new DmsDocument();
      dmsDocument.setAangemaaktDoor(l.getAangemaaktDoor());
      dmsDocument.setDatatype(l.getDatatype());
      dmsDocument.setDatum(along(l.getDatum()));
      dmsDocument.setExtensie(l.getExtensie());
      dmsDocument.setBestandsnaam(l.getBestandsnaam());
      dmsDocument.setPad(l.getPad());
      dmsDocument.setTijd(along(l.getTijd()));
      dmsDocument.setTitel(l.getTitel());
      dmsDocument.setZaakId(l.getZaakId());
      dmsDocument.setDmsNaam(l.getDmsNaam());
      dmsDocument.setVertrouwelijkheid(l.getVertrouwelijkheid());

      dmsResultaat.getDocumenten().add(dmsDocument);
    }

    Collections.sort(dmsResultaat.getDocumenten());

    return dmsResultaat;
  }

  private List<String> getBestandSubMappen(BasePLExt pl) {

    List<String> list = new UniqueList<>();

    if (pl != null) {
      if (pl.getPersoon().getBsn().isNotBlank()) {
        list.add(astr(along(pl.getPersoon().getBsn().getCode())));
        list.add(pl.getPersoon().getBsn().getCode());
      }

      if (pl.getPersoon().getAnr().isNotBlank()) {
        list.add(astr(along(pl.getPersoon().getAnr().getCode())));
        list.add(pl.getPersoon().getAnr().getCode());
      }
    }

    return list;
  }

  /**
   * Geeft alle bestanden terug met de opgegeven submappen
   */
  private List<LokaleDmsDocument> getDocumenten(List<String> subMappen, String zaakId) {

    List<LokaleDmsDocument> files = new ArrayList<>();
    for (File dir : LokaleDmsUtils.getGenormaliseerdeMappen(getDocumentenFolder(), subMappen)) {
      if (fil(zaakId)) {
        LokaleDmsUtils.laadBestandenByZaakId(dir, files, null, zaakId);
      } else {
        LokaleDmsUtils.laadBestandenByFolder(dir, files, null);
      }
    }

    return files;
  }

  /**
   * Geeft aantal bestanden terug met de opgegeven submappen
   */
  private int getAantalDocumenten(List<String> subMappen, String zaakId) {

    int count = 0;
    for (File dir : LokaleDmsUtils.getGenormaliseerdeMappen(getDocumentenFolder(), subMappen)) {
      if (fil(zaakId)) {
        count += LokaleDmsUtils.laadAantalBestandenByZaakId(dir, zaakId);
      } else {
        count += LokaleDmsUtils.laadAantalBestandenByFolder(dir);
      }
    }

    return count;
  }

  private File maakMap(File dir) {

    try {
      if (!dir.exists()) {
        FileUtils.forceMkdir(dir);
        LOGGER.info("Directory: " + dir + " created.");
      }
      return dir;
    } catch (IOException e) {
      throw new ProException(ERROR, "Map kon niet worden aangemaakt");
    }
  }

  private DmsDocument save(File bestand, String subMap, String titel, String gebruiker, String datatype, String zaakId,
      String vertrouwelijkheid) {
    File map = LokaleDmsUtils.getGenormaliseerdeFolder(getDocumentenFolder(), subMap);
    String dmsNaam = "";
    return LokaleDmsUtils.opslaan(bestand, map, titel, gebruiker, datatype, zaakId, dmsNaam, vertrouwelijkheid);
  }
}
