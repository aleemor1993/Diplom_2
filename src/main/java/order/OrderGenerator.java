package order;

import order.requests.Ingredients;
import order.responses.ingredients.Ingredient;
import order.responses.ingredients.IngredientsList;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {

    public static final String WRONG_HASH = "5";

    public Ingredients genericEmptyOrder(){

        List<String> order = new ArrayList<>();
        return new Ingredients(order);
    }

    //получение списка валидных ингредиентов для бургера через запрос к ручке /api/ingredients
    public Ingredients getListOfIngredients(){

        OrderClient orderClient = new OrderClient();

        IngredientsList ingredientsList = orderClient.getIngredients();

        List<String> ing = new ArrayList<>();
        List<String> types = new ArrayList<>();

        //добавление 3х валидных ингредиентов для бургера из ответа
        for (Ingredient ingredients: ingredientsList.getData()){

            if (!types.contains(ingredients.getType())){
                types.add(ingredients.getType());
                ing.add(ingredients.get_id());
            }
            if (ing.size() == 3) break;
        }

        return new Ingredients(ing);
    }

}
