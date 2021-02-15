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

package nl.procura.gba.web.modules.zaken.curatele.page1;

import javax.xml.datatype.XMLGregorianCalendar;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.standard.ProcuraDate;

public class CurateleUtils {

  public static Adres getAdres(String straat, String hnr, String hnrt, String pc, String plaats, String gem) {

    BasePLElem str = getElement(straat);
    BasePLElem huisnummer = getElement(hnr);
    BasePLElem huisnummertoev = getElement(hnrt);
    BasePLElem postcode = getElement(pc);
    BasePLElem woonplaats = getElement(plaats);
    BasePLElem gemeente = getElement(gem);

    BasePLElem huisletter = new BasePLElem();
    BasePLElem huisnummeraand = new BasePLElem();
    BasePLElem locatie = new BasePLElem();
    BasePLElem datum_aanvang = new BasePLElem();
    BasePLElem gemeentedeel = new BasePLElem();
    BasePLElem emigratieland = new BasePLElem();
    BasePLElem emigratiedatum = new BasePLElem();
    BasePLElem buitenland1 = new BasePLElem();
    BasePLElem buitenland2 = new BasePLElem();
    BasePLElem buitenland3 = new BasePLElem();

    return new Adres(str, huisnummer, huisletter, huisnummertoev, huisnummeraand, locatie, postcode, gemeentedeel,
        woonplaats, gemeente, datum_aanvang, emigratieland, emigratiedatum, buitenland1, buitenland2,
        buitenland3);
  }

  public static BasePLElem getDatum(XMLGregorianCalendar geboorteDatum) {

    ProcuraDate date = new ProcuraDate(geboorteDatum.toString().replaceAll("\\D", ""));

    BasePLElem element = new BasePLElem();
    element.getValue().setCode(date.getSystemDate());
    element.getValue().setVal(date.getSystemDate());
    element.getValue().setDescr(date.getFormatDate());

    return element;
  }

  public static BasePLElem getElement(String waarde) {

    BasePLElem element = new BasePLElem();
    element.getValue().setCode(waarde);
    element.getValue().setVal(waarde);
    element.getValue().setDescr(waarde);
    return element;
  }

  public static Naam getNaam(String vnaam, String anaam, String voorv) {
    return new Naam(getElement(vnaam), getElement(anaam), getElement(voorv), getElement(""), getElement(""), null);
  }

}
