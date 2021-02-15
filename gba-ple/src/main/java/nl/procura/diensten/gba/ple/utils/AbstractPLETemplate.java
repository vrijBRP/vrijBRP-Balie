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

package nl.procura.diensten.gba.ple.utils;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.PLESkipException;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPLETemplate {

  private BasePLBuilder builder = new BasePLBuilder();
  private Cache         cache   = new Cache();

  public void init(AbstractPLETemplate t) {
    setBuilder(t.getBuilder());
    setCache(t.getCache());
  }

  public String anr(Object a1, Object a2, Object a3) {
    if (aval(astr(a1)) > 100) {
      return padl(a1, 3) + padl(a2, 4) + padl(a3, 3);
    }

    return null;
  } // anr (a1, a2, a3)

  public String padl(Object waarde, int i) {
    if (aval(astr(waarde)) >= 0) {
      return pad_left(astr(waarde), "0", i);
    }

    return null;
  } // padl

  public void addPL() {
    getBuilder().addNewPL(PLEDatasource.PROCURA);
  } // addPL

  public GBARecStatus getStatus(SortableObject so) {
    GBARecStatus status = GBARecStatus.CURRENT;

    if ("Z".equalsIgnoreCase(so.getHist())) {
      status = GBARecStatus.HIST;

    } else if ("<".equals(so.getHist())) {
      status = GBARecStatus.MUTATION;
    }
    return status;
  }

  public BasePLRec addCat(GBACat categorie, SortableObject so) throws PLESkipException {
    return getBuilder().addCat(categorie, so.getVCat(), getStatus(so));
  }

  public BasePLRec addCat(GBACat categorie, GBARecStatus status) throws PLESkipException {
    return getBuilder().addCat(categorie, status);
  }

  public int getCat() {
    BasePL pl = getBuilder().getPL();
    return pl.getCats().getLast()
        .orElseThrow(() -> new RuntimeException("Nog categories")).getCatType().getCode();
  }

  public BasePLBuilder getBuilder() {
    return builder;
  }

  public void setBuilder(BasePLBuilder pleHandler) {
    this.builder = pleHandler;
  }

  public Cache getCache() {
    return cache;
  }

  public void setCache(Cache cache) {
    this.cache = cache;
  }

  @SuppressWarnings("unused")
  protected void parse(Object o) {
  } // Override

  public static class Time {

    private long               st            = 0;
    private List<CategoryTime> categoryTimes = new ArrayList<>();

    public void start() {
      st = System.currentTimeMillis();
    }

    public void end(int cat) {
      CategoryTime c = getCategory(cat);
      long ct = (System.currentTimeMillis() - st);
      c.setTime(c.getTime() + ct);
      c.setNr(c.getNr() + 1);
    }

    public void show() {
      int total = 0;
      for (CategoryTime ct : getCategoryTimes()) {
        total += ct.getTime();
        log.info(String.format("Cat. %02d: %4s ms. total (%d)", ct.getCat(), String.valueOf(ct.getTime()),
            ct.getNr()));
      }

      log.info("Totaal: " + total + "ms.");
    }

    public List<CategoryTime> getCategoryTimes() {
      return categoryTimes;
    }

    private CategoryTime getCategory(int cat) {
      for (CategoryTime c : getCategoryTimes()) {
        if (c.getCat() == cat) {
          return c;
        }
      }

      CategoryTime c = new CategoryTime();
      c.setCat(cat);
      categoryTimes.add(c);
      return c;
    }

    public static class CategoryTime {

      private int  cat  = 0;
      private long time = 0;
      private int  nr   = 0;

      public int getCat() {
        return cat;
      }

      public void setCat(int cat) {
        this.cat = cat;
      }

      public long getTime() {
        return time;
      }

      public void setTime(long time) {
        this.time = time;
      }

      public int getNr() {
        return nr;
      }

      public void setNr(int nr) {
        this.nr = nr;
      }
    }
  }

  public static class Cache {

    private Map<Class<?>, HashMap<Object, Object>> cacheMap = new HashMap<>();

    public void set(Class<?> c, Object key, Object value) {
      HashMap<Object, Object> map = cacheMap.computeIfAbsent(c, k -> new HashMap<>());
      map.put(key, value);
    }

    public Object get(Class<?> c, Object key) {
      if (cacheMap.containsKey(c)) {
        return (cacheMap.get(c).getOrDefault(key, null));
      }
      return null;
    }
  }
}
