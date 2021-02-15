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

package nl.procura.gba.web.services.bs.overlijden;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.algemeen.enums.Doodsoorzaak;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging;
import nl.procura.gba.web.services.bs.algemeen.enums.WijzeLijkbezorging;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public interface DossierOverlijdenLijkbezorging {

  String getBestemming();

  String getBuitenBeneluxTekst();

  DateTime getDatumLijkbezorging();

  void setDatumLijkbezorging(DateTime dateTime);

  Doodsoorzaak getDoodsoorzaak();

  void setDoodsoorzaak(Doodsoorzaak doodsoorzaak);

  FieldValue getLandBestemming();

  void setLandBestemming(FieldValue landBestemming);

  OntvangenDocument getOntvangenDocumentLijkbezorging();

  void setOntvangenDocumentLijkbezorging(OntvangenDocument ontvLijkBezorgDoc);

  String getPlaatsBestemming();

  void setPlaatsBestemming(String plaats);

  String getPlaatsOntleding();

  void setPlaatsOntleding(String plaats);

  TermijnLijkbezorging getTermijnLijkbezorging();

  void setTermijnLijkbezorging(TermijnLijkbezorging termijnLijkbezorging);

  DateTime getTijdLijkbezorging();

  void setTijdLijkbezorging(DateTime dateTime);

  String getTijdLijkbezorgingStandaard();

  String getVervoermiddel();

  void setVervoermiddel(String vervoermiddel);

  String getViaBestemming();

  void setViaBestemming(String via);

  WijzeLijkbezorging getWijzeLijkBezorging();

  void setWijzeLijkBezorging(WijzeLijkbezorging wijzeLijkBezorging);

  boolean isBuitenBenelux();

  void setBuitenBenelux(boolean buitenBenelux);

}
