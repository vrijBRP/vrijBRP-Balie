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

package nl.procura.gba.web.modules.zaken.personmutations;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.gba.jpa.personen.db.PlMutRec;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class PersonListMutationsTravelDocUtils {

  public static Optional<InfoLayout> getInfoLayout(List<PlMutRec> mutations) {
    if (new ArrayList<>(mutations).stream()
        .anyMatch(m -> m.getChanged().intValue() > 0
            && GBACat.getByCode(m.getPlMut().getCat().intValue()).is(GBACat.PERSOON)
            && GBAElem.getByCode(m.getId().getElem()).is(BSN, GESLACHTSNAAM, VOORNAMEN,
                GEBOORTEDATUM, GESLACHTSAAND))) {
      return Optional.of(createLayout());
    }
    return Optional.empty();
  }

  public static Optional<InfoLayout> getInfoLayout(PersonListMutElems mutations) {
    if (mutations.stream().anyMatch(m -> m.isChanged()
        && m.getCat().is(GBACat.PERSOON)
        && m.getElem().isElement(BSN, GESLACHTSNAAM, VOORNAMEN, GEBOORTEDATUM, GESLACHTSAAND))) {
      return Optional.of(createLayout());
    }
    return Optional.empty();
  }

  private static InfoLayout createLayout() {
    return new InfoLayout(
        "Een nog geldig reisdocument vervalt van rechtswege als de persoonsgegevens van de " +
            "houder van het reisdocument wijzigen. ",
        ProcuraTheme.ICOON_24.WARNING,
        "De gegevens van de houderpagina van het " +
            "reisdocument komen dan niet meer overeen met de actuele gegevens " +
            "in categorie 01 (Persoon) op de persoonlijst. <br/>Dat kan het geval zijn na het actualiseren of " +
            "corrigeren van: <b>het burgerservicenummer, de geslachtsnaam, de voornamen, de geboortedatum, " +
            "het geslacht</b>.");
  }
}
