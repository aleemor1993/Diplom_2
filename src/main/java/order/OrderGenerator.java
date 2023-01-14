package order;

import order.requests.Ingredients;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static constants.order.Ingredients.*;

public class OrderGenerator {

    public String getBun(){
        return BUNS[new Random().nextInt(2)];
    }

    public String getFilling(){
        return FILLINGS[new Random().nextInt(2)];
    }

    public String getSauce(){
        return SAUCES[new Random().nextInt(2)];
    }

    public String getWrong(){
        return WRONG_HASH[0];
    }


    public Ingredients genericRandomOrder(){

        List<String> order = new ArrayList<>();
        order.add(getBun());
        order.add(getFilling());
        order.add(getSauce());
        return new Ingredients(order);
    }

    public Ingredients genericEmptyOrder(){

        List<String> order = new ArrayList<>();
        return new Ingredients(order);
    }

    public Ingredients genericWrongHashOrder(){

        List<String> order = new ArrayList<>();
        order.add(getWrong());
        return new Ingredients(order);
    }
}
