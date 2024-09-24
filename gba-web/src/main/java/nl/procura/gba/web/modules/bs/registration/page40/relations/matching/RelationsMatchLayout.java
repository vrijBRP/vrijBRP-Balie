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

package nl.procura.gba.web.modules.bs.registration.page40.relations.matching;

import java.text.MessageFormat;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class RelationsMatchLayout extends VLayout {

  private final RelationsMatchForm form;

  public RelationsMatchLayout(RelationType type, DossierPersoon person, BasePLExt plRelatedPerson) {
    form = new RelationsMatchForm(type, person, plRelatedPerson);
    addComponent(getInfoLayout(type, plRelatedPerson));
    addComponent(form);
  }

  public RelationsMatchInfo getMatchingInfo() {
    return form.getBean().getMatchingRelative();
  }

  private InfoLayout getInfoLayout(RelationType type, BasePLExt plRelatedPerson) {
    String line1 = "";
    String line2 = "";

    DossierPersoon relatedPerson = BsPersoonUtils.kopieDossierPersoon(plRelatedPerson, new DossierPersoon());
    String name = BsDossierNaamgebruikUtils.getNormalizedName(relatedPerson);

    if (!form.getRelatives().isEmpty()) {
      if (form.getMatch().isPresent()) {
        line2 = "<br>Er zijn <u>overeenkomstige gegevens</u> gevonden. Controleer of dit correct is.";
      } else {
        line2 = "<br><br>Er is echter geen sprake van een match." +
            "<br><ul><li>Dit kan terecht zijn omdat het een andere persoon betreft.</li>" +
            "<li>Dit kan terecht zijn omdat u probeert een verkeerde relatie te leggen.</li>" +
            "<li>Dit kan onterecht zijn volgens u en in dat geval zullen " +
            "de gegevens eerst gecorrigeerd moeten worden.</li></ul>";
      }
    }

    switch (type) {
      case PARENT_OF:
        add(new Fieldset(MessageFormat.format("Koppelen met bestaande oudersgegevens van {0}", name)));
        if (form.getRelatives().isEmpty()) {
          line1 = "Op de persoonlijst van {0} staan geen ouders.";
        } else {
          line1 = "Op de persoonlijst van {0} staan al ouders." + line2;
        }
        break;
      case PARTNER_OF:
        add(new Fieldset(MessageFormat.format("Koppelen met bestaande partnergegevens van {0}", name)));
        if (form.getRelatives().isEmpty()) {
          line1 = "Op de persoonlijst van {0} staan geen partners.";
        } else {
          line1 = "Op de persoonlijst van {0} staan al huwelijk / GPS gegevens." + line2;
        }
        break;
      case CHILD_OF:
        add(new Fieldset(MessageFormat.format("Koppelen met bestaande kindgegevens van {0}", name)));
        if (form.getRelatives().isEmpty()) {
          line1 = "Op de persoonlijst van {0} staan geen kinderen.";
        } else {
          line1 = "Op de persoonlijst van {0} staan al kindgegevens." + line2;
        }
        break;
      case ONLY_1_LEGAL_PARENT:
      case NO_LEGAL_PARENTS:
      case UNKNOWN:
        break;
      default:
        throw new ProException(ProExceptionSeverity.ERROR, "Type {0} is not implemented", type);
    }

    return new InfoLayout(MessageFormat.format(line1, name));
  }

  public void commit() {
    form.commit();
  }
}
