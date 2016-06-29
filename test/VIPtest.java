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
 * Created by Administrator on 2016/6/29.
 */
public class VIPtest {
    private File indexFile;
    private File itemsFile;
    private File usersFile;

    @Before
    public void setUp() throws Exception {
        indexFile = new File("./indexFile.json");
        itemsFile = new File("./itemsFile.json");
        usersFile = new File("./usersFile.json");
    }

    @After
    public void tearDown() throws Exception {
        if (indexFile.exists()) {
            indexFile.delete();
        }
        if (itemsFile.exists()) {
            itemsFile.delete();
        }
        if (usersFile.exists()) {
            usersFile.delete();
        }
    }
    private void WriteToFile(File file, String content) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.write(content);
        printWriter.close();
    }
    @Test
    public void testVipImformationForSimpleItem() throws Exception {//测试VIP信息，对于普通（无打折，无促销）商品
        String sampleIndex = new StringBuilder()  //标准输入 输入到index.json文件
                .append("{\n")
                .append("'ITEM000003':{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 5.00\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder() //标准输入，输入到item.json文件
                .append("{\n")
                .append("\"user\": 'USER001',\n")
                .append("\"items\":[\n")
                .append("\"ITEM000003\",\n")
                .append("\"ITEM000003\",\n")
                .append("\"ITEM000003\"")
                .append("]")
                .append("}\n")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        String sampleUsers = new StringBuilder() //标准输入，输入到user.json文件
                .append("{\n")
                .append("\"USER001\": {\n")
                .append("\"name\": 'USER 001',\n")
                .append("\"isVip\",true\n")
                .append("},\n")
                .append("\"USER002\": {\n")
                .append("\"name\": 'USER 002',\n")
                .append("\"isVip\",false\n")
                .append("},\n")
                .append("\"USER003\": {\n")
                .append("\"name\": 'USER 0031',\n")
                .append("\"isVip\",true\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(usersFile, sampleUsers);

        InputParser inputParser = new InputParser(indexFile, itemsFile, usersFile);   //文件读入 InputParser类
        ArrayList<Item> items = inputParser.parser().getItems();

        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item i : items) {  //为购物单循环输入商品信息
            shoppingChart.add(i);
        }

        Pos pos = new Pos();

        String actualShoppingList = pos.getShoppingList(shoppingChart);
        String expectedShoppingList =    //标准输出打印 POS类
                "***商店购物清单***\n"
                        + "打印时间：!!!\n"
                        + "----------------------\n"
                        + "名称：电池，数量：3个，单价：5.00(元)，小计：15.00(元)\n"
                        + "----------------------\n"
                        + "总计：15.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testVipImformationForVipDiscount() throws Exception {
        String sampleIndex = new StringBuilder()  //标准输入 输入到index.json文件
                .append("{\n")
                .append("'ITEM000000':{\n")
                .append("\"name\": '可口可乐',\n")
                .append("\"unit\": '瓶',\n")
                .append("\"price\": 3.00,\n")
                .append("\"vipDiscount\": 0.90\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder() //标准输入，输入到item.json文件
                .append("{\n")
                .append("\"user\": 'USER001',\n")
                .append("\"items\":[\n")
                .append("\"ITEM000000\",\n")
                .append("\"ITEM000000\",\n")
                .append("\"ITEM000000\"")
                .append("]")
                .append("}\n")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        String sampleUsers = new StringBuilder() //标准输入，输入到user.json文件
                .append("{\n")
                .append("\"USER001\": {\n")
                .append("\"name\": 'USER 001',\n")
                .append("\"isVip\",true\n")
                .append("},\n")
                .append("\"USER002\": {\n")
                .append("\"name\": 'USER 002',\n")
                .append("\"isVip\",false\n")
                .append("},\n")
                .append("\"USER003\": {\n")
                .append("\"name\": 'USER 0031',\n")
                .append("\"isVip\",true\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(usersFile, sampleUsers);

        InputParser inputParser = new InputParser(indexFile, itemsFile, usersFile);
        ArrayList<Item> items = inputParser.parser().getItems();

        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item i : items) {
            shoppingChart.add(i);
        }

        Pos pos = new Pos();

        String actualShoppingList = pos.getShoppingList(shoppingChart);
        String expectedShoppingList =    //标准输出打印
                "***商店购物清单***\n"
                        + "打印时间：!!!\n"
                        + "----------------------\n"
                        + "名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：8.10(元)\n"
                        + "----------------------\n"
                        + "总计：8.10(元)\n"
                        + "节省：0.90(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testNotVipForVipDiscount() throws Exception {
        String sampleIndex = new StringBuilder()  //标准输入 输入到index.json文件
                .append("{\n")
                .append("'ITEM000000':{\n")
                .append("\"name\": '可口可乐',\n")
                .append("\"unit\": '瓶',\n")
                .append("\"price\": 3.00,\n")
                .append("\"vipDiscount\": 0.90\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder() //标准输入，输入到item.json文件
                .append("{\n")
                .append("\"user\": 'USER002',\n")
                .append("\"items\":[\n")
                .append("\"ITEM000000\",\n")
                .append("\"ITEM000000\",\n")
                .append("\"ITEM000000\"")
                .append("]")
                .append("}\n")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        String sampleUsers = new StringBuilder() //标准输入，输入到user.json文件
                .append("{\n")
                .append("\"USER001\": {\n")
                .append("\"name\": 'USER 001',\n")
                .append("\"isVip\",true\n")
                .append("},\n")
                .append("\"USER002\": {\n")
                .append("\"name\": 'USER 002',\n")
                .append("\"isVip\",false\n")
                .append("},\n")
                .append("\"USER003\": {\n")
                .append("\"name\": 'USER 0031',\n")
                .append("\"isVip\",true\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(usersFile, sampleUsers);

        InputParser inputParser = new InputParser(indexFile, itemsFile, usersFile);
        ArrayList<Item> items = inputParser.parser().getItems();

        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item i : items) {
            shoppingChart.add(i);
        }

        Pos pos = new Pos();

        String actualShoppingList = pos.getShoppingList(shoppingChart);
        String expectedShoppingList =    //标准输出打印
                "***商店购物清单***\n"
                        + "打印时间：!!!\n"
                        + "----------------------\n"
                        + "名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：9.00(元)\n"
                        + "----------------------\n"
                        + "总计：9.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testVipImformationForVipDiscountAndDiscount() throws Exception {
        String sampleIndex = new StringBuilder()  //标准输入 输入到index.json文件
                .append("{\n")
                .append("'ITEM000001':{\n")
                .append("\"name\": '雪碧',\n")
                .append("\"unit\": '瓶',\n")
                .append("\"price\": 3.00,\n")
                .append("\"Discount\": 0.80,\n")
                .append("\"vipDiscount\": 0.95\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder() //标准输入，输入到item.json文件
                .append("{\n")
                .append("\"user\": 'USER001',\n")
                .append("\"items\":[\n")
                .append("\"ITEM000001\",\n")
                .append("\"ITEM000001\",\n")
                .append("\"ITEM000001\"")
                .append("]")
                .append("}\n")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        String sampleUsers = new StringBuilder() //标准输入，输入到user.json文件
                .append("{\n")
                .append("\"USER001\": {\n")
                .append("\"name\": 'USER 001',\n")
                .append("\"isVip\",true\n")
                .append("},\n")
                .append("\"USER002\": {\n")
                .append("\"name\": 'USER 002',\n")
                .append("\"isVip\",false\n")
                .append("},\n")
                .append("\"USER003\": {\n")
                .append("\"name\": 'USER 0031',\n")
                .append("\"isVip\",true\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(usersFile, sampleUsers);

        InputParser inputParser = new InputParser(indexFile, itemsFile, usersFile);
        ArrayList<Item> items = inputParser.parser().getItems();

        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item i : items) {
            shoppingChart.add(i);
        }

        Pos pos = new Pos();

        String actualShoppingList = pos.getShoppingList(shoppingChart);
        String expectedShoppingList =    //标准输出打印
                "***商店购物清单***\n"
                        + "打印时间：!!!\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：3瓶，单价：3.00(元)，小计：6.84(元)\n"
                        + "----------------------\n"
                        + "总计：6.84(元)\n"
                        + "节省：2.15(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testVipImformationForVipDiscountAndPromotion() throws Exception {
        String sampleIndex = new StringBuilder()  //标准输入 输入到index.json文件
                .append("{\n")
                .append("'ITEM000001':{\n")
                .append("\"name\": '雪碧',\n")
                .append("\"unit\": '瓶',\n")
                .append("\"price\": 3.00,\n")
                .append("\"promotion\": true,\n")
                .append("\"vipDiscount\": 0.90\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);
        InputParser inputParser = new InputParser(indexFile, itemsFile, usersFile);
        ArrayList<Item> items = inputParser.parser().getItems();

        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item i : items) {
            shoppingChart.add(i);
        }

        Pos pos = new Pos();

        String actualShoppingList = pos.getShoppingList(shoppingChart);
        String expectedShoppingList =    //标准输出打印
                "***商店购物清单***\n"
                        + "打印时间：!!!\n"
                        + "雪碧不参加 “买二赠一” 优惠活动\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：3瓶，单价：3.00(元)，小计：8.10(元)\n"
                        + "----------------------\n"
                        + "总计：8.10(元)\n"
                        + "节省：0.90(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
}
