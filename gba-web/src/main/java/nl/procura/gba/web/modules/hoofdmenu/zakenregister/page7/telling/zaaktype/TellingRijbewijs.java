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
import static nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType.RIJBEWIJS_UITGEREIKT;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hamcrest.Matchers;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;
import nl.procura.gba.web.services.zaken.rijbewijs.*;

import ch.lambdaj.group.Group;

public class TellingRijbewijs extends TellingTemplate {

  private static final String VERSTREKT     = "Verstrekt";
  private static final String GEACCORDEERD  = "geaccordeerd";
  private static final String GEANNULEERD   = "geannuleerd";
  private static final String GEREGISTREERD = "geregistreerd";

  public static TypeRijbewijs get(ZaakPeriode zaakPeriode, ZaakType zaakType, Group<Zaak> zakenGroup) {

    TypeRijbewijs rijbewijs = new TypeRijbewijs(zaakType, zakenGroup);

    Group<RijbewijsAanvraag> reisdocGroup1 = getRijbewijsByType(get(RijbewijsAanvraag.class, zakenGroup.findAll()));
    Group<RijbewijsAanvraag> reisdocGroup2 = getRijbewijsByStatus(
        get(RijbewijsAanvraag.class, zakenGroup.findAll()));
    Group<RijbewijsAanvraag> reisdocGroup3 = getRijbewijsByReden(
        get(RijbewijsAanvraag.class, zakenGroup.findAll()));

    for (Group<RijbewijsAanvraag> rijbewijsZaken : reisdocGroup1.subgroups()) {

      RijbewijsAanvraagSoort rKey = (RijbewijsAanvraagSoort) rijbewijsZaken.key();
      RijbewijsSoort soort = new RijbewijsSoort(rKey, rKey.getOms(), rijbewijsZaken);

      soort.getVerstrekt().setZaken(getVerstrekt(zaakPeriode, rijbewijsZaken.findAll()));
      soort.getGeregistreerd().setZaken(getGeregistreerd(rijbewijsZaken.findAll()));
      soort.getGeaccordeerd().setZaken(getGeaccordeerd(rijbewijsZaken.findAll()));
      soort.getGeannuleerd().setZaken(getGeannuleerd(rijbewijsZaken.findAll()));

      rijbewijs.getTypes().add(soort);
    }

    for (Group<RijbewijsAanvraag> rijbewijsZaken : reisdocGroup2.subgroups()) {

      RijbewijsStatusType rKey = (RijbewijsStatusType) rijbewijsZaken.key();
      RijbewijsSoort soort = new RijbewijsSoort(rKey, rKey.getOms(), rijbewijsZaken);
      soort.setCode(rKey.getCode());
      rijbewijs.getStatussen().add(soort);
    }

    for (Group<RijbewijsAanvraag> rijbewijsZaken : reisdocGroup3.subgroups()) {

      RijbewijsAanvraagReden rKey = (RijbewijsAanvraagReden) rijbewijsZaken.key();
      RijbewijsSoort soort = new RijbewijsSoort(rKey, rKey.getOms(), rijbewijsZaken);
      soort.setCode(rKey.getCode());
      rijbewijs.getRedenen().add(soort);
    }

    rijbewijs.getTypes().sort(new Sorter());
    rijbewijs.getStatussen().sort(new Sorter());

    rijbewijs.getGeregistreerd().setZaken(
        flatten(collect(rijbewijs.getTypes(), on(RijbewijsSoort.class).getGeregistreerd().getZaken())));
    rijbewijs.getGeaccordeerd().setZaken(
        flatten(collect(rijbewijs.getTypes(), on(RijbewijsSoort.class).getGeaccordeerd().getZaken())));
    rijbewijs.getGeannuleerd().setZaken(
        flatten(collect(rijbewijs.getTypes(), on(RijbewijsSoort.class).getGeannuleerd().getZaken())));
    rijbewijs.getVerstrekt().setZaken(
        flatten(collect(rijbewijs.getTypes(), on(RijbewijsSoort.class).getVerstrekt().getZaken())));

    return rijbewijs;
  }

  private static List<RijbewijsAanvraag> getGeaccordeerd(List<RijbewijsAanvraag> zaken) {
    return select(zaken,
        having(on(RijbewijsAanvraag.class).getStatussen().getStatus(RijbewijsStatusType.GEACCORDEERD),
            Matchers.notNullValue()));
  }

