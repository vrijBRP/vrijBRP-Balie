/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.jpa.personen.db;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import nl.procura.gba.jpa.personen.converters.BigDecimalDateConverter;

@Entity
@Table(name = "kiesr_verk")
public class KiesrVerk extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_kiesr_verk",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "kiesr_verk",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_kiesr_verk")
  @Column(name = "c_kiesr_verk",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cKiesrVerk;

  @Column(name = "c_gem")
  private Long codeGemeente;

  @Column(name = "s_gem")
  private String gemeente;

  @Column(name = "ind_kiezerspas")
  private boolean indKiezerspas;

  @Column(name = "ind_briefstem")
  private boolean indBriefstembewijs;

  @Column(name = "ind_gemacht_kiesr")
  private boolean indGemachtKiesr;

  @Column(name = "afk_verk")
  private String afkVerkiezing;

  @Column(name = "s_verk")
  private String verkiezing;

  @Column(name = "d_verk")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date dVerk;

  @Column(name = "d_kand")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date dKand;

  @Column(name = "aant_volm")
  private Long aantalVolm;

  @OneToMany(mappedBy = "kiesrVerk")
  private List<KiesrVerkInfo> infos;

  @Override
  public Long getUniqueKey() {
    return getcKiesrVerk();
  }

  @Transient
  public String getCodeGemeenteFormat() {
    return String.format("%04d", codeGemeente);
  }

  public Long getcKiesrVerk() {
    return cKiesrVerk;
  }

  public void setcKiesrVerk(Long cKiesrVerk) {
    this.cKiesrVerk = cKiesrVerk;
  }

  public Long getCodeGemeente() {
    return codeGemeente;
  }

  public void setCodeGemeente(Long codeGemeente) {
    this.codeGemeente = codeGemeente;
  }

  public String getGemeente() {
    return gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
  }

  public String getAfkVerkiezing() {
    return afkVerkiezing;
  }

  public void setAfkVerkiezing(String afkVerkiezing) {
    this.afkVerkiezing = afkVerkiezing;
  }

  public String getVerkiezing() {
    return verkiezing;
  }

  public void setVerkiezing(String verkiezing) {
    this.verkiezing = verkiezing;
  }

  public Date getdVerk() {
    return dVerk;
  }

  public void setdVerk(Date dVerk) {
    this.dVerk = dVerk;
  }

  public Date getdKand() {
    return dKand;
  }

  public void setdKand(Date dKand) {
    this.dKand = dKand;
  }

  public boolean isIndKiezerspas() {
    return indKiezerspas;
  }

  public void setIndKiezerspas(boolean indKiezerspas) {
    this.indKiezerspas = indKiezerspas;
  }

  public boolean isIndBriefstembewijs() {
    return indBriefstembewijs;
  }

  public void setIndBriefstembewijs(boolean indBriefstembewijs) {
    this.indBriefstembewijs = indBriefstembewijs;
  }

  public boolean isIndGemachtKiesr() {
    return indGemachtKiesr;
  }

  public void setIndGemachtKiesr(boolean indGemachtigdeBuiten) {
    this.indGemachtKiesr = indGemachtigdeBuiten;
  }

  public Long getAantalVolm() {
    return aantalVolm;
  }

  public void setAantalVolm(Long aantalVolm) {
    this.aantalVolm = aantalVolm;
  }

  public List<KiesrVerkInfo> getInfos() {
    return infos;
  }
}
