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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;

public class BsPersoonRequirement {

  private List<DossierPersoonType> persoonTypes             = new ArrayList<>();
  private List<String>             beanFields               = new ArrayList<>();
  private List<ZaakType>           zaakTypes                = new ArrayList<>();
  private boolean                  nationaliteitenVerplicht = false;
  private boolean                  definitiefNaControle     = false;

  public BsPersoonRequirement() {
  }

  public BsPersoonRequirement(DossierPersoonType... persoonTypes) {
    setPersoonTypes(persoonTypes);
  }

  public List<String> getBeanFields() {
    return beanFields;
  }

  public void setBeanFields(String... beanFields) {
    this.beanFields = new ArrayList<>(asList(beanFields));
  }

  public List<DossierPersoonType> getPersoonTypes() {
    return persoonTypes;
  }

  public void setPersoonTypes(DossierPersoonType... persoonTypes) {
    this.persoonTypes = new ArrayList<>(asList(persoonTypes));
  }

  public List<ZaakType> getZaakTypes() {
    return zaakTypes;
  }

  public void setZaakTypes(ZaakType... zaakTypes) {
    this.zaakTypes = new ArrayList<>(asList(zaakTypes));
  }

  public boolean isDefinitiefNaControle() {
    return definitiefNaControle;
  }

  public void setDefinitiefNaControle(boolean definitiefNaControle) {
    this.definitiefNaControle = definitiefNaControle;
  }

  public boolean isNationaliteitenVerplicht() {
    return nationaliteitenVerplicht;
  }

  public void setNationaliteitenVerplicht(boolean nationaliteitenVerplicht) {
    this.nationaliteitenVerplicht = nationaliteitenVerplicht;
  }
}
