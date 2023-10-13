package com.example.grocerieswizard.data.remote.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.grocerieswizard.addRecipe.IngredientModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String idMeal;
    private String strMeal;
    private String strDrinkAlternate;
    private String strCategory;
    private String strArea;
    private String strInstructions;
    private String strMealThumb;
    private String strTags;
    private String strYoutube;
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;
    private String strIngredient11;
    private String strIngredient12;
    private String strIngredient13;
    private String strIngredient14;
    private String strIngredient15;
    private String strIngredient16;
    private String strIngredient17;
    private String strIngredient18;
    private String strIngredient19;
    private String strIngredient20;
    private String strMeasure1;
    private String strMeasure2;
    private String strMeasure3;
    private String strMeasure4;
    private String strMeasure5;
    private String strMeasure6;
    private String strMeasure7;
    private String strMeasure8;
    private String strMeasure9;
    private String strMeasure10;
    private String strMeasure11;
    private String strMeasure12;
    private String strMeasure13;
    private String strMeasure14;
    private String strMeasure15;
    private String strMeasure16;
    private String strMeasure17;
    private String strMeasure18;
    private String strMeasure19;
    private String strMeasure20;
    private String strSource;
    private String strImageSource;
    private String strCreativeCommonsConfirmed;
    private String dateModified;
    List<IngredientModel> ingredients = new ArrayList<>();
    private Bitmap imageBitmap;
    private static final String TAG = "Meal";

    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrDrinkAlternate() {
        return strDrinkAlternate;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public String getStrIngredient1() {
        return strIngredient1;
    }

    public String getStrIngredient2() {
        return strIngredient2;
    }

    public String getStrIngredient3() {
        return strIngredient3;
    }

    public String getStrIngredient4() {
        return strIngredient4;
    }

    public String getStrIngredient5() {
        return strIngredient5;
    }

    public String getStrIngredient6() {
        return strIngredient6;
    }

    public String getStrIngredient7() {
        return strIngredient7;
    }

    public String getStrIngredient8() {
        return strIngredient8;
    }

    public String getStrIngredient9() {
        return strIngredient9;
    }

    public String getStrIngredient10() {
        return strIngredient10;
    }

    public String getStrIngredient11() {
        return strIngredient11;
    }

    public String getStrIngredient12() {
        return strIngredient12;
    }

    public String getStrIngredient13() {
        return strIngredient13;
    }

    public String getStrIngredient14() {
        return strIngredient14;
    }

    public String getStrIngredient15() {
        return strIngredient15;
    }

    public String getStrIngredient16() {
        return strIngredient16;
    }

    public String getStrIngredient17() {
        return strIngredient17;
    }

    public String getStrIngredient18() {
        return strIngredient18;
    }

    public String getStrIngredient19() {
        return strIngredient19;
    }

    public String getStrIngredient20() {
        return strIngredient20;
    }

    public String getStrMeasure1() {
        return strMeasure1;
    }

    public String getStrMeasure2() {
        return strMeasure2;
    }

    public String getStrMeasure3() {
        return strMeasure3;
    }

    public String getStrMeasure4() {
        return strMeasure4;
    }

    public String getStrMeasure5() {
        return strMeasure5;
    }

    public String getStrMeasure6() {
        return strMeasure6;
    }

    public String getStrMeasure7() {
        return strMeasure7;
    }

    public String getStrMeasure8() {
        return strMeasure8;
    }

    public String getStrMeasure9() {
        return strMeasure9;
    }

    public String getStrMeasure10() {
        return strMeasure10;
    }

    public String getStrMeasure11() {
        return strMeasure11;
    }

    public String getStrMeasure12() {
        return strMeasure12;
    }

    public String getStrMeasure13() {
        return strMeasure13;
    }

    public String getStrMeasure14() {
        return strMeasure14;
    }

    public String getStrMeasure15() {
        return strMeasure15;
    }

    public String getStrMeasure16() {
        return strMeasure16;
    }

    public String getStrMeasure17() {
        return strMeasure17;
    }

    public String getStrMeasure18() {
        return strMeasure18;
    }

    public String getStrMeasure19() {
        return strMeasure19;
    }

    public String getStrMeasure20() {
        return strMeasure20;
    }

    public String getStrSource() {
        return strSource;
    }

    public String getStrImageSource() {
        return strImageSource;
    }

    public String getStrCreativeCommonsConfirmed() {
        return strCreativeCommonsConfirmed;
    }

    public String getDateModified() {
        return dateModified;
    }

    public List<IngredientModel> getIngredients() {
        String[] ingredientNames = {
                getStrIngredient1(),
                getStrIngredient2(),
                getStrIngredient3(),
                getStrIngredient4(),
                getStrIngredient5(),
                getStrIngredient6(),
                getStrIngredient7(),
                getStrIngredient8(),
                getStrIngredient9(),
                getStrIngredient10(),
                getStrIngredient11(),
                getStrIngredient12(),
                getStrIngredient13(),
                getStrIngredient14(),
                getStrIngredient15(),
                getStrIngredient16(),
                getStrIngredient17(),
                getStrIngredient18(),
                getStrIngredient19(),
                getStrIngredient20()
        };

        String[] ingredientMeasures = {
                getStrMeasure1(),
                getStrMeasure2(),
                getStrMeasure3(),
                getStrMeasure4(),
                getStrMeasure5(),
                getStrMeasure6(),
                getStrMeasure7(),
                getStrMeasure8(),
                getStrMeasure9(),
                getStrMeasure10(),
                getStrMeasure11(),
                getStrMeasure12(),
                getStrMeasure13(),
                getStrMeasure14(),
                getStrMeasure15(),
                getStrMeasure16(),
                getStrMeasure17(),
                getStrMeasure18(),
                getStrMeasure19(),
                getStrMeasure20()
        };

        for (int i = 0; i < ingredientNames.length; i++) {
            String name = ingredientNames[i];
            String measure = ingredientMeasures[i];

            if (name != null && !name.isEmpty() && measure != null && !measure.isEmpty()) {
                IngredientModel ingredient = new IngredientModel(name, parseQuantity(measure), parseUnit(measure));
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    private double parseQuantity(String measure) {
        StringBuilder quantityStr = new StringBuilder();
        boolean foundDigit = false;

        for (char c : measure.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                quantityStr.append(c);
                foundDigit = true;
            } else if (foundDigit) {
                break;
            }
        }

        try {
            return Double.parseDouble(quantityStr.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String parseUnit(String measure) {
        StringBuilder unitStr = new StringBuilder();
        boolean foundDigit = false;

        for (char c : measure.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                foundDigit = true;
            } else if (foundDigit) {
                unitStr.append(c);
            }
        }

        String unit = unitStr.toString().trim();

        if (unit.isEmpty()) {
            unit = "piece";
        }

        return unit;
    }


    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}