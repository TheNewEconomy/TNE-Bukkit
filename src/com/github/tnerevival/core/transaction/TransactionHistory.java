package com.github.tnerevival.core.transaction;

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
  List<Record> sorted = new ArrayList<>();

  public void add(Record record) {
    records.add(record);
  }

  public void sort(String world, String type) {
    String worldFilter = world;
    String typeFilter = type;

    for(Record r : records) {
      Boolean worldCheck = false;
      Boolean typeCheck = false;
      if(!worldFilter.equalsIgnoreCase("all")) {
        if(worldFilter.equalsIgnoreCase(r.getWorld())) {

        }
      }

      if(!typeFilter.equalsIgnoreCase("all")) {
        if(typeFilter.equalsIgnoreCase(r.getType())) {

        }
      }

      if(worldCheck && typeCheck) {
        sorted.add(r);
      }
    }
  }

  public List<Record> getRecords() {
    return records;
  }

  public List<Record> getRecords(String world, String type, int page) {
    return getRecords(5, world, type, page);
  }

  public List<Record> getRecords(int limit, String world, String type, int page) {
    sort(world, type);
    List<Record> pageRecords = new ArrayList<>();

    int recordPerPage = limit;
    int max = getMaxPages(recordPerPage);
    int start = (page == 1)? 0 : ((page > max)? max : page);

    for(int i = start; i < start + recordPerPage; i++) {
      if(i < sorted.size()) {
        pageRecords.add(sorted.get(i));
      }
    }
    return pageRecords;
  }

  public Integer getMaxPages(int perPage) {
    int max = records.size() / perPage;
    if(records.size() % perPage > 0) max++;
    return max;
  }
}
