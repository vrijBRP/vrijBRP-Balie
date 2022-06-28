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

package nl.procura.gba.web.modules.hoofdmenu.klapper;

import static java.lang.String.format;

import java.util.List;

import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperUtils;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperZoekargumenten;
import nl.procura.vaadin.component.layout.info.InfoLayout;

/**
 * Geeft info over gaten tussen klappers
 */
public class KlapperGatenLayout extends InfoLayout {

  private final List<DossierAkte> aktes;
  private final List<DossierAkte> volgorde;
  private List<DossierAkte>       gaten;

  public KlapperGatenLayout(List<DossierAkte> aktes,
      KlapperZoekargumenten zoekargumenten,
      boolean checkGaten,
      boolean checkVolgorde) {

    this.aktes = aktes;

    gaten = KlapperUtils.getGaten(aktes);
    volgorde = KlapperUtils.getVerkeerdeVolgorde(aktes);

    String periodes = zoekargumenten.heeftPeriodes()
        ? format(" over de periode %s", zoekargumenten.getPeriodes())
        : "";

    int aantalGaten = gaten.size();
    int aantalVolgorde = volgorde.size();

    StringBuilder message = new StringBuilder();
    if (checkGaten && aantalGaten > 0) {
      if (aantalGaten == 1) {
        message.append(String.format(
            "Er zit 1 gat tussen de aktenummers%s. Druk op de knop 'gaten' voor meer informatie.",
            periodes));
      } else {
        message.append(String.format(
            "Er zitten %d gaten tussen de aktenummers%s. Druk op de knop 'gaten' voor meer informatie.",
            aantalGaten, periodes));
      }
    }

    if (checkVolgorde && aantalVolgorde > 0) {
      if (aantalGaten > 0) {
        message.append("</br>");
      }
      if (aantalVolgorde == 1) {
        message.append(
            String.format("Er is een akte met met een verkeerde volgorde%s. Deze is in rood aangegeven.",
                periodes));
      } else {
        message.append(
            String.format("Er staan %d aktes in de verkeerde volgorde%s. Deze zijn in rood aangegeven.",
                aantalVolgorde, periodes));
      }
    }

    if (zoekargumenten.getJaar() <= 0 && aktes.size() == 5000) {
      message
          .append("<hr><b>Het aantal zoekresultaten is beperkt tot 5000 omdat er geen 'periode' is geselecteerd.</b>");
    }

    setMessage(message.toString());
  }

  public List<DossierAkte> getAktes() {
    return aktes;
  }

  public List<DossierAkte> getGaten() {
    return gaten;
  }

  public boolean isGaten() {
    return !gaten.isEmpty();
  }

  public void setGaten(List<DossierAkte> gaten) {
    this.gaten = gaten;
  }

  public boolean isVerkeerdeVolgorde() {
    return !volgorde.isEmpty();
  }

  public boolean isVerkeerdeVolgorde(DossierAkte akte) {
    for (DossierAkte volgordeAkte : volgorde) {
      if (volgordeAkte.getCode().equals(akte.getCode())) {
        return true;
      }
    }
    return false;
  }
}
