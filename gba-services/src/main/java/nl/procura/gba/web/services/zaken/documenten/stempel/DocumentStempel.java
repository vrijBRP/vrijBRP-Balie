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

package nl.procura.gba.web.services.zaken.documenten.stempel;

import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.List;
import java.util.Optional;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.Stempel;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocumentStempel;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintUtils;

public class DocumentStempel
    extends Stempel
    implements KoppelbaarAanDocument, DatabaseTable<Long> {

  private static final String BEGIN_COUPLIN_ERROR_MESSAGE = "Object van type ";
  private static final String END_COUPLIN_ERROR_MESSAGE   = " kan niet gekoppeld worden aan een gebruiker.";
  private static final int    DEFAULT_FONT                = 9;

  public long getBreedte() {
    return along(getB());
  }

  public void setBreedte(long breedte) {
    setB(toBigDecimal(breedte));
  }

  public Long getCode() {
    return getCStempel();
  }

  public String getDocumentStempel() {
    return getStempel();
  }

  public void setDocumentStempel(String stempel) {
    setStempel(stempel);
  }

  public long getFontSize() {
    long size = along(getFontsize());
    return (size <= 0) ? DEFAULT_FONT : size;
  }

  public void setFontSize(long fontsize) {
    setFontsize(toBigDecimal(fontsize));
  }

  public DocumentFontType getFontType() {
    return DocumentFontType.get(along(getFont()));
  }

  public void setFontType(DocumentFontType type) {
    setFont(toBigDecimal(type != null ? type.getCode() : -1));
  }

  public long getHoogte() {
    return along(getH());
  }

  public void setHoogte(long hoogte) {
    setH(toBigDecimal(hoogte));
  }

  public List<Integer> getPaginaNummers() {
    return PrintUtils.getPages(getPaginas());
  }

  public PositieType getPositie() {
    return PositieType.get(along(getPos()));
  }

  public void setPositie(PositieType type) {
    setPos(toBigDecimal(type.getCode()));
  }

  public DocumentStempelType getStempelType() {
    return DocumentStempelType.get(along(getType()));
  }

  public void setStempelType(DocumentStempelType type) {
    setType(toBigDecimal(type != null ? type.getCode() : -1));
  }

  public long getVolgorde() {
    return along(getzIndex());
  }

  public void setVolgorde(long volgorde) {
    setzIndex(toBigDecimal(volgorde));
  }

  public long getXcoordinaat() {
    return along(getX());
  }

  public void setXcoordinaat(long coordinaat) {
    setX(toBigDecimal(coordinaat));
  }

  public long getYcoordinaat() {
    return along(getY());
  }

  public void setYcoordinaat(long coordinaat) {
    setY(toBigDecimal(coordinaat));
  }

  public boolean isAfbeelding() {
    return DocumentStempelType.AFBEELDING == getStempelType();
  }

  @Override
  public boolean isGekoppeld(DocumentRecord document) {
    return MiscUtils.contains(document, getDocuments());
  }

  public <K extends KoppelbaarAanDocumentStempel> boolean isGekoppeld(List<K> objectList) {

    Optional<K> entity = objectList.stream().filter(object -> (object instanceof DocumentRecord)).findFirst();
    if (entity.isPresent()) {
      return isGekoppeld((DocumentRecord) entity.get());
    }
    throw new IllegalArgumentException(BEGIN_COUPLIN_ERROR_MESSAGE + entity.getClass() + END_COUPLIN_ERROR_MESSAGE);
  }

  @Override
  public void koppelActie(DocumentRecord document, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getDocuments().add(deepCopyBean(Document.class, document));
    } else {
      getDocuments().remove(document);
    }
  }

  public <K extends KoppelbaarAanDocumentStempel> void koppelActie(K koppelObject, KoppelActie koppelActie) {
    if (koppelObject instanceof DocumentRecord) {
      koppelActie((DocumentRecord) koppelObject, koppelActie);
    } else {
      throw new IllegalArgumentException(
          BEGIN_COUPLIN_ERROR_MESSAGE + koppelObject.getClass() + END_COUPLIN_ERROR_MESSAGE);
    }
  }
}
