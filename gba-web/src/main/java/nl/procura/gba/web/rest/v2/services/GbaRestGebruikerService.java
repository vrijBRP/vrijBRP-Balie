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

package nl.procura.gba.web.rest.v2.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestGebruiker;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestGebruikerVraag;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestGebruikerZoekenAntwoord;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestParameter;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;
import nl.procura.standard.ProcuraDate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GbaRestGebruikerService extends GbaRestAbstractService {

  public GbaRestGebruikerZoekenAntwoord getGebruiker(GbaRestGebruikerVraag vraag) {
    GbaRestGebruikerZoekenAntwoord antwoord = new GbaRestGebruikerZoekenAntwoord();

    List<Gebruiker> gebruikerList = new ArrayList<>();
    String gebruikersnaam = StringUtils.defaultIfBlank(vraag.getGebruikersnaam(), vraag.getEmail());
    gebruikerList.add(getServices().getGebruikerService().getGebruikerByNaam(gebruikersnaam));

    for (Gebruiker gebruiker : gebruikerList.stream()
        .filter(Objects::nonNull)
        .filter(this::isToegangTotGebruiker)
        .collect(Collectors.toList())) {
      GbaRestGebruiker restGebruiker = new GbaRestGebruiker();
      restGebruiker.setGebruikersnaam(gebruiker.getGebruikersnaam());
      restGebruiker.setNaam(gebruiker.getNaam());
      restGebruiker.setEmail(gebruiker.getEmail());
      restGebruiker.setBeheerder(gebruiker.isAdministrator());
      restGebruiker.setGeblokkeerd(gebruiker.isGeblokkeerd());
      restGebruiker.setVerlopen(new ProcuraDate(gebruiker.getDatumEinde().getIntDate()).isExpired());
      restGebruiker.setParameters(getParameters(gebruiker));
      antwoord.setGebruiker(restGebruiker);
    }

    return antwoord;
  }

  private boolean isToegangTotGebruiker(Gebruiker g) {
    return getServices().getGebruiker().isAdministrator()
        || Objects.equals(g.getCUsr(), getServices().getGebruiker().getCUsr());
  }

  private List<GbaRestParameter> getParameters(Gebruiker gebruiker) {
    List<GbaRestParameter> restParameters = new ArrayList<>();
    Field[] declaredFields = ParameterBean.class.getDeclaredFields();
    for (Parameter parameter : gebruiker.getParameters().getAlle()) {
      for (Field field : declaredFields) {
        ParameterAnnotation appAnn = field.getAnnotation(ParameterAnnotation.class);
        nl.procura.vaadin.annotation.field.Field fieldAnn = field.getAnnotation(
            nl.procura.vaadin.annotation.field.Field.class);

        if (appAnn != null) {
          if (appAnn.value().getKey().equals(parameter.getParm())) {
            if (fieldAnn != null) {
              restParameters.add(new GbaRestParameter(parameter.getParm(), parameter.getValue(), fieldAnn.caption()));
              break;
            }
          }
        }
      }
    }
    return restParameters;
  }
}
