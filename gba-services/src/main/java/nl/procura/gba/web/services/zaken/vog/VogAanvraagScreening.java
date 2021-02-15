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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VogAanvraagScreening implements Serializable {

  private static final long serialVersionUID = 2314244318899701837L;

  private VogProfiel       profiel             = new VogProfiel();
  private List<VogFunctie> functiegebieden     = new ArrayList<>();
  private String           omstandighedenTekst = "";

  private VogAanvraag aanvraag;

  public VogAanvraagScreening(VogAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public VogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public List<VogFunctie> getFunctiegebieden() {
    Collections.sort(functiegebieden);
    return functiegebieden;
  }

  public void setFunctiegebieden(List<VogFunctie> functiegebieden) {
    this.functiegebieden = functiegebieden;
  }

  public String getFunctiegebiedenSamenvatting() {

    StringBuilder sb = new StringBuilder();
    for (VogFunctie fg : getFunctiegebieden()) {
      sb.append(fg.getVogFuncTab());
      sb.append(", ");
    }

    return trim(sb.toString());
  }

  public String getOmstandighedenTekst() {
    return omstandighedenTekst;
  }

  public void setOmstandighedenTekst(String omstandighedenTekst) {

    this.omstandighedenTekst = omstandighedenTekst;
    getAanvraag().setOmstandigTekst(omstandighedenTekst);
  }

  public VogProfiel getProfiel() {
    return profiel;
  }

  public void setProfiel(VogProfiel profiel) {
    this.profiel = profiel;
  }

  public boolean isGebruikFunctiegebied() {

    for (VogFunctie fg : getFunctiegebieden()) {
      if (pos(fg.getVogFuncTab())) {
        return true;
      }
    }

    return false;
  }

  public boolean isGebruikProfiel() {
    return (getProfiel() != null) && pos(getProfiel().getVogProfTab());
  }

  public boolean isOmstandigheden() {
    return fil(getOmstandighedenTekst());
  }
}
