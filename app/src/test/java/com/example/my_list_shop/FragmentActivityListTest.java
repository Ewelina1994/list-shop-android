package com.example.my_list_shop;

import com.example.my_list_shop.entity.Item;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class FragmentActivityListTest {

    private List<Item> itemList; // not mocked!

    @Mock
    Item itemA;

    @Mock
    Item itemB;

    @Before
    public void setup(){
        itemList= Arrays.asList(itemA, itemB);
    }
//    public boolean archive_listIsContainsPositionInList(){
//        int position = 5;
//        Assert.assertThat(5, itemList.size());
//    }
//    private void archive_list(int position) {
//        if(itemList.contains(position)){
//            Item item=itemList.get(position);
//            item.setIsRemoved(1);
//            dbHelper.updateItem(item);
//        }
//        assertThat(itemList, containsInAnyOrder(
//                hasProperty("name", is("Apple")),
//                hasProperty("name", is("Banana"))
//        ));
//    }
}
