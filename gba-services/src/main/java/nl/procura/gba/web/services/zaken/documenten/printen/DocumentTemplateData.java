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

package nl.procura.gba.web.services.zaken.documenten.printen;

import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.HashMap;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class DocumentTemplateData extends HashMap<String, Object> {

  public Object putData(String key, DataInterface dataInterface) {
    DocumentTemplateData dataTemplate = new DocumentTemplateData();
    dataInterface.get(dataTemplate);
    return put(key, dataTemplate);
  }

  @Override
  public Object put(String key, Object value) {
    return super.put(key, trimQuotes(value));
  }

  /**
   * Fix om overbodige ; tekens te verwijderen
   */
  private Object trimQuotes(Object value) {
    if (value == null) {
      return "";
    }
    if (value instanceof String) {
      return astr(value).replaceAll(";+$", "");
    }
    return value;
  }

  protected String getBooleanValue(Boolean value) {
    return value != null ? (value ? "Ja" : "Nee") : "Onbekend";
  }

  protected DocumentDossierPerson toDocumentDossierPersoon(DossierPersoon person) {
    return deepCopyBean(DocumentDossierPerson.class, person);
  }

  public interface DataInterface {

    void get(DocumentTemplateData data);
  }
}
