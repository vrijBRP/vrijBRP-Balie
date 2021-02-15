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
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;

import ch.lambdaj.group.Group;

public class TellingReisdocument extends TellingTemplate {

  private static final String VERSTREKT = "Verstrekt";
  private static final String GRATIS    = "Gratis";

  public static TypeReisdocument get(ZaakPeriode zaakPeriode, ZaakType zaakType, Group<Zaak> zakenGroup) {

    TypeReisdocument reisdocument = new TypeReisdocument(zaakType, zakenGroup);

    Group<ReisdocumentAanvraag> reisdocGroup = getReisdocument(
        get(ReisdocumentAanvraag.class, zakenGroup.findAll()));

    for (Group<ReisdocumentAanvraag> reisdocZaken : reisdocGroup.subgroups()) {

      ReisdocumentType rKey = (ReisdocumentType) reisdocZaken.key();
      ReisdocumentSoort soort = new ReisdocumentSoort(rKey, rKey.getOms(), reisdocZaken.findAll());

      soort.getGratis().setZaken(getGratis(reisdocZaken.findAll()));
      soort.getVerstrekt().setZaken(getVerstrekt(zaakPeriode, reisdocZaken.findAll()));

      reisdocument.getTypes().add(soort);
    }

    reisdocument.getGratis().setZaken(
        flatten(collect(reisdocument.getTypes(), on(ReisdocumentSoort.class).getGratis().getZaken())));
    reisdocument.getVerstrekt().setZaken(
        flatten(collect(reisdocument.getTypes(), on(ReisdocumentSoort.class).getVerstrekt().getZaken())));

    return reisdocument;
  }

  private static List<ReisdocumentAanvraag> getGratis(List<ReisdocumentAanvraag> zaken) {
    return select(zaken, having(on(ReisdocumentAanvraag.class).isGratis()));
  }

  private static Group<ReisdocumentAanvraag> getReisdocument(List<ReisdocumentAanvraag> zaken) {
    return group(zaken, by(on(ReisdocumentAanvraag.class).getReisdocumentType()));
  }

  private static List<ReisdocumentAanvraag> getVerstrekt(ZaakPeriode zaakPeriode, List<ReisdocumentAanvraag> zaken) {
    List<ReisdocumentAanvraag> verstrekt = new ArrayList<>();
    for (ReisdocumentAanvraag zaak : zaken) {
      if (zaakPeriode.isTussen(zaak.getDatumVerstrek().getLongDate())) {
        verstrekt.add(zaak);
      }
    }
    return verstrekt;
  }

  public static class ReisdocumentSoort<K, T> extends DashboardTellingType<K, T> {

    public ReisdocumentSoort() {
    }

    private ReisdocumentSoort(K key, String oms, List<T> zaken) {
      super(key, oms, zaken);
    }

    public DashboardTellingType getGratis() {
      return getTelling(GRATIS);
    }

    public DashboardTellingType getVerstrekt() {
      return getTelling(VERSTREKT);
    }
  }

  public static class TypeReisdocument extends DashboardTellingType {

    private List<DashboardTellingType<ReisdocumentSoort, Zaak>> types = new ArrayList<>();

    public TypeReisdocument() {
    }

    public TypeReisdocument(ZaakType key, Group<Zaak> group) {
      super(key, key.getOms(), group);
    }

    public DashboardTellingType getGratis() {
      return getTelling(GRATIS);
    }

    public List<DashboardTellingType<ReisdocumentSoort, Zaak>> getTypes() {
      return types;
    }

    public void setTypes(List<DashboardTellingType<ReisdocumentSoort, Zaak>> types) {
      this.types = types;
    }

    public DashboardTellingType getVerstrekt() {
      return getTelling(VERSTREKT);
    }
  }
}
