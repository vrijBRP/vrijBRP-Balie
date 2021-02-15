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

package nl.procura.covog.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;

public class VogSoapFunctions {

  private static Node streamToNode(InputStream is) throws Exception {

    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setNamespaceAware(true);
    DocumentBuilder b = f.newDocumentBuilder();

    return b.parse(is);
  }

  private static OutputStream objectToStream(Object p, OutputStream os) throws Exception {
    objectToStream(p, os, true);
    return os;
  }

  private static void objectToStream(Object p, OutputStream os, boolean close) throws Exception {

    try {
      Class<?> c = p.getClass();

      if (p instanceof JAXBElement<?>) {
        c = ((JAXBElement<?>) p).getDeclaredType();
      }

      JAXBContext context = JAXBContext.newInstance(c);
      Marshaller marshaller = context.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(p, os);

      if (close) {
        os.flush();
        os.close();
      }
    } catch (Exception e) {
      throw new MarshallException("Het vraagbericht kan niet worden omgezet.");
    }
  }

  public static Node objectToNode(Object p) throws Exception {

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    objectToStream(p, os);
    return streamToNode(new ByteArrayInputStream(os.toByteArray()));
  }

  public static class IntakeValidationEventHandler implements ValidationEventHandler {

    private Exception exception = null;

    public Exception getException() {
      return exception;
    }

    public void setException(Exception exception) {
      this.exception = exception;
    }

    @Override
    public boolean handleEvent(ValidationEvent ve) {

      ve.getLinkedException().printStackTrace();

      if (ve.getSeverity() == ValidationEvent.FATAL_ERROR || ve.getSeverity() == ValidationEvent.ERROR) {
        ValidationEventLocator locator = ve.getLocator();
        String msg = "Bericht is incorrect.<hr>Message is " + ve.getMessage();

        msg += "<br/>Column is " + locator.getColumnNumber() + " at line number " + locator.getLineNumber();

        setException(new Exception(msg));
      }

      return true;
    }
  }

  public static class MarshallException extends Exception {

    private static final long serialVersionUID = -3420258059733069240L;

    private MarshallException(String s) {

      super(s);
    }
  }

  public static class UnMarshallException extends Exception {

    private static final long serialVersionUID = 5252362518100631919L;

    private UnMarshallException(String s) {

      super(s);
    }
  }
}
