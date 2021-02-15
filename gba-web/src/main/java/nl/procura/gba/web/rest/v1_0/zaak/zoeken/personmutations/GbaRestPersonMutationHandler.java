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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.personmutations;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.gba.jpa.personen.db.PlMutRec;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class GbaRestPersonMutationHandler extends GbaRestElementHandler {

  public GbaRestPersonMutationHandler(Services services) {
    super(services);
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    PersonListMutation zaak = (PersonListMutation) zaakParm;
    GbaRestElement mutElement = gbaZaak.add(MUTATIE);
    int cat = zaak.getCat().intValue();
    int action = zaak.getAction().intValue();

    mutElement.add(ACTIE).set(action, PersonListActionType.get(action).getDescription());
    mutElement.add(CATEGORIE).set(cat, GBACat.getByCode(cat).getDescr());
    mutElement.add(SET).set(zaak.getSet().toString());
    mutElement.add(OMSCHRIJVING).set(zaak.getExplanation());

    for (PlMutRec mutationRecord : zaak.getPlMutRecs()) {
      GbaRestElement recElement = mutElement.add(GbaRestElementType.ELEMENT);
      int elem = mutationRecord.getId().getElem();
      recElement.add(NUMMER).set(elem, GBAGroupElements.getByCat(cat, elem).getElem().getDescr());
      recElement.add(HUIDIG).set(mutationRecord.getValOrg(), mutationRecord.getValOrgDescr());
      recElement.add(NIEUW).set(mutationRecord.getValNew(), mutationRecord.getValNewDescr());
      recElement.add(GEWIJZIGD).set(mutationRecord.getChanged().intValue() > 0);
    }

    GbaRestElement deelZaken = gbaZaak.add(GbaRestElementType.DEELZAKEN);
    GbaRestElement deelZaak = deelZaken.add(GbaRestElementType.DEELZAAK);

    add(deelZaak, GbaRestElementType.BSN, zaak.getBurgerServiceNummer());
    add(deelZaak, GbaRestElementType.ANR, zaak.getAnummer());
  }
}
