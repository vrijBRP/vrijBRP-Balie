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

import static nl.procura.gba.common.ZaakStatusType.VERWERKT;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT_IN_GBA;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.GeboorteService;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesTemplate;
import nl.procura.gba.web.services.zaken.algemeen.controle.StandaardControle;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

/**
 * Alle controles van geboortes
 */
@SuppressWarnings("unused")
public class DossierAkteControles extends ControlesTemplate<AkteService> {

  public DossierAkteControles(AkteService service) {
    super(service);
  }

  @Override
  public Controles getControles(ControlesListener listener) {

    Controles controles = new Controles();
    /*
        KlapperZoekargumenten klapperArgs = new KlapperZoekargumenten ();
        klapperArgs.getSoorten ().add (AKTE_GEBOORTE);
    
        for (DossierAkte geboorteAkte : getService ().getAktes (klapperArgs)) {
    
            String gesl = geboorteAkte.getAktePersoon ().getGeslacht ().getAfkorting ();
    
            if (emp (gesl)) {                                                   // Erkenning bij ongeboren vrucht hebben geen geslacht.
                continue;
                }
    
            if (geboorteAkte.isDossierAkte ()) {                                // Is ingevoerd in Proweb Personen
    
                if (!isBsnCorrect (geboorteAkte)) {                             // Heeft kind wel in klapper wel of geen BSN
    
                    DossierGeboorte dossierGeboorte = getDossierGeboorte (geboorteAkte.getZaakId ());
    
                    if (dossierGeboorte != null) {                              // Geboorte dossier gevonden
    
                        if (isWoonGemeente (dossierGeboorte.getMoeder ())) {    // Is geboren in de gemeente
    
                            // Zoek ook de erkenningen
    
                            for (DossierAkte akte : getService ().getAktes (dossierGeboorte.getDossier ())) {
    
                                updateAkte (akte, controles);
                                }
                            }
                        }
                    }
                }
            }
    */
    return controles;
  }

  private DossierGeboorte getDossierGeboorte(String zaakId) {

    GeboorteService geboortes = getService().getServices().getGeboorteService();

    if (fil(zaakId)) {

      // Zoek de moeder
      ZaakArgumenten zaakArgumenten = new ZaakArgumenten(zaakId);
      zaakArgumenten.setStatussen(VERWERKT, VERWERKT_IN_GBA);

      return geboortes.getMinimalZaken(zaakArgumenten)
          .stream()
          .findFirst()
          .map(dossier -> (DossierGeboorte) geboortes.getStandardZaak(dossier).getZaakDossier())
          .orElse(null);
    }

    return null;
  }

  private boolean isBsnCorrect(DossierAkte akte) {
    return akte.getAktePersoon().getBurgerServiceNummer().isCorrect();
  }

  /**
   * De gemeente van de moeder is de huidige gemeente
   */
  private boolean isWoonGemeente(DossierPersoon moeder) {
    long woongemeente = along(moeder.getWoongemeente().getValue());
    long gemeente = along(getService().getServices().getGebruiker().getGemeenteCode());
    return gemeente > 0 && gemeente == woongemeente;
  }

  private void updateAkte(DossierAkte akte, Controles controles) {

    String naam = akte.getAktePersoon().getGeslachtsnaam();
    String voorn = akte.getAktePersoon().getVoornaam();
    String voorv = akte.getAktePersoon().getVoorvoegsel();
    String gesl = akte.getAktePersoon().getGeslacht().getAfkorting();
    String dGeb = akte.getAktePersoon().getGeboortedatum().getStringValue();

    if ((fil(naam) || fil(voorn)) && fil(gesl) && fil(dGeb)) {

      PLEArgs zoekArgs = new PLEArgs();
      zoekArgs.setGeslachtsnaam(naam);
      zoekArgs.setVoornaam(voorn);
      zoekArgs.setVoorvoegsel(voorv);
      zoekArgs.setGeslacht(gesl);
      zoekArgs.setGeboortedatum(dGeb);
      zoekArgs.setDatasource(PLEDatasource.PROCURA);
      zoekArgs.setMaxFindCount(2);

      PersonenWsService personenWs = getService().getServices().getPersonenWsService();
      List<BasePLExt> persoonslijsten = personenWs.getPersoonslijsten(zoekArgs, false).getBasisPLWrappers();

      String voornaam = akte.getAktePersoon().getNaam().getNaam_naamgebruik_eerste_voornaam();
      StandaardControle controle = controles.addControle(new StandaardControle("Klappers", voornaam));

      controle.setGewijzigd(false);
      controle.setId(akte.getZaakId());

      if (persoonslijsten.size() == 1) {

        BasePLValue bsn = persoonslijsten.get(0).getPersoon().getBsn();

        if (fil(bsn.getVal())) {

          controle.setGewijzigd(true);
          controle.addOpmerking(
              "Bsn " + bsn.getDescr() + " toegevoegd aan klapper: " + akte.getAkte());
          akte.getAktePersoon().setBurgerServiceNummer(new BsnFieldValue(bsn.getVal()));
          getService().save(akte);
        }
      } else {
        controle.addOpmerking("Geen persoon gevonden");
      }
    }
  }
}
