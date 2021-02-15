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

package nl.procura.gba.common;

import static nl.procura.standard.Globalfunctions.fil;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

public class StreamUtils {

  private static String ENCODING = "UTF-8";

  // Returns the contents of the file in a byte array.

  public static String getRealPath(String path) {

    if (path.matches("^(\\\\|/)(.*)")) {
      return path;
    }

    String root = System.getProperty("root");

    return (fil(root) ? (root + "/") : "") + path;
  }

  public static byte[] fromStream(InputStream is) {

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
    byte[] byteData;

    try {
      byte[] bytes = new byte[512];

      int readBytes;
      while ((readBytes = is.read(bytes)) > 0) {
        outputStream.write(bytes, 0, readBytes);
      }

      byteData = outputStream.toByteArray();

    } catch (Exception e) {
      throw new RuntimeException("Fout bij inlezen XML stream", e);
    } finally {
      IOUtils.closeQuietly(is);
      IOUtils.closeQuietly(outputStream);
    }

    return byteData;
  }

  /**
   * Convert inputstream naar object
   */
  @SuppressWarnings("unchecked")
  public static <T> T fromStream(Class<T> clazz, InputStream is) {

    try {
      JAXBContext jc = JAXBContext.newInstance(clazz);

      Unmarshaller u = jc.createUnmarshaller();

      return (T) u.unmarshal(is);
    } catch (Exception e) {
      throw new RuntimeException("Fout bij inlezen XML stream", e);
    } finally {
      IOUtils.closeQuietly(is);
    }
  }

  /**
   * Converteert object naar stream
   */
  @SuppressWarnings("unchecked")
  public static void toStream(Object o, OutputStream os) {

    try {
      JAXBContext jc = JAXBContext.newInstance(o.getClass());

      Marshaller marshaller = jc.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      QName qname = new QName("", o.getClass().getAnnotation(XmlType.class).name());

      JAXBElement element = new JAXBElement(qname, o.getClass(), null, o);

      marshaller.marshal(element, os);
    } catch (Exception e) {
      throw new RuntimeException("Fout bij omzetten object naar XML", e);
    } finally {
      IOUtils.closeQuietly(os);
    }
  }

  /**
   * Convert object to String
   */
  public static String toString(Object o) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    try {
      toStream(o, bos);
      return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Fout bij inlezen", e);
    }
  }

  public static String toPrettyString(String xml, boolean xslt) {

    try {
      xml = xml.replaceAll("\r|\n", ""); // Opschonen
      xml = xml.replaceAll(">\\s+<", "><"); // Spaties weghalen

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = parser.parse(IOUtils.toInputStream(xml, ENCODING));

      TransformerFactory tfactory = TransformerFactory.newInstance();
      Transformer serializer;

      if (xslt) {
        InputStream xsltStream = StreamUtils.class.getClassLoader().getResourceAsStream("xmlverbatim.xsl");
        serializer = tfactory.newTransformer(new StreamSource(xsltStream));
      } else {
        serializer = tfactory.newTransformer();
      }

      serializer.setParameter("indent-elements", "yes");
      serializer.setOutputProperty(OutputKeys.INDENT, "yes");
      serializer.transform(new DOMSource(doc), new StreamResult(bos));

      return new String(bos.toByteArray()).replaceFirst("<br>", "");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] fromFile(File file) {
    try {
      return fromStream(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Fout bij inlezen bestand", e);
    }
  }

  public static void toFile(byte[] bytes, String filename) {
    toFile(bytes, new File(filename));
  }

  public static void toFile(byte[] bytes, File file) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      fos.write(bytes);
      fos.flush();
    } catch (Exception e) {
      throw new RuntimeException("Fout bij omzetten naar bestand", e);
    } finally {
      IOUtils.closeQuietly(fos);
    }
  }
}
