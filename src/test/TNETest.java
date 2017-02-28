package test;

import com.github.tnerevival.TNE;
import org.junit.Test;
import org.mockito.Mockito;

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
 * Created by creatorfromhell on 10/17/2016.
 */
public class TNETest {

  private static TNE instance = Mockito.mock(TNE.class);

  @Test
  public void enabled() {
    System.out.println(instance != null);
  }

  public static TNE instance() {
    return instance;
  }
}
