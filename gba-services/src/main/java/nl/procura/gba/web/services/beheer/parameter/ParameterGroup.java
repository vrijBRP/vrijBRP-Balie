/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.beheer.parameter;

public enum ParameterGroup {

  GROUP_BSM("Taakplanner"),
  GROUP_ZAKEN_DMS("Zaken DMS", "Zaken DMS"),
  GROUP_ZAKEN_DMS_TYPES("Zaken DMS Types", "Zaken DMS types"),
  GROUP_ALGEMEEN("Algemeen"),
  GROUP_DATABASES("Databases"),
  GROUP_APPS("Applicaties"),
  GROUP_CONFIGURATIEBESTAND("Configuratiebestand"),
  GROUP_DIENSTEN("Diensten"),
  GROUP_OVERIG("Overig"),
  GROUP_ALGEMENE_INSTELLINGEN("Algemene Instellingen"),
  GROUP_ZAKEN("Zaken"),
  GROUP_ZAKEN_ALGEMEEN("Zaken algemeen", "Algemeen"),
  GROUP_ZAKEN_BEHANDELEN("Behandelen"),
  GROUP_ZAKEN_STATUS("Initiële statussen"),
  GROUP_COVOG("COVOG"),
  GROUP_DOCUMENTEN("Documenten"),
  GROUP_HANDLEIDINGEN("Handleidingen"),
  GROUP_EMAIL("E-mail"),
  GROUP_SMS("SMS"),
  GROUP_GEO("Geo / BAG"),
  GROUP_PORTAAL("Portaal"),
  GROUP_RAAS("RAAS"),
  GROUP_SYSTEM("Systeem"),
  GROUP_GPK("GPK"),
  GROUP_WERKPROCES("Werkproces"),
  GROUP_VOORRAAD("Voorraad"),
  GROUP_CONTACT("Contactgegevens"),
  GROUP_KASSA("Kassa"),
  GROUP_MIDOFFICE("Midoffice"),
  GROUP_PRINT("Afdrukken"),
  GROUP_CONNECT("VrijBRP Connect"),
  GROUP_PRESENTIEVRAAG("Presentievraag"),
  GROUP_PROCURA("PROCURA"),
  GROUP_PROTOCOLLERING("Protocollering"),
  GROUP_TERUGMELDINGEN("Terugmeldingen"),
  GROUP_GV("Gegevensverstrekking"),
  GROUP_ONTBINDING("Ontbinding GPS/huwelijk"),
  GROUP_ONDERZOEK("Adresonderzoek"),
  GROUP_RIJBEWIJZEN("Rijbewijzen"),
  GROUP_REISDOCUMENTEN("Reisdocumenten"),
  GROUP_CURATELE("Curateleregister"),
  GROUP_VERHUIZING("Verhuizing"),
  GROUP_RISKANALYSIS("Risicoanalyse"),
  GROUP_VERIFICATIEVRAAG("Verificatievraag"),
  GROUP_ZOEKEN("Zoeken"),
  GROUP_ZOEKEN_ALGEMEEN("Zoeken algemeen", "Algemeen"),
  GROUP_ZOEKEN_PROFIEL_STANDAARD("Standaardprofiel"),
  GROUP_ZOEKEN_PROFIEL_GBAV_PLUS("GBAV+ profiel"),
  GROUP_KENNISBANK("Kennisbank"),
  GROUP_MIJN_OVERHEID("Mijn overheid"),
  GROUP_PROBEV("PROBEV"),
  GROUP_INBOX("Verzoeken"),
  GROUP_INTERNAL("INTERNAL");

  private String             id;
  private String             caption;
  private ParameterValidator validator;

  ParameterGroup(String id) {
    this(id, id, new DefaultParameterValidator());
  }

  ParameterGroup(String id, String caption) {
    this(id, caption, new DefaultParameterValidator());
  }

  ParameterGroup(String id, String caption, ParameterValidator validator) {
    this.id = id;
    this.caption = caption;
    this.validator = validator;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ParameterValidator getValidator() {
    return validator;
  }

  public void setValidator(ParameterValidator validator) {
    this.validator = validator;
  }

  @Override
  public String toString() {
    return id;
  }
}
