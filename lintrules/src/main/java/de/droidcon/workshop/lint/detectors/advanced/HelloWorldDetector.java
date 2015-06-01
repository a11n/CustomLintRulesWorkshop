package de.droidcon.workshop.lint.detectors.advanced;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import static com.android.SdkConstants.ATTR_LABEL;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.STRING_PREFIX;
import static com.android.SdkConstants.TAG_APPLICATION;
import static com.android.SdkConstants.TAG_STRING;

/**
 * Check which determines if application title equals "Hello world"
 */
public class HelloWorldDetector extends ResourceXmlDetector {

  public static final Issue ISSUE = Issue.create(
      "HelloWorld",                                        //ID
      "Unexpected application title",                      //brief description
      "The application title should state 'Hello world'",  //explanation
      Category.CORRECTNESS,                                //category
      5,                                                   //priority
      Severity.INFORMATIONAL,                              //severity
      new Implementation(                                  //implementation
          HelloWorldDetector.class,                        //detector
          Scope.MANIFEST_AND_RESOURCE_SCOPE                //scope
      ));

  private static final String TITLE = "Hello world";

  //fill these during scan phase
  private String applicationLabel = null;
  private Location applicationLabelLocation = null;
  private Map<String, String> stringValues = new HashMap<>();

  @Override public Collection<String> getApplicableElements() {
    return Arrays.asList(
        TAG_APPLICATION,
        TAG_STRING
    );
  }

  @Override public Collection<String> getApplicableAttributes() {
    return Arrays.asList(
        ATTR_LABEL
    );
  }

  @Override public void visitElement(@NonNull XmlContext context, @NonNull Element element) {
    if(!TAG_STRING.equals(element.getTagName())) return;

    String name = element.getAttribute(ATTR_NAME);
    String value = element.getTextContent();

    stringValues.put(name, value);
  }

  @Override public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
    //avoid checking of <application>'s children, such as <activity>
    if (!TAG_APPLICATION.equals(attribute.getOwnerElement().getTagName())) {
      return;
    }

    if(applicationLabel != null)
      return;

    applicationLabel = attribute.getValue();
    applicationLabelLocation = context.getLocation(attribute);
  }

  @Override public void afterCheckProject(@NonNull Context context) {
    if (applicationLabel == null) return;

    if(!applicationLabel.startsWith(STRING_PREFIX) && !TITLE.equals(applicationLabel)){
      context.report(ISSUE, applicationLabelLocation,
          String.format("Unexpected title \"%1$s\". Should be \"Hello world\".", applicationLabel));
    } else if(applicationLabel.startsWith(STRING_PREFIX) &&
        stringValues.containsKey(applicationLabel.substring(8))){
      String resolvedApplicationLabel = stringValues.get(applicationLabel.substring(8));
      if(!TITLE.equals(resolvedApplicationLabel)){
        context.report(ISSUE, applicationLabelLocation,
            String.format("Unexpected title \"%1$s\". Should be \"Hello world\".", resolvedApplicationLabel));
      }
    }
  }
}
