package net.tnemc.core.item.data;

import net.tnemc.core.TNE;
import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class BookData implements SerialItemData {

  private List<String> pages = new ArrayList<>();

  private String title;
  private String author;

  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof BookMeta) {
      BookMeta bookMeta = (BookMeta)meta;
      valid = true;

      title = bookMeta.getTitle();
      author = bookMeta.getAuthor();
      pages = bookMeta.getPages();
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      BookMeta meta = (BookMeta) stack.getItemMeta();
      if(title != null) meta.setTitle(title);
      if(author != null) meta.setAuthor(author);
      meta.setPages(pages);
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    TNE.debug("Start book to json");
    json.put("name", "book");
    if(title != null) json.put("title", title);
    TNE.debug("title");
    if(author != null) json.put("author", author);
    TNE.debug("author");
    JSONObject pagesObj = new JSONObject();
    TNE.debug("start pages");
    for(int i = 0; i < pages.size(); i++) {
      pagesObj.put(i, pages.get(i));
    }
    TNE.debug("mid pages");
    json.put("pages", pagesObj);
    TNE.debug("end pages");
    TNE.debug("end book to json");
    return json;
  }

  @Override
  public void readJSON(JSONHelper json) {
    valid = true;
    TNE.debug("Start book from json");
    if(json.has("title")) title = json.getString("title");
    TNE.debug("title");
    if(json.has("author")) author = json.getString("author");
    TNE.debug("author");
    JSONObject pagesObj = json.getJSON("pages");
    TNE.debug("pages");
    pages = new ArrayList<>();
    pagesObj.forEach((key, page)->{
      final String pageTXT = String.valueOf(page);
      TNE.debug("Page: " + pageTXT);
      pages.add(pageTXT);
      TNE.debug("Pages Size: " + pages.size());
    });
    TNE.debug("End book from json");
  }
}