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

package nl.procura.gba.web.services.zaken.algemeen;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.ZaakStatusType.DOCUMENT_ONTVANGEN;
import static nl.procura.gba.common.ZaakStatusType.GEANNULEERD;
import static nl.procura.gba.common.ZaakStatusType.GEWEIGERD;
import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.INCOMPLEET;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT_IN_GBA;
import static nl.procura.gba.common.ZaakStatusType.WACHTKAMER;
import static nl.procura.gba.common.ZaakType.CORRESPONDENTIE;
import static nl.procura.gba.common.ZaakType.COVOG;
import static nl.procura.gba.common.ZaakType.ERKENNING;
import static nl.procura.gba.common.ZaakType.GEBOORTE;
import static nl.procura.gba.common.ZaakType.GEGEVENSVERSTREKKING;
import static nl.procura.gba.common.ZaakType.GPK;
import static nl.procura.gba.common.ZaakType.HUWELIJK_GPS_GEMEENTE;
import static nl.procura.gba.common.ZaakType.INBOX;
import static nl.procura.gba.common.ZaakType.INDICATIE;
import static nl.procura.gba.common.ZaakType.INHOUD_VERMIS;
import static nl.procura.gba.common.ZaakType.LEVENLOOS;
import static nl.procura.gba.common.ZaakType.LIJKVINDING;
import static nl.procura.gba.common.ZaakType.LV;
import static nl.procura.gba.common.ZaakType.NAAMGEBRUIK;
import static nl.procura.gba.common.ZaakType.NAAMSKEUZE;
import static nl.procura.gba.common.ZaakType.NATURALISATIE;
import static nl.procura.gba.common.ZaakType.OMZETTING_GPS;
import static nl.procura.gba.common.ZaakType.ONDERZOEK;
import static nl.procura.gba.common.ZaakType.ONTBINDING_GEMEENTE;
import static nl.procura.gba.common.ZaakType.OVERLIJDEN_IN_BUITENLAND;
import static nl.procura.gba.common.ZaakType.OVERLIJDEN_IN_GEMEENTE;
import static nl.procura.gba.common.ZaakType.PL_MUTATION;
import static nl.procura.gba.common.ZaakType.REGISTRATION;
import static nl.procura.gba.common.ZaakType.REISDOCUMENT;
import static nl.procura.gba.common.ZaakType.RIJBEWIJS;
import static nl.procura.gba.common.ZaakType.RISK_ANALYSIS;
import static nl.procura.gba.common.ZaakType.TERUGMELDING;
import static nl.procura.gba.common.ZaakType.UITTREKSEL;
import static nl.procura.gba.common.ZaakType.VERHUIZING;
import static nl.procura.gba.common.ZaakType.VERSTREKKINGSBEPERKING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;

public class ZaakTypeStatussen {

  private static final List<ZaakTypeStatus> list = new ArrayList<>();

