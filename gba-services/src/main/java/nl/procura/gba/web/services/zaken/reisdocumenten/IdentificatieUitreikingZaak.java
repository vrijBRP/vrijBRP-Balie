package nl.procura.gba.web.services.zaken.reisdocumenten;

import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;

public interface IdentificatieUitreikingZaak extends Zaak {

  Identificatie getIdentificatieBijUitreiking();

  void setIdentificatieBijUitreiking(Identificatie identificatie);
}
