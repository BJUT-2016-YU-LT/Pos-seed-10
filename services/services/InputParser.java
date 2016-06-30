package com.thoughtworks.pos.services.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.domains.User;
import com.thoughtworks.pos.domains.UserList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/1/2.
 */
public class InputParser {
    private File indexFile;
    private File itemsFile;
    private File usersFile;
    private final ObjectMapper objectMapper;

    public InputParser(File indexFile, File itemsFile, File usersFile) throws IOException {
        this.indexFile = indexFile;
        this.itemsFile = itemsFile;
        this.usersFile = usersFile;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    public ShoppingChart parser() throws IOException {
        return BuildShoppingChart(getUserList(), getItemIndexes(), getUserIndexes());
    }

    private ShoppingChart BuildShoppingChart(UserList userShoppingList, HashMap<String, Item> itemIndexes, HashMap<String, User> userIndexes) {
        ShoppingChart shoppingChart = new ShoppingChart();
        User user;
        if (!userShoppingList.getUser().isEmpty()) {
            if (userIndexes.containsKey(userShoppingList.getUser())) {
                User mappedUser = userIndexes.get(userShoppingList.getUser());
                user = new User(userShoppingList.getUser(), mappedUser.getName(), mappedUser.getIsVip(), mappedUser.getScore());
                shoppingChart.setUser(user);
            } else {
                user = new User(userShoppingList.getUser(), "新会员", true, 0);
                userIndexes.put(userShoppingList.getUser(), user);
                shoppingChart.setUser(user);
            }
        }
        if (userShoppingList.getItems().length != 0) {
            for (String barcode : userShoppingList.getItems()) {
                if (itemIndexes.containsKey(barcode)) {
                    Item mappedItem = itemIndexes.get(barcode);
                    Item item = new Item(mappedItem.getVipDiscount(), barcode, mappedItem.getName(), mappedItem.getUnit(), mappedItem.getPrice(), mappedItem.getDiscount(), mappedItem.getPromotion());
                    shoppingChart.add(item);
                }
            }
        }
        return shoppingChart;
    }

    private UserList getUserList() throws IOException {
        String userShoppingListStr = FileUtils.readFileToString(indexFile);
        return objectMapper.readValue(userShoppingListStr, UserList.class);
    }

    private HashMap<String, Item> getItemIndexes() throws IOException {
        String itemsIndexStr = FileUtils.readFileToString(itemsFile);
        TypeReference<HashMap<String, Item>> typeRef = new TypeReference<HashMap<String, Item>>() {
        };
        return objectMapper.readValue(itemsIndexStr, typeRef);
    }

    private HashMap<String, User> getUserIndexes() throws IOException {
        String userIndexStr = FileUtils.readFileToString(usersFile);
        TypeReference<HashMap<String, User>> typeRef = new TypeReference<HashMap<String, User>>() {
        };
        return objectMapper.readValue(userIndexStr, typeRef);
    }

    private HashMap<String, User> setUsersScore(User user) throws IOException {
        HashMap<String, User> newUserIndexes = getUserIndexes();
        // 消费获取用户列表中用户编号相同的用户并修改其积分
        if (newUserIndexes.containsKey(user.getNumber())) {
            //System.out.println("该用户有记录");
            newUserIndexes.get(user.getNumber()).setScore(user.getScore());
            return newUserIndexes;
        } else if (user.getNumber().isEmpty()) {
            //System.out.println("该用户无记录");
            return newUserIndexes;
        }
        // 若为有编号但不存在与用户列表中，新增该用户至表中
        //System.out.println("该用户无记录，已添加");
        newUserIndexes.put(user.getNumber(), user);
        return newUserIndexes;
    }

    public String saveFile(ShoppingChart shoppingChart) throws IOException {
        HashMap<String, User> newUserIndexes = setUsersScore(shoppingChart.getUser());
        String userIndexesJson = objectMapper.writeValueAsString(newUserIndexes);
        return userIndexesJson;
    }
}
