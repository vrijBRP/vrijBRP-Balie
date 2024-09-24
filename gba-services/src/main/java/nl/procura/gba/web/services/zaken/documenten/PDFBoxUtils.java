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

package nl.procura.gba.web.services.zaken.documenten;

import static nl.procura.gba.web.services.zaken.documenten.stempel.PositieType.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;
import nl.procura.commons.core.exceptions.ProException;

import lombok.Data;

public class PDFBoxUtils {

  public static void addText(PDPageContentStream contentStream,
      String text,
      PDType1Font fontFamily,
      float fontSize,
      Coordinates coordinates) throws IOException {

    contentStream.setFont(fontFamily, fontSize);
    contentStream.beginText();
    contentStream.newLineAtOffset(coordinates.getX(), coordinates.getY());
    contentStream.showText(text);
    contentStream.endText();
  }

  public static void addQrCode(PDDocument document,
      PDPageContentStream contentStream,
      String text,
      Coordinates coordinates,
      int width,
      int height) throws IOException, WriterException {

    Map<EncodeHintType, Object> hintMap = new HashMap<>();
    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

    BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);
    MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
    BufferedImage bImage = MatrixToImageWriter.toBufferedImage(matrix, config);
    PDImageXObject image = JPEGFactory.createFromImage(document, bImage);
    contentStream.drawImage(image, coordinates.getX(), coordinates.getY(), width, height);
  }

  public static void addImage(PDDocument document,
      PDPageContentStream contentStream,
      byte[] content,
      Coordinates coordinates,
      int width,
      int height) throws IOException {

    PDImageXObject image = PDImageXObject.createFromByteArray(document, content, "");
    contentStream.drawImage(image, coordinates.getX(), coordinates.getY(), width, height);
  }

  public static byte[] mergePDFs(List<byte[]> contents) {
    if (contents.size() == 1) {
      return contents.get(0);
    }
    try {
      PDFMergerUtility merger = new PDFMergerUtility();
      contents.stream()
          .map(ByteArrayInputStream::new)
          .forEach(merger::addSource);
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        merger.setDestinationStream(bos);
        merger.mergeDocuments(null);
        return bos.toByteArray();
      }
    } catch (Exception e) {
      throw new ProException("Fout bij samenvoegen van PDF bestanden", e);
    }
  }

  public static PDFTextIndexer indexPDFPageText(PDPage page) throws IOException {
    return new PDFTextIndexer(page);
  }

  public static Coordinates getAbsoluteCoordinates(DocumentStempel stempel, PDPage page) {
    Coordinates coordinates = new Coordinates();
    coordinates.setX(stempel.getXcoordinaat());
    coordinates.setY(stempel.getYcoordinaat());

    if (stempel.getPositie().is(RECHTSBOVEN, RECHTSONDER)) {
      coordinates.setX(page.getCropBox().getUpperRightX()
          - stempel.getBreedte()
          + stempel.getXcoordinaat());

    }

    if (stempel.getPositie().is(LINKSBOVEN, RECHTSBOVEN)) {
      coordinates.setY(page.getCropBox().getUpperRightY()
          - stempel.getHoogte()
          - stempel.getYcoordinaat());
    }

    return coordinates;
  }

  public static Coordinates getRelativeCoordinates(DocumentStempel stempel,
      PDPage page,
      float x,
      float y) {
    Coordinates coordinates = new Coordinates();
    coordinates.setX(x + stempel.getXcoordinaat());
    coordinates.setY(page.getCropBox().getUpperRightY()
        - stempel.getHoogte()
        - stempel.getYcoordinaat()
        - y);
    return coordinates;
  }

  public static float getTextLength(String tekst, PDType1Font fontFamily, float fontSize) throws IOException {
    return (fontFamily.getStringWidth(tekst) / 1000.0f) * fontSize;
  }

  public static class PDFTextIndexer {

    private final Indexer indexer;

    private PDFTextIndexer(PDPage page) throws IOException {
      this.indexer = new Indexer(page);
    }

    public Optional<TextPosition> getText(String text) {
      return Optional.ofNullable(indexer.findText(text));
    }

    private static class Indexer extends PDFTextStripper {

      private final List<LineEntry> entries = new ArrayList<>();

      public Indexer(PDPage page) throws IOException {
        this.setStartPage(0);
        this.setEndPage(0);
        output = new OutputStreamWriter(new ByteArrayOutputStream());
        processPage(page);
      }

      public TextPosition findText(String text) {
        for (LineEntry entry : entries) {
          int index = entry.getLine().indexOf(text);
          if (index > -1) {
            return entry.positions.get(index);
          }
        }
        return null;
      }

      @Override
      protected void writeString(String line, List<TextPosition> textPositions) {
        entries.add(new LineEntry(line, textPositions));
      }
    }

    @Data
    private static class LineEntry {

      private String             line;
      private List<TextPosition> positions = new ArrayList<>();

      public LineEntry(String line, List<TextPosition> positions) {
        this.line = line;
        this.positions = positions;
      }
    }
  }

  @Data
  public static class Coordinates {

    private float x = 0;
    private float y = 0;
  }
}
