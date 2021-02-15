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

package nl.procura.gba.web.services.zaken.algemeen.contact;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;

import lombok.Data;

@Data
public class ZaakContactpersoon {

  private final ZaakContactpersoonType type;
  private final String                 naam;
  private List<PlContactgegeven>       contactgegevens = new ArrayList<>();

  public ZaakContactpersoon(ZaakContactpersoonType type, BasePLExt basisPersoon) {
    this(type, basisPersoon.getPersoon().getNaam().getPredEerstevoornAdelVoorvGesl());
  }

  public ZaakContactpersoon(ZaakContactpersoonType type, String naam) {
    this.type = type;
    this.naam = naam;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ZaakContactpersoon other = (ZaakContactpersoon) obj;
    return new EqualsBuilder().append(type + naam, other.getType() + other.getNaam()).build();
  }

  public String getGegeven(String type) {
    for (PlContactgegeven gegeven : getContactgegevens()) {
      if (gegeven.getContactgegeven().getGegeven().equals(type)) {
        return gegeven.getAant();
      }
    }
    return "";
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(type + naam).build();
  }
}
