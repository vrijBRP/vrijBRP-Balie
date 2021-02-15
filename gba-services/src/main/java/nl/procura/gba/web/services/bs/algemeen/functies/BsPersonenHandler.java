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

package nl.procura.gba.web.services.bs.algemeen.functies;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.jpa.personen.db.DossPerEntity;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersonen;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonComparators;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.java.reflection.ReflectionUtil;

public class BsPersonenHandler {

  private final List<DossierPersoon> personen = new UniqueList<>();

  /**
   * Voeg personen toe
   */
  public List<DossierPersoon> addPersonen(DossierPersonen dossier, List<DossierPersoon> personen) {
    for (DossierPersoon persoon : personen) {
      addPersoon(dossier, persoon);
    }
    return personen;
  }

  /**
   * Voeg persoon toe
   */
  public DossierPersoon addPersoon(DossierPersonen dossier, DossierPersoon persoon) {
    if (!heeftPersoon(persoon)) {
      persoon.koppelenAan(dossier);
      personen.add(persoon);
    }

    return persoon;
  }

  /**
   * Alle personen dus ook de personen van de personen
   */
  public List<DossierPersoon> getAllePersonen() {
    List<DossierPersoon> personen = new ArrayList<>();
    for (DossierPersoon persoon : getPersonen()) {
      personen.add(persoon);
      personen.addAll(persoon.getPersonen());
    }
    return personen;
  }

  public List<DossierPersoon> getPersonen() {
    personen.sort(DossierPersoonComparators.getDefault());
    return personen;
  }

  public List<DossierPersoon> getPersonen(DossierPersoonType... types) {
    List<DossierPersoon> dps = new ArrayList<>();
    for (DossierPersoonType type : types) {
      for (DossierPersoon persoon : getPersonen()) {
        if (persoon.getDossierPersoonType().is(type)) {
          dps.add(persoon);
        }
      }
    }

    return dps;
  }

  /**
   * Eerste persoon van een bepaald type
   */
  public DossierPersoon getPersoon(DossierPersoon persoon) {
    for (DossierPersoon dossierPersoon : personen) {
      if (dossierPersoon.isPersoon(persoon)) {
        return dossierPersoon;
      }
    }

    return null;
  }

  /**
   * Eerste persoon van een bepaald type
   */
  public DossierPersoon getPersoon(DossierPersoonType... types) {
    return getPersoon(DossierPersoonFilter.filter(0, null, types));
  }

  public DossierPersoon getPersoon(int index, DossierPersoonType... types) {
    return getPersoon(DossierPersoonFilter.filter(index, null, types));
  }

  /**
   * Get person by type, gender and index
   */
  public DossierPersoon getPersoon(int index, Geslacht geslacht, DossierPersoonType... types) {
    return getPersoon(DossierPersoonFilter.filter(index, geslacht, types));
  }

  public DossierPersoon getPersoon(DossierPersoonFilter filter) {
    List<DossierPersoon> list = personen
        .stream()
        .filter(p -> p.getDossierPersoonType().is(filter.getTypes()))
        .sorted(DossierPersoonComparators.getByIndex())
        .collect(Collectors.toList());

    List<DossierPersoon> genderList = list
        .stream()
        .filter(p -> p.getGeslacht() == filter.getGender())
        .collect(Collectors.toList());

    if (list.size() == 0) {
      return null;
    }

    /**
     * Index only relevant if there are multiple people with the same gender
     * or if gender is null
     */
    if (filter.getGender() != null) {
      if (list.size() == 1 && genderList.size() == 0) { // One person and not correct gender
        return null;

      } else if (genderList.size() == 1) { // One person with specific gender
        return getPersoonByIndex(genderList, 0);

      } else if (genderList.size() > 1) { // Multiple people with that gender. Choose specific index
        return getPersoonByIndex(genderList, filter.getIndex());
      }
    }

    return getPersoonByIndex(list, filter.getIndex());
  }

  public String getSamenvatting(DossierPersoonType... types) {
    StringBuilder sb = new StringBuilder();
    for (DossierPersoon g : getPersonen(types)) {
      if (g.isVolledig()) {
        sb.append(g.getNaam().getPred_adel_voorv_gesl_voorn() + "; ");
      }
    }

    return trim(sb.toString()).replaceAll(";$", "");
  }

  public boolean heeftPersoon(DossierPersoon persoon) {

    for (DossierPersoon dossierPersoon : personen) {
      if (dossierPersoon.isPersoon(persoon)) {
        return true;
      }
    }
    return false;
  }

  public boolean isPersoon(DossierPersoon persoon) {
    for (DossierPersoon bestaandPersoon : getPersonen()) {
      if (bestaandPersoon.isPersoon(persoon)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Zijn de personen hetzelfde
   */
  public boolean isPersoon(DossierPersoon p1, DossierPersoon p2) {

    String v1 = astr(p1.getVoornaam()).trim();
    String v2 = astr(p2.getVoornaam()).trim();

    String g1 = astr(p1.getGeslachtsnaam());
    String g2 = astr(p2.getGeslachtsnaam());

    String d1 = astr(p1.getDatumGeboorte());
    String d2 = astr(p2.getDatumGeboorte());

    DossierPersoonType t1 = p1.getDossierPersoonType();
    DossierPersoonType t2 = p2.getDossierPersoonType();

    String u1 = trim(p1.getUid());
    String u2 = trim(p2.getUid());

    boolean isUid = fil(u1) && fil(u2) && eq(u1, u2);
    boolean isGesl = fil(g1) && fil(g2) && eq(g1, g2);
    boolean isVoorn = eq(v1, v2);
    boolean isGeb = d1.equals(d2);
    boolean isType = t1.is(t2);

    return isUid || (isGesl && isVoorn && isGeb && isType);
  }

  /**
   * Verwijder bepaalde persoon van een DossPerEntity
   */
  public void verwijderPersoon(DossPerEntity dossPers, DossierPersoon persoon) {
    // Verwijder de subpersonen
    for (DossPer dp : dossPers.getDossPers()) {
      verwijderPersoon(dp, persoon);
    }
    dossPers.getDossPers().remove(ReflectionUtil.deepCopyBean(DossPer.class, persoon));
    getPersonen().remove(persoon);
  }

  private DossierPersoon getPersoonByIndex(List<DossierPersoon> personen, int index) {
    return (index < personen.size()) ? personen.get(index) : null;
  }
}
