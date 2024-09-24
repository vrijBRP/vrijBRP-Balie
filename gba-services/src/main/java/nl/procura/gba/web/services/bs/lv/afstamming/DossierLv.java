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

package nl.procura.gba.web.services.bs.lv.afstamming;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.ADOPTIEFOUDER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.OUDER;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossLv;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.lv.LvType;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DossierLv extends DossLv implements ZaakDossier {

  private Dossier dossier = null;

  public DossierLv() {
    super();
    setDossier(new Dossier(ZaakType.LV, this));
    getDossier().toevoegenPersoon(KIND);
  }

  @Override
  public void beforeSave() {
    setCDossLv(getDossier().getCode());
  }

  public List<DossierPersoon> getBetrokkenen() {
    List<DossierPersoon> personen = new ArrayList<>();
    personen.add(getKind());
    personen.addAll(getOuders());
    personen.addAll(getAdoptiefouders());
    return personen;
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getKind().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getKind().setAnummer(anummer);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getKind().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getKind().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDossLv();
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  public boolean isVolledig() {
    return true;
  }

  public LvType getSoort() {
    return LvType.get(getSoortLv().intValue());
  }

  public List<DossierLvVerbetering> getVerbeteringen() {
    List<DossierLvVerbetering> list = new ArrayList<>();
    if (StringUtils.isNotBlank(getVerbeterd())) {
      for (String lines : getVerbeterd().split("\n")) {
        String[] values = lines.split("=");
        if (values.length == 2) {
          list.add(new DossierLvVerbetering(values[0].trim(), values[1].trim()));
        }
      }
    }
    return list;
  }

  public void setVerbeteringen(List<DossierLvVerbetering> verbeteringen) {
    StringBuilder sb = new StringBuilder();
    for (DossierLvVerbetering verbetering : verbeteringen) {
      sb.append(verbetering.getOmschrijving().trim())
          .append("=")
          .append(verbetering.getWaarde().trim())
          .append("\n");
    }
    setVerbeterd(sb.toString());
  }

  public DossierPersoon getKind() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(KIND));
  }

  public List<DossierPersoon> getOuders() {
    return getDossier().getPersonen(OUDER);
  }

  public List<DossierPersoon> getAdoptiefouders() {
    return getDossier().getPersonen(ADOPTIEFOUDER);
  }

  public DossierPersoon getBetreftOuderPersoon() {
    if (getBetreftOuderP() != null && getBetreftOuderP().longValue() > 0) {
      return getOuders().stream()
          .filter(persoon -> Objects.equals(persoon.getCode(), getBetreftOuderP().longValue()))
          .findFirst()
          .orElse(new DossierPersoon());
    }
    return new DossierPersoon();
  }
}
