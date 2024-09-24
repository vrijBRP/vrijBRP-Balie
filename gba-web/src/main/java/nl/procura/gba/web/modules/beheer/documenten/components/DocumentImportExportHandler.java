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

package nl.procura.gba.web.modules.beheer.documenten.components;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.*;
import static nl.procura.commons.core.exceptions.ProExceptionType.DOCUMENTS;
import static nl.procura.commons.core.exceptions.ProExceptionType.PROGRAMMING;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.common.Serializer;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.Koppelenum;
import nl.procura.gba.jpa.personen.db.KoppelenumDocument;
import nl.procura.gba.jpa.personen.db.KoppelenumDocumentPK;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.misc.ImportExportHandler;
import nl.procura.gba.web.modules.beheer.documenten.components.DocumentExport.DocumentExportEntry;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing.DocumentImportOptieType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

import lombok.Setter;

public class DocumentImportExportHandler extends ImportExportHandler {

  private final List<String> docReports;
  private final List<String> templReports;

  @Setter
  private boolean                 overwriteDocs;
  @Setter
  private DocumentImportOptieType kopieOpslaan          = null;
  @Setter
  private DocumentImportOptieType protocollering        = null;
  @Setter
  private DocumentImportOptieType iedereenToegang       = null;
  @Setter
  private DocumentImportOptieType standaardGeselecteerd = null;
  @Setter
  private DocumentImportOptieType stillbornAllowed      = null;
  @Setter
  private DocumentImportOptieType alias                 = null;
  @Setter
  private DocumentImportOptieType omschrijving          = null;
  @Setter
  private DocumentImportOptieType documentTypeOms       = null;
  @Setter
  private DocumentImportOptieType vertrouwelijkheid     = null;
  @Setter
  private DocumentImportOptieType formats               = null;

  public DocumentImportExportHandler() {
    overwriteDocs = false;
    docReports = new ArrayList<>();
    templReports = new ArrayList<>();
  }

  /**
   * Exporteert de documenten naar een zip-bestand.
   */
  public void exportDocumenten(GbaWindow window, List<DocumentRecord> docList) {
    Map<String, byte[]> map = new HashMap<>();
    serializeDocs(docList, map);
    serializeTemplates(window, docList, map);
    exportObject(map, "documenten.zip", new DownloadHandlerImpl(window));
  }

  /**
   * Exporteert sjablonen als byte-array naar een zipfile genaamd 'sjablonen.zip'.
   */
  public void exportTemplates(File templDir, List<File> templList, GbaWindow window) {
    Map<String, byte[]> map = new HashMap<>();
    try {
      for (File file : templList) {
        String relPath = MiscUtils.getRelatiefPad(templDir, file);
        map.put(relPath, IOUtils.toByteArray(new FileInputStream(file)));
      }
    } catch (IOException e) {
      throw new ProException(PROGRAMMING, ERROR, "Fout bij het exporteren van sjabloon.");
    }

    ImportExportHandler.exportObject(map, "sjablonen.zip", new DownloadHandlerImpl(window));
  }

  /**
   * Importeert documenten en sjablonen vanuit een zip-bestand
   *
   * @return lijst van meldingen
   */

  public List<List<String>> importDocsAndTemplates(final ImportDocumentArguments args) {
    final List<List<String>> reportsList = new ArrayList<>();
    reportsList.add(docReports);
    reportsList.add(templReports);

    if (args.getImportFileName().endsWith("odt")) {
      importTemplate(args);

    } else if (args.getImportFileName().endsWith("zip")) {
      readZip(args.getImportFile(), (zis, entry, dir, name) -> {

        if (name.endsWith("ser")) {
          importDocument(args, zis);

        } else if (name.endsWith("odt")) {
          importTemplate(args, zis, dir, name);
        }
      });
    } else {
      throw new ProException(INFO, "Alleen <b>zip</b> of <b>odt</b> bestanden kunnen geüpload worden.");
    }

    return reportsList;
  }

  private void addDocReports(DocumentRecord document, boolean docNameExists) {

    if (docNameExists) {
      docReports.add(
          "Updaten document: " + document.getDocument() + " met sjabloon: " + document.getBestand() + " en van type: "
              + document.getType() + ".");
    } else {
      docReports.add(
          "Importeren document: " + document.getDocument() + " met sjabloon: " + document.getBestand()
              + " en van type: " + document.getType() + ".");
    }
  }

  private File findFile(File templateDir, File file) {
    for (File existingFile : FileUtils.listFiles(templateDir, new NameFileFilter(file.getName()),
        DirectoryFileFilter.DIRECTORY)) {
      return existingFile;
    }

    return file;
  }

