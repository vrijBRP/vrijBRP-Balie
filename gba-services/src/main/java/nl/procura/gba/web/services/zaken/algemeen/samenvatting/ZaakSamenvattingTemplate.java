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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;

import lombok.Data;

/**
 * Een samenvatting van de zaak t.b.v. een samenvattingsdocument
 */
@Data
public abstract class ZaakSamenvattingTemplate<T> {

  private String           titel;
  private ZaakSamenvatting zaakSamenvatting;

  public ZaakSamenvattingTemplate(ZaakSamenvatting zaakSamenvatting, T zaak) {
    this(zaakSamenvatting, zaak, "");
  }

  public ZaakSamenvattingTemplate(ZaakSamenvatting zaakSamenvatting, T zaak, String titel) {
    this.zaakSamenvatting = zaakSamenvatting;
    this.titel = titel;
    zaakSamenvatting.setZaakSpecifiek(true);

    addZaakItems(zaak);
    addDeelzaken(zaak);
  }

  public abstract void addDeelzaken(T zaak);

  public abstract void addZaakItems(T zaak);

  protected Deelzaken addDeelzaken(int count) {
    return addDeelzaken(count == 1 ? "Aanvrager / aangevraagd voor" : "Betrokkenen");
  }

  protected Deelzaken addDeelzaken(String naam) {
    Deelzaken deelZaken = new Deelzaken(naam);
    zaakSamenvatting.setDeelzaken(deelZaken);
    return deelZaken;
  }

  protected ZaakItemRubriek addRubriek(String naam) {
    return zaakSamenvatting.getZaakItemRubrieken().add(naam);
  }

  protected void setAktes(Dossier zaak) {

    for (DossierAkte akte : zaak.getAktes()) {

      ZaakItemRubriek rubriek = addRubriek("Akte " + akte.getAkte());
      rubriek.add("Aktedatum", akte.getDatumIngang().getFormatDate());

      StringBuilder personen = new StringBuilder();
      StringBuilder gemeenten = new StringBuilder();

      int i = 0;
      for (DossierPersoon person : BsPersoonUtils.sort(akte.getPersonen())) {
        if (ToonAktePersoonUtils.toon(zaak.getZaakDossier(), person)) {
          i++;

          String naam = person.getNaam().getPred_adel_voorv_gesl_voorn();
          personen.append(i + ". " + naam);
          personen.append(" (" + person.getDossierPersoonType() + ")\n");
          String adres = person.getWoongemeente().getDescription();

          if (person.isRNI()) {
            adres = "RNI";
          } else if (!Landelijk.isNederland(person.getLand())) {
            adres = person.getLand().getDescription();
          }

          gemeenten.append((fil(adres) ? adres : "-") + "\n");
        }
      }

      rubriek.add("Personen", personen.toString());
    }
  }

  protected String getBooleanValue(Boolean value) {
    return value != null ? (value ? "Ja" : "Nee") : "Onbekend";
  }
}
