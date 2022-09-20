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

package nl.procura.gba.web.services.zaken.identiteit;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.trim;
import static org.apache.commons.lang3.StringUtils.capitalize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.Idvaststelling;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Identificatie extends Idvaststelling {

  private static final long serialVersionUID = 8642728288818582549L;

  private final List<IdentificatieType> types = new ArrayList<>();

  public Identificatie() {
  }

  public Identificatie(BasePLExt pl) {
    setBurgerServiceNummer(new BsnFieldValue(pl.getPersoon().getBsn().getCode()));
  }

  public void addIdentificatieType(IdentificatieType identificatieType) {
    types.add(identificatieType);
  }

  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    Identificatie other = (Identificatie) obj;

    if (getBsn() == null) {
      return other.getBsn() == null;
    } else {
      return getBsn().equals(other.getBsn());
    }
  }

  public String getAanDeHandVan() {

    if (isVastgesteld()) {
      StringBuilder sb = new StringBuilder("Aan de hand van ");
      int i = 0;
      List<IdentificatieType> list = getIdentificatieTypes();
      for (IdentificatieType id : list) {
        i++;
        switch (id) {
          case IDENTITEITSKAART:
            sb.append("een identiteitskaart (" + getDocumentnr() + ")");
            break;

          case PASPOORT:
            sb.append("een paspoort (" + getDocumentnr() + ")");
            break;

          case RIJBEWIJS:
            sb.append("een rijbewijs (" + getDocumentnr() + ")");
            break;

          case VERBLIJFSDOCUMENT:
            sb.append("een verblijfsdocument (" + getDocumentnr() + ")");
            break;

          case REISDOCUMENTEN_ADMINISTRATIE:
            sb.append("de reisdocumentenadministratie");
            break;

          case RIJBEWIJZEN_ADMINISTRATIE:
            sb.append("de rijbewijzenadministratie");
            break;

          case PERSOON_GEZIEN_VNM:
            sb.append("persoonlijke waarneming");
            break;

          case EXTERNE_APPLICATIE:
            sb.append("een externe applicatie");
            break;

          case RPS:
            sb.append("het Register Paspoortsignaleringen (RPS)");
            break;

          case VRAGEN:
          default:
            sb.append("vragen");
        }

        if (i < list.size()) {
          if ((i + 1) == list.size()) {
            sb.append(" en ");
          } else {
            sb.append(", ");
          }
        }
      }

      return trim(sb.toString());
    }

    return "";
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getBsn()));
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  public String getExternVerificatieAntwoord() {
    return getSVerificatie();
  }

  public void setExternVerificatieAntwoord(String verificatie) {
    setSVerificatie(verificatie);
  }

  public UsrFieldValue getGebruiker() {
    return new UsrFieldValue(getUsr().getCUsr(), getUsr().getUsrfullname());
  }

  public List<IdentificatieType> getIdentificatieTypes() {

    if (types.isEmpty()) {
      for (String type : astr(getSoort()).split(",")) {
        if (fil(type)) {
          types.add(IdentificatieType.get(type));
        }
      }
    }

    return types;
  }

  public String getKorteOmschrijving() {
    StringBuilder sb = new StringBuilder();
    List<IdentificatieType> list = getIdentificatieTypes();
    for (IdentificatieType id : list) {
      switch (id) {
        case IDENTITEITSKAART:
          sb.append(", id-" + getDocumentnr());
          break;

        case PASPOORT:
          sb.append(", pp-" + getDocumentnr());
          break;

        case RIJBEWIJS:
          sb.append(", rbw-" + getDocumentnr());
          break;

        case VERBLIJFSDOCUMENT:
          sb.append(", vd-" + getDocumentnr());
          break;

        case REISDOCUMENTEN_ADMINISTRATIE:
          sb.append(", rdadmin");
          break;

        case RIJBEWIJZEN_ADMINISTRATIE:
          sb.append(", rbwadmin");
          break;

        case PERSOON_GEZIEN_VNM:
          sb.append(", pgvnm");
          break;

        case EXTERNE_APPLICATIE:
          sb.append(", extern");
          break;

        case VRAGEN:
          sb.append(", idvragen");
          break;

        default:
          break;
      }
    }

    return trim(sb.toString().trim()).trim();
  }

  public String getOmschrijving() {

    if (isVastgesteld()) {
      return trim(capitalize(String.format("Vastgesteld op %s %s ", getTijdstip(), getAanDeHandVan()).toLowerCase()));
    }

    return "Niet vastgesteld";
  }

  public DateTime getTijdstip() {
    return new DateTime(getDIn(), getTIn());
  }

  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((getBsn() == null) ? 0 : getBsn().hashCode());

    return result;
  }

  public boolean isExternGeverificeerd() {
    return pos(getVerificatie());
  }

  public void setExternGeverificeerd(boolean b) {
    setVerificatie(BigDecimal.valueOf(b ? 1 : 0));
  }

  public boolean isVastgesteld() {
    return pos(getCIdvaststelling());
  }
}
