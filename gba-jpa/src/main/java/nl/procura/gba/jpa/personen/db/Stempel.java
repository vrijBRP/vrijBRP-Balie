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

package nl.procura.gba.jpa.personen.db;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "stempel")
@NamedQuery(name = "Stempel.findAll",
    query = "SELECT s FROM Stempel s")
public class Stempel extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_stempel",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "stempel",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_stempel")
  @Column(name = "c_stempel",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cStempel;

  private byte[] data;

  @Column(name = "data_type")
  private String dataType;

  @Column(name = "z_index",
      precision = 5)
  private BigDecimal zIndex;

  @Column(name = "woord")
  private String woord;

  @Column(name = "stempel_type",
      precision = 2)
  private BigDecimal type;

  @Column(name = "fontsize",
      precision = 2)
  private BigDecimal fontsize;

  @Column(name = "font",
      precision = 2)
  private BigDecimal font;

  @Column()
  private String stempel;

  @Column(precision = 4)
  private BigDecimal x;

  @Column(precision = 4)
  private BigDecimal y;

  @Column(precision = 5)
  private BigDecimal b;

  @Column(precision = 5)
  private BigDecimal h;

  @Column(precision = 1)
  private BigDecimal pos;

  @Column()
  private String paginas;

  @ManyToMany(mappedBy = "stempels")
  private List<Document> documents;

  public Stempel() {
  }

  public Long getCStempel() {
    return this.cStempel;
  }

  public void setCStempel(Long cStempel) {
    this.cStempel = cStempel;
  }

  public byte[] getData() {
    return this.data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String getDataType() {
    return this.dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getStempel() {
    return this.stempel;
  }

  public void setStempel(String stempel) {
    this.stempel = stempel;
  }

  public BigDecimal getType() {
    return this.type;
  }

  public void setType(BigDecimal type) {
    this.type = type;
  }

  public BigDecimal getX() {
    return this.x;
  }

  public void setX(BigDecimal x) {
    this.x = x;
  }

  public BigDecimal getY() {
    return this.y;
  }

  public void setY(BigDecimal y) {
    this.y = y;
  }

  public List<Document> getDocuments() {
    return this.documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  public BigDecimal getB() {
    return b;
  }

  public void setB(BigDecimal b) {
    this.b = b;
  }

  public BigDecimal getH() {
    return h;
  }

  public void setH(BigDecimal h) {
    this.h = h;
  }

  public BigDecimal getPos() {
    return pos;
  }

  public void setPos(BigDecimal pos) {
    this.pos = pos;
  }

  public String getWoord() {
    return woord;
  }

  public void setWoord(String woord) {
    this.woord = woord;
  }

  public BigDecimal getzIndex() {
    return zIndex;
  }

  public void setzIndex(BigDecimal zIndex) {
    this.zIndex = zIndex;
  }

  public String getPaginas() {
    return paginas;
  }

  public void setPaginas(String paginas) {
    this.paginas = paginas;
  }

  public BigDecimal getFontsize() {
    return fontsize;
  }

  public void setFontsize(BigDecimal fontsize) {
    this.fontsize = fontsize;
  }

  public BigDecimal getFont() {
    return font;
  }

  public void setFont(BigDecimal font) {
    this.font = font;
  }
}
