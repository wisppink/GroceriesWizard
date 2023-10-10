package com.example.grocerieswizard;

import android.app.Application;
import com.example.grocerieswizard.di.GroceriesWizardInjector;

public class GroceriesWizardApp extends Application {
    @Override
    public void onCreate() {
        GroceriesWizardInjector.init(getApplicationContext());
        super.onCreate();
    }
}