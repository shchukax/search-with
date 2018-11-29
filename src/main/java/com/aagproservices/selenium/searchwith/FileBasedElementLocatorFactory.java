package com.aagproservices.selenium.searchwith;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class FileBasedElementLocatorFactory implements ElementLocatorFactory {
    private final SearchContext searchContext;

    public FileBasedElementLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        return new FileBasedElementLocator(searchContext, new SearchWithAnnotations(field));
    }
}
