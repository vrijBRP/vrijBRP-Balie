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

package nl.procura.gba.web.rest.v1_0.algemeen;

import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

import nl.procura.standard.exceptions.ProException;

@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "type", "code", "waarde", "omschrijving", "elementen" })
public class GbaRestElement {

  private String type         = null;
  private String code         = null;
  private String waarde       = null;
  private String omschrijving = null;

  @XmlElementWrapper(name = "elementen")
  @XmlElement(name = "element")
  private List<GbaRestElement> elementen = null;

  public GbaRestElement() {
  }

  public GbaRestElement(String type) {
    this.type = type;
  }

  /**
   * Voeg een element type met een bepaald type
   */
  @Transient
  public GbaRestElement add(String type) {
    return add(new GbaRestElement(type));
  }

  /**
   * Voeg een element type met een bepaald type
   */
  @Transient
  public <T> T add(GbaRestElement element) {
    if (elementen == null) {
      setElementen(new ArrayList<>());
    }
    getElementen().add(element);
    return (T) element;
  }

  /**
   * Zet een element
   */
  @Transient
  public GbaRestElement set(Object waarde) {
    this.waarde = astr(waarde);
    return this;
  }

  /**
   * Zet een element
   */
  @Transient
  public GbaRestElement set(Object waarde, String omschrijving) {
    set(waarde);
    this.omschrijving = omschrijving;
    return this;
  }

  /**
   * Zet een element
   */
  @Transient
  public GbaRestElement set(Object code, Object waarde, String omschrijving) {
    set(waarde, omschrijving);
    setCode(code);
    return this;
  }

  /**
   * Geeft alle elementen terug
   */
  @Transient
  public List<GbaRestElement> getAll(String type) {
    List<GbaRestElement> sel = new ArrayList<>();
    if (heeftElementen()) {
      for (GbaRestElement element : getElementen()) {
        if (eq(element.getType(), type)) {
          sel.add(element);
        }
      }
    }

    return sel;
  }

  /**
   * Geef een element terug van dit type
   */
  @Transient
  public GbaRestElement get(String type) {
    return get(type, false);
  }

  /**
   * Geef een element terug van dit type
   */
  @Transient
  public GbaRestElement get(String type, boolean recursief) {
    GbaRestElement element = getInternal(this, type, recursief);
    if (element == null) {
      throw new ProException(ERROR, "Element '" + getType() + "' heeft geen subelement genaamd '" + type + "'");
    }

    return element;
  }

  /**
   * Zoekt een element in alle onderliggende elementen
   */
  @Transient
  private GbaRestElement getInternal(GbaRestElement parent, String type, boolean recursief) {
    if (parent.heeftElementen()) {
      for (GbaRestElement element : parent.getElementen()) {
        if (eq(element.getType(), type)) {
          return element;
        }

        if (recursief) {
          GbaRestElement subelement = getInternal(element, type, recursief);
          if (subelement != null) {
            return subelement;
          }
        }
      }
    }

    return null;
  }

  /**
   * Komt dit element voor met dit type?
   */
  @Transient
  public boolean is(String type) {
    GbaRestElement element = getInternal(this, type, false);
    return element != null && (element.heeftWaarden() || element.heeftElementen());
  }

  /**
   * Komt dit element voor met dit type?
   */
  @Transient
  public boolean is(String type, boolean recursief) {
    GbaRestElement element = getInternal(this, type, recursief);
    return element != null && (element.heeftWaarden() || element.heeftElementen());
  }

  @Transient
  public String getWaarde(boolean isVerplicht) {
    if (isVerplicht && emp(getWaarde())) {
      throw new ProException(ERROR, "Element '" + getType() + "' is niet gevuld terwijl deze verplicht is.");
    }
    return getWaarde();
  }

  @Transient
  public boolean isWaarde(boolean isVerplicht, String waarde) {
    return equalsIgnoreCase(getWaarde(isVerplicht), waarde);
  }

  public String getCode() {
    return code;
  }

  public void setCode(Object code) {
    this.code = astr(code);
  }

  public String getWaarde() {
    return waarde;
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Transient
  public List<GbaRestElement> getElementen() {
    if (elementen == null) {
      elementen = new ArrayList<>();
    }
    return elementen;
  }

  public void setElementen(List<GbaRestElement> elementen) {
    this.elementen = elementen;
  }

  public boolean heeftElementen() {
    return elementen != null && elementen.size() > 0;
  }

  public boolean heeftWaarden() {
    return fil(waarde);
  }
}
