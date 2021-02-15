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

package nl.procura.rdw.functions;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RdwUtils {

  private static final String PROC_IDENT     = "PROC-IDENT";
  private static final String PROC_FUNC      = "PROC-FUNC";
  private static final String SYST_AFK       = "SYST-AFK";
  private static final String MELDING_NR     = "MELDING-NR";
  private static final String KORTE_MELDING  = "KORTE-MELDING";
  private static final String SOORT_MELDING  = "SOORT-MELDING";
  private static final String MELDING_VAR    = "MELDING-VAR";
  private static final String STAT_RIP       = "STAT-RIP";
  private static final String STAT_TEKST_RIP = "STAT-TEKST-RIP";

  public static Proces getId(InputStream is) {

    Proces id;

    try {
      id = new Proces();

      DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

      f.setNamespaceAware(true);

      DocumentBuilder b = f.newDocumentBuilder();

      Node node = b.parse(is);

      findId(id, node);
    } catch (Exception e) {
      throw new RuntimeException("Fout RDW-webservice", e);
    } finally {
      IOUtils.closeQuietly(is);
    }

    return id;
  }

  private static void findId(Proces id, Node node) {

    NodeList nl = node.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {

      Node cn = nl.item(i);

      String value = "";

      if (cn.hasChildNodes()) {
        value = cn.getChildNodes().item(0).getNodeValue();
      }

      if (PROC_IDENT.equalsIgnoreCase(cn.getLocalName())) {
        id.setProces(along(value));
      }

      if (PROC_FUNC.equalsIgnoreCase(cn.getLocalName())) {
        id.setFunctie(along(value));
      }

      id.getMelding().setProces(RdwProces.get(id.getProces(), id.getFunctie()));

      if (SYST_AFK.equalsIgnoreCase(cn.getLocalName())) {
        id.getMelding().setSysteem(value);
      }

      if (MELDING_NR.equalsIgnoreCase(cn.getLocalName())) {
        id.getMelding().setNr(along(value));
      }

      if (KORTE_MELDING.equalsIgnoreCase(cn.getLocalName())) {
        id.getMelding().setMeldingKort(value);
      }

      if (SOORT_MELDING.equalsIgnoreCase(cn.getLocalName())) {
        id.getMelding().setMeldingSoort(RdwMeldingSoort.get(value));
      }

      if (MELDING_VAR.equalsIgnoreCase(cn.getLocalName())) {
        id.getMelding().setMeldingVar(value);
      }

      if (STAT_RIP.equalsIgnoreCase(cn.getLocalName())) {
        id.getMelding().setNr(along(value));
        id.getMelding().setRipNr(along(value));
      }

      if (STAT_TEKST_RIP.equalsIgnoreCase(cn.getLocalName())) {
        id.getMelding().setMeldingKort(value);
      }

      findId(id, cn);
    }
  }

  @SuppressWarnings("unchecked")
  public static void toStream(Proces p, OutputStream os, boolean close, boolean validate) {

    try {
      Object o = p.getObject();

      JAXBContext jc = JAXBContext.newInstance(o.getClass());

      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      Schema schema = schemaFactory.newSchema(new SAXSource(new InputSource(
          RdwUtils.class.getClassLoader().getResourceAsStream(
              String.format("xsd/PROC-%04d-%02d.xsd", p.getProces(), p.getFunctie())))));

      Marshaller marshaller = jc.createMarshaller();

      if (validate) {
        marshaller.setSchema(schema);
      }

      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      QName qname = new QName("", o.getClass().getAnnotation(XmlType.class).name());

      @SuppressWarnings("rawtypes")
      JAXBElement element = new JAXBElement(qname, o.getClass(), null, o);

      marshaller.marshal(element, os);
    } catch (Exception e) {
      throw new RuntimeException("Fout RDW-webservice: " + e.getMessage(), e);
    } finally {
      if (close) {
        IOUtils.closeQuietly(os);
      }
    }
  }

  public static Proces fromStream(InputStream is, boolean fillEmpty) {

    ByteArrayOutputStream bos = null;
    InputStream temp1 = null;
    InputStream temp2 = null;

    try {
      bos = new ByteArrayOutputStream();

      IOUtils.copy(is, bos);

      temp1 = new ByteArrayInputStream(bos.toByteArray());
      temp2 = new ByteArrayInputStream(bos.toByteArray());

      Proces id = RdwUtils.getId(temp1);
      String format = String.format("nl.procura.rdw.processen.p%04d.f%02d", id.getProces(), id.getFunctie());
      JAXBContext jc = JAXBContext.newInstance(format);
      Unmarshaller u = jc.createUnmarshaller();
      JAXBElement<?> element = (JAXBElement<?>) u.unmarshal(temp2);

      if (fillEmpty) {
        Filler.fill(element.getValue());
      }

      id.setObject((ProcesObject) element.getValue());
      return id;
    } catch (Exception e) {
      throw new RuntimeException("Fout RDW-webservice", e);
    } finally {
      IOUtils.closeQuietly(is);
      IOUtils.closeQuietly(bos);
      IOUtils.closeQuietly(temp1);
      IOUtils.closeQuietly(temp2);
    }
  }

  public static void init(Object o, BigInteger procId, BigInteger funcId, String gebrIdent, String ww, String info) {

    try {
      String method = "getAlggeg";
      Method m = o.getClass().getMethod(method);
      Object algGeg = m.invoke(o);

      if (algGeg == null) {
        algGeg = RdwReflection.instantiate(m.getReturnType());
      }

      RdwReflection.setValue(algGeg, "Procident", procId);
      RdwReflection.setValue(algGeg, "Procfunc", funcId);
      RdwReflection.setValue(algGeg, "Gebrident", gebrIdent);
      RdwReflection.setValue(algGeg, "Wachtwoordact", ww);
      RdwReflection.setValue(algGeg, "Infogebr", info);
      RdwReflection.setValue(algGeg, "Optstattekst", BigInteger.valueOf(2));

      RdwReflection.setValue(o, method, algGeg);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Proces getRandomStream(BigInteger procId, BigInteger procFunc, RdwProcesType out) {

    try {
      RdwProces proces = RdwProces.get(aval(procId), aval(procFunc), out);
      File dir = new File(RdwUtils.class.getClassLoader().getResource(
          String.format("examples/generated/%d_%d", proces.p, proces.f)).getFile());

      SecureRandom random = new SecureRandom();
      int randomNr = random.nextInt(dir.listFiles().length);
      int nr = 0;
      File randomFile = null;

      for (File file : dir.listFiles()) {
        nr++;
        if (nr == randomNr) {
          randomFile = file;
          break;
        }
      }

      return RdwUtils.fromStream(new FileInputStream(randomFile), true);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
