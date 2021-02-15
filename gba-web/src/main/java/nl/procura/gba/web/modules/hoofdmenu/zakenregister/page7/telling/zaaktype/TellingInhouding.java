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
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;

import ch.lambdaj.group.Group;

public class TellingInhouding extends TellingTemplate {

  public static TypeInhoudingVermissing get(ZaakType key, Group<Zaak> zakenGroup) {

    TypeInhoudingVermissing type = new TypeInhoudingVermissing(key, zakenGroup);
    Group<ReisdocumentAanvraag> groups = getInhoudingType(get(ReisdocumentAanvraag.class, zakenGroup.findAll()));

    for (Group<ReisdocumentAanvraag> group : groups.subgroups()) {

      InhoudingType inhoudingTypeKey = (InhoudingType) group.key();
      type.getInhoudingTypes().add(new DashboardTellingType(inhoudingTypeKey, group));
    }

    return type;
  }

  private static Group<ReisdocumentAanvraag> getInhoudingType(List<ReisdocumentAanvraag> zaken) {
    return group(zaken, by(on(DocumentInhouding.class).getInhoudingType()));
  }

  public static class TypeInhoudingVermissing extends DashboardTellingType<ZaakType, Zaak> {

    private List<DashboardTellingType<InhoudingType, Zaak>> inhoudingTypes = new ArrayList<>();

    public TypeInhoudingVermissing() {
    }

    public TypeInhoudingVermissing(ZaakType key, Group group) {
      super(key, group.findAll());
    }

    public List<DashboardTellingType<InhoudingType, Zaak>> getInhoudingTypes() {
      return inhoudingTypes;
    }

    public void setInhoudingTypes(List<DashboardTellingType<InhoudingType, Zaak>> inhoudingTypes) {
      this.inhoudingTypes = inhoudingTypes;
    }
  }
}
