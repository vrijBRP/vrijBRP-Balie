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

package nl.procura.gba.web.services.bs.onderzoek.enums;

import java.util.Arrays;
import java.util.Objects;

public enum AanduidingOnderzoekType {

  ONBEKEND("", ""),
  AAND_080000("080000", "08.00.00 (hele categorie)"),
  AAND_081100("081100", "08.11.00 (groep adres)"),
  AAND_080900("080900", "08.09.00 (groep gemeente)"),
  AAND_081000("081000", "08.10.00 (groep adreshouding)"),
  AAND_081200("081200", "08.12.00 (groep locatie)"),
  AAND_081300("081300", "08.13.00 (groep adres buitenland)"),
  AAND_081400("081400", "08.14.00 (groep immigratie)"),
  AAND_080910("080910", "08.09.10 (gemeente van inschrijving)"),
  AAND_080920("080920", "08.09.20 (datum inschrijving)"),
  AAND_081010("081010", "08.10.10 (functie adres)"),
  AAND_081020("081020", "08.10.20 (gemeentedeel)"),
  AAND_081030("081030", "08.10.30 (datum aanvang adreshouding)"),
  AAND_081110("081110", "08.11.10 (straatnaam)"),
  AAND_081115("081115", "08.11.15 (naam openbare ruimte)"),
  AAND_081120("081120", "08.11.20 (huisnummer)"),
  AAND_081130("081130", "08.11.30 (huisletter)"),
  AAND_081140("081140", "08.11.40 (huisnummertoevoeging)"),
  AAND_081150("081150", "08.11.50 (aanduiding bij huisnummer)"),
  AAND_081160("081160", "08.11.60 (postcode)"),
  AAND_081170("081170", "08.11.70 (woonplaatsnaam)"),
  AAND_081180("081180", "08.11.80 (identificatiecode verblijfplaats)"),
  AAND_081190("081190", "08.11.90 (identificatiecode nummeraanduiding)"),
  AAND_081210("081210", "08.12.10 (locatiebeschrijving)"),
  AAND_081310("081310", "08.13.10 (land adres buitenland)"),
  AAND_081320("081320", "08.13.20 (datum aanvang adres buitenland)"),
  AAND_081330("081330", "08.13.30 (regel 1 adres buitenland)"),
  AAND_081340("081340", "08.13.40 (regel 2 adres buitenland)"),
  AAND_081350("081350", "08.13.50 (regel 3 adres buitenland)"),
  AAND_081410("081410", "08.14.10 (land vanwaar ingeschreven)"),
  AAND_081420("081420", "08.14.20 (datum vestiging in Nederland)");

  private String code = "";
  private String oms  = "";

  AanduidingOnderzoekType(String code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public static AanduidingOnderzoekType get(String code) {
    return Arrays.stream(values())
        .filter(a -> Objects.equals(a.getCode(), code))
        .findFirst()
        .orElse(ONBEKEND);

  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
