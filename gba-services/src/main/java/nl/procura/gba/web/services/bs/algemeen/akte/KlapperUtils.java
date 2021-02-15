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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort.AKTE_GPS;
import static nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort.AKTE_HUWELIJK;
import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class KlapperUtils {

  public static List<DossierAkte> appenderen(List<DossierAkte> aktes, KlapperVolgordeType volgordeType) {

    List<DossierAkte> copyAktes = new ArrayList<>(aktes);

    for (DossierAkte akte : aktes) {

      DossierAkte akteImpl = akte;

      if (akte.getAkteRegistersoort().is(AKTE_GPS, AKTE_HUWELIJK)) {

        DossierAkte nieuweAkte = new DossierAkte();
        nieuweAkte.setdIn(akteImpl.getdIn());
        nieuweAkte.setJaar(akteImpl.getJaar());
        nieuweAkte.setRegisterdeel(akteImpl.getRegisterdeel());
        nieuweAkte.setRegistersoort(akteImpl.getRegistersoort());
        nieuweAkte.setVnr(akteImpl.getVnr());
        nieuweAkte.setDoss(akteImpl.getDoss());

        nieuweAkte.getAktePersoon().set(akte.getAktePartner());
        nieuweAkte.getAktePartner().set(akte.getAktePersoon());

        copyAktes.add(nieuweAkte);
      }
    }

    return sorteren(copyAktes, volgordeType);
  }

  /**
   * Geef de ontbrekende aktes terug
   */
  public static List<DossierAkte> getGaten(List<DossierAkte> aktes) {

    List<DossierAkte> copyAktes = new ArrayList<>(aktes);

    sorteren(copyAktes, KlapperVolgordeType.VNR_OPLOPEND);

    List<DossierAkte> ontbrekende = new ArrayList<>();

    for (int i = 0; i < copyAktes.size(); i++) {

      DossierAkte akteHuidig = copyAktes.get(i);

      if ((i + 1) < copyAktes.size()) {

        DossierAkte akteVolgende = copyAktes.get(i + 1);

        boolean isJaar = akteHuidig.getJaar().equals(akteVolgende.getJaar());
        boolean isSoort = akteHuidig.getRegistersoort().equals(akteVolgende.getRegistersoort());
        boolean isDeel = eq(akteHuidig.getRegisterdeel(), akteVolgende.getRegisterdeel());

        if (isSoort && isDeel && isJaar) {

          long vnrHuidig = akteHuidig.getVnr().longValue();
          long vnrVolgende = akteVolgende.getVnr().longValue();
          long verschil = vnrVolgende - vnrHuidig;

          if (verschil > 1) {
            long nextIndex = vnrHuidig + 1;
            for (long j = nextIndex; j < vnrVolgende; j++) {
              ontbrekende.add(getNieuweBlancoAkte(akteHuidig, j));
            }
          }
        }
      }
    }

    return ontbrekende;
  }

  /**
   * Geef de ontbrekende aktes terug die in de verkeerde volgorde staan
   */
  public static List<DossierAkte> getVerkeerdeVolgorde(List<DossierAkte> aktes) {

    List<DossierAkte> copyAktes = new ArrayList<>(aktes);

    sorteren(copyAktes, KlapperVolgordeType.VNR_OPLOPEND);

    List<DossierAkte> verkeerde = new ArrayList<>();

    for (int i = 0; i < copyAktes.size(); i++) {

      DossierAkte akteHuidig = copyAktes.get(i);

      if ((i + 1) < copyAktes.size()) {

        DossierAkte akteVolgende = copyAktes.get(i + 1);

        boolean isJaar = akteHuidig.getJaar().equals(akteVolgende.getJaar());
        boolean isSoort = akteHuidig.getRegistersoort().equals(akteVolgende.getRegistersoort());
        boolean isDeel = eq(akteHuidig.getRegisterdeel(), akteVolgende.getRegisterdeel());

        if (isSoort && isDeel && isJaar) {

          long datumHuidig = akteHuidig.getDatumIngang().getLongDate();
          long datumVolgende = akteVolgende.getDatumIngang().getLongDate();
          long verschil = datumVolgende - datumHuidig;

          if (verschil < 0) {
            verkeerde.add(akteHuidig);
            verkeerde.add(akteVolgende);
          }
        }
      }
    }

    return verkeerde;
  }

  /**
   * Sorteren van de aktes
   */
  public static List<DossierAkte> sorteren(List<DossierAkte> aktes, KlapperVolgordeType volgordeType) {
    aktes.sort(new Sorteerder(volgordeType));
    return aktes;
  }

  private static DossierAkte getNieuweBlancoAkte(DossierAkte akteHuidig, long vnr) {

    DossierAkte dossierAkte = new DossierAkte();
    dossierAkte.setDatumIngang(akteHuidig.getDatumIngang());
    dossierAkte.setJaar(akteHuidig.getJaar());
    dossierAkte.setRegistersoort(akteHuidig.getRegistersoort());
    dossierAkte.setRegisterdeel(akteHuidig.getRegisterdeel());
    dossierAkte.setInvoerType(DossierAkteInvoerType.BLANCO);
    dossierAkte.setVnr(toBigDecimal(vnr));

    return dossierAkte;
  }

  public static class Sorteerder implements Comparator<DossierAkte> {

    private final KlapperVolgordeType volgordeType;

    public Sorteerder(KlapperVolgordeType volgordeType) {
      this.volgordeType = volgordeType;
    }

    @Override
    public int compare(DossierAkte o1, DossierAkte o2) {

      CompareToBuilder c = new CompareToBuilder();

      long datum1 = o1.getDatumIngang().getLongDate();
      long datum2 = o2.getDatumIngang().getLongDate();
      String geslachtsnaam1 = astr(o1.getAktePersoon().getGeslachtsnaam()).toLowerCase();
      String geslachtsnaam2 = astr(o2.getAktePersoon().getGeslachtsnaam()).toLowerCase();
      BigDecimal soort1 = o1.getRegistersoort();
      BigDecimal soort2 = o2.getRegistersoort();
      String deel1 = o1.getRegisterdeel();
      String deel2 = o2.getRegisterdeel();
      BigDecimal vnr1 = o1.getVnr();
      BigDecimal vnr2 = o2.getVnr();

      switch (volgordeType) {
        case NAAM_OPLOPEND:
          c.append(geslachtsnaam1, geslachtsnaam2);
          break;

        case NAAM_AFLOPEND:
          c.append(geslachtsnaam2, geslachtsnaam1);
          break;

        case VNR_OPLOPEND:
          c.append(soort1, soort2).append(deel1, deel2).append(vnr1, vnr2);
          break;

        case VNR_AFLOPEND:
          c.append(soort2, soort1).append(deel2, deel1).append(vnr2, vnr1);
          break;

        case DATUM_AFLOPEND:
          c.append(datum2, datum1);
          break;

        case DATUM_OPLOPEND:
        default:
          c.append(datum1, datum2);
          break;
      }

      if (volgordeType.isAfdrukbaar()) {
        c.append(soort1, soort2);
        c.append(deel1, deel2);
        c.append(vnr1, vnr2);
      } else {
        c.append(soort2, soort1);
        c.append(deel2, deel1);
        c.append(vnr2, vnr1);
      }

      return c.toComparison();
    }
  }
}
