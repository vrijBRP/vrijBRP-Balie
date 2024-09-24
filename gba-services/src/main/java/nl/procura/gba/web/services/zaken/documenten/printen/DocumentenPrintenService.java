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

package nl.procura.gba.web.services.zaken.documenten.printen;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.DOCUMENTS;
import static nl.procura.commons.core.exceptions.ProExceptionType.PRINT;
import static nl.procura.commons.core.exceptions.ProExceptionType.WEBSERVICE;
import static nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType.ondertekening_naam;
import static nl.procura.gba.web.services.zaken.algemeen.ZaakUtils.getRelevantZaakId;
import static nl.procura.gba.web.services.zaken.documenten.PDFBoxUtils.getRelativeCoordinates;
import static nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType.PDF;
import static nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType.PDF_A1;
import static nl.procura.gba.web.services.zaken.documenten.stempel.PositieType.RECHTSBOVEN;
import static nl.procura.gba.web.services.zaken.documenten.stempel.PositieType.RECHTSONDER;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;
import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jodreports.templates.DocumentTemplate;
import org.jodreports.templates.DocumentTemplateFactory;
import org.jodreports.templates.xmlfilters.XmlEntryFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.WriterException;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.extensions.WKResultWrapper;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gba.jpa.personen.db.Translation;
import nl.procura.gba.jpa.personen.db.TranslationRec;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.bsm.BsmMijnOverheidBestand;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfo;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentTranslation;
import nl.procura.gba.web.services.zaken.documenten.PDFBoxUtils;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.gba.web.services.zaken.documenten.printen.printers.CommandPrinter;
import nl.procura.gba.web.services.zaken.documenten.printen.printers.ConnectPrinter;
import nl.procura.gba.web.services.zaken.documenten.printen.printers.LocalPrinter;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;
import nl.procura.gba.web.services.zaken.documenten.stempel.stempels.OsaraQrZaakStempel;
import nl.procura.gba.web.services.zaken.documenten.stempel.stempels.ProcuraQrZaakStempel;
import nl.procura.openoffice.OODocumentFormats;
import nl.procura.openoffice.OOTools;
import nl.procura.openoffice.jodconverter.DocumentFormat;
import nl.procura.openoffice.jodconverter.TranslationFilter;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.functies.downloading.DownloadHandler;

