package com.aagproservices.selenium.searchwith;

import com.google.common.base.Preconditions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;

class SearchWithAnnotations extends AbstractAnnotations {
    private final Field field;

    SearchWithAnnotations(Field field) {
        this.field = field;
    }

    @Override
    public By buildBy() throws IllegalArgumentException {
        SearchWith search = field.getAnnotation(SearchWith.class);

        if (search != null) {
            String pageName = search.page();
            Preconditions.checkArgument(isNotNullAndEmpty(pageName), "Page name is missing.");

            String elementName = search.name();
            Preconditions.checkArgument(isNotNullAndEmpty(elementName), "Element name is not found.");

            String locatorsFile = search.locatorsFile();
            Preconditions.checkArgument(isNotNullAndEmpty(locatorsFile), "Locators file name not provided");

            return SearchWithProvider.getProvider(locatorsFile).getLocator(pageName, elementName);
        } else {
            return new Annotations(field).buildBy();
        }
    }

    @Override
    public boolean isLookupCached() {
        return (field.getAnnotation(CacheLookup.class) != null);
    }

    private boolean isNotNullAndEmpty(String arg) {
        return ((arg != null) && (!arg.trim().isEmpty()));
    }
}
