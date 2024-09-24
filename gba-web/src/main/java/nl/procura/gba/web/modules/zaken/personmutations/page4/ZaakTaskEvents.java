package nl.procura.gba.web.modules.zaken.personmutations.page4;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AANDUIDING_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ANR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.NATIONALITEIT;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.HUWELIJK;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.PARTNERSCHAP;
import static nl.procura.gba.web.common.misc.Landelijk.EUKIESRECHT_ONTVANG_OPROEP;
import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_SET;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType.TASK_BERICHT_HUW_PARTNER;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType.TASK_BERICHT_OVERL_PARTNER;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType.TASK_EU_KIESR;
import static nl.procura.standard.Globalfunctions.along;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaat.BurgerlijkeStandStatus;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType;
import nl.procura.validation.Anummer;

public class ZaakTaskEvents {

  public static List<Task> getEvents(Services services, Zaak zaak, PersonListMutElems mutations) {
    Set<Task> tasks = new HashSet<>();
    if (zaak instanceof PersonListMutation) {
      PersonListMutation mutation = (PersonListMutation) zaak;
      Anummer anr = new Anummer(mutation.getAnr().longValue());
      if (mutation.getActionType().is(ADD_SET)) {
        // Sprake van Overlijden
        if (mutation.getCatType().is(GBACat.OVERL)) {
          PersonListMutElem datumOverl = getElem(mutations, DATUM_OVERL);
          if (isBlank(datumOverl.getCurrentValue().getVal()) && isNotBlank(datumOverl.getNewValue())) {
            Anummer anrHuwelijk = getHuwelijk(services, anr);
            if (anrHuwelijk != null) {
              BasePLValue andereGemeente = getAndereWoongemeente(services, anrHuwelijk);
              if (andereGemeente != null) {
                tasks.add(getNewTask(services, zaak, TASK_BERICHT_OVERL_PARTNER,
                    String.format("Het overlijden wat is toegevoegd kan niet worden verwerkt op de PL van de "
                        + "partner omdat deze niet in de gemeente woont. Stuur een kennisgeving of vrij bericht "
                        + "naar de gemeente waar de partner staat ingeschreven. Ofwel: Voeg bij de PL van de "
                        + "partner een PL mutatie toe met actie 'Actualiseren (toevoegen gegevens)' met "
                        + "hetzelfde brondocument als gebruikt is voor het toevoegen van het overlijden.\n"
                        + "Gegevens: a-nummer overleden persoon: %s, a-nummer partner: %s, Gemeente partner: %s (%s)",
                        anr.getFormatAnummer(),
                        anrHuwelijk.getFormatAnummer(),
                        andereGemeente.getDescr(),
                        andereGemeente.getVal())));
              } else {
                tasks.add(getNewTask(services, zaak, TASK_BERICHT_OVERL_PARTNER,
                    String.format("Het overlijden wat is toegevoegd moet nog worden verwerkt op de PL van de partner.\n"
                        + "Gegevens: a-nummer overleden persoon: %s, a-nummer partner: %s",
                        anr.getFormatAnummer(),
                        anrHuwelijk.getFormatAnummer())));
              }
            }
          }
        }
      }
      // Sprake van Huwelijk
      if (mutation.getCatType().is(GBACat.HUW_GPS)) {
        PersonListMutElem datumHuw = getElem(mutations, DATUM_VERBINTENIS);
        if (isBlank(datumHuw.getCurrentValue().getVal()) && isNotBlank(datumHuw.getNewValue())) {
          Anummer anrPartner = new Anummer(getElem(mutations, ANR).getNewValue());
          if (anrPartner.isCorrect()) {
            BasePLValue gemeente = getAndereWoongemeente(services, anrPartner);
            if (gemeente != null) {
              tasks.add(getNewTask(services, zaak, TASK_BERICHT_HUW_PARTNER,
                  String.format("De verbintenis die is toegevoegd kan niet worden verwerkt op de PL van de partner "
                      + "omdat deze niet in de gemeente woont. Stuur een kennisgeving of vrij bericht "
                      + "naar de gemeente waar de partner staat ingeschreven.\n"
                      + "Gegevens: a-nummer partner 1: %s, a-nummer partner 2: %s, gemeente partner 2: %s (%s)",
                      anr.getFormatAnummer(),
                      anrPartner.getFormatAnummer(),
                      gemeente.getDescr(),
                      gemeente.getVal())));
            } else {
              // Automatisch huwelijk verwerken op persoonslijst partner
            }
          }
        }
      }
      // Sprake van Nationaliteit. Ontvang oproep voor EU kiesrecht
      if (mutation.getCatType().is(GBACat.NATIO)) {
        PersonListMutElem nat = getElem(mutations, NATIONALITEIT);
        if (!nat.isBlank()) {
          if (isOntvangOproepEUKiesr(services, anr)) {
            tasks.add(getNewTask(services, zaak, TASK_EU_KIESR,
                String.format("Bij deze persoon die de Nederlandse nationaliteit heeft verkregen is"
                    + " geconstateerd dat categorie 13 (Kiesrecht) voorkomt met de vermelding dat de persoon "
                    + "een oproep wil ontvangen voor de verkiezingen voor het Europees Parlement.\n"
                    + "Verwijder categorie 13 door in de PL mutatie bij categorie 13 te kiezen voor "
                    + "de superuseractie 'Verwijder actueel record'.\n"
                    + "Als deze niet als actie beschikbaar is, vraag het dan aan een gebruiker "
                    + "die wel deze rechten heeft.\n"
                    + "Gegevens: A-nummer persoon: %s", anr.getFormatAnummer())));
          }
        }
      }
    }
    return new ArrayList<>(tasks);
  }

  private static Task getNewTask(Services services, Zaak zaak, TaskType taskType, String remarks) {
    return services.getTaskService().newTask(zaak.getZaakId(), taskType, remarks);
  }

  private static boolean isOntvangOproepEUKiesr(Services services, Anummer anr) {
    BasePLExt pl = services.getPersonenWsService().getPersoonslijst(anr.getAnummer());
    long eukiesr = along(pl.getCat(GBACat.KIESR).getCurrentRec().getElemVal(AANDUIDING_EURO_KIESR).getVal());
    return eukiesr == EUKIESRECHT_ONTVANG_OPROEP;
  }

  private static Anummer getHuwelijk(Services services, Anummer anr) {
    BasePLExt pl = services.getPersonenWsService().getPersoonslijst(anr.getAnummer());
    BurgerlijkeStandStatus status = pl.getPersoon().getBurgerlijkeStand().getStatus();
    if (status.getType().is(HUWELIJK, PARTNERSCHAP)) {
      return new Anummer(pl.getHuwelijk().getAnr().getVal());
    }
    return null;
  }

  private static BasePLValue getAndereWoongemeente(Services services, Anummer anr) {
    BasePLElem gemeente = services.getPersonenWsService()
        .getPersoonslijst(anr.getAnummer())
        .getVerblijfplaats()
        .getAdres().getGemeente();
    long gemeenteCode = along(gemeente.getValue().getVal());
    if (gemeenteCode > 0 && !services.getGebruiker().isGemeente(gemeenteCode)) {
      return gemeente.getValue();
    }
    return null;
  }

  private static PersonListMutElem getElem(PersonListMutElems mutations, GBAElem gbaElem) {
    return mutations.stream().filter(m -> m.getElemType() == gbaElem).findFirst().orElse(null);
  }
}