  private <T> T get(DocumentImportOptieType type, boolean stored, T existingValue, T newValue) {

    switch (type) {
      case AAN:
        return (T) Boolean.TRUE;

      case UIT:
        return (T) Boolean.FALSE;

      case OVERSCHRIJVEN:
        return newValue;

      case INITIEEL_OVERNEMEN:
        return stored ? existingValue : newValue;

      case NIET_WIJZIGEN:
      default:
        return existingValue;
    }
  }

  /**
   * Haalt het document uit de database met de opgegeven naam.
   */

  private DocumentRecord getDocument(String docName, GbaApplication gbaAppl) {
    DocumentRecord document = null;

    List<DocumentRecord> doclList = gbaAppl.getServices().getDocumentService().getDocumenten(false);
    for (DocumentRecord doc : doclList) {
      if (doc.getDocument().equals(docName)) {
        document = doc;
      }
    }
    return document;
  }

  private Koppelenum getKEnum(Long e) {
    Koppelenum args = new Koppelenum();
    args.setKoppelenum(e);
    for (Koppelenum koppelEnum : GenericDao.findByExample(args)) {
      return koppelEnum;
    }

    return GenericDao.saveEntity(args);
  }

  /**
   * Slaat de documenten en sjablonen in de Zip file op.
   */
  private void importDocument(ImportDocumentArguments args, InputStream zis) {

    DocumentExport im = Serializer.deSerialize(zis, DocumentExport.class);

    GbaApplication gbaAppl = args.getGbaWindow().getGbaApplication();

    EntityManager em = GbaJpa.getManager();

    em.getTransaction().begin();

    for (DocumentExportEntry entry : im.getdList()) {

      DocumentRecord document = new DocumentRecord();
      String docName = entry.getNaam();
      boolean docNameExists = isDocumentExists(docName, gbaAppl);

      if (docNameExists) {
        document = getDocument(docName, gbaAppl);

        if (!overwriteDocs) {
          docReports.add("Overgeslagen document: "
              + document.getDocument() + " met sjabloon: "
              + document.getBestand()
              + " en van type: " + document.getType() + ".");
          continue;
        }
      }

      setDocument(document, entry);

      addDocReports(document,
          docNameExists); // afhankelijk van mappenstructuur wordt `bestand' van document gewijzigd.

      GenericDao.saveEntity(document);

      for (Long e : entry.getKoppelEnums()) {

        KoppelenumDocument kd = new KoppelenumDocument();
        KoppelenumDocumentPK id = new KoppelenumDocumentPK();
        id.setCDocument(document.getCDocument());
        id.setCKoppelenum(getKEnum(e).getCKoppelenum());
        kd.setId(id);

        GenericDao.saveEntity(kd);
      }
    }

    em.getTransaction().commit();
  }

  private void importTemplate(ImportDocumentArguments args) {

    try {
      importTemplate(args, new FileInputStream(args.getImportFile()), "", args.getImportFileName());
    } catch (FileNotFoundException e) {
      throw new ProException(DOCUMENTS, WARNING, "Fout bij opslaan bestand.", e);
    }
  }

  /**
   * Zorgt voor het opslaan van de sjablonen op de harde schijf. Wanneer een sjabloon
   * al opgeslagen is, wordt het overschreven, anders wordt het in de homedir voor sjablonen
   * opgeslagen.
   */

  private void importTemplate(ImportDocumentArguments args, InputStream is, String dir, String name) {

    try {
      GbaApplication appl = args.getGbaWindow().getGbaApplication();
      File templateDir = appl.getServices().getDocumentService().getSjablonenMap(); // homedirectory sjablonen
      File targetMap = new File(templateDir, dir);
      File targetFile = findFile(templateDir, new File(targetMap, name));
      boolean fileExists = targetFile.exists();

      FileUtils.forceMkdir(targetMap);

      String relpath = "." + MiscUtils.getRelatiefPad(templateDir, targetFile);

      if (fileExists && !overwriteDocs) {
        templReports.add("Overgeslagen sjabloon " + relpath);
      } else {
        storeTemplate(targetFile.getAbsolutePath(), is);
        templReports.add(
            fileExists ? "Updaten bestaand sjabloon " + relpath : "Importeren nieuw sjabloon " + relpath);
      }
    } catch (IOException e) {
      throw new ProException(DOCUMENTS, WARNING, "Fout bij opslaan bestand.", e);
    }
  }

