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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.zaaktype;

import static ch.lambdaj.Lambda.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;

import ch.lambdaj.group.Group;

public class TellingUittreksel extends TellingTemplate {

  public static TypeUittreksel get(ZaakType zaakType, Group<Zaak> zakenGroup) {

    TypeUittreksel uittreksel = new TypeUittreksel(zaakType, zakenGroup);

    Group<DocumentZaak> uittrekselGroup = getUittreksels(get(DocumentZaak.class, zakenGroup.findAll()));
    Group<DocumentZaak> doelenGroup = getDoelen(get(DocumentZaak.class, zakenGroup.findAll()));
    Group<DocumentZaak> afnemersGroup = getAfnemers(get(DocumentZaak.class, zakenGroup.findAll()));
    Group<DocumentZaak> attestatieDeVitaGroup = getAttestatiesDeVita(get(DocumentZaak.class, zakenGroup.findAll()));

    for (Group<DocumentZaak> group : uittrekselGroup.subgroups()) {

      String rKey = (String) group.key();
      String oms = fil(rKey) ? rKey : "Onbekend";

      uittreksel.getTypes().add(new DashboardTellingType(oms, group.findAll()));
    }

    for (Group<DocumentZaak> group : doelenGroup.subgroups()) {

      String rKey = (String) group.key();
      String oms = fil(rKey) ? rKey : "Onbekend";

      uittreksel.getDoelen().add(new DashboardTellingType(oms, group.findAll()));
    }

    for (Group<DocumentZaak> zaak : afnemersGroup.subgroups()) {

      String rKey = (String) zaak.key();
      String oms = fil(rKey) ? rKey : "Onbekend";

      uittreksel.getAfnemers().add(new DashboardTellingType(oms, zaak.findAll()));
    }

    for (Group<DocumentZaak> group : attestatieDeVitaGroup.subgroups()) {

      DocumentRecord key = (DocumentRecord) group.key();

      uittreksel.getDocumenten().add(new DashboardTellingType(key.getDocument(), group.findAll()));
    }

    return uittreksel;
  }

  private static Group<DocumentZaak> getAfnemers(List<DocumentZaak> zaken) {
    return group(zaken, by(on(DocumentZaak.class).getDocumentAfn()));
  }

  private static Group<DocumentZaak> getAttestatiesDeVita(List<DocumentZaak> zaken) {
    return group(zaken, by(on(DocumentZaak.class).getDoc()));
  }

  private static Group<DocumentZaak> getDoelen(List<DocumentZaak> zaken) {
    return group(zaken, by(on(DocumentZaak.class).getDocumentDoel()));
  }

  private static Group<DocumentZaak> getUittreksels(List<DocumentZaak> zaken) {
    return group(zaken, by(on(DocumentZaak.class).getDoc().getDocument()));
  }

  public static class TypeUittreksel extends DashboardTellingType<ZaakType, Zaak> {

    private final List<DashboardTellingType> types      = new ArrayList<>();
    private final List<DashboardTellingType> doelen     = new ArrayList<>();
    private final List<DashboardTellingType> afnemers   = new ArrayList<>();
    private final List<DashboardTellingType> documenten = new ArrayList<>();

    public TypeUittreksel() {
    }

    public TypeUittreksel(ZaakType zaakType, Group<Zaak> group) {
      super(zaakType, group);
    }

    public List<DashboardTellingType> getAfnemers() {
      return afnemers;
    }

    public List<DashboardTellingType> getDocumenten() {
      return documenten;
    }

    public List<DashboardTellingType> getDoelen() {
      return doelen;
    }

    public List<DashboardTellingType> getTypes() {
      return types;
    }
  }
}