  static {
    add(CORRESPONDENTIE, OPGENOMEN, INBEHANDELING, VERWERKT, GEANNULEERD);
    add(COVOG, INCOMPLEET, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(ERKENNING, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(GEBOORTE, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(ONDERZOEK, INCOMPLEET, INBEHANDELING, VERWERKT, GEANNULEERD);
    add(RISK_ANALYSIS, OPGENOMEN, VERWERKT, GEANNULEERD);
    add(REGISTRATION, INCOMPLEET, WACHTKAMER, OPGENOMEN, INBEHANDELING, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(GEGEVENSVERSTREKKING, INBEHANDELING, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(GPK, OPGENOMEN, VERWERKT, GEANNULEERD);
    add(HUWELIJK_GPS_GEMEENTE, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(OMZETTING_GPS, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(ONTBINDING_GEMEENTE, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(INDICATIE, WACHTKAMER, OPGENOMEN, VERWERKT, GEANNULEERD);
    add(INBOX, WACHTKAMER, OPGENOMEN, INBEHANDELING, VERWERKT, GEANNULEERD);
    add(NAAMGEBRUIK, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(NATURALISATIE, INCOMPLEET, WACHTKAMER, OPGENOMEN, INBEHANDELING, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(NAAMSKEUZE, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(LV, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(OVERLIJDEN_IN_BUITENLAND, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(OVERLIJDEN_IN_GEMEENTE, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(LEVENLOOS, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(LIJKVINDING, INCOMPLEET, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(REISDOCUMENT, INCOMPLEET, WACHTKAMER, OPGENOMEN, INBEHANDELING, DOCUMENT_ONTVANGEN,
        VERWERKT, VERWERKT_IN_GBA, GEANNULEERD);
    add(RIJBEWIJS, INBEHANDELING, DOCUMENT_ONTVANGEN, VERWERKT, GEANNULEERD);
    add(TERUGMELDING, OPGENOMEN, INBEHANDELING, VERWERKT, GEANNULEERD);
    add(UITTREKSEL, OPGENOMEN, VERWERKT, GEANNULEERD);
    add(VERHUIZING, INCOMPLEET, WACHTKAMER, OPGENOMEN, INBEHANDELING, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(VERSTREKKINGSBEPERKING, WACHTKAMER, OPGENOMEN, GEWEIGERD, VERWERKT, GEANNULEERD);
    add(INHOUD_VERMIS, WACHTKAMER, OPGENOMEN, VERWERKT, GEANNULEERD);
    add(PL_MUTATION, WACHTKAMER, OPGENOMEN, VERWERKT, GEANNULEERD);
  }

  public static List<ZaakStatusType> getAlle(ZaakType... types) {
    List<ZaakStatusType> typesList = new UniqueList<>();
    for (ZaakTypeStatus ts : list) {
      if (types == null || types.length == 0) {
        typesList.addAll(ts.getStatussen());
      } else {
        for (ZaakType type : types) {
          if (ts.getType() == type) {
            typesList.addAll(ts.getStatussen());
          }
        }
      }
    }

    return sort(typesList);
  }

  public static List<ZaakStatusType> getOvereenkomstige(List<Zaak> zaken) {
    List<ZaakType> types = new UniqueList<>();
    for (Zaak zaak : zaken) {
      types.add(zaak.getType());
    }

    return getOvereenkomstige(types.toArray(new ZaakType[types.size()]));
  }

  public static Map<ZaakType, List<Zaak>> groupByType(List<Zaak> zaken) {

    Map<ZaakType, List<Zaak>> map = new HashMap<>();

    for (Zaak zaak : zaken) {
      List<Zaak> l = map.get(zaak.getType());

      if (l == null) {
        l = new ArrayList<>();
      }

      l.add(zaak);
      map.put(zaak.getType(), l);
    }

    return map;
  }

  private static void add(ZaakType type, ZaakStatusType... statussen) {
    list.add(new ZaakTypeStatus(type, statussen));
  }

  private static List<ZaakStatusType> getOvereenkomstige(ZaakType... types) {
    List<ZaakStatusType> typesList = getAlle();
    for (ZaakStatusType status : getAlle()) {
      for (ZaakType type : types) {
        if (!getAlle(type).contains(status)) {
          typesList.remove(status);
        }
      }
    }

    return sort(typesList);
  }

  private static List<ZaakStatusType> sort(List<ZaakStatusType> typesList) {
    typesList.sort((o1, o2) -> o1.getSortCode() > o2.getSortCode() ? 1 : -1);
    return typesList;
  }

  private static class ZaakTypeStatus {

    private final List<ZaakStatusType> statussen;
    private final ZaakType             type;

    public ZaakTypeStatus(ZaakType type, ZaakStatusType... statussen) {
      this.type = type;
      this.statussen = asList(statussen);
    }

    public List<ZaakStatusType> getStatussen() {
      return statussen;
    }

    public ZaakType getType() {
      return type;
    }
  }
}