import au.com.bytecode.opencsv.CSVWriter;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DocumentenPrintenService extends AbstractService {

  private final static Logger LOGGER = LoggerFactory.getLogger(DocumentenPrintenService.class.getName());

  public DocumentenPrintenService() {
    super("OpenOffice");
  }

  public static File newTijdelijkBestand(String fileName) {
    try {
      String e = FilenameUtils.getExtension(fileName);
      String n = "upload-" + FilenameUtils.getBaseName(fileName) + "-";
      n = n.replaceAll("\\W+", "-"); // Niet tekst-karakters vervangen met underscore
      return File.createTempFile(n, ("." + e));
    } catch (IOException exception) {
      throw new ProException(ERROR, "Fout bij aanmaken tijdelijk bestand", exception);
    }
  }

  @ThrowException(type = DOCUMENTS,
      value = "Fout bij het converteren van een Excel bestand")
  public byte[] convertSpreadsheet(List<String[]> lines, UitvoerformaatType format) {
    ByteArrayOutputStream csvOs = new ByteArrayOutputStream();
    CSVWriter csv;

    try {
      // Specify the BOM (Byte order Mask) for UTF-8 so that Excel can identify the encoding
      // and open it in the correct format
      csvOs.write(new byte[]{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
      OutputStreamWriter writer = new OutputStreamWriter(csvOs, StandardCharsets.UTF_8);
      csv = new CSVWriter(writer, format.getSeparator(), '"');
      for (String[] line : lines) {
        csv.writeNext(line);
      }

      try {
        csv.flush();
      } finally {
        IOUtils.closeQuietly(csv);
      }

      return csvOs.toByteArray();
    } catch (IOException e) {
      throw new ProException("Fout bij maken spreadsheet", e);
    } finally {
      IOUtils.closeQuietly(csvOs);
    }
  }

  public String getRestApi() {
    return astr(getServices().getParameterService().getParm(ParameterConstant.OPENOFFICE_REST_API));
  }

  public String getConnectionHost() {
    return astr(getServices().getParameterService().getParm(ParameterConstant.OPENOFFICE_HOSTNAME));
  }

  public int getConnectionPort() {
    return aval(getServices().getParameterService().getParm(ParameterConstant.OPENOFFICE_PORT));
  }

  public File getSjablonenMap() {
    String standaardPad = GbaConfig.getPath().getApplicationDir() + "/documenten/odt_templates/";
    String customPad = getServices().getParameterService().getParm(ParameterConstant.DOC_TEMPLATE_PAD);
    return maakMap(new File(fil(customPad) ? customPad : standaardPad));
  }

  public List<File> getSjabloonBestanden() {
    return getSjabloonBestanden(getServices().getDocumentService().getSjablonenMap());
  }

  @ThrowException(type = PRINT,
      value = "Fout bij het tonen van het document")
  public byte[] preview(PrintActie printActie) {
    return samenvoegenEnConverteren(printActie);
  }

  @ThrowException(type = PRINT,
      value = "Fout bij het afdrukken van het document")
  public void print(PrintActie printActie, boolean asDownload, DownloadHandler downloadHandler) {
    byte[] documentBytes = samenvoegenEnConverteren(printActie);
    getServices().getDmsService().save(printActie, documentBytes);

    switch (printActie.getPrintOptie().getPrintType()) {
      case COMMAND:
        new CommandPrinter(printActie.getPrintOptie(), getServices().getGebruiker())
            .print(documentBytes);
        break;

      case LOCAL_PRINTER:
        new LocalPrinter(printActie.getPrintOptie(), getServices().getGebruiker())
            .print(documentBytes);
        break;

      case NETWORK_PRINTER:
        new ConnectPrinter(printActie.getPrintOptie(), getServices())
            .print(documentBytes);
        break;

      case MIJN_OVERHEID: // Stuur naar de BSM toe
        String ext = printActie.getPrintOptie().getUitvoerformaatType().getExt();
        String bestandsnaam = genereerBestandsnaam(printActie);
        String bsmId = printActie.getPrintOptie().getBsmId();
        String berichttype = printActie.getPrintOptie().getMoBerichttype();

        BsmMijnOverheidBestand bestand = new BsmMijnOverheidBestand(bsmId, berichttype, printActie.getZaak(),
            bestandsnaam, ext, documentBytes);
        getServices().getBsmService().addMijnOverheidBestand(bestand);
        break;

      default:
        // Toon gewoon aan gebruiker
        toonGebruiker(printActie, documentBytes, asDownload, downloadHandler);
    }
  }

  public byte[] samenvoegenEnConverteren(PrintActie printActie) {
    return converteerPrintActie(printActie, samenvoegen(printActie));
  }

  /**
   * Voeg één of meerdere stempels toe
   */
  private byte[] bestempel(byte[] inBytes, PrintActie printActie) {
    PrintOptie po = printActie.getPrintOptie();
    List<DocumentStempel> stempels = printActie.getDocument().getDocumentStempels();

    // Geen PDF dan niet bestempelen
    if (!asList(PDF, PDF_A1).contains(po.getUitvoerformaatType()) || stempels.isEmpty()) {
      return inBytes;
    }

    try {
      PDDocument document = PDDocument.load(inBytes);
      ByteArrayOutputStream stamperOutputStream = new ByteArrayOutputStream();

      try {
        for (DocumentStempel stempel : stempels) {
          List<Integer> pages = stempel.getPaginaNummers();
          int width = aval(stempel.getBreedte());
          int height = aval(stempel.getHoogte());
          byte[] content = stempel.getData();
          float fontSize = stempel.getFontSize();
          PDType1Font fontFamily = PDType1Font.HELVETICA;

          for (PDPage page : getPages(pages, document)) {
            PDFBoxUtils.Coordinates coordinates = PDFBoxUtils.getAbsoluteCoordinates(stempel, page);
            PDFBoxUtils.PDFTextIndexer textIndexer = PDFBoxUtils.indexPDFPageText(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, APPEND, true, true)) {
              switch (stempel.getStempelType()) {
                case OSARA_QR_ZAAK_ID:
                  String tekst = OsaraQrZaakStempel.getTekst(printActie.getZaak());
                  PDFBoxUtils.addQrCode(document, contentStream, tekst, coordinates, width, height);
                  break;

                case PROCURA_QR_ZAAK_ID:
                  tekst = ProcuraQrZaakStempel.getTekst(printActie.getZaak());
                  PDFBoxUtils.addQrCode(document, contentStream, tekst, coordinates, width, height);
                  break;

                case PROCURA_TEKST_ZAAK_ID:
                  tekst = getRelevantZaakId(printActie.getZaak());
                  switch (stempel.getFontType()) {
                    case COURIER:
                      fontFamily = PDType1Font.COURIER;
                      break;
                    case TIMES_ROMAN:
                      fontFamily = PDType1Font.TIMES_ROMAN;
                      break;
                  }

                  if (stempel.getPositie().is(RECHTSBOVEN, RECHTSONDER)) {
                    // Subtract width of text from x-coordinate if alignment is to the right side
                    coordinates.setX(coordinates.getX() - PDFBoxUtils.getTextLength(tekst, fontFamily, fontSize));
                  }
                  PDFBoxUtils.addText(contentStream, tekst, fontFamily, fontSize, coordinates);
                  break;

                case AFBEELDING:
                  String woord = stempel.getWoord();
                  if (StringUtils.isNotBlank(woord)) {
                    // Find the keyword and it's coordinates
                    coordinates = textIndexer.getText(woord)
                        .map(position -> getRelativeCoordinates(stempel, page, position.getX(), position.getY()))
                        .orElseThrow(() -> new ProException(ERROR,
                            "Het woord ''{0}'' is niet gevonden in het sjabloon. " +
                                "De PDF kan niet voorzien worden van stempel ''{1}''",
                            woord, stempel.getStempel()));

                  }

                  PDFBoxUtils.addImage(document, contentStream, content, coordinates, width, height);
                  break;
                default:
                case ONBEKEND:
                  break;
              }
            }
          }
        }

        document.save(stamperOutputStream);
        document.close();
        return stamperOutputStream.toByteArray();
      } finally {
        IOUtils.closeQuietly(stamperOutputStream);
      }
    } catch (IOException | WriterException e) {
      throw new ProException("Fout bij toevoegen stempels", e);
    }
  }

  private static List<PDPage> getPages(List<Integer> pageNrs, PDDocument document) {
    return IntStream.range(0, document.getNumberOfPages())
        .filter(index -> pageNrs.isEmpty() || pageNrs.contains(index + 1))
        .mapToObj(document::getPage)
        .collect(Collectors.toList());
  }

  private synchronized byte[] converteer(UitvoerformaatType invoerFormaat, UitvoerformaatType uitvoerFormaat,
      byte[] inBytes) {

    if (StringUtils.isNotBlank(getRestApi())) {
      return convertWithRestAPI(invoerFormaat, uitvoerFormaat, inBytes);

    } else {
      return convertWithRemoteClient(invoerFormaat, uitvoerFormaat, inBytes);
    }
  }

  private synchronized byte[] convertWithRestAPI(UitvoerformaatType invoerFormaat,
      UitvoerformaatType uitvoerFormaat,
      byte[] inBytes) {

    OkHttpClient client = new OkHttpClient();
    String url = getRestApi() + "/lool/convert-to/" + uitvoerFormaat.getExt();

    // Add extra parameter for PDF/A1
    if (uitvoerFormaat == PDF_A1) {
      url += "?lfdSelectPdfVersion=1";
    }

    LOGGER.info(String.format("Sending request to %s for conversion file to %s ...", url, uitvoerFormaat.getExt()));
    RequestBody fileBody = RequestBody.create(inBytes);
    String tempFileName = UUID.randomUUID() + "." + invoerFormaat.getExt();
    MultipartBody multipartBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("data", tempFileName, fileBody) // file param
        .build();

    Request request = new Request.Builder()
        .url(url)
        .post(multipartBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().bytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] convertWithRemoteClient(UitvoerformaatType invoerFormaat,
      UitvoerformaatType uitvoerFormaat,
      byte[] inBytes) {
    ByteArrayOutputStream os = null;
    ByteArrayInputStream is = null;
    byte[] outBytes;

    try {
      if (invoerFormaat.getId().equals(uitvoerFormaat.getId())) {
        outBytes = inBytes;
      } else {
        if (!OOTools.canConnect(getConnectionHost(), getConnectionPort())) {
          throw new ProException(WEBSERVICE, ERROR,
              "Er kan geen verbinding worden gemaakt met de OpenOffice listener op " + getConnectionHost() + ":"
                  + getConnectionPort()
                  + ".<br>Probeer het nogmaals.<br>Neem anders contact op met applicatiebeheer");
        }

        LOGGER.debug("Document wordt geconverteerd naar " + uitvoerFormaat);

        boolean isLocal = getParm(ParameterConstant.OPENOFFICE_CONVERT).equalsIgnoreCase("local");

        DocumentFormat inFormat = OODocumentFormats.getFormat(invoerFormaat.getId());
        DocumentFormat outFormat = OODocumentFormats.getFormat(uitvoerFormaat.getId());

        is = new ByteArrayInputStream(inBytes);
        os = new ByteArrayOutputStream();

        new OOTools().convert(is, inFormat, os, outFormat, getConnectionHost(), getConnectionPort(), isLocal);
        outBytes = os.toByteArray();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(os);
      IOUtils.closeQuietly(is);
    }

    return outBytes;
  }

  private byte[] converteerPrintActie(PrintActie printActie, byte[] inBytes) {

    PrintOptie po = printActie.getPrintOptie();
    List<byte[]> bytes = new ArrayList<>();

    UitvoerformaatType uitvoerformaatType = po.getUitvoerformaatType();
    if (asList(PDF, PDF_A1).contains(po.getUitvoerformaatType())) {
      for (PrintListener pa : printActie.getListeners()) {
        PrintActie plPa = pa.getPrintActie();
        if (plPa != null) {
          bytes.add(samenvoegenEnConverteren(plPa));
        }
      }

      // Switch to PDF/A if that is a configured option
      if (printActie.getDocument()
          .getPrintOpties()
          .stream()
          .anyMatch(optie -> PDF_A1 == optie.getUitvoerformaatType())) {
        uitvoerformaatType = PDF_A1;
      }
    }

    bytes.add(bestempel(converteer(UitvoerformaatType.ODT, uitvoerformaatType, inBytes), printActie));

    return PDFBoxUtils.mergePDFs(bytes);
  }

  private String genereerBestandsnaam(PrintActie printActie) {
    return "document-" + new Date().getTime() + "." + printActie.getPrintOptie().getUitvoerformaatType().getExt();
  }

  private List<File> getSjabloonBestanden(File map) {
    List<File> l = new ArrayList<>(FileUtils.listFiles(map, new String[]{ "odt" }, true));
    l.sort(Comparator.comparing(this::getSorteerPad));
    return l;
  }

  private String getSorteerPad(File sjabloon) {

    String path = null;

    if (isBestand(sjabloon)) {
      path = MiscUtils.getRelatiefPad(getSjablonenMap(), sjabloon);
    }

    return path;
  }

  private boolean isBestand(File file) {
    return file != null && file.isFile();
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

  private byte[] samenvoegen(PrintActie printActie) {

    DocumentRecord document = printActie.getDocument();

    try {
      ConditionalMap model = printActie.getModel();

      GebruikerInfo on = getServices().getGebruiker().getInformatie().getInfo(ondertekening_naam);
      String fn = getServices().getGebruiker().getNaam();
      String naam = ((on != null) && fil(on.getWaarde())) ? on.getWaarde() : fn;

      model.put("datum", new Date());
      model.put("tijd", new ProcuraDate().getFormatTime());
      model.put("code_gebruiker", getServices().getGebruiker().getCUsr());
      model.put("gebruiker", naam);
      model.put("gemeente", GbaConfig.get(GbaConfigProperty.GEMEENTE));
      model.put("code_gemeente", format("%04d", aval(getServices().getGebruiker().getGemeenteCode())));
      model.put("naam_gemeente", getServices().getGebruiker().getGemeente());

      ContactgegevensService contactService = getServices().getContactgegevensService();
      model.put("contactgegevens", contactService.getGevuldeContactgegevens(printActie.getZaak()));
      model.put("contact", contactService.getContactgegevens2(printActie.getZaak()));
      model.put("identificatie", getServices().getIdentificatieService().getIdentificatie(printActie.getZaak()));
      model.put("vervolgblad", printActie.getVervolgblad());
      model.put("zaakid", printActie.getZaak() != null ? getRelevantZaakId(printActie.getZaak()) : "");

      for (GebruikerInfo gi : getServices().getGebruiker().getInformatie().getAlles()) {
        model.put("gebruiker_" + gi.getInfo(), gi.getWaarde());
      }

      if (document.getDocumentKenmerken().is(DocumentKenmerkType.OPHALEN_BEWONERS)) {
        setNumberOfResidents(model);
      }

      LOGGER.debug("Samenvoegen: " + new File(getSjablonenMap(), document.getBestand()).getAbsolutePath());

      DocumentTemplateFactory factory = new DocumentTemplateFactory();
      ByteArrayOutputStream mergeOut = new ByteArrayOutputStream();
      try {
        // Merge
        DocumentTemplate mergeTemplate = factory.getTemplate(new File(getSjablonenMap(), document.getBestand()));
        mergeTemplate.createDocument(model, mergeOut);

        // Translate
        if (document.hasTranslations()) {
          ByteArrayOutputStream transOut = new ByteArrayOutputStream();
          ByteArrayInputStream bis = new ByteArrayInputStream(mergeOut.toByteArray());
          try {
            Translation translation = document.getTranslation();
            DocumentTemplate translateTemplate = factory.getTemplate(bis);
            Translation tr = getServices().getDocumentService().getTranslationById(translation.getCTranslation());
            TranslationFilter filter = getTranslationsFilter(new DocumentTranslation(tr));
            translateTemplate.setXmlEntryFilters(new XmlEntryFilter[]{ filter });
            translateTemplate.createDocument(model, transOut);
            return transOut.toByteArray();
          } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(transOut);
          }
        } else {
          return mergeOut.toByteArray();
        }
      } finally {
        IOUtils.closeQuietly(mergeOut);
      }

    } catch (Exception e) {
      throw new ProException(PRINT, ERROR,
          "Fout bij samenvoegen document: " + document.getDocument() + ", bestand: " + document.getBestand(),
          e);
    }
  }

  private TranslationFilter getTranslationsFilter(DocumentTranslation documentTranslation) {
    Map<String, String> translations = new LinkedHashMap<>();
    for (TranslationRec record : documentTranslation.getSortedRecords()) {
      translations.put(record.getNl(), record.getFl());
    }
    return new TranslationFilter(translations, true);
  }

  private void setNumberOfResidents(ConditionalMap model) {
    Object persoonslijsten = model.get("persoonslijsten");
    if (!(persoonslijsten instanceof List)) {
      throw new ProException("Geen persoonslijsten gevonden in model");
    }
    List<?> people = (List<?>) persoonslijsten;
    for (Object person : people) {
      if (person instanceof DocumentPL) {
        setNumberOfResidents((DocumentPL) person);
      }
    }
  }

  private void setNumberOfResidents(DocumentPL person) {
    List<DocumentPL.OOVerblijfplaats> verblijfplaatsen = person.getVerblijfplaatsen();
    if (verblijfplaatsen.isEmpty()) {
      throw new ProException(format("Persoon %s heeft geen verblijfplaats", person.getPersoon().getAnummer()));
    }
    Adres adres = person.getBasisPl().getVerblijfplaats().getAdres();
    WKResultWrapper result = getServices().getPersonenWsService().getAdres(ZoekArgumenten.of(adres), true);
    List<BaseWKExt> wrappers = result.getBasisWkWrappers();
    if (wrappers.size() != 1) {
      throw new ProException(format("Adres %s %d keer gevonden, 1 keer verwacht", adres.getAdres(), wrappers.size()));
    }
    verblijfplaatsen.get(0).setAantalBewoners(wrappers.get(0).getCurrentResidentsCount());
  }

  private void toonGebruiker(PrintActie printActie, byte[] docBytes, boolean asDownload,
      DownloadHandler downloadHandler) {

    ByteArrayInputStream stream = new ByteArrayInputStream(docBytes);
    String bestandsnaam = genereerBestandsnaam(printActie);
    downloadHandler.download(stream, bestandsnaam, asDownload);
  }
}