  private static List<RijbewijsAanvraag> getGeannuleerd(List<RijbewijsAanvraag> zaken) {
    return select(zaken,
        having(on(RijbewijsAanvraag.class).getStatussen().getStatus(RijbewijsStatusType.GEANNULEERD),
            Matchers.notNullValue()));
  }

  private static List<RijbewijsAanvraag> getGeregistreerd(List<RijbewijsAanvraag> zaken) {
    return select(zaken, having(on(RijbewijsAanvraag.class).getStatussen().getStatus().getStatus(),
        Matchers.equalTo(RijbewijsStatusType.GEREGISTREERD)));
  }

  private static Group<RijbewijsAanvraag> getRijbewijsByReden(List<RijbewijsAanvraag> zaken) {
    return group(zaken, by(on(RijbewijsAanvraag.class).getRedenAanvraag()));
  }

  private static Group<RijbewijsAanvraag> getRijbewijsByStatus(List<RijbewijsAanvraag> zaken) {
    return group(zaken, by(on(RijbewijsAanvraag.class).getStatussen().getStatus().getStatus()));
  }

  private static Group<RijbewijsAanvraag> getRijbewijsByType(List<RijbewijsAanvraag> zaken) {
    return group(zaken, by(on(RijbewijsAanvraag.class).getSoortAanvraag()));
  }

  private static List<RijbewijsAanvraag> getVerstrekt(ZaakPeriode zaakPeriode, List<RijbewijsAanvraag> zaken) {
    List<RijbewijsAanvraag> verstrekt = new ArrayList<>();
    for (RijbewijsAanvraag zaak : zaken) {
      RijbewijsAanvraagStatus status = zaak.getStatussen().getStatus(RIJBEWIJS_UITGEREIKT);
      if (status != null && zaakPeriode.isTussen(status.getDatumTijdRdw().getLongDate())) {
        verstrekt.add(zaak);
      }
    }
    return verstrekt;
  }

  public static class RijbewijsSoort<K, T> extends DashboardTellingType<K, T> {

    private long code = 0;

    private RijbewijsSoort(K key, String oms, Group<T> group) {
      super(key, oms, group);
    }

    public long getCode() {
      return code;
    }

    public void setCode(long code) {
      this.code = code;
    }

    public DashboardTellingType getGeaccordeerd() {
      return getTelling(GEACCORDEERD);
    }

    public DashboardTellingType getGeannuleerd() {
      return getTelling(GEANNULEERD);
    }

    public DashboardTellingType getGeregistreerd() {
      return getTelling(GEREGISTREERD);
    }

    public DashboardTellingType getVerstrekt() {
      return getTelling(VERSTREKT);
    }
  }

  public static class Sorter implements Comparator<RijbewijsSoort> {

    @Override
    public int compare(RijbewijsSoort o1, RijbewijsSoort o2) {

      if (pos(o1.getCode()) && pos(o2.getCode())) {
        return o1.getCode() < o2.getCode() ? -1 : 1;
      }

      return o1.getOms().compareTo(o2.getOms());
    }
  }

  public static class TypeRijbewijs extends DashboardTellingType<ZaakType, Zaak> {

    private final List<RijbewijsSoort> types     = new ArrayList<>();
    private final List<RijbewijsSoort> statussen = new ArrayList<>();
    private final List<RijbewijsSoort> redenen   = new ArrayList<>();

    public TypeRijbewijs() {
    }

    public TypeRijbewijs(ZaakType zaakType, Group group) {
      super(zaakType, group);
    }

    public DashboardTellingType getGeaccordeerd() {
      return getTelling(GEACCORDEERD);
    }

    public DashboardTellingType getGeannuleerd() {
      return getTelling(GEANNULEERD);
    }

    public DashboardTellingType getGeregistreerd() {
      return getTelling(GEREGISTREERD);
    }

    public List<RijbewijsSoort> getRedenen() {
      return redenen;
    }

    public List<RijbewijsSoort> getStatussen() {
      return statussen;
    }

    public List<RijbewijsSoort> getTypes() {
      return types;
    }

    public DashboardTellingType getVerstrekt() {
      return getTelling(VERSTREKT);
    }
  }
}
