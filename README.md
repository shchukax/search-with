Using Selenium Locators from External Files
=

Introduction
-
Selenium annotations `@findBy`, `@findBys` and `@FindAll` make it very easy to
build Page Object, taking away the need to initialise all elements and element
collections on pages. This should suffice most of simple use cases. We have
encountered a case where this was not enough.  We have a number of websites
all built on the same platform and sharing the functionality, but having
different front-end user interface. The usual `@FindBy` mechanism is
perfectly fine to run tests against one site, but if we want to run the same
test suite against multiple sites, we needed a way to dynamically specify
locators.

One way of doing it would be to put locators in property files, then indicate
which property file to load during the runtime. This however would not allow
to use PageFactory model. A more complex solution was needed.  Digging into
selenium API details, we realised that we need to build custom
`ElementLocator` and `ElementLocatorFactory` along with a custom annotation
to handle loading locators from a file.

Approach
-

After a very long search, I found
[this blog post](https://rationaleemotions.wordpress.com/2016/06/27/pagefactory-page-objects-and-locators-from-an-external-file/),
which gave me the starting point. There are several limitations with the code
in that post:

* It only handles xpath locators
* It does not work with `List<WebElement>`, only with single elements
* It cannot be combined with any other annotations on the fields in the class
* It is re-reading the file every time it tries to resolve a locator
* The file with locators needs to be specified in the source code

I set about to address all of these shortcomings. This repo contains my
solution all of the above problems.

* Instead of a hardcoded path, a system property can be specified as a
locator file by using `{file}` notation; also a file from resources can be
specified by using `[file]` notation; to use a system property to point to
a resource, simply set the value of the property to be `[file]`
* Data from each file is parsed only once and cached internally
* Multiple files can be used - with all data cached
* Standard `@FindBy` annotations can be used for some fields while
custom `@SearchWith` annotation for other
* List of elements are handled correctly
* All valid locator types are supported

I have further updated the format of the JSON file to make it easier to read and quicker to parse.

Usage
-

Using this annotation is very simple. Start by creating a JSON file with
locators, e.g. `locators.json`. The content is self-explanatory.

```javascript
{
  "myPage" : {
    "myField" : {
      "type" : "css",
      "locator": ".header h2"
    },
    "myCollection" : {
      "type" : "className",
      "locator" : "list-item"
    },
    ...
  },
  ...
}
```

This file can be placed in any directory on disk or added to resources.

Now start using the `@SearchWith` annotation in page objects:

```java
public class MyPage {
    protected WebDriver driver;
    
    @SearchWith(page="myPage", name="myField", locatorsFile="{locators.file}")
    private WebElement myField;
    
    @SearchWith(page="myPage", name="myCollection", locatorsFile="{locators.file}")
    private List<WebElement> myCollection;
    
    @FindBy(id="myId")
    private WebElement anotherElement;

    public MyPage(WebDriver driver) {
      this.driver = driver;
      PageFactory.initElements(new SearchWithDecorator(new FileBasedElementLocatorFactory(driver)), this);
    }
    
    ...
}
```

As the specified `locatorsFile` has `{}`, the actual filename will be
retrieved from system property `locators.file`.  Simply add
`-Dlocators.file=/path/to/locators.json` to your command line.

You can also specify `-Dlocators.file=[locators.json]`, in which case
system property `locators.file` will be retrieved and file `locators.json`
from resources will be used to load locators.