  /**
   * Checkt of de opgegeven documentnaam al voorkomt.
   *
   * @return true als document al opgeslagen is
   */

  private boolean isDocumentExists(String docName, GbaApplication gbaAppl) {
    return gbaAppl.getServices().getDocumentService().getDocumentZonderAttributen(docName) != null;
  }

  private void serializeDocs(List<DocumentRecord> docList, Map<String, byte[]> map) {

    DocumentExport export = new DocumentExport();

    for (DocumentRecord doc : docList) {

      DocumentExportEntry d = export.addExportEntry();

      d.setVolgNr(along(doc.getVDocument()));
      d.setNaam(doc.getDocument());
      d.setSjabloon(doc.getBestand());
      d.setType(doc.getType());
      d.setMap(doc.getPad());
      d.setVervalDatum(toBigDecimal(doc.getDatumEinde().getLongDate()));
      d.setAlias(doc.getAlias());
      d.setOmschrijving(doc.getOmschrijving());
      d.setVertrouwelijkheid(doc.getVertrouwelijkheid().getNaam());
      d.setDocumentDmsType(doc.getDocumentDmsType());
      d.setFormats(doc.getFormats());
      d.setKopieOpslaan(doc.isKopieOpslaan());
      d.setStandaardGeselecteerd(doc.isStandaardDocument());
      d.setProtocollering(doc.isProtocolleren());
      d.setIedereenToegang(doc.isIedereenToegang());
      d.setStillbornAllowed(doc.isStillbornAllowed());

      for (Koppelenum e : doc.getKoppelenumen()) {
        d.getKoppelEnums().add(e.getKoppelenum());
      }
    }

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Serializer.serialize(bos, export);
    map.put("documenten.ser", bos.toByteArray());
  }

  private void serializeTemplates(GbaWindow window, List<DocumentRecord> docList, Map<String, byte[]> map) {

    DocumentService documenten = window.getGbaApplication().getServices().getDocumentService();

    File templateDir = documenten.getSjablonenMap();

    for (DocumentRecord doc : docList) {

      File template = new File(templateDir, doc.getBestand());

      if (template.exists()) {

        try {
          map.put(doc.getBestand(), IOUtils.toByteArray(new FileInputStream(template)));
        } catch (IOException e) {
          throw new ProException(PROGRAMMING, ERROR, "Fout bij het exporteren van sjabloon.");
        }
      }
    }
  }

  /**
   * Zet de waarden van het doc zoals die in d opgeslagen zijn.
   */
  private void setDocument(DocumentRecord doc, DocumentExportEntry d) {

    doc.setVDocument(BigDecimal.valueOf(d.getVolgNr()));
    doc.setDocument(d.getNaam());
    doc.setType(d.getType());
    doc.setPad(d.getMap());
    doc.setBestand(d.getSjabloon());

    doc.setAlias(get(alias, doc.isStored(), doc.getAlias(), d.getAlias()));
    doc.setOmschrijving(get(omschrijving, doc.isStored(), doc.getOmschrijving(), d.getOmschrijving()));
    doc.setDocumentDmsType(get(documentTypeOms, doc.isStored(), doc.getDocumentDmsType(), d.getDocumentDmsType()));
    doc.setVertrouwelijkheid(get(vertrouwelijkheid, doc.isStored(), doc.getVertrouwelijkheid(),
        DocumentVertrouwelijkheid.get(d.getVertrouwelijkheid())));
    doc.setFormats(get(formats, doc.isStored(), doc.getFormats(), d.getFormats()));

    doc.setKopieOpslaan(get(kopieOpslaan, doc.isStored(), doc.isKopieOpslaan(), d.isKopieOpslaan()));
    doc.setProtocolleren(get(protocollering, doc.isStored(), doc.isProtocolleren(), d.isProtocollering()));
    doc.setStandaardDocument(get(standaardGeselecteerd, doc.isStored(),
        doc.isStandaardDocument(), d.isStandaardGeselecteerd()));
    doc.setIedereenToegang(get(iedereenToegang, doc.isStored(), doc.isIedereenToegang(), d.isIedereenToegang()));
    doc.setStillbornAllowed(get(stillbornAllowed, doc.isStored(), doc.isStillbornAllowed(), d.isStillbornAllowed()));
  }

  /**
   * Slaat een sjabloon op,op de locatie filePath.
   */
  private void storeTemplate(String filePath, InputStream is) throws IOException {
    FileUtils.writeByteArrayToFile(new File(filePath), IOUtils.toByteArray(is));
  }
}
