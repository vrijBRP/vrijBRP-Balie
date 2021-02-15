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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2;

import static ch.lambdaj.Lambda.join;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import nl.procura.gba.jpa.personen.dao.UsrDao;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

/**
 * Cached alle profielen
 */
public class Page2ProfielenMap {

  private final List<ProfielMapGebruiker> gebruikers = new ArrayList<>();

  public Page2ProfielenMap() {

    for (Object[] row : UsrDao.findUsrProfiles()) {

      ProfielMapGebruiker g = new ProfielMapGebruiker(along(row[0]), astr(row[1]));
      ProfielMapProfiel p = new ProfielMapProfiel(along(row[2]), astr(row[3]));

      boolean exists = gebruikers.contains(g);

      if (exists) {
        g = gebruikers.get(gebruikers.indexOf(g));
      }

      if (!g.getProfielen().contains(p)) {
        g.getProfielen().add(p);
      }

      if (!exists) {
        gebruikers.add(g);
      }
    }

    for (ProfielMapGebruiker gebruiker : gebruikers) {
      Collections.sort(gebruiker.getProfielen());
    }
  }

  public String getProfielen(FieldValue user) {
    List<ProfielMapProfiel> profielen = getGebruikerProfielen(user);
    return profielen.isEmpty() ? "Geen profiel" : trim(join(profielen));
  }

  private List<ProfielMapProfiel> getGebruikerProfielen(FieldValue user) {
    ProfielMapGebruiker g = new ProfielMapGebruiker(along(user.getValue()), user.getDescription());

    if (gebruikers.contains(g)) {
      return gebruikers.get(gebruikers.indexOf(g)).getProfielen();
    }

    return new ArrayList<>();
  }

  public static class ProfielMapGebruiker {

    private long                    code;
    private String                  gebruiker;
    private List<ProfielMapProfiel> profielen = new ArrayList<>();

    public ProfielMapGebruiker(long code, String gebruiker) {

      this.code = code;
      this.gebruiker = gebruiker;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (obj == this) {
        return true;
      }
      if (obj.getClass() != getClass()) {
        return false;
      }
      return new EqualsBuilder().append(code, ((ProfielMapGebruiker) obj).getCode()).build();
    }

    public long getCode() {
      return code;
    }

    public void setCode(long code) {
      this.code = code;
    }

    public String getGebruiker() {
      return gebruiker;
    }

    public void setGebruiker(String gebruiker) {
      this.gebruiker = gebruiker;
    }

    public List<ProfielMapProfiel> getProfielen() {
      return profielen;
    }

    public void setProfielen(List<ProfielMapProfiel> profielen) {
      this.profielen = profielen;
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(code).build();
    }

    @Override
    public String toString() {
      return getCode() + ": " + getGebruiker();
    }
  }

  public static class ProfielMapProfiel implements Comparable<ProfielMapProfiel> {

    private long   code;
    private String profiel;

    public ProfielMapProfiel(long code, String profiel) {

      this.code = code;
      this.profiel = profiel;
    }

    @Override
    public int compareTo(ProfielMapProfiel o) {
      return getProfiel().compareTo(o.getProfiel());
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (obj == this) {
        return true;
      }
      if (obj.getClass() != getClass()) {
        return false;
      }
      return new EqualsBuilder().append(code, ((ProfielMapProfiel) obj).getCode()).build();
    }

    public long getCode() {
      return code;
    }

    public void setCode(long code) {
      this.code = code;
    }

    public String getProfiel() {
      return profiel;
    }

    public void setProfiel(String gebruiker) {
      this.profiel = gebruiker;
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(code).build();
    }

    @Override
    public String toString() {
      return getProfiel();
    }
  }
}
