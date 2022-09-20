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

import static nl.procura.standard.Globalfunctions.along;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import nl.procura.covog.objecten.isAanvraagOntvangen.CovogIsAanvraagOntvangenRequest;
import nl.procura.covog.objecten.verzendAanvraagNp.CovogAanvraag;
import nl.procura.covog.objecten.verzendAanvraagNp.CovogVerzendAanvraagRequest;
import nl.procura.soap.CustomSoapException;
import nl.procura.soap.SoapHandler;

public class VogSoapHandler extends SoapHandler {

  private static final String UNICODEAANVRAAGNPAPI = "UnicodeAanvraagNpAPI";
  private static final String VERZENDAANVRAAGNP    = "verzendAanvraagNP";
  private static final String ISAANVRAAGONTVANGEN  = "isAanvraagOntvangen";
  private String              relatieCode          = "";
  private String              identificatieCode    = "";

  public VogSoapHandler() {
  }

  public VogSoapHandler(String endpoint, String relatieCode, String identificatieCode) {

    super(endpoint);
    setRelatieCode(relatieCode);
    setIdentificatieCode(identificatieCode);
  }

  public long send(String aanvraagNummer) {

    try {
      return isAanvraagOntvangen(aanvraagNummer, getRelatieCode(), getIdentificatieCode());
    } catch (Exception e) {
      throw new CustomSoapException(e);
    }
  }

  public long send(CovogAanvraag aanvraag) {

    try {
      return verzendAanvraagNP(getEndpoint(), getRelatieCode(), getIdentificatieCode(), aanvraag);
    } catch (Exception e) {
      throw new CustomSoapException(e);
    }
  }

  public boolean isSuccess() {

    try {
      return (getResponse().getSoapMessage().getSOAPBody().getFault() == null);
    } catch (SOAPException e) {}

    return false;
  }

  @SuppressWarnings("unchecked")
  private SOAPBody send(String endpoint, String namespace, String operation, Object request) {
    SOAPConnection soapConnection = null;
    SOAPBody soapBody;

    try {
      soapConnection = SOAPConnectionFactory.newInstance().createConnection();

      MessageFactory messageFactory = MessageFactory.newInstance();
      SOAPMessage soapMessage = messageFactory.createMessage();
      SOAPPart soapPart = soapMessage.getSOAPPart();
      SOAPEnvelope envelope = soapPart.getEnvelope();

      envelope.setEncodingStyle(SOAPConstants.URI_NS_SOAP_ENCODING);

      SOAPBody body = envelope.getBody();

      QName qname = new QName(namespace, operation);

      JAXBElement element = new JAXBElement(qname, request.getClass(), null, request);

      body.addDocument((Document) VogSoapFunctions.objectToNode(element));

      soapMessage.saveChanges();

      ByteArrayOutputStream requestBos = new ByteArrayOutputStream();
      soapMessage.writeTo(requestBos);
      getRequest().setSoapMessage(soapMessage);
      getRequest().setXmlMessage(new String(requestBos.toByteArray(), StandardCharsets.UTF_8));

      SOAPMessage reply = soapConnection.call(soapMessage, new URL(endpoint));

      ByteArrayOutputStream responseBos = new ByteArrayOutputStream();
      reply.writeTo(responseBos);
      getResponse().setSoapMessage(reply);
      getResponse().setXmlMessage(new String(responseBos.toByteArray(), StandardCharsets.UTF_8));

      soapBody = reply.getSOAPBody();
    } catch (Exception e) {
      throw new RuntimeException("Fout bij versturen van COVOG bericht", e);
    } finally {
      if (soapConnection != null) {
        try {
          soapConnection.close();
        } catch (SOAPException e) {
          e.printStackTrace();
        }
      }
    }

    if (soapBody != null && soapBody.getFault() != null) {
      getResponse().setSoapFault(soapBody.getFault());
      throw new SOAPFaultException(soapBody.getFault());
    }

    return soapBody;
  }

  /**
   * Is de aanvraag door covog ontvangen?
   */
  private long isAanvraagOntvangen(String aanvraagNummer, String relatieCode, String identificatieCode) {

    CovogIsAanvraagOntvangenRequest request = new CovogIsAanvraagOntvangenRequest(aanvraagNummer, relatieCode,
        identificatieCode);

    return getReturnValue(send(getEndpoint(), UNICODEAANVRAAGNPAPI, ISAANVRAAGONTVANGEN, request));
  }

  /**
   * Verstuur nieuwe aanvraag
   */
  private long verzendAanvraagNP(String endpoint, String relatieCode, String identificatieCode, CovogAanvraag a) {

    CovogVerzendAanvraagRequest request = new CovogVerzendAanvraagRequest(relatieCode, identificatieCode, a);

    return getReturnValue(send(endpoint, UNICODEAANVRAAGNPAPI, VERZENDAANVRAAGNP, request));
  }

  /**
   * Haalt de waarde uit het antwoord
   */
  private long getReturnValue(SOAPBody soapBody) {

    if (soapBody != null) {

      try {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile("//return");
        Object result = expr.evaluate(soapBody, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;

        if (nodes.getLength() > 0) {
          return along(nodes.item(0).getTextContent());
        }
      } catch (XPathExpressionException | DOMException e) {
        throw new RuntimeException("Fout bij uitlezen van antwoord van COVOG", e);
      }
    }

    return 0;
  }

  public String getRelatieCode() {
    return relatieCode;
  }

  public void setRelatieCode(String relatieCode) {
    this.relatieCode = relatieCode;
  }

  public String getIdentificatieCode() {
    return identificatieCode;
  }

  public void setIdentificatieCode(String identificatieCode) {
    this.identificatieCode = identificatieCode;
  }
}
