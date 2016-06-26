import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.Pos;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.services.services.InputParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016/6/26.
 */
public class PromotionTest {
        private File indexFile;
        private File itemsFile;

        @Before
        public void setUp() throws Exception {
            indexFile = new File("./sampleIndex.json");
            itemsFile = new File("./itemsFile.json");
        }

        @After
        public void tearDown() throws Exception {
            if(indexFile.exists()){
                indexFile.delete();
            }
            if(itemsFile.exists()){
                itemsFile.delete();
            }
        }

        @Test
        public void testParseJsonFileToPromotionItems() throws Exception {
            String sampleIndex = new StringBuilder()
                    .append("{\n")
                    .append("'ITEM000000':{\n")
                    .append("\"name\": '可口可乐',\n")
                    .append("\"unit\": '瓶',\n")
                    .append("\"price\": 3.00,\n")
                    .append("\"promotion\": true\n")
                    .append("}\n")
                    .append("}\n")
                    .toString();
            WriteToFile(indexFile, sampleIndex);

            String sampleItems = new StringBuilder()
                    .append("[\n")
                    .append("\"ITEM000000\",\n")
                    .append("\"ITEM000000\",\n")
                    .append("\"ITEM000000\"")
                    .append("]")
                    .toString();
            WriteToFile(itemsFile, sampleItems);

            InputParser inputParser = new InputParser(indexFile, itemsFile);
            ArrayList<Item> items = inputParser.parser().getItems();

            ShoppingChart shoppingChart = new ShoppingChart();
            for (Item i:items){
                shoppingChart.add(i);
            }

            Pos pos = new Pos();

            String actualShoppingList = pos.getShoppingList(shoppingChart);
            String expectedShoppingList =
                    "***商店购物清单***\n"
                            + "名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：6.00(元)\n"
                            + "----------------------\n"
                            + "挥泪赠送商品：\n"
                            + "名称：可口可乐，数量：1瓶\n"
                            + "----------------------\n"
                            + "总计：6.00(元)\n"
                            + "节省：3.00(元)\n"
                            + "**********************\n";
            assertThat(actualShoppingList, is(expectedShoppingList));
        }

        private void WriteToFile(File file, String content) throws FileNotFoundException {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(content);
            printWriter.close();
        }

        @Test
        public void testParseJsonWhenHasNoPromotion() throws Exception {
            String sampleIndex = new StringBuilder()
                    .append("{\n")
                    .append("'ITEM000000':{\n")
                    .append("\"name\": '可口可乐',\n")
                    .append("\"unit\": '瓶',\n")
                    .append("\"price\": 3.00,\n")
                    .append("\"promotion\": true\n")
                    .append("}\n")
                    .append("}\n")
                    .toString();
            WriteToFile(indexFile, sampleIndex);

            String sampleItems = new StringBuilder()
                    .append("[\n")
                    .append("\"ITEM000000\"")
                    .append("]")
                    .toString();
            WriteToFile(itemsFile, sampleItems);

            InputParser inputParser = new InputParser(indexFile, itemsFile);
            ArrayList<Item> items = inputParser.parser().getItems();

            ShoppingChart shoppingChart = new ShoppingChart();
            for (Item i:items){
                shoppingChart.add(i);
            }


            Pos pos = new Pos();

            String actualShoppingList = pos.getShoppingList(shoppingChart);
            String expectedShoppingList =
                    "***商店购物清单***\n"
                            + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                            + "----------------------\n"
                            + "总计：3.00(元)\n"
                            + "**********************\n";
            assertThat(actualShoppingList, is(expectedShoppingList));
        }


}
