package nl.procura.gba.web.services.zaken.algemeen.tasks.events;

import static nl.procura.gba.web.services.beheer.personmutations.PersonListActionType.ADD_SET;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEventType;

public class ZaakTaskEvents {

  public static List<TaskEventType> getEvents(Zaak zaak) {
    Set<TaskEventType> eventTypes = new HashSet<>();
    if (zaak instanceof PersonListMutation) {
      PersonListMutation mutation = (PersonListMutation) zaak;
      if (mutation.getActionType().is(ADD_SET)) {
        if (mutation.getCatType().is(GBACat.OVERL)) {
          //TODO tijdelijk uitgezet
          //eventTypes.add(TaskEventType.EVENT_MUT_OVERL);
        }
      }
    }
    return new ArrayList<>(eventTypes);
  }
}
