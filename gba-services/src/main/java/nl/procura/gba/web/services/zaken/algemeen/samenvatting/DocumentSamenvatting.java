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

import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakPersoon;

/**
 * Maakt samenvatting van gegevens over een specifieke uittrekselaanvraag
 */
public class DocumentSamenvatting extends ZaakSamenvattingTemplate<DocumentZaak> {

  public DocumentSamenvatting(ZaakSamenvatting zaakSamenvatting, DocumentZaak zaak) {
    super(zaakSamenvatting, zaak);
  }

  /**
   * Voegt de deelzaken (betrokkenen) toe
   */
  @Override
  public void addDeelzaken(DocumentZaak zaak) {

    Deelzaken deelZaken = addDeelzaken(zaak.getPersonen().size());
    for (DocumentZaakPersoon p : zaak.getPersonen()) {

      String naam = p.getPersoon().getPersoon().getFormats().getNaam().getPred_eerstevoorn_adel_voorv_gesl();
      String leeftijd = p.getPersoon().getPersoon().getFormats().getGeboorte().getDatum_leeftijd();

      Deelzaak deelZaak = new Deelzaak();
      deelZaak.add("Persoon", naam);
      deelZaak.add("BSN", p.getBurgerServiceNummer().getDescription());
      deelZaak.add("Geboren", leeftijd);
      deelZaken.add(deelZaak);
    }
  }

  /**
   * Voegt de rubrieken toe
   */
  @Override
  public void addZaakItems(DocumentZaak zaak) {
    ZaakItemRubriek rubriek = addRubriek("Uittreksel");
    rubriek.add("Document", zaak.getDoc().getDocument());
    rubriek.add("Afnemer", fil(zaak.getDocumentAfn()) ? zaak.getDocumentAfn() : "Niet van toepassing");
    rubriek.add("Doel", fil(zaak.getDocumentDoel()) ? zaak.getDocumentDoel() : "Niet van toepassing");
  }
}
