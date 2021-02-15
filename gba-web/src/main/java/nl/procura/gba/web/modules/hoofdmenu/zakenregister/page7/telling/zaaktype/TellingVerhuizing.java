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

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;

import ch.lambdaj.group.Group;

public class TellingVerhuizing extends TellingTemplate {

  public static TypeVerhuizing get(ZaakType zaakType, Group<Zaak> zakenGroup) {

    TypeVerhuizing verhuizing = new TypeVerhuizing(zaakType, zakenGroup);

    Group<VerhuisAanvraag> verhuizingGroup = getVerhuizing(get(VerhuisAanvraag.class, zakenGroup.findAll()));

    for (Group<VerhuisAanvraag> verhuisZaken : verhuizingGroup.subgroups()) {

      VerhuisType verhuisType = (VerhuisType) verhuisZaken.key();

      verhuizing.getTypes().add(new VerhuizingSoort(verhuisType, verhuisZaken));
    }

    return verhuizing;
  }

  private static Group<VerhuisAanvraag> getVerhuizing(List<VerhuisAanvraag> zaken) {
    return group(zaken, by(on(VerhuisAanvraag.class).getTypeVerhuizing()));
  }

  public static class TypeVerhuizing extends DashboardTellingType<ZaakType, Zaak> {

    private final List<VerhuizingSoort> types = new ArrayList<>();

    public TypeVerhuizing() {
    }

    private TypeVerhuizing(ZaakType zaakType, Group<Zaak> group) {
      super(zaakType, group);
    }

    public List<VerhuizingSoort> getTypes() {
      return types;
    }
  }

  public static class VerhuizingSoort extends DashboardTellingType<VerhuisType, VerhuisAanvraag> {

    private final VerhuisType verhuisType;

    private VerhuizingSoort(VerhuisType verhuisType, Group<VerhuisAanvraag> group) {
      super(verhuisType, group);
      this.verhuisType = verhuisType;
    }

    public VerhuisType getVerhuisType() {
      return verhuisType;
    }
  }
}
