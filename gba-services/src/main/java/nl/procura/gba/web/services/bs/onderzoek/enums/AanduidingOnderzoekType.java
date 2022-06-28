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

  ONBEKEND("", "", false),
  AAND_080000("080000", "08.00.00 (hele categorie)", false),
  AAND_081100("081100", "08.11.00 (groep adres)", false),
  AAND_080900("080900", "08.09.00 (groep gemeente)", false),
  AAND_081000("081000", "08.10.00 (groep adreshouding)", false),
  AAND_081200("081200", "08.12.00 (groep locatie)", false),
  AAND_081300("081300", "08.13.00 (groep adres buitenland)", false),
  AAND_081400("081400", "08.14.00 (groep immigratie)", false),
  AAND_080910("080910", "08.09.10 (gemeente van inschrijving)", false),
  AAND_080920("080920", "08.09.20 (datum inschrijving)", false),
  AAND_081010("081010", "08.10.10 (functie adres)", false),
  AAND_081020("081020", "08.10.20 (gemeentedeel)", false),
  AAND_081030("081030", "08.10.30 (datum aanvang adreshouding)", false),
  AAND_081110("081110", "08.11.10 (straatnaam)", false),
  AAND_081115("081115", "08.11.15 (naam openbare ruimte)", false),
  AAND_081120("081120", "08.11.20 (huisnummer)", false),
  AAND_081130("081130", "08.11.30 (huisletter)", false),
  AAND_081140("081140", "08.11.40 (huisnummertoevoeging)", false),
  AAND_081150("081150", "08.11.50 (aanduiding bij huisnummer)", false),
  AAND_081160("081160", "08.11.60 (postcode)", false),
  AAND_081170("081170", "08.11.70 (woonplaatsnaam)", false),
  AAND_081180("081180", "08.11.80 (identificatiecode verblijfplaats)", false),
  AAND_081190("081190", "08.11.90 (identificatiecode nummeraanduiding)", false),
  AAND_081210("081210", "08.12.10 (locatiebeschrijving)", false),
  AAND_081310("081310", "08.13.10 (land adres buitenland)", false),
  AAND_081320("081320", "08.13.20 (datum aanvang adres buitenland)", false),
  AAND_081330("081330", "08.13.30 (regel 1 adres buitenland)", false),
  AAND_081340("081340", "08.13.40 (regel 2 adres buitenland)", false),
  AAND_081350("081350", "08.13.50 (regel 3 adres buitenland)", false),
  AAND_081410("081410", "08.14.10 (land vanwaar ingeschreven)", false),
  AAND_081420("081420", "08.14.20 (datum vestiging in Nederland)", false),
  AAND_089999("089999", "08.99.99 (betrokkene niet meer woonachtig op adres)", true);

  private String  code = "";
  private String  oms  = "";
  private boolean deelresultaat;

  AanduidingOnderzoekType(String code, String oms, boolean deelresultaat) {
    this.code = code;
    this.oms = oms;
    this.deelresultaat = deelresultaat;
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

  public String getOms() {
    return oms;
  }

  public boolean isDeelresultaat() {
    return deelresultaat;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
