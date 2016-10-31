package com.github.tnerevival.core.transaction;

import com.github.tnerevival.utils.MISCUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by creatorfromhell on 10/20/2016.
 */
public class TransactionHistory {
  private List<Record> records = new ArrayList<>();
  private List<Record> sorted = new ArrayList<>();

  public void add(Record record) {
    records.add(record);
  }

  private void sort(String world, String type) {
    sorted = new ArrayList<>();
    for(Record r : records) {
      Boolean worldCheck = world.equalsIgnoreCase("all") || world.equalsIgnoreCase(r.getWorld());
      Boolean typeCheck = type.equalsIgnoreCase("all") || type.equalsIgnoreCase(r.getType());

      if(worldCheck && typeCheck) {
        sorted.add(r);
      }
    }
  }

  public List<Record> getRecords() {
    return records;
  }

  public List<Record> getRecords(String world, String type, int page) {
    MISCUtils.debug(records.size() + "");
    return getRecords(5, world, type, page);
  }

  private List<Record> getRecords(int limit, String world, String type, int page) {
    sort(world, type);
    List<Record> pageRecords = new ArrayList<>();

    int max = getMaxPages(world, type, limit);
    int start = (page == 1)? 0 : ((page > max)? max : page);

    for(int i = start; i < start + limit; i++) {
      if(i < sorted.size()) {
        pageRecords.add(sorted.get(i));
      }
    }
    return pageRecords;
  }

  public Integer getMaxPages(String world, String type, int perPage) {
    sort(world, type);
    int max = sorted.size() / perPage;
    if(sorted.size() % perPage > 0) max++;
    return max;
  }
}
