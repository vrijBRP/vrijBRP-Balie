package nl.procura.gba.web.services.zaken.reisdocumenten;

import nl.procura.gba.web.services.zaken.identiteit.Identificatie;

public interface IdentificatieBijUitreikingService {

  <T extends IdentificatieUitreikingZaak> Identificatie getIdentificatieBijUitreiking(T zaak);
}
