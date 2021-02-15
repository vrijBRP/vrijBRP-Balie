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

package nl.procura.gba.web.modules.bs.common.utils;

import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.gba.web.modules.bs.common.utils.DossierKennisgevingType.*;
import static nl.procura.gba.web.modules.bs.common.utils.DossierKennisgevingType.get;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;

public class DossierKennisgevingSituaties {

  public static boolean isKennisgeving(GbaApplication application, Dossier dossier) {

    List<KennisgevingSituatie> situaties = new ArrayList<>();

    ZaakType zaakType = dossier.getType();
    DossierKennisgevingType type1 = DossierKennisgevingType.ONBEKEND;
    DossierKennisgevingType type2 = DossierKennisgevingType.ONBEKEND;
    DossierKennisgevingType type3 = DossierKennisgevingType.ONBEKEND;
    Set<Long> gemeenteCodes = new HashSet<>();

    if (dossier.getType().is(GEBOORTE)) {

      DossierGeboorte zaakDossier = (DossierGeboorte) dossier.getZaakDossier();

      type2 = get(application, zaakDossier.getMoeder());
      DossierPersoon vaderErkenner = zaakDossier.getVaderErkenner();

      if (zaakDossier.getVragen().heeftErkenningBijGeboorte()) {
        vaderErkenner = zaakDossier.getErkenningBijGeboorte().getErkenner();
      }

      type3 = get(application, vaderErkenner);

      gemeenteCodes.add(getGemeente(zaakDossier.getMoeder()));
      gemeenteCodes.add(getGemeente(vaderErkenner));
    }

    if (dossier.getType().is(OVERLIJDEN_IN_GEMEENTE, LIJKVINDING)) {

      DossierOverlijden zaakDossier = (DossierOverlijden) dossier.getZaakDossier();

      DossierPersoon overledene = zaakDossier.getOverledene();

      type2 = get(application, overledene);

      for (DossierPersoon partner : zaakDossier.getHuidigePartners()) {

        type3 = get(application, partner);

        gemeenteCodes.add(getGemeente(partner));
      }

      gemeenteCodes.add(getGemeente(overledene));
    }

    if (dossier.getType().is(HUWELIJK_GPS_GEMEENTE)) {

      DossierHuwelijk zaakDossier = (DossierHuwelijk) dossier.getZaakDossier();

      type2 = get(application, zaakDossier.getPartner1());
      type3 = get(application, zaakDossier.getPartner2());

      gemeenteCodes.add(getGemeente(zaakDossier.getPartner1()));
      gemeenteCodes.add(getGemeente(zaakDossier.getPartner2()));
    }

    if (dossier.getType().is(OMZETTING_GPS)) {

      DossierOmzetting zaakDossier = (DossierOmzetting) dossier.getZaakDossier();

      type2 = get(application, zaakDossier.getPartner1());
      type3 = get(application, zaakDossier.getPartner2());

      gemeenteCodes.add(getGemeente(zaakDossier.getPartner1()));
      gemeenteCodes.add(getGemeente(zaakDossier.getPartner2()));
    }

    if (dossier.getType().is(ERKENNING)) {

      DossierErkenning zaakDossier = (DossierErkenning) dossier.getZaakDossier();

      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        type1 = get(application, kind);
        gemeenteCodes.add(getGemeente(kind));
      }

      type2 = get(application, zaakDossier.getMoeder());
      type3 = get(application, zaakDossier.getErkenner());

      gemeenteCodes.add(getGemeente(zaakDossier.getMoeder()));
      gemeenteCodes.add(getGemeente(zaakDossier.getErkenner()));
    }

    if (dossier.getType().is(NAAMSKEUZE)) {

      DossierNaamskeuze zaakDossier = (DossierNaamskeuze) dossier.getZaakDossier();

      for (DossierPersoon kind : zaakDossier.getKinderen()) {
        type1 = get(application, kind);
        gemeenteCodes.add(getGemeente(kind));
      }

      type2 = get(application, zaakDossier.getMoeder());
      type3 = get(application, zaakDossier.getPartner());

      gemeenteCodes.add(getGemeente(zaakDossier.getMoeder()));
      gemeenteCodes.add(getGemeente(zaakDossier.getPartner()));
    }

