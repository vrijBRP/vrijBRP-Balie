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

import static ch.lambdaj.Lambda.joinFrom;
import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.Collection;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class BsNatioUtils extends BsUtils {

  private static final String RIJKSWET_OP_NEDERLANDERSCHAP_ART_3_LID_1 = "017";
  private static final String RIJKSWET_OP_NEDERLANDERSCHAP_ART_3_LID_3 = "122";
  private static final String RIJKSWET_OP_NEDERLANDERSCHAP_ART_4_LID_2 = "164";
  private static final String VASTSTELLING_BEZIT_NATIONALITEIT         = "301";
  private static final String VASTSTELLING_BIJZONDER_NEDERLANDERSCHAP  = "310";
  private static final String VASTSTELLING_ONBEKENDE_NATIONALTEIT      = "311";
  private static final String VASTSTELLING_STAATLOOS                   = "312";
  private static final String ONBEKEND                                 = "000";

  /**
   * Controleer de nationaliteit
   */
  public static void checkNationaliteit(DossierNationaliteit nat) {

    FieldValue reden = getRedenVerkrijging(nat.getNationaliteit(), nat.getVerkrijgingType());

    if (reden != null) {
      nat.setRedenverkrijgingNederlanderschap(reden);
    }

    // Datum / tijd ontlening alsnog toevoegen
    if (!pos(nat.getDatumTijdOntlening().getLongTime())) {
      nat.setDatumTijdOntlening(new DateTime());
    }
  }

  public static FieldValue getGedeeldeNationaliteit(DossierNamenrecht afstammingsrecht) {
    List<DossierNationaliteit> list = new UniqueList<>();
    for (DossierNationaliteit nat1 : afstammingsrecht.getMoeder().getNationaliteiten()) {
      for (DossierNationaliteit nat2 : afstammingsrecht.getVaderErkenner().getNationaliteiten()) {
        if (nat1.getNationaliteit().equals(nat2.getNationaliteit())) {
          list.add(nat1);
        }
      }
    }

    if (list.size() == 1) {

      // Max. één gemeenschappelijke nationaliteit.

      return list.get(0).getNationaliteit();
    }

    return new FieldValue();
  }

  public static DossierNationaliteit getNationaliteit(DossierNationaliteit nationaliteit,
      Collection<DossierNationaliteit> nationaliteiten) {

    for (DossierNationaliteit bestaandeNatio : nationaliteiten) {

      Object o1 = bestaandeNatio.getNationaliteit().getValue();
      Object o2 = nationaliteit.getNationaliteit().getValue();

      if (o1.equals(o2)) {
        return bestaandeNatio;
      }
    }

    return null;
  }

  /**
   * Geeft de belangrijkste nationaliteit terug
   */
  public static DossierNationaliteit getNationaliteit(DossierNationaliteiten dossier) {

    if (dossier.getNationaliteiten().size() > 0) {
      for (DossierNationaliteit dn : dossier.getNationaliteiten()) {
        if (Landelijk.isNederland(dn)) {
          return dn;
        }
      }

      return dossier.getNationaliteiten().iterator().next();
    }

    return new DossierNationaliteit();
  }

  public static String getNationaliteitOmschrijving(Collection<? extends DossierNationaliteiten> dossiers) {
    return dossiers.stream().findFirst().map(BsNatioUtils::getNationaliteitOmschrijving).orElse("");
  }

  /**
   * Geeft de omschrijvingen terug van de nationaliteiten
   */
  public static String getNationaliteitOmschrijving(DossierNationaliteiten dossier) {
    if (dossier.getNationaliteiten().size() > 0) {
      return joinFrom(dossier.getNationaliteiten()).getNationaliteitOmschrijving();
    }

    return "";
  }

  public static boolean heeftAsiel(DossierPersoon persoon) {
    return asList(26, 27).contains(aval(persoon.getVerblijfstitel().getValue()));
  }

  public static boolean heeftGedeeldeNationaliteit(DossierNamenrecht afstammingsrecht) {
    return pos(getGedeeldeNationaliteit(afstammingsrecht).getValue());
  }

  public static boolean heeftNationaliteiten(DossierNationaliteiten dossier) {
    return dossier.getNationaliteiten().size() > 0;
  }

  public static boolean heeftNederlandseNationaliteit(Collection<? extends DossierNationaliteiten> dossiers) {
    for (DossierNationaliteiten dossier : dossiers) {
      if (heeftNederlandseNationaliteit(dossier)) {
        return true;
      }
    }

    return false;
  }

  public static boolean heeftNederlandseNationaliteit(DossierNationaliteiten dossier) {
    for (DossierNationaliteit nationaliteit : dossier.getNationaliteiten()) {
      if (Landelijk.isNederland(nationaliteit)) {
        return true;
      }
    }

    return false;
  }

  public static FieldValue getRedenVerkrijging(FieldValue nat, DossierNationaliteitDatumVanafType type) {

    if (nat != null && type != null) {
      if (Landelijk.isNederland(nat)) {
        switch (type) {
          case GEBOORTE_DATUM:
            return GbaTables.REDEN_NATIO.get(RIJKSWET_OP_NEDERLANDERSCHAP_ART_3_LID_1);

          case GEBOORTE_DATUM_DERDE:
            return GbaTables.REDEN_NATIO.get(RIJKSWET_OP_NEDERLANDERSCHAP_ART_3_LID_3);

          case ERKENNING_AANGIFTE_DATUM:
          case ERKENNINGS_DATUM:
            return GbaTables.REDEN_NATIO.get(RIJKSWET_OP_NEDERLANDERSCHAP_ART_4_LID_2);

          case ANDERS:
            break;

          case ONBEKEND:
          default:
            return GbaTables.REDEN_NATIO.get(ONBEKEND);
        }
      } else if (Landelijk.isNationaliteit(nat, Landelijk.ONBEKEND)) {
        return GbaTables.REDEN_NATIO.get(VASTSTELLING_ONBEKENDE_NATIONALTEIT);

      } else if (Landelijk.isNationaliteit(nat, Landelijk.NATIONALITEIT_STAATLOOS)) {
        return GbaTables.REDEN_NATIO.get(VASTSTELLING_STAATLOOS);

      } else if (Landelijk.isNationaliteit(nat, Landelijk.NATIONALITEIT_BEHANDELD_ALS_NEDERLANDER)) {
        return GbaTables.REDEN_NATIO.get(VASTSTELLING_BIJZONDER_NEDERLANDERSCHAP);

      } else {
        return GbaTables.REDEN_NATIO.get(VASTSTELLING_BEZIT_NATIONALITEIT);
      }
    }

    return GbaTables.REDEN_NATIO.get(ONBEKEND);
  }
}
