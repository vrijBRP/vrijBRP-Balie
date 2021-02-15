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

package nl.procura.gba.web.services.bs.algemeen.persoon;

import java.math.BigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierPersoonVerbintenis {

  private final DossierPersoon persoonImpl;

  private final Sluiting   sluiting   = new Sluiting();
  private final Ontbinding ontbinding = new Ontbinding();

  public DossierPersoonVerbintenis(DossierPersoon persoonImpl) {
    this.persoonImpl = persoonImpl;
  }

  public Ontbinding getOntbinding() {
    return ontbinding;
  }

  public Sluiting getSluiting() {
    return sluiting;
  }

  public SoortVerbintenis getSoort() {
    return SoortVerbintenis.get(persoonImpl.getSrtHuw());
  }

  public void setSoort(SoortVerbintenis soort) {
    persoonImpl.setSrtHuw(soort.getCode());
  }

  public class Ontbinding {

    private FieldValue plaats = new FieldValue();
    private FieldValue land   = new FieldValue();
    private FieldValue reden  = new FieldValue();

    public DateTime getDatum() {
      return new DateTime(persoonImpl.getdHuwOntbDatum());
    }

    public void setDatum(DateTime datum) {
      persoonImpl.setdHuwOntbDatum(BigDecimal.valueOf(datum.getLongDate()));
    }

    public FieldValue getLand() {
      return land;
    }

    public void setLand(FieldValue land) {
      this.land = FieldValue.from(land);
      persoonImpl.setcHuwOntbLand(this.land.getBigDecimalValue());
    }

    public FieldValue getPlaats() {
      return plaats;
    }

    public void setPlaats(FieldValue plaats) {
      this.plaats = FieldValue.from(plaats);
      persoonImpl.setcHuwOntbPlaats(this.plaats.getBigDecimalValue());
      persoonImpl.setHuwOntbPlaats(this.plaats.getDescription());
    }

    public FieldValue getReden() {
      return reden;
    }

    public void setReden(FieldValue reden) {
      this.reden = FieldValue.from(reden);
      persoonImpl.setHuwOntbRdn(this.reden.getStringValue());
    }
  }

  public class Sluiting {

    private FieldValue plaats = new FieldValue();
    private FieldValue land   = new FieldValue();

    public DateTime getDatum() {
      return new DateTime(persoonImpl.getdHuwSluitDatum());
    }

    public void setDatum(DateTime datum) {
      persoonImpl.setdHuwSluitDatum(BigDecimal.valueOf(datum.getLongDate()));
    }

    public FieldValue getLand() {
      return land;
    }

    public void setLand(FieldValue land) {
      this.land = FieldValue.from(land);
      persoonImpl.setcHuwSluitLand(this.land.getBigDecimalValue());
    }

    public FieldValue getPlaats() {
      return plaats;
    }

    public void setPlaats(FieldValue plaats) {
      this.plaats = FieldValue.from(plaats);
      persoonImpl.setcHuwSluitPlaats(this.plaats.getBigDecimalValue());
      persoonImpl.setHuwSluitPlaats(this.plaats.getDescription());
    }
  }
}
