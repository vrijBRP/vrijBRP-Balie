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

package nl.procura.curatele.soap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.soap.SOAPHandler;

import org.w3c.dom.Node;

import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;

import nl.procura.soap.CustomSoapException;
import nl.procura.soap.SoapFunctions;
import nl.procura.soap.SoapHandler;
import nl.procura.soap.listeners.WSSUsernameTokenSecurityHandler;
import nl.procura.standard.Resource;
import nl.rechtspraak.namespaces.ccr.CCRWS;
import nl.rechtspraak.namespaces.ccr01.Curateleregister;
import nl.rechtspraak.namespaces.ccr01.CurateleregisterSoap;
import nl.rechtspraak.namespaces.ccr01.SearchPersonResponse.SearchPersonResult;

public class CurateleSoapHandler extends SoapHandler {

  private String username;
  private String password;

  public CurateleSoapHandler(String endpoint, String username, String password) {
    super(endpoint);
    this.username = username;
    this.password = password;
  }

  @Override
  public List<SOAPHandler> getSoapHandlers() {
    return Arrays.asList(new WSSUsernameTokenSecurityHandler(username, password), new WSASoapHandler());
  }

  private SearchPersonResult sendMessage(String prefix, String surname, String date) {

    try {
      WebServiceFeature[] enabledRequiredwsf = { new MemberSubmissionAddressingFeature() };
      CurateleregisterSoap proxy = getStub().getCurateleregisterSoap(enabledRequiredwsf);
      updateProxy((BindingProvider) proxy);
      return proxy.searchPerson(prefix, surname, getDate(date));
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  public List<CCRWS> send(String prefix, String surname, String date) {

    List<CCRWS> list = new ArrayList<>();
    for (Object person : sendMessage(prefix, surname, date).getContent()) {
      if (person != null) {
        if (person instanceof Node) {
          list.add(SoapFunctions.nodeToObject(CCRWS.class, (Node) person));
        }
      }
    }

    return list;
  }

  private Curateleregister getStub() {
    Curateleregister service = new Curateleregister(Resource.getURL("wsdl/curatele_v3.0.wsdl"));
    service.setHandlerResolver(new MyHandlerResolver(this));
    return service;
  }

  private XMLGregorianCalendar getDate(String dateString) {

    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      GregorianCalendar g = new GregorianCalendar();
      g.setTime(sdf.parse(dateString));

      XMLGregorianCalendar dob = DatatypeFactory.newInstance().newXMLGregorianCalendar(g);
      dob.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
      return dob;
    } catch (ParseException | DatatypeConfigurationException e) {
      e.printStackTrace();
    }
    return null;
  }
}
