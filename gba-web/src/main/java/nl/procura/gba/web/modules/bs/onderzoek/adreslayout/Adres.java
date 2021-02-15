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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public interface Adres {

  FieldValue getAdres();

  void setAdres(FieldValue adres);

  String getHnr();

  void setHnr(String hnr);

  String getHnrL();

  void setHnrL(String hnrL);

  FieldValue getHnrA();

  void setHnrA(FieldValue hnrA);

  String getHnrT();

  void setHnrT(String hnrT);

  FieldValue getPc();

  void setPc(FieldValue pc);

  FieldValue getPlaats();

  void setPlaats(FieldValue plaats);

  FieldValue getGemeente();

  void setGemeente(FieldValue gemeente);

  String getBuitenl1();

  void setBuitenl1(String buitenl1);

  String getBuitenl2();

  void setBuitenl2(String buitenl2);

  String getBuitenl3();

  void setBuitenl3(String buitenl3);

  FieldValue getLand();

  void setLand(FieldValue land);

  void setGemeentePostbus(Gemeente vermoedelijkeGemeente);

  FieldValue getAantalPersonen();

  void setAantalPersonen(FieldValue aantalPersonen);
}
