package order;

import order.requests.Ingredients;
import order.responses.ingredients.Ingredient;
import order.responses.ingredients.IngredientsList;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {

    public Ingredients genericEmptyOrder(){

        List<String> order = new ArrayList<>();
        return new Ingredients(order);
    }

    public Ingredients genericWrongHashOrder(){

        List<String> order = new ArrayList<>();
        order.add("5");
        return new Ingredients(order);
    }

    //получение списка валидных ингредиентов для бургера через запрос к ручке /api/ingredients
    public Ingredients getListOfIngredients(){

        OrderClient orderClient = new OrderClient();

        IngredientsList ingredientsList = orderClient.getIngredients();

        List<String> ing = new ArrayList<>();

        int bun = 0;
        int main = 0;
        int sauce = 0;

        //добавление 3х валидных ингредиентов для бургера из ответа
        for (Ingredient ingredients: ingredientsList.getData()){

            if ("bun".equals(ingredients.getType()) && bun == 0){
                ing.add(ingredients.get_id());
                bun += 1;
            }
            if ("main".equals(ingredients.getType()) && main == 0){
                ing.add(ingredients.get_id());
                main += 1;
            }
            if ("sauce".equals(ingredients.getType()) && sauce == 0){
                ing.add(ingredients.get_id());
                sauce += 1;
            }
            if (bun > 0 && main > 0 && sauce > 0) break;
        }

        return new Ingredients(ing);
    }

}
