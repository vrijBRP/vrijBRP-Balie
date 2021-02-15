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

package nl.procura.gba.web.services.zaken.protocol;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.ProtNewSearch;
import nl.procura.gba.jpa.personen.db.ProtNewSearchAttr;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;

public class ProtocolRecord {

  private DateTime                      datum      = new DateTime();
  private AnrFieldValue                 anummer    = new AnrFieldValue();
  private String                        gebruiker  = null;
  private ProtNewSearch                 protNewSearch;
  private List<ProtocolRecordAttribuut> attributen = null;

  public ProtocolRecord(ProtNewSearch protNew) {
    this.protNewSearch = protNew;
  }

  public AnrFieldValue getAnummer() {
    return anummer;
  }

  public void setAnummer(AnrFieldValue anummer) {
    this.anummer = anummer;
  }

  public List<ProtocolRecordAttribuut> getAttributen() {

    if (attributen == null) {
      attributen = new ArrayList<>();
    }

    for (ProtNewSearchAttr attr : protNewSearch.getProtNewSearchAttrs()) {

      ProtocolRecordAttribuut pa = new ProtocolRecordAttribuut();

      pa.setDoel(attr.getId().getFieldType());
      pa.setVeld(attr.getId().getField());

      attributen.add(pa);
    }

    return attributen;
  }

  public Map<String, List<ProtocolRecordAttribuut>> getCategorieen() {
    Map<String, List<ProtocolRecordAttribuut>> countMap = new TreeMap<>();

    for (ProtocolRecordAttribuut attr : getAttributen()) {
      if (countMap.containsKey(attr.getDoel())) {
        if (fil(attr.getVeld())) {
          countMap.get(attr.getDoel()).add(attr);
        }
      } else {
        if (fil(attr.getVeld())) {
          countMap.put(attr.getDoel(), new ArrayList<>(asList(attr)));
        } else {
          countMap.put(attr.getDoel(), new ArrayList<>());
        }
      }
    }

    return countMap;
  }

  public DateTime getDate() {
    return datum;
  }

  public String getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(String user) {
    this.gebruiker = user;
  }

  public void setDatum(DateTime datum) {
    this.datum = datum;
  }
}
