package com.aagproservices.selenium.searchwith;

import com.google.gson.*;
import org.openqa.selenium.By;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SearchWithProvider {
    //this map is filename -> search provider
    private static Map<String, SearchWithProvider> providers = new ConcurrentHashMap<>();

    //this map is page -> (name -> locator))
    private Map<String, Map<String, By>> locators = new ConcurrentHashMap<>();

    private SearchWithProvider(String locatorsFile) throws IllegalArgumentException {
        boolean isResource = false;
        if (locatorsFile.matches("\\{(.+)}")) {
            String propName = locatorsFile.substring(1, locatorsFile.length() - 1);
            locatorsFile = System.getProperty(propName);
            if (locatorsFile == null || locatorsFile.trim().isEmpty()) {
                throw new IllegalArgumentException("Undefined system property for locators file - " + propName);
            }
        }

        if (locatorsFile.matches("\\[(.+)]")) {
            isResource = true;
            locatorsFile = "/" + locatorsFile.substring(1, locatorsFile.length() - 1);
        }

        loadLocators(locatorsFile, isResource);
    }

    public static SearchWithProvider getProvider(String locatorsFile) throws IllegalArgumentException {
        SearchWithProvider provider;

        if (!providers.containsKey(locatorsFile)) {
            provider = new SearchWithProvider(locatorsFile);
            providers.put(locatorsFile, provider);
        } else {
            provider = providers.get(locatorsFile);
        }

        return provider;
    }

    public By getLocator(String page, String name) {
        Map<String, By> pageLocators = locators.get(page);
        return pageLocators == null ? null : pageLocators.get(name);
    }

    private void loadLocators(String locatorsFile, boolean isResource) throws IllegalArgumentException {
        try {
            Reader reader;

            if (isResource) {
                InputStream locStream = this.getClass().getResourceAsStream(locatorsFile);
                if (locStream == null) {
                    throw new FileNotFoundException("Locators file not found in resources: " + locatorsFile);
                }
                reader = new InputStreamReader(locStream);
            } else {
                reader = new FileReader(locatorsFile);
            }

            JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
            reader.close();
            Iterator<String> iterator = object.keySet().iterator();

            JsonObject pageLevel;
            Map<String, By> pageLocators;
            Iterator<String> pageIterator;
            JsonObject oneLocator;
            String page;
            String name;
            String type;
            String locator;
            By by;

            while(iterator.hasNext()) {
                page = iterator.next();
                pageLevel = object.getAsJsonObject(page);

                pageLocators = new ConcurrentHashMap<>();
                locators.put(page, pageLocators);

                pageIterator = pageLevel.keySet().iterator();
                while (pageIterator.hasNext()) {
                    name = pageIterator.next();
                    oneLocator = pageLevel.get(name).getAsJsonObject();

                    if (oneLocator.get("type") != null) {
                        type = oneLocator.get("type").getAsString();
                    } else {
                        throw new IllegalArgumentException("Missing required property - type - for locator " + page + "/" + name);
                    }

                    if (oneLocator.get("locator") != null) {
                        locator = oneLocator.get("locator").getAsString();
                    } else {
                        throw new IllegalArgumentException("Missing required property - locator - for locator " + page + "/" + name);
                    }

                    switch (type) {
                        case "id":
                            by = By.id(locator);
                            break;
                        case "css":
                            by = By.cssSelector(locator);
                            break;
                        case "className":
                            by = By.className(locator);
                            break;
                        case "xpath":
                            by = By.xpath(locator);
                            break;
                        case "linkText":
                            by = By.linkText(locator);
                            break;
                        case "partialLinkText":
                            by = By.partialLinkText(locator);
                            break;
                        case "name":
                            by = By.name(locator);
                            break;
                        case "tagName":
                            by = By.tagName(locator);
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported locator type - " + type);
                    }
                    pageLocators.put(name, by);
                }
            }
        } catch (JsonIOException | JsonSyntaxException e) {
            throw new IllegalArgumentException("Error parsing locators file " + locatorsFile, e);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unable to find locators file " + locatorsFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading locators file " + locatorsFile);
        }
    }
}
