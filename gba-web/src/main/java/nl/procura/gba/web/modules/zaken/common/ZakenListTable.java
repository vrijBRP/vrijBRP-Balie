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

package nl.procura.gba.web.modules.zaken.common;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.common.SearchLayout.ReloadListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.vaadin.component.table.indexed.LazyLoadable;

public abstract class ZakenListTable extends GbaTable implements LazyLoadable, ReloadListener {

  private final ZaakArgumenten zaakArgumenten;
  private final SearchLayout   searchLayout = new SearchLayout();

  public ZakenListTable(ZaakArgumenten zaakArgumenten) {
    this.zaakArgumenten = zaakArgumenten;
    searchLayout.addReloadListener(this::init);
  }

  @Override
  public void setRecords() {

    List<ZaakRecord> zaken = getZaakKeys();
    for (ZaakRecord z : zaken) {
      addRecord(z).addValues(getColumns().size());
    }

    super.setRecords();
  }

  protected abstract void loadZaak(Record record, ZaakRecord zaakRecord);

  @Override
  public void onLazyLoad(List<Record> records) {

    if (!records.isEmpty()) {
      ZaakArgumenten za = new ZaakArgumenten(zaakArgumenten);
      za.setMax(-1);
      for (Record record : records) {
        ZaakRecord object = record.getObject(ZaakRecord.class);
        za.addZaakKey(object.getZaakKey());
      }

      List<Zaak> minimaleZaken = Services.getInstance().getZakenService().getMinimaleZaken(za);
      for (Record record : records) {
        for (Zaak zaak : minimaleZaken) {
          ZaakRecord zaakRecord = record.getObject(ZaakRecord.class);
          if (zaakRecord.getZaakKey().getZaakId().equals(zaak.getZaakId())) {
            zaakRecord.setZaak(zaak);
            loadZaak(record, zaakRecord);
          }
        }
      }
    }
  }

  public List<ZaakRecord> getZaakKeys() {
    List<ZaakRecord> records = new ArrayList<>();
    ZakenService service = getApplication().getServices().getZakenService();
    ZaakArgumenten argumenten = searchLayout.getZaakArgumenten(zaakArgumenten);
    List<ZaakKey> zaakKeys = service.getZaakKeys(argumenten);
    zaakKeys.forEach(zk -> {
      int nr = zaakArgumenten.getSortering().getNumber(zaakKeys.size(), records.size() + 1);
      records.add(new ZaakRecord(nr, zk));
    });
    return records;
  }

  public SearchLayout getSearchLayout() {
    return searchLayout;
  }

  public class ZaakRecord<Z extends Zaak> {

    private final ZaakKey zaakKey;
    private int           nr = 0;
    private Z             zaak;

    public ZaakRecord(int nr, ZaakKey zaakKey) {
      this.setNr(nr);
      this.zaakKey = zaakKey;
    }

    public Z getZaak() {
      return zaak;
    }

    public void setZaak(Z zaak) {
      this.zaak = zaak;
    }

    public ZaakKey getZaakKey() {
      return zaakKey;
    }

    public int getNr() {
      return nr;
    }

    public void setNr(int nr) {
      this.nr = nr;
    }
  }
}
