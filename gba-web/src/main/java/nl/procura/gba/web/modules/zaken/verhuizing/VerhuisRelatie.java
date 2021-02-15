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

package nl.procura.gba.web.modules.zaken.verhuizing;

import static nl.procura.gba.web.services.gba.presentievraag.PresentieVraagResultaatType.OVERGESLAGEN;
import static nl.procura.gba.web.services.gba.presentievraag.PresentieVraagResultaatType.RESULTAAT_GEVONDEN;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.verhuizing.page10.AangifteSelect;
import nl.procura.gba.web.modules.zaken.verhuizing.page10.InstellingAangifteSelect;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.presentievraag.PresentieVraagResultaatType;

public class VerhuisRelatie {

  private Relatie                     relatie;
  private GbaNativeSelect             select;
  private boolean                     hoofdInstelling    = false;
  private boolean                     ambtshalve         = false;
  private PresentieVraagResultaatType presentievraagType = PresentieVraagResultaatType.NOG_NIET_UITGEVOERD;

  public VerhuisRelatie(Relatie relatie) {

    setRelatie(relatie);
    setSelect(new AangifteSelect(relatie.getRelatieType().getAangifte()));
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof VerhuisRelatie) {

      String nr1 = relatie.getPl().getPersoon().getNummer().getVal();
      String nr2 = ((VerhuisRelatie) obj).getRelatie().getPl().getPersoon().getNummer().getVal();

      return nr1.equals(nr2);
    }

    return super.equals(obj);
  }

  public PresentieVraagResultaatType getPresentievraagType() {
    return presentievraagType;
  }

  public void setPresentievraagType(PresentieVraagResultaatType presentievraagType) {
    this.presentievraagType = presentievraagType;
  }

  public Relatie getRelatie() {
    return relatie;
  }

  public void setRelatie(Relatie relatie) {
    this.relatie = relatie;
  }

  public GbaNativeSelect getSelect() {

    InstellingAangifteSelect hs = new InstellingAangifteSelect(AangifteSoort.HOOFDINSTELLING);
    InstellingAangifteSelect as = new InstellingAangifteSelect(AangifteSoort.AMBTSHALVE);
    return isHoofdInstelling() ? hs : (isAmbtshalve() ? as : select);
  }

  public void setSelect(GbaNativeSelect select) {
    this.select = select;
  }

  @Override
  public int hashCode() {

    String nr = relatie.getPl().getPersoon().getNummer().getVal();

    final int prime = 31;
    int result = 1;
    result = prime * result + ((nr == null) ? 0 : nr.hashCode());

    return result;
  }

  public boolean isAmbtshalve() {
    return ambtshalve;
  }

  public void setAmbtshalve(boolean ambtshalve) {
    this.ambtshalve = ambtshalve;
  }

  public boolean isHoofdInstelling() {
    return hoofdInstelling;
  }

  public void setHoofdInstelling(boolean hoofdInstelling) {
    this.hoofdInstelling = hoofdInstelling;
  }

  public boolean isPresentievraag() {
    return presentievraagType != null && presentievraagType.is(RESULTAAT_GEVONDEN, OVERGESLAGEN);
  }
}
