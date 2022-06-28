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

package nl.procura.gba.web.rest.v1_0.gebruiker;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler.add;
import static nl.procura.standard.Globalfunctions.*;

import java.lang.reflect.Field;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.*;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfo;
import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.proweb.rest.resources.v1.ProRestAuthenticationResources;
import nl.procura.proweb.rest.v1_0.Rol;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruikerAntwoord;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruikerInformatie;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMeldingen;

@RequestScoped
@Path("v1.0/gebruiker")
@AuthenticatieVereist
public class GbaRestAuthenticatieResources extends GbaRestServiceResource implements ProRestAuthenticationResources {

  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/synchronize")
  public GbaRestGebruikerSyncAntwoord sync(GbaRestGebruikerSyncVraag syncVraag) {

    GbaRestGebruikerToevoegenSyncVraag toevoegen = syncVraag.getGebruikerToevoegen();
    GbaRestGebruikerVerwijderenSyncVraag verwijderen = syncVraag.getGebruikerVerwijderen();
    GbaRestGebruikerWachtwoordSyncVraag wachtwoord = syncVraag.getWachtwoord();

    if (toevoegen != null) {
      getServices().getGebruikerService().syncAddLocalUser(toevoegen.getGebruikersnaam(), toevoegen.getNaam(), toevoegen
          .isAdmin(),
          toevoegen.isGeblokkeerd(), toevoegen.getDatumIngang(),
          toevoegen.getDatumEinde());
    } else if (wachtwoord != null) {
      getServices().getGebruikerService().syncChangeLocalPassword(wachtwoord.getGebruikersnaam(), wachtwoord.getDatum(),
          wachtwoord.getTijd(), wachtwoord.getWachtwoord(),
          wachtwoord.isResetPassword());
    } else if (verwijderen != null) {
      getServices().getGebruikerService().syncRemoveLocalUser(verwijderen.getGebruikersnaam());
    }

    return new GbaRestGebruikerSyncAntwoord();
  }

  @Override
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/{naam}")
  public ProRestGebruikerAntwoord get(@PathParam("naam") String name) {

    ProRestGebruikerAntwoord antwoord = new ProRestGebruikerAntwoord();

    if (fil(loggedInUser.getGebruikersnaam())) {
      ProRestGebruiker restGebruiker = new ProRestGebruiker();
      Gebruiker gebruiker = getServices().getGebruikerService().getGebruikerByNaamWithCache(name);

      boolean accessAllowed = false;
      boolean userIsAdmin = loggedInUser.getRollen().contains(Rol.BEHEERDER);
      boolean isUser = (gebruiker != null && loggedInUser.getGebruikersnaam().equals(
          gebruiker.getGebruikersnaam()));

      // Ingelogde gebruiker is ADMIN
      if (userIsAdmin) {
        accessAllowed = true;
      }

      // Ingelogde gebruiker is geen ADMIN, maar zoekt zichzelf.
      if (!userIsAdmin && gebruiker != null && isUser) {
        accessAllowed = true;
      }

      if (!accessAllowed) {
        antwoord.getMeldingen().add(ProRestMeldingen.GEEN_TOEGANG);
      } else {

        if (gebruiker != null) {

          // ADMIN mag iedereen opvragen.
          // USER mag alleen zichzelf opvragen

          restGebruiker.setCode(astr(gebruiker.getCUsr()));
          restGebruiker.setGebruikersnaam(gebruiker.getGebruikersnaam());
          restGebruiker.setNaam(gebruiker.getNaam());
          restGebruiker.setOmschrijving(gebruiker.getOmschrijving());

          restGebruiker.getRollen().add(Rol.GEBRUIKER);

          if (gebruiker.isAdministrator()) {
            restGebruiker.getRollen().add(Rol.BEHEERDER);
          }

          for (GebruikerInfo info : gebruiker.getInformatie().getAlles()) {

            ProRestGebruikerInformatie restInfo = new ProRestGebruikerInformatie();
            restInfo.setId(info.getInfo());
            restInfo.setOmschrijving(info.getOmschrijving());
            restInfo.setWaarde(info.getWaarde());

            restGebruiker.getInformatie().add(restInfo);
          }

          antwoord.setGebruiker(restGebruiker);
        } else {
          antwoord.getMeldingen().add(ProRestMeldingen.GEEN_GEGEVENS);
        }
      }
    }

    return antwoord;
  }

  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken")
  @AuthenticatieVereist(rollen = { Rol.BEHEERDER })
  public GbaRestGebruikerAntwoord zoekenPersonen(GbaRestGebruikerVraag vraag) {

    GbaRestGebruikerAntwoord antwoord = new GbaRestGebruikerAntwoord();

    GbaRestElement gebruikers = antwoord.getAntwoordElement().add(GEBRUIKERS);
    Gebruiker gebruiker;

    if (pos(vraag.getCode())) {
      gebruiker = getServices().getGebruikerService().getGebruikerByCode(vraag.getCode(), true);
    } else {
      String gebruikersnaam = StringUtils.defaultIfBlank(vraag.getGebruikersnaam(), vraag.getEmail());
      gebruiker = getServices().getGebruikerService().getGebruikerByNaamWithCache(gebruikersnaam);
    }

    if (gebruiker != null && gebruiker.isStored()) {
      GbaRestElement g = gebruikers.add(GEBRUIKER);
      g.add(CODE).set(gebruiker.getCUsr());

      add(g, GEBRUIKERSNAAM, gebruiker.getGebruikersnaam());
      add(g, NAAM, gebruiker.getNaam());
      add(g, OMSCHRIJVING, gebruiker.getOmschrijving());
      add(g, ADMIN, gebruiker.isAdministrator());
      add(g, DATUM_INGANG, gebruiker.getDatumIngang());
      add(g, DATUM_EINDE, gebruiker.getDatumEinde());
      add(g, WACHTWOORD_VERLOPEN, gebruiker.isWachtwoordVerlopen());
      add(g, GEBLOKKEERD, gebruiker.isGeblokkeerd());
      add(g, EMAIL, gebruiker.getEmail());
      add(g, TELEFOON, gebruiker.getTelefoonnummer());
      add(g, AFDELING, gebruiker.getAfdeling());
      add(g, PAD, gebruiker.getPad());

      // Parameters toevoegen

      GbaRestElement ps = g.add(PARAMETERS);
      Field[] declaredFields = ParameterBean.class.getDeclaredFields();

      for (Parameter parameter : gebruiker.getParameters().getAlle()) {
        for (Field field : declaredFields) {
          ParameterAnnotation appAnn = field.getAnnotation(ParameterAnnotation.class);
          nl.procura.vaadin.annotation.field.Field fieldAnn = field.getAnnotation(
              nl.procura.vaadin.annotation.field.Field.class);

          if (appAnn != null) {
            if (appAnn.value().getKey().equals(parameter.getParm())) {
              if (fieldAnn != null) {
                GbaRestElement p = ps.add(PARAMETER);
                add(p, NAAM, parameter.getParm());
                add(p, OMSCHRIJVING, fieldAnn.caption());
                add(p, WAARDE, parameter.getValue());
                break;
              }
            }
          }
        }
      }
    }

    return antwoord;
  }
}