    boolean verschillendeGemeentes = gemeenteCodes.size() > 1;

    // Geboorte
    add(situaties, GEBOORTE, NVT, NVT, ANDERE_GEMEENTE, verschillendeGemeentes);
    add(situaties, GEBOORTE, NVT, RNI, RNI, true);

    // Overlijden / lijkvinding
    add(situaties, OVERLIJDEN_IN_GEMEENTE, NVT, NVT, ANDERE_GEMEENTE, verschillendeGemeentes);
    add(situaties, LIJKVINDING, NVT, NVT, ANDERE_GEMEENTE, verschillendeGemeentes);

    add(situaties, OVERLIJDEN_IN_GEMEENTE, NVT, RNI, RNI, true);
    add(situaties, LIJKVINDING, NVT, RNI, RNI, true);

    // Huwelijk
    add(situaties, HUWELIJK_GPS_GEMEENTE, NVT, RNI, RNI, true);

    // Erkenning
    add(situaties, ERKENNING, ANDERE_GEMEENTE, ANDERE_GEMEENTE, ANDERE_GEMEENTE, verschillendeGemeentes);
    add(situaties, ERKENNING, RNI, RNI, RNI, true);

    // Naamskeuze
    add(situaties, NAAMSKEUZE, ANDERE_GEMEENTE, ANDERE_GEMEENTE, ANDERE_GEMEENTE, verschillendeGemeentes);
    add(situaties, NAAMSKEUZE, RNI, RNI, RNI, true);

    for (KennisgevingSituatie situatie : situaties) {

      boolean isZaakType = situatie.getZaakType() == zaakType;
      boolean isType1 = situatie.getType1() == type1;
      boolean isType2 = situatie.getType2() == type2;
      boolean isType3 = situatie.getType3() == type3;

      if (isZaakType && (isType1 || isType2 || isType3)) {
        return situatie.isSturen();
      }
    }

    return false;
  }

  private static void add(List<KennisgevingSituatie> situaties, ZaakType zaakType, DossierKennisgevingType type1,
      DossierKennisgevingType type2, DossierKennisgevingType type3, boolean kennisgeving) {

    situaties.add(new KennisgevingSituatie(zaakType, type1, type2, type3, kennisgeving));
  }

  private static long getGemeente(DossierPersoon persoon) {
    return (persoon != null && persoon.getWoongemeente() != null) ? persoon.getWoongemeente().getLongValue() : -1;
  }

  public static class KennisgevingSituatie {

    private ZaakType                zaakType;
    private DossierKennisgevingType type1;
    private DossierKennisgevingType type2;
    private DossierKennisgevingType type3;
    private boolean                 sturen;

    public KennisgevingSituatie(ZaakType zaakType, DossierKennisgevingType type1, DossierKennisgevingType type2,
        DossierKennisgevingType type3, boolean sturen) {

      this.zaakType = zaakType;
      this.type1 = type1;
      this.type2 = type2;
      this.type3 = type3;
      this.sturen = sturen;
    }

    public DossierKennisgevingType getType1() {
      return type1;
    }

    public void setType1(DossierKennisgevingType type1) {
      this.type1 = type1;
    }

    public DossierKennisgevingType getType2() {
      return type2;
    }

    public void setType2(DossierKennisgevingType type2) {
      this.type2 = type2;
    }

    public DossierKennisgevingType getType3() {
      return type3;
    }

    public void setType3(DossierKennisgevingType type3) {
      this.type3 = type3;
    }

    public ZaakType getZaakType() {
      return zaakType;
    }

    public void setZaakType(ZaakType zaakType) {
      this.zaakType = zaakType;
    }

    public boolean isSturen() {
      return sturen;
    }

    public void setSturen(boolean sturen) {
      this.sturen = sturen;
    }
  }
}
